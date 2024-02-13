package com.adiluhung.mobilejournaling.ui.utils

fun validateEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validatePassword(password: String): Boolean {
    return password.length >= 8
}

fun validateForm(inputList: List<String>): Boolean {
    var isValid = true
    inputList.forEach {
        isValid = isValid && it.isNotEmpty()
    }
    return isValid
}