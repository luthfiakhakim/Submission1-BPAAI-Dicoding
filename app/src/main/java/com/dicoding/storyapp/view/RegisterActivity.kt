package com.dicoding.storyapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.helper.ApiCallbackString
import com.dicoding.storyapp.helper.isEmailValid
import com.dicoding.storyapp.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        buttonEnable()
        editTextListener()
        buttonRegister()
        progressBar()
        playAnimation()

        binding.setting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun editTextListener() {
        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                buttonEnable()
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
        binding.btnRegister.isEnabled = binding.email.text.toString().isNotEmpty() && binding.password.text.toString().isNotEmpty() && binding.password.text.toString().length >= 6 && isEmailValid(binding.email.text.toString())
    }

    private fun buttonRegister() {
        binding.btnRegister.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            registerViewModel.register(name, email, password, object : ApiCallbackString {
                override fun onResponse(success: Boolean, message: String) {
                    showDialogInformation(success, message)
                }
            })
        }
    }

    private fun showDialogInformation(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.registration_success))
                setPositiveButton(getString(R.string._continue)) { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.registration_failed)+", $message")
                setPositiveButton(getString(R.string.try_again)) { _, _ ->
                    binding.progressBar.visibility = View.GONE
                }
                create()
                show()
            }
        }
    }

    private fun progressBar() {
        registerViewModel.isLoading.observe(this) {
            binding.apply {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    btnRegister.visibility = View.INVISIBLE
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

        val create = ObjectAnimator.ofFloat(binding.createAcc, View.ALPHA, 1f).setDuration(500)
        val greeting = ObjectAnimator.ofFloat(binding.greetings, View.ALPHA, 1f).setDuration(500)
        val nameTextView = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.name, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val account = ObjectAnimator.ofFloat(binding.account, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.login, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(create, greeting, nameTextView, nameEditTextLayout, emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout, register, account, login)
            startDelay = 500
        }.start()
    }
}