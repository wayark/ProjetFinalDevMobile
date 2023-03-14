package com.example.projetfinaldevmobile_lucasguenotmerlin

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantDetailActivity : AppCompatActivity() {

    private val api = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GooglePlacesAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        val placeId = intent.getStringExtra("place_id")

        // Appeler l'API pour récupérer les détails du restaurant sélectionné
        placeId?.let { api.getRestaurantDetails(it, apiKey = API_KEY) }
            ?.enqueue(object : Callback<PlaceDetailsResult> {
                override fun onResponse(
                    call: Call<PlaceDetailsResult>,
                    response: Response<PlaceDetailsResult>
                ) {
                    val placeDetailsResult = response.body()
                    if (placeDetailsResult != null) {
                        val restaurant = placeDetailsResult.result
                        if(restaurant != null) {

                            val photoUrl = restaurant.photos?.get(0)?.getUrl()?.toString()
                            Picasso.get().load(photoUrl).into(findViewById<ImageView>(R.id.restaurantImage))
                            findViewById<TextView>(R.id.restaurantName).text = restaurant.name
                            findViewById<TextView>(R.id.restaurantAddress).text = restaurant.formatted_address
                            findViewById<TextView>(R.id.restaurantPhone).text = restaurant.formatted_phone_number
                            findViewById<TextView>(R.id.restaurantWebsite).text = restaurant.website
                            // Ajouter les heures d'ouverture
                            findViewById<TextView>(R.id.restaurantDelivery).text = restaurant.delivery.toString()
                            findViewById<TextView>(R.id.restaurantDineIn).text = restaurant.dine_in.toString()
                            findViewById<TextView>(R.id.restaurantRating).text = restaurant.rating.toString()

                        }

                    }
                }

                override fun onFailure(call: Call<PlaceDetailsResult>, t: Throwable) {
                    // Handle failure
                }
            })
    }
}
