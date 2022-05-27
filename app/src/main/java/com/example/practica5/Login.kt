package com.example.practica5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        var mail: EditText = findViewById<EditText>(R.id.mail)
        var pass: EditText = findViewById<EditText>(R.id.pass)
        val login: Button = findViewById<Button>(R.id.login)

        login.setOnClickListener {
            //ralejandrobm@gmail.com
            //alex11
            auth.signInWithEmailAndPassword(mail.text.toString(), pass.text.toString()).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Usuario Autenticado", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if(currentUser == null) {
            Toast.makeText(this, "No hay usuarios autenticados", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Bienvenido de vuelta", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun revisarUsuario(usuario:FirebaseUser?)
    {
        if(usuario==null){
            Toast.makeText(this, "No hay usuarios autenticados", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Inicie sesión", Toast.LENGTH_SHORT).show()
        }
    }
}