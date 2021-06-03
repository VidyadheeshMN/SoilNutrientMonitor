package com.example.soilnutrientmonitor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.soilnutrientmonitor.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    private lateinit var googleSignInBtn: com.google.android.gms.common.SignInButton
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        googleSignInBtn = findViewById(R.id.googleSignInBtn)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        firebaseAuth = FirebaseAuth.getInstance()

        googleSignInBtn.setOnClickListener{
            Log.d(TAG, "begin google sign in")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
            checkUser()
        }
    }

    private fun checkUser(){
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            startActivity(Intent(this@MainActivity, PredictActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            Log.d(TAG, "Google sign in intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            }
            catch (e: Exception){
                Log.d(TAG,"sign in failed")
                Toast.makeText(this@MainActivity, "login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?){
        Log.d(TAG, "begin firebase auth with google account")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                Log.d(TAG, "login successful")

                val firebaseUser = firebaseAuth.currentUser
                val uid = firebaseAuth!!.uid
                val email = firebaseUser!!.email

                Log.d(TAG, "uid: ${uid}")
                Log.d(TAG, "email: ${email}")

                if(authResult.additionalUserInfo!!.isNewUser){
                    Log.d(TAG, "account created with \n$email address")
                    Toast.makeText(this@MainActivity, "Account created with \n$email address",
                        Toast.LENGTH_SHORT).show()
                }
                else{
                    Log.d(TAG, "existing user with \n$email address")
                    Toast.makeText(this@MainActivity, "logged in with existing email ${email}", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener{ e->
            Log.d(TAG, "firebaseAuthWithGoogleAccount: login failed due to ${e.message}")
                Toast.makeText(this@MainActivity, "login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}