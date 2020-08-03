package ru.dmitry.moviesearch.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*
import ru.dmitry.moviesearch.R

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private var mSignInButton: SignInButton? = null
    private var mSignInClient: GoogleSignInClient? = null

    // Firebase instance variables
    private var mFirebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Set click listeners
        sign_in_button.setOnClickListener(this)

        // Configure Google Sign In
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sign_in_button -> signIn()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent in signIn()
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id)
        val credential: AuthCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mFirebaseAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this
            ) { task ->
                Log.d(
                    TAG,
                    "signInWithCredential:onComplete:" + task.isSuccessful
                )

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful) {
                    Log.w(
                        TAG,
                        "signInWithCredential",
                        task.exception
                    )
                    Toast.makeText(
                        this@SignInActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish()
                }
            }
    }

    companion object {
        private const val TAG = "SignInActivity"
        private const val RC_SIGN_IN = 9001
    }
}