package com.pcodelight.quadrant.view.screen

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.pcodelight.joindesign.AuthHelper
import com.pcodelight.quadrant.R
import com.pcodelight.quadrant.viewmodel.LoginViewModel
import com.pcodelight.quadrant.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_login_screen.*

class LoginScreen : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        if (AuthHelper.instance.getAuthToken()?.isNotBlank() == true) {
            gotoDashboardScreen()
            finish()
        }

        initViewModel()
        initView()
    }

    private fun initViewModel() {
        loginViewModel = ViewModelFactory().create(LoginViewModel::class.java).apply {
            isLoading.observe(this@LoginScreen, isLoadingObserver)
            onError.observe(this@LoginScreen, errorObserver)
            onSuccess.observe(this@LoginScreen, isSuccessObserver)
        }
    }

    private val isLoadingObserver = Observer<Boolean> {
        etPass.isEnabled = !it
        etUser.isEnabled = !it
        btnLogin.isEnabled = !it
        tvError.visibility = if (it) View.GONE else View.VISIBLE
    }

    private val errorObserver = Observer<String> {
        if (it.isNotBlank()) {
            tvError.visibility = View.VISIBLE
            tvError.text = it
        } else {
            tvError.visibility = View.GONE
        }
    }

    private val isSuccessObserver = Observer<String> {
        if (it.isNotBlank()) {
            gotoDashboardScreen()
        }
    }

    private fun gotoDashboardScreen() {
        startActivity(Intent(this@LoginScreen, DashboardScreen::class.java))
        finish()
    }

    private fun getPasswordText() = etPass.text.toString()
    private fun getUsernameText() = etUser.text.toString()
    private fun initView() {
        btnLogin.setOnClickListener {
            val username = getUsernameText()
            val password = getPasswordText()

            loginViewModel.login(username, password)
        }

        val btnLoginEnabler = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                btnLogin.isEnabled =
                    getPasswordText().isNotBlank() && getUsernameText().isNotBlank()
            }
        }

        etPass.addTextChangedListener(btnLoginEnabler)
        etUser.addTextChangedListener(btnLoginEnabler)
    }
}
