package com.example.projetfinaldevmobile_lucasguenotmerlin

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RestaurantAdapter(private val restaurants: MutableList<Place>) : RecyclerView.Adapter<RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)
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

    private val restaurantImage: ImageView = itemView.findViewById(R.id.restaurant_image)

    fun bind(restaurant: Place) {
        // Bind restaurant data to view
        itemView.findViewById<TextView>(R.id.restaurant_name).text = restaurant.name
        itemView.findViewById<TextView>(R.id.restaurant_address).text = restaurant.formatted_address
        itemView.findViewById<TextView>(R.id.restaurant_open_now).text = if (restaurant.opening_hours?.open_now == true) "Open now" else "Closed"

        // Load restaurant image using Picasso
        Log.d("RestaurantViewHolder", "Loading image for ${restaurant.name} from ${restaurant.photos?.get(0)?.getUrl()}")
        val photoUrl = restaurant.photos?.get(0)?.getUrl()?.toString()
        Picasso.get().load(photoUrl).into(restaurantImage)


        // Handle click listeners on buttons
        // itemView.findViewById<Button>(R.id.like_button).setOnClickListener { /* handle like button click */ }
        /*itemView.findViewById<Button>(R.id.profile_button).setOnClickListener {
            val intent = Intent(itemView.context, RestaurantDetailActivity::class.java)
            intent.putExtra("place_id", restaurant.place_id)
            itemView.context.startActivity(intent)
        }

         */
    }
}
