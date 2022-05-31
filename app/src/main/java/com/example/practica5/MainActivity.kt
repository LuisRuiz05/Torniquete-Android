package com.example.practica5

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.practica5.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    val database = Firebase.database
    val myRef = database.getReference("claves")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //////////////////////////
        /*myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value
                Log.d(TAG, "Value is: " + value)
                snapshot.children.forEach() { hijo ->
                    //Log.d(TAG,"hijo default: " + hijo.key)
                    //Log.d(TAG,"hijo acual: " + myRef.child("-N2RKqYoq7wo8opZWQtq").toString())

                    Log.d(TAG, (hijo.key == "-N2RKqYoq7wo8opZWQtq").toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read a value", error.toException())
            }
        })*/

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val salir: Button = binding.botonSalir
        auth = Firebase.auth
        salir.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        binding.camera.setOnClickListener {
            Toast.makeText(this, "Abriendo cámara", Toast.LENGTH_LONG).show()
            leerCodigo()
        }
    }

     private fun leerCodigo() {
         //IntentIntegrator(this).initiateScan()
         val integrator = IntentIntegrator(this)
         integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
         integrator.setPrompt("Lectura de QRs")
         //integrator.setTorchEnabled(true)
         //integrator.setBeepEnabled(true)
         integrator.setTimeout(10000)
         integrator.initiateScan()
     }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data:
    Intent?) {
        var isAccepted = false
        val result = IntentIntegrator.parseActivityResult(requestCode,
            resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                binding.lectura.text = result.contents.toString()
                myRef.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.value
                        Log.d(TAG, "Value is: " + value)

                        snapshot.children.forEach() { hijo ->
                            var clave: Claves? = hijo.getValue(Claves::class.java)
                            if (hijo.key == result.contents.toString()){
                                if (clave != null) {
                                    Log.d(TAG, "Clave servidor: " + hijo.key)
                                    Log.d(TAG, "Clave escaneada: " + result.contents.toString())
                                    Log.d(TAG, "Status: " + clave.status)
                                    if (clave.status == "libre") {
                                        myRef.child(result.contents.toString()).child("status").setValue("ocupado")
                                        myRef.child(result.contents.toString()).child("usuario").setValue(auth.uid)
                                        startActivity(Intent(this@MainActivity, Successful::class.java))
                                        isAccepted = true
                                        finish()
                                    }
                                }
                            }
                        }
                        if(!isAccepted) {
                            startActivity(Intent(this@MainActivity, Denied::class.java))
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(TAG, "Failed to read a value", error.toException())
                    }
                })
                return
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        finish()
    }
}