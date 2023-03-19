package com.example.projetfinaldevmobile_lucasguenotmerlin

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class RestaurantListActivity : AppCompatActivity() {

    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var restaurantList: RecyclerView

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1

    private val api = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GooglePlacesAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        // Récupère le pseudo de l'utilisateur
        val pseudo = intent.getStringExtra("pseudo")

        // Set up RecyclerView and adapter
        restaurantList = findViewById(R.id.restaurant_recycler_view)
        restaurantList.layoutManager = LinearLayoutManager(this)
        restaurantAdapter = RestaurantAdapter(mutableListOf(), pseudo ?: "")
        restaurantList.adapter = restaurantAdapter

        // Load first page of data
        loadRestaurants(this)

        // Add scroll listener for infinite scrolling
        restaurantList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        currentPage++
                        loadRestaurants(this@RestaurantListActivity)
                    }
                }
            }
        })
    }

    private fun loadRestaurants(context: Context) {
        isLoading = true
        val file = File(context.cacheDir, "restaurants.json")
        if (file.exists()) {
            // Si le fichier JSON existe déjà, on charge les données depuis le fichier
            val json = file.readText()
            val placesResult = Gson().fromJson(json, PlacesResult::class.java)
            val places = placesResult.results
            restaurantAdapter.addRestaurants(places)

            Log.d("RestaurantListActivity", "Loaded restaurants from cache")
            isLoading = false
            if (places.isEmpty()) {
                isLastPage = true
            }
        } else {
            // Si le fichier JSON n'existe pas encore, on fait l'appel à l'API
        val call = api.searchRestaurants("restaurant in Lyon", apiKey = API_KEY)
        call.enqueue(object : Callback<PlacesResult> {
            override fun onResponse(call: Call<PlacesResult>, response: Response<PlacesResult>) {
                val placesResult = response.body()
                if (placesResult != null) {
                    val places = placesResult.results
                    restaurantAdapter.addRestaurants(places)

                    Log.d("RestaurantListActivity", "Loaded ${places.size} restaurants")

                    // Enregistrer les données dans le fichier JSON
                    val json = Gson().toJson(placesResult)
                    file.writeText(json)

                    isLoading = false
                    if (places.isEmpty()) {
                        isLastPage = true
                    }
                }
            }

            override fun onFailure(call: Call<PlacesResult>, t: Throwable) {
                // Handle failure
            }
        })

        }
    }
}
