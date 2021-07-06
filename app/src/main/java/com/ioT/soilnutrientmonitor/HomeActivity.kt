package com.ioT.soilnutrientmonitor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.ioT.soilnutrientmonitor.fragments.AboutFragment
import com.ioT.soilnutrientmonitor.fragments.ContinousSoilData
import com.ioT.soilnutrientmonitor.fragments.PredictFragment
import com.ioT.soilnutrientmonitor.fragments.adapters.ViewPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class HomeActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        setupTabs()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.threedotmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id:Int = item.itemId;
        when(id){
            R.id.signOut -> signOut()
        }
        return true
    }

    private fun signOut(){
        firebaseAuth = FirebaseAuth.getInstance()
        if(firebaseAuth.currentUser != null){
            firebaseAuth.signOut()
            startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun setupTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(PredictFragment(), "Predict")
        adapter.addFragment(AboutFragment(), "About")
        adapter.addFragment(ContinousSoilData(), "Soil Data")
        val viewPager = findViewById<androidx.viewpager.widget.ViewPager>(R.id.viewPager)
        val tabs = findViewById<com.google.android.material.tabs.TabLayout>(R.id.tabs)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }
}