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
    val photos: List<Photo>?
)

data class OpeningHours(
    val open_now: Boolean
)

data class Photo(
    val photo_reference: String
) {
    fun getUrl(): Any {
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$photoReference&key=API_KEY"
    }

    val photoReference: String
        get() = photo_reference
}

