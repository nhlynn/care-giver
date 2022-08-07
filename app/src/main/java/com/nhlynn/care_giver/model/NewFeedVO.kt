package com.nhlynn.care_giver.model

import com.google.gson.annotations.SerializedName

data class NewFeedVO(
    @SerializedName("instruction")
    var instruction: String? = null,
    @SerializedName("caution")
    var caution: String? = null,
    @SerializedName("photo")
    var photo: String? = null
)
