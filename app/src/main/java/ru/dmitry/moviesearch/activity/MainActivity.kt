package ru.dmitry.moviesearch.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home.*
import ru.dmitry.moviesearch.R


class MainActivity : AppCompatActivity() {

    companion object {
        var userName: String? = null
        var email: String? = null
        val LIKE = "LIKE"
        val BOOKMARK = "BOOKMARK"
        var mFirebaseAuth: FirebaseAuth? = null
        var mFirebaseUser: FirebaseUser? = null
        var mLikeDatabaseReference: DatabaseReference? = null
        var mBookmarkDatabaseReference: DatabaseReference? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val appBarConfig = AppBarConfiguration(setOf(R.id.search_movie_fragment, R.id.profile_fragment))
        val navController = findNavController(R.id.fragment)
        setupActionBarWithNavController(navController, appBarConfig)
        bottom_navigation_view.setupWithNavController(navController)

        // Initialize Firebase Auth
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth!!.currentUser
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
        userName = mFirebaseUser!!.displayName
        email = mFirebaseUser!!.email

        mLikeDatabaseReference = FirebaseDatabase.getInstance().getReference(LIKE)
        mBookmarkDatabaseReference = FirebaseDatabase.getInstance().getReference(BOOKMARK)
    }
}