package com.rafaelescudero.firebase.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.rafaelescudero.firebase.*
import com.rafaelescudero.firebase.R
import kotlinx.android.synthetic.main.activity_s_m_s.*
import java.util.concurrent.TimeUnit

class SMS : AppCompatActivity() {

    lateinit var callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var auth:FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken:PhoneAuthProvider.ForceResendingToken

    var view = MainActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_s_m_s)

        auth = FirebaseAuth.getInstance()

        var currentUser = auth.currentUser
        if (currentUser != null) {
            //startActivity(Intent(applicationContext, MainActivity::class.java))
            //finish()
        }

        numeroBut.setOnClickListener {
            login()
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, MainActivity2::class.java))
                finish()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(applicationContext,"Failed",Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                var intent = Intent(applicationContext,Verify::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                startActivity(intent)
            }
        }

    }


    private fun login() {
        var number=numero.text.toString().trim()

        if(!number.isEmpty()){

            number="+34"+number
            sendVerificationCode(number)

        } else {
            Toast.makeText(this,"Inserte el número de teléfono.",Toast.LENGTH_LONG).show()
        }
    }

    private fun sendVerificationCode(number: String) {

        val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(number).setTimeout(60L,TimeUnit.SECONDS).setActivity(this).setCallbacks(callbacks).build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
}