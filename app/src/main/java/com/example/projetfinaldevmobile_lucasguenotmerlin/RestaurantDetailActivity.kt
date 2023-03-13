package com.example.projetfinaldevmobile_lucasguenotmerlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var api: GooglePlacesAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        val placeId =
            intent.getStringExtra("place_id") // Récupérer l'ID de l'emplacement du restaurant sélectionné

        // Appeler l'API pour récupérer les détails du restaurant sélectionné
        placeId?.let { api.getRestaurantDetails(it, apiKey = "API_KEY") }
            ?.enqueue(object : Callback<PlaceDetailsResult> {
                override fun onResponse(
                    call: Call<PlaceDetailsResult>,
                    response: Response<PlaceDetailsResult>
                ) {
                    val placeDetailsResult = response.body()
                    if (placeDetailsResult != null) {
                        val restaurant = placeDetailsResult.result
                        // Afficher les informations détaillées du restaurant dans les vues appropriées
                        // ...

                    }
                }

                override fun onFailure(call: Call<PlaceDetailsResult>, t: Throwable) {
                    // Handle failure
                }
            })
    }
}
