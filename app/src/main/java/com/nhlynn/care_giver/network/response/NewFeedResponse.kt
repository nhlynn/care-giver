package com.nhlynn.care_giver.network.response

import com.google.gson.annotations.SerializedName
import com.nhlynn.care_giver.model.NewFeedVO

class NewFeedResponse : BaseResponse() {
    @SerializedName("new_feed")
    var newFeed: ArrayList<NewFeedVO>? = null
}