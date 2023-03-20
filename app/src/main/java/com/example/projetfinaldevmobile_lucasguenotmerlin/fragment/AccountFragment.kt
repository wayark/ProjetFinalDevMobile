package com.example.projetfinaldevmobile_lucasguenotmerlin.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.projetfinaldevmobile_lucasguenotmerlin.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AccountFragment : Fragment() {

    val database = Firebase.database("https://devmobile-877ca-default-rtdb.europe-west1.firebasedatabase.app/").reference
    val myRef = database.child("utilisateurs")

    private var userKey: String? = null
    private var pseudo: String? = null
    @SuppressLint("MissingInflatedId", "ShowToast")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val accountNickname = view.findViewById<TextView>(R.id.account_nickname)
        val changePasswordButton = view.findViewById<TextView>(R.id.change_password_button)
        val passwordInput = view.findViewById<TextView>(R.id.password_input)

        accountNickname.text = pseudo//change the text of the textview by the nickname of the user

        changePasswordButton.setOnClickListener { view ->
            if(passwordInput.text != null){
                //change the password of the user
                userKey?.let { myRef.child(it).updateChildren(mapOf("motDePasse" to passwordInput.text.toString())).addOnSuccessListener {
                    passwordInput.text = null
                    Toast.makeText(context, "Mot de passe changé", Toast.LENGTH_SHORT).show()
                } }
            }

        }




        return view
    }

    fun setPseudo(pseudo: String) {
        this.pseudo = pseudo
    }
    fun setUserKey(userKey: String) {
        this.userKey = userKey
    }
}