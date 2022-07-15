package com.dicoding.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.helper.ApiCallbackString
import com.dicoding.storyapp.model.LoginResponse
import com.dicoding.storyapp.model.Preference
import com.dicoding.storyapp.model.User
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val preference: Preference) : ViewModel() {

    private val loading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = loading

    fun login(email: String, pass: String, callback: ApiCallbackString){
        loading.value = true

        val service = ApiConfig().getApiService().login(email, pass)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {

                        callback.onResponse(response.body() != null, SUCCESS)

                        val model = User(
                            responseBody.loginResult.name,
                            email,
                            pass,
                            responseBody.loginResult.userId,
                            responseBody.loginResult.token,
                            true
                        )
                        saveUser(model)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")

                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            preference.saveUserLogin(user)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
        private const val SUCCESS = "success"
    }
}