package com.example.projetfinaldevmobile_lucasguenotmerlin.models

class Utilisateur(var pseudo: String, var mdp: String) {

    constructor() : this("", "")

    // Getters personnalisés
    val getPseudo: String
        get() = pseudo

    val getMdp: String
        get() = mdp
}

