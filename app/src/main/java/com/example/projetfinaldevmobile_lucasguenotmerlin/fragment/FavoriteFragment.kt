package com.example.projetfinaldevmobile_lucasguenotmerlin.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetfinaldevmobile_lucasguenotmerlin.*
import com.example.projetfinaldevmobile_lucasguenotmerlin.API_KEY
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class FavoriteFragment : Fragment() {

    private lateinit var restaurantAdapter: RestaurantLikeAdapter
    private lateinit var restaurantList: RecyclerView

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var pseudo: String? = null

    val database = Firebase.database("https://devmobile-877ca-default-rtdb.europe-west1.firebasedatabase.app/").reference
    val myRef = database.child("restLike")

    private val api = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GooglePlacesAPI::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_restaurant_like, container, false)
        val context = container?.context

        restaurantList = view.findViewById(R.id.restaurant_like_recycler_view)
        restaurantList.layoutManager = LinearLayoutManager(context)
        restaurantAdapter = RestaurantLikeAdapter(mutableListOf(), pseudo?:"")
        restaurantList.adapter = restaurantAdapter

        // Load first page of data
        if (context != null) {
            loadRestaurants(context)
        }

        return view
    }

    private fun loadRestaurants(context: Context) {
        isLoading = true
        val file = File(context.cacheDir, "restaurants_like.json")
        if (file.exists()) {
            // Si le fichier JSON existe déjà, on charge les données depuis le fichier
            val json = file.readText()
            val placesResult = Gson().fromJson(json, PlacesResult::class.java)
            val places = placesResult.results
            restaurantAdapter.addRestaurants(places)

            Log.d("RestaurantLikeActivity", "Loaded restaurants from cache")
            isLoading = false
            if (places.isEmpty()) {
                isLastPage = true
            }
        } else {
            // Recupere les restaurants like de l'utilisateur
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val restaurantsLike = mutableListOf<String>()
                    for (restaurant in dataSnapshot.children) {
                        val pseudoBD = restaurant.value.toString().split(",")[0].split(":")[1]
                        val id = restaurant.value.toString().split(",")[1].split(":")[1]
                        Log.d("PSEUDO", "$pseudoBD $pseudo")
                        if (pseudoBD == pseudo) {
                            restaurantsLike.add(id)
                        }
                    }
                    // Si l'utilisateur n'a pas de restaurant like, on affiche un message
                    if (restaurantsLike.isEmpty()) {
                        restaurantAdapter.addRestaurants(mutableListOf())
                        isLoading = false
                        isLastPage = true
                    } else {
                        // Sinon on recupere les details de chaque restaurant like
                        for (restaurantLike in restaurantsLike) {
                            api.getRestaurantById(restaurantLike, apiKey = API_KEY).enqueue(object : Callback<PlaceDetailsResult> {
                                override fun onResponse(
                                    call: Call<PlaceDetailsResult>,
                                    response: Response<PlaceDetailsResult>
                                ) {
                                    if (response.isSuccessful) {
                                        val placeDetailsResult = response.body()
                                        if (placeDetailsResult != null) {
                                            val place = placeDetailsResult.result
                                            if (place != null) {
                                                restaurantAdapter.addRestaurant(place)
                                            }
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<PlaceDetailsResult>, t: Throwable) {
                                    Log.e("RestaurantLikeActivity", "Error loading restaurant details", t)
                                }
                            })
                        }
                        isLoading = false
                        if (restaurantsLike.isEmpty()) {
                            isLastPage = true
                        }
                    }
                    
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("RestaurantLikeActivity", "Failed to read value.", error.toException())
                }
            })

        }
    }

    fun setPseudo(pseudo: String) {
        this.pseudo = pseudo
    }

}