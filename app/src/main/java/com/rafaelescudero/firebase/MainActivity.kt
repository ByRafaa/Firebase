package com.rafaelescudero.firebase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rafaelescudero.firebase.ui.main.SMS
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setup()
    }

    public fun setup() {
        register.setOnClickListener {

            if (email.text.isNotEmpty() && pass.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(), pass.text.toString()).addOnCompleteListener {

                    if (it.isSuccessful) {
                        persistencia()
                        sesion()

                    } else {

                        showError()

                    }

                }
            } else {

                Toast.makeText(this, "No puede haber campos vacíos", Toast.LENGTH_LONG).show()

            }

        }
        login.setOnClickListener {

            if (email.text.isNotEmpty() && pass.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(), pass.text.toString()).addOnCompleteListener {

                    if (it.isSuccessful) {
                        persistencia()
                        sesion()

                    } else {

                        showError()

                    }

                }
            } else {

                Toast.makeText(this, "No puede haber campos vacíos", Toast.LENGTH_LONG).show()

            }

        }

        google.setOnClickListener(View.OnClickListener {

            //Configuracion

            var googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

            var googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            var signInIntent = googleClient?.signInIntent

            startActivityForResult(signInIntent, GOOGLE_SIGN_IN)

        })

        telefono.setOnClickListener {

            var intent = Intent(applicationContext, SMS::class.java)
            startActivity(intent)


        }

    }

    public fun sesion() {

        val prefs: SharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)

        if (email == null) {

            //EN ESTE CASO SIGNIFICARIA QUE NO TENEMOS SESION INICIADA, YA QUE NO HAY NINGUN EMAIL EN EL FICHERO

        } else {

            var intent = Intent(applicationContext, MainActivity2::class.java)
            startActivity(intent)

        }

    }


    public fun persistencia() {

        val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email.text.toString())
        prefs.apply()

    }

    public fun showError() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No conectado")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {

            var task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            val account = task.getResult(ApiException::class.java)

            if (account != null) {

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {

                    if (it.isSuccessful) {
                        persistencia()
                        sesion()
                    } else {
                        showError()
                    }

                }
            }
        }
    }
}

    //https://www.youtube.com/watch?v=8-BKXBJ3JL4&feature=youtu.be