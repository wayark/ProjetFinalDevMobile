package com.example.projetfinaldevmobile_lucasguenotmerlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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

    fun bind(restaurant: Place) {
        // Bind restaurant data to view
        itemView.findViewById<TextView>(R.id.restaurant_name).text = restaurant.name
        itemView.findViewById<TextView>(R.id.restaurant_address).text = restaurant.formatted_address
        itemView.findViewById<TextView>(R.id.restaurant_open_now).text = if (restaurant.opening_hours?.open_now == true) "Open now" else "Closed"
        // Set image using a library like Picasso or Glide
        // itemView.findViewById<ImageView>(R.id.restaurant_image).load(restaurant.imageUrl)
        // Handle click listeners on buttons
        // itemView.findViewById<Button>(R.id.like_button).setOnClickListener { /* handle like button click */ }
        // itemView.findViewById<Button>(R.id.profile_button).setOnClickListener { /* handle profile button click */ }
    }
}
