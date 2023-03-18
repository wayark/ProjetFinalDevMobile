package com.example.projetfinaldevmobile_lucasguenotmerlin

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Connexion: AppCompatActivity() {

    val database = Firebase.database("https://devmobile-877ca-default-rtdb.europe-west1.firebasedatabase.app/").reference
    val myRef = database.child("utilisateurs")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)

        val button = findViewById<Button>(R.id.btnConnexion)
        button.setOnClickListener {

            val pseudo = findViewById<EditText>(R.id.pseudo).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            myRef.orderByChild("pseudo")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (userSnapshot in snapshot.children) {
                                val userPseudo = userSnapshot.child("pseudo").getValue(String::class.java)
                                if (pseudo == userPseudo) {
                                    // L'utilisateur existe, lance RestaurantListActivity avec pseudo en extra
                                    val intent = Intent(this@Connexion, RestaurantListActivity::class.java)
                                    intent.putExtra("pseudo", pseudo)
                                    startActivity(intent)
                                    return
                                }
                            }

                            // Si on arrive ici, l'utilisateur n'a pas été trouvé dans la base de données, on le créé
                            val utilisateur = Utilisateur(pseudo, password)
                            myRef.push().setValue(utilisateur)

                            // Lance RestaurantListActivity avec pseudo en extra
                            val intent = Intent(this@Connexion, RestaurantListActivity::class.java)
                            intent.putExtra("pseudo", pseudo)
                            startActivity(intent)
                        } else {
                            // La base de données est vide, on créé un nouvel utilisateur
                            val utilisateur = Utilisateur(pseudo, password)
                            myRef.push().setValue(utilisateur)

                            // Lance RestaurantListActivity avec pseudo en extra
                            val intent = Intent(this@Connexion, RestaurantListActivity::class.java)
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


