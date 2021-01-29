package com.rafaelescudero.firebase.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.rafaelescudero.firebase.MainActivity2
import com.rafaelescudero.firebase.MapsActivity
import com.rafaelescudero.firebase.R
import kotlinx.android.synthetic.main.activity_s_m_s.numero
import kotlinx.android.synthetic.main.activity_verify.*

class Verify : AppCompatActivity() {

    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        auth= FirebaseAuth.getInstance()

        val storedVerificationId=intent.getStringExtra("storedVerificationId")

        codigoBut.setOnClickListener {

            var codigo = numero.text.toString().trim()

            if(codigo.isNotEmpty()){

                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId.toString(),codigo)
                signInWithPhoneAuthCredential(credential)

            } else {
                Toast.makeText(this,"Inserte el código enviado a su Teléfono.",Toast.LENGTH_LONG).show()
            }

        }


    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential).addOnCompleteListener {

            if (it.isSuccessful){
                startActivity(Intent(applicationContext,MainActivity2::class.java))
                finish()
            } else {
                if (it.exception is FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(this,"El código no es correcto",Toast.LENGTH_LONG).show()
                }
            }

        }

    }
}