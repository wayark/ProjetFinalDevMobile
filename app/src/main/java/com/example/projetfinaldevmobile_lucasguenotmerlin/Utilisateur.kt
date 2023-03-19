package com.example.projetfinaldevmobile_lucasguenotmerlin

data class Utilisateur(
    val pseudo: String = "",
    val motDePasse: String = ""
)

data class RestLike(
    val pseudo: String = "",
    val id: String = "",
    val idRestLike: String = "$pseudo|$id"
){
    override fun toString(): String {
        return idRestLike
    }

}


