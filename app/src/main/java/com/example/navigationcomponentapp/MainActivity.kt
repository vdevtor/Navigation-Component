package com.example.navigationcomponentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.navigationcomponentapp.ui.profile.ProfileFragment
import com.example.navigationcomponentapp.ui.start.StartFragment

class MainActivity : AppCompatActivity(), StartFragment.OnButtonClicked {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
                .add(R.id.container,StartFragment.newInstance())
                .commit()
    }

    override fun onclicked() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container,ProfileFragment.newInstance()).commit()
    }
}