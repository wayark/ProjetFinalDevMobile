package com.example.projetfinaldevmobile_lucasguenotmerlin

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projetfinaldevmobile_lucasguenotmerlin.fragment.HomeFragment
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_connexion.*

class Connexion: AppCompatActivity() {

    val database = Firebase.database("https://devmobile-877ca-default-rtdb.europe-west1.firebasedatabase.app/").reference
    val myRef = database.child("utilisateurs")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)

        btnConnexion.setOnClickListener {

            val pseudo = pseudo.text.toString()
            val password = password.text.toString()

            myRef.orderByChild("pseudo")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (userSnapshot in snapshot.children) {
                                val userPseudo = userSnapshot.child("pseudo").getValue(String::class.java)
                                if (pseudo == userPseudo ) {
                                    if(password != userSnapshot.child("motDePasse").getValue(String::class.java)) {
                                        // Le mot de passe est incorrect
                                        Toast.makeText(this@Connexion, "Mot de passe incorrect", Toast.LENGTH_SHORT).show()
                                        return
                                    }
                                    // L'utilisateur existe, lance RestaurantListActivity avec pseudo en extra
                                    val intent = Intent(this@Connexion, MainActivity::class.java)
                                    intent.putExtra("pseudo", pseudo)
                                    startActivity(intent)
                                    return
                                }
                            }

                            // Si on arrive ici, l'utilisateur n'a pas été trouvé dans la base de données, on le créé
                            val utilisateur = Utilisateur(pseudo, password)
                            myRef.push().setValue(utilisateur)

                            // Lance RestaurantListActivity avec pseudo en extra
                            val intent = Intent(this@Connexion, MainActivity::class.java)
                            intent.putExtra("pseudo", pseudo)
                            startActivity(intent)
                        } else {
                            // La base de données est vide, on créé un nouvel utilisateur
                            val utilisateur = Utilisateur(pseudo, password)
                            myRef.push().setValue(utilisateur)

                            // Lance RestaurantListActivity avec pseudo en extra
                            val intent = Intent(this@Connexion, MainActivity::class.java)
                            intent.putExtra("pseudo", pseudo)
                            startActivity(intent)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "onCancelled", error.toException())
                    }
                })
        }
    }
}


