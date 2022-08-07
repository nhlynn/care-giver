package com.nhlynn.care_giver.network.response

import com.google.gson.annotations.SerializedName

open class BaseResponse {
    @SerializedName("status")
    var status: Int? = null

    @SerializedName("message")
    var message: String? = null
}