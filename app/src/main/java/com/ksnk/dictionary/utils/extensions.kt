package com.ksnk.dictionary.utils

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ksnk.dictionary.R

fun AppCompatActivity.showToast(text: String) {
    Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.changeFragment(fragment: Fragment){
    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.fragmentContainerViewMain, fragment)
    transaction.addToBackStack(null)
    transaction.commit()
}

fun Fragment.showToast(text: String) {
    Toast.makeText(activity?.applicationContext, text, Toast.LENGTH_SHORT).show()
}