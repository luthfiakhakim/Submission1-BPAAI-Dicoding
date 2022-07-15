package com.dicoding.storyapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.helper.ApiCallbackString
import com.dicoding.storyapp.helper.isEmailValid
import com.dicoding.storyapp.model.Preference
import com.dicoding.storyapp.viewmodel.LoginViewModel
import com.dicoding.storyapp.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        loginViewModel()
        buttonEnable()
        editTextListener()
        buttonLogin()
        progressBar()
        playAnimation()

        binding.setting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

    private fun loginViewModel() {
        loginViewModel = ViewModelProvider(this, ViewModelFactory(Preference.getInstance(dataStore)))[LoginViewModel::class.java]
    }

    private fun editTextListener() {
        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                buttonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                buttonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }


    private fun buttonEnable() {
        val resultEmail = binding.email.text
        val resultPass = binding.password.text

        binding.btnLogin.isEnabled = resultPass != null && resultEmail != null && binding.password.text.toString().length >= 6 && isEmailValid(binding.email.text.toString())
    }

    private fun showDialogInformation(param: Boolean, message: String) {
        if (param) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.login_failed) +", $message")
                setPositiveButton(getString(R.string.try_again)) { _, _ ->
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                }
                create()
                show()
            }
        }
    }

    private fun buttonLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            loginViewModel.login(email, pass, object : ApiCallbackString {
                override fun onResponse(success: Boolean,message: String) {
                    showDialogInformation(success, message)
                }
            })
        }
    }

    private fun progressBar() {
        loginViewModel.isLoading.observe(this) {
            binding.apply {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    btnLogin.visibility = View.INVISIBLE
                }
                else progressBar.visibility = View.GONE
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgHeader, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val welcome = ObjectAnimator.ofFloat(binding.welcome, View.ALPHA, 1f).setDuration(500)
        val greeting = ObjectAnimator.ofFloat(binding.greetings, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val account = ObjectAnimator.ofFloat(binding.account, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.register, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(welcome, greeting, emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout, login, account, register)
            startDelay = 500
        }.start()
    }
}