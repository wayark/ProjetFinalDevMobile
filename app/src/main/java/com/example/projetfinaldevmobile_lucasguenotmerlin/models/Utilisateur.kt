package com.example.projetfinaldevmobile_lucasguenotmerlin.models

class Utilisateur(var pseudo: String, var mdp: String) {

    constructor() : this("", "")

    // Getters personnalisés
    val getPseudo: String
        get() = pseudo

    val getMdp: String
        get() = mdp
}

data class User(val username: String? = null, val email: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}

