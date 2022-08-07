package com.nhlynn.care_giver.model

import com.google.gson.annotations.SerializedName

data class AlarmVO(
    @SerializedName("alid")
    var alId: Int? = null,
    @SerializedName("alarm_time")
    var alarmTime: String? = null
)