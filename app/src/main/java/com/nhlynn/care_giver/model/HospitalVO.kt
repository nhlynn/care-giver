package com.nhlynn.care_giver.model

import com.google.gson.annotations.SerializedName

data class HospitalVO(
    @SerializedName("hospital_name")
    var hospitalName: String? = null,
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("address")
    var address: String? = null
)
