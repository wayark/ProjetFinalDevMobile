package com.example.projetfinaldevmobile_lucasguenotmerlin

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesAPI {
    @GET("/maps/api/place/textsearch/json")
    fun searchRestaurants(
        @Query("query") query: String,
        @Query("type") type: String = "restaurant",
        @Query("fields") fields: String = "place_id,name,formatted_address,rating,opening_hours,photos",
        @Query("key") apiKey: String
    ): Call<PlacesResult>

    @GET("/maps/api/place/details/json")
    fun getRestaurantDetails(
        @Query("place_id") placeId: String,
        @Query("fields") fields: String = "name,formatted_address,geometry,photo,opening_hours,formatted_phone_number,website,delivery,dine_in,rating",
        @Query("key") apiKey: String
    ): Call<PlaceDetailsResult>

}
