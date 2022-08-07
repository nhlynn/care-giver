package com.nhlynn.care_giver.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserVO(
    @SerializedName("user_id")
    var userId: Int? = null,
    @SerializedName("user_name")
    var userName: String? = null,
    @SerializedName("contact")
    var contact: String? = null
) : Parcelable
