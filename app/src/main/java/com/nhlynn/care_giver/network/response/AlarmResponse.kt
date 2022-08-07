package com.nhlynn.care_giver.network.response

import com.google.gson.annotations.SerializedName
import com.nhlynn.care_giver.model.AlarmVO

class AlarmResponse : BaseResponse() {
    @SerializedName("alarm")
    var alarm: ArrayList<AlarmVO>? = null
}