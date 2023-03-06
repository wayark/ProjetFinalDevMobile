package com.example.projetfinaldevmobile_lucasguenotmerlin

import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesAPI {
    @GET("/maps/api/place/textsearch/json")
    fun searchRestaurants(
        @Query("query") query: String,
        @Query("type") type: String = "restaurant",
        @Query("key") apiKey: String
    ): retrofit2.Call<PlacesResult>
}
