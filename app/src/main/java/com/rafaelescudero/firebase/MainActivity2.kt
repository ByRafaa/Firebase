package com.rafaelescudero.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {

    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        auth = FirebaseAuth.getInstance()
        var currentUser=auth.currentUser

        if (currentUser==null){

            startActivity(Intent(this,MainActivity::class.java))
            finish()

        }

        logoutBut.setOnClickListener {

            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()

        }

    }
}