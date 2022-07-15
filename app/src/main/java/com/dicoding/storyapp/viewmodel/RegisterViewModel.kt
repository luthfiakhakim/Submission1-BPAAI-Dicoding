package com.dicoding.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.helper.ApiCallbackString
import com.dicoding.storyapp.model.ApiResponse
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val loading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = loading

    fun register(name: String, email: String, pass: String, callback: ApiCallbackString){
        loading.value = true

        val service = ApiConfig().getApiService().register(name, email, pass)
        service.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                loading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error)
                        callback.onResponse(response.body() != null, SUCCESS)

                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")

                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                loading.value = false
                Log.e(TAG, "onFailure2: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
        private const val SUCCESS = "success"
    }
}