package com.example.navigationcomponentapp.extensions

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.navigationcomponentapp.R

private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_ride,)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

fun NavController.navigateWithAnimastions(destinationId: Int){
        this.navigate(destinationId,null, navOptions)
}