package com.example.projetfinaldevmobile_lucasguenotmerlin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Connexion : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private val utilisateursRef = db.collection("Utilisateurs")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)

        utilisateursRef.get()
            .addOnSuccessListener { querySnapshot ->
                // Parcourir les documents
                for (document in querySnapshot) {
                    Log.d("Utilisateurs", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("UtilisateursBUG", "Erreur lors de la récupération des documents", exception)
            }


        // Initialiser Firebase Auth
        auth = FirebaseAuth.getInstance()

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val username = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()

            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // L'utilisateur est connecté avec succès, vous pouvez maintenant le rediriger vers l'activité RestaurantListActivity.kt

                        // Ajouter l'utilisateur à Firestore
                        val utilisateur = Utilisateur(username, password)
                        utilisateursRef.add(utilisateur)
                            .addOnSuccessListener { documentReference ->
                                Log.d(TAG, "Document ajouté avec ID : ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Erreur lors de l'ajout du document", e)
                            }
                    } else {
                        // La connexion a échoué, vous pouvez afficher un message d'erreur à l'utilisateur
                        Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        val test = findViewById<Button>(R.id.test)
        test.setOnClickListener {
            val intent = Intent(this, RestaurantListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
