package com.ahmrh.storyapp.data.local.preferences

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Login(
    val name: String,
    val userId: String,
    val token: String
) : Parcelable
