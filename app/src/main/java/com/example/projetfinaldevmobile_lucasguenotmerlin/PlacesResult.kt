package com.example.projetfinaldevmobile_lucasguenotmerlin

data class PlacesResult(
    val results: List<Place>,
    val status: String
)

data class Place(
    val place_id: String,
    val name: String,
    val formatted_address: String,
    val opening_hours: OpeningHours?,
    val rating: Float?,
    val photos: List<Photo>?,
    val geometry: Geometry?,
    val formatted_phone_number: String?,
    val website: String?,
    val delivery: Boolean?,
    val dine_in: Boolean?
)

class Geometry {
    val location: Location? = null

    data class Location(
        val lat: Double,
        val lng: Double
    )
}

data class OpeningHours(
    val open_now: Boolean,
    val weekday_text: List<String>?
)

data class Photo(
    val photo_reference: String
) {
    fun getUrl(): Any {
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$photoReference&key=$API_KEY"
    }

    val photoReference: String
        get() = photo_reference
}

