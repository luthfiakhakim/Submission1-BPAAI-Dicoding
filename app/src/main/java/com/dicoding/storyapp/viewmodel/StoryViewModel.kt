package com.dicoding.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.helper.Event
import com.dicoding.storyapp.model.DataListStory
import com.dicoding.storyapp.model.UserStoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {
    private val _itemStory = MutableLiveData<List<DataListStory>>()
    val itemStory: LiveData<List<DataListStory>> = _itemStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isGetData = MutableLiveData<Boolean>()
    val isGetData: LiveData<Boolean> = _isGetData

    private val _snackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = _snackBarText

    fun showListStory(token: String) {
        _isLoading.value = true
        _isGetData.value = true
        val client = ApiConfig
            .getApiService()
            .getAllStories("Bearer $token")

        client.enqueue(object : Callback<UserStoryResponse> {
            override fun onResponse(
                call: Call<UserStoryResponse>,
                response: Response<UserStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (!responseBody.error) {
                            _itemStory.value = response.body()?.listStory
                            _isGetData.value = responseBody.message == "Story successfully fetched"
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _snackBarText.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<UserStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _snackBarText.value = Event(t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "StoryViewModel"
    }
}