package com.nhlynn.care_giver.network.response

import com.google.gson.annotations.SerializedName
import com.nhlynn.care_giver.model.UserVO

class LoginResponse : BaseResponse() {
    @SerializedName("user")
    var user: UserVO? = null
}