package com.example.navigationcomponentapp.extensions

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.disMissError() {
    this.error = null
    this.isErrorEnabled = false
}