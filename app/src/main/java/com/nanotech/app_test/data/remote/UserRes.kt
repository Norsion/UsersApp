package com.nanotech.app_test.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserRes(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
): Parcelable