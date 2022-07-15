package com.dicoding.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.model.Preference
import com.dicoding.storyapp.model.User
import kotlinx.coroutines.launch

class MainViewModel(private val preference: Preference) : ViewModel()  {

    fun getUser(): LiveData<User> {
        return preference.getUserLogin().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }
}