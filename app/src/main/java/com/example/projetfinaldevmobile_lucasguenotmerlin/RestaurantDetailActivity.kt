package com.example.projetfinaldevmobile_lucasguenotmerlin

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
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
                            if (restaurant.opening_hours?.open_now == true) {
                                findViewById<TextView>(R.id.restaurantOpeningHour).text = "Ouvert"
                            } else {
                                findViewById<TextView>(R.id.restaurantOpeningHour).text = "Fermé"
                                findViewById<TextView>(R.id.restaurantOpeningHour).background = resources.getDrawable(R.drawable.rounded_corner_red)
                            }

                            if (restaurant.delivery == true) {
                                val image = resources.getDrawable(R.drawable.delivery)
                                findViewById<ImageView>(R.id.restaurantDelivery).setImageDrawable(image)
                                findViewById<TextView>(R.id.restaurantDeliveryText).text = "Livraison"
                            }
                            if (restaurant.dine_in == true) {
                                val image = resources.getDrawable(R.drawable.dine_in)
                                findViewById<ImageView>(R.id.restaurantDineIn).setImageDrawable(image)
                                findViewById<TextView>(R.id.restaurantDineInText).text = "À emporter"
                            }
                            findViewById<TextView>(R.id.restaurantRating).text = restaurant.rating.toString()

                            val allPhotos = restaurant.photos
                            if (allPhotos != null) {
                                var i = 1
                                for (photo in allPhotos) {
                                    if (i > 4) {
                                        break
                                    }
                                    val photoUrl = photo.getUrl()?.toString()
                                    val imageView = findViewById<ImageView>(resources.getIdentifier("restaurantImage$i", "id", packageName))
                                    Picasso.get().load(photoUrl).into(imageView)
                                    i++

                                }
                            }
                        }

                    }
                }

                override fun onFailure(call: Call<PlaceDetailsResult>, t: Throwable) {
                    // Handle failure
                }
            })
    }
}

