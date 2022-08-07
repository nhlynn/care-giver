package com.nhlynn.care_giver.network.response

import com.google.gson.annotations.SerializedName
import com.nhlynn.care_giver.model.HospitalVO

class HospitalResponse : BaseResponse() {
    @SerializedName("nearest_hospital")
    var nearestHospitals: ArrayList<HospitalVO>? = null
}