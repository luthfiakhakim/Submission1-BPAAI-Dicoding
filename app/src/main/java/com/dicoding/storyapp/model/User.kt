package com.dicoding.storyapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: String,
    val name: String,
    val email: String,
    val password: String,
    val token: String,
    val login: Boolean
): Parcelable