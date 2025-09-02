package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityHomeBinding
import com.example.myapplication.launch.FeaturesFragment
import com.example.myapplication.launch.PrivacyTermsFragment
import com.example.myapplication.launch.WelcomeFragment

class HomeActivity : AppCompatActivity(),
    WelcomeFragment.OnNextClickListener,
    FeaturesFragment.OnNextClickListener,
    PrivacyTermsFragment.OnFinishClickListener {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            window.statusBarColor = android.graphics.Color.WHITE
            window.navigationBarColor = android.graphics.Color.WHITE
            window.decorView.systemUiVisibility =
                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                        android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            window.statusBarColor = android.graphics.Color.BLACK
            window.navigationBarColor = android.graphics.Color.BLACK
        }


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, WelcomeFragment())
                .commit()
        }
    }

    override fun onNextClicked() {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, FeaturesFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onFeaturesNextClicked() {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, PrivacyTermsFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onFinishClicked() {

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
