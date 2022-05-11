package com.screenrecorder.activity.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseActivity<T : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {
    protected lateinit var dataBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, layoutId)
    }

    protected fun loadFragment(oldId: Int, fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(oldId, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    protected fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}