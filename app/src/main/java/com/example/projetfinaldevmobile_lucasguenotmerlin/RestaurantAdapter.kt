package com.example.projetfinaldevmobile_lucasguenotmerlin

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_restaurant.view.*

class RestaurantAdapter(private val restaurants: MutableList<Place>, private val pseudo: String) : RecyclerView.Adapter<RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant, pseudo)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    fun addRestaurants(newRestaurants: List<Place>) {
        restaurants.addAll(newRestaurants)
        notifyDataSetChanged()
    }
}

class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val restaurantImage: ImageView = itemView.restaurant_image

    fun bind(restaurant: Place, pseudo: String) {
        // Bind restaurant data to view
        itemView.restaurant_name.text = restaurant.name
        itemView.restaurant_address.text = restaurant.formatted_address
        if (restaurant.opening_hours?.open_now == true){
            itemView.restaurant_open_now.text = "Ouvert"
            itemView.restaurant_open_now.background = itemView.resources.getDrawable(R.drawable.rounded_corner)
        } else {
            itemView.restaurant_open_now.text = "Fermé"
            itemView.restaurant_open_now.background = itemView.resources.getDrawable(R.drawable.rounded_corner_red)
        }
        // Load restaurant image using Picasso
        val photoUrl = restaurant.photos?.get(0)?.getUrl()?.toString()
        Picasso.get().load(photoUrl).into(restaurantImage)

        Picasso.get().load(photoUrl).into(itemView.profileButton)

        // Handle click listeners on buttons
        val database = Firebase.database("https://devmobile-877ca-default-rtdb.europe-west1.firebasedatabase.app/").reference
        val restLikeRef = database.child("restLike")
        val likeButton = itemView.likeButton
        // Set the likeButton image
        restLikeRef
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (restLikeSnapshot in snapshot.children) {
                            Log.d("pseudo", pseudo)
                            Log.d("id", restaurant.place_id)
                            val restLike = RestLike(pseudo, restaurant.place_id)
                            // L'utilisateur a déjà aimé ce restaurant
                            if(restLikeSnapshot.value.toString() == restLike.toString()){
                                Log.d("LIKE", "L'utilisateur a déjà aimé ce restaurant")
                                likeButton.setImageResource(R.drawable.union_stroke)
                                return
                            }
                        }
                        // L'utilisateur n'a pas encore aimé ce restaurant
                        Log.d("LIKE", "L'utilisateur n'a pas encore aimé ce restaurant")
                        likeButton.setImageResource(R.drawable.union)
                    } else {
                        // La table est vide
                        Log.d("LIKE", "La table est vide")
                        likeButton.setImageResource(R.drawable.union)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Failed to read value.", error.toException())
                }
            })

        likeButton.setOnClickListener {
            val restaurantId = restaurant.place_id
            val restLike = RestLike(pseudo, restaurantId)
            restLikeRef
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (restLikeSnapshot in snapshot.children) {
                                // L'utilisateur a déjà aimé ce restaurant
                                if(restLikeSnapshot.value.toString() == restLike.toString()){
                                    Log.d("LIKE", "L'utilisateur a déjà aimé ce restaurant")
                                    restLikeSnapshot.ref.removeValue()
                                    likeButton.setImageResource(R.drawable.union)
                                    return
                                }
                            }
                            // L'utilisateur n'a pas encore aimé ce restaurant
                            Log.d("AJOUT", restLike.toString())
                            restLikeRef.push().setValue(restLike.toString())
                            likeButton.setImageResource(R.drawable.union_stroke)
                        } else {
                            // La table est vide
                            Log.d("AJOUT", restLike.toString())
                            restLikeRef.push().setValue(restLike.toString())
                            likeButton.setImageResource(R.drawable.union_stroke)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "onCancelled", error.toException())
                    }
                })
        }

        itemView.profileButton.setOnClickListener {
            val intent = Intent(itemView.context, RestaurantDetailActivity::class.java)
            intent.putExtra("place_id", restaurant.place_id)
            itemView.context.startActivity(intent)
        }
    }
}
