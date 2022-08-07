package com.nhlynn.care_giver.utils

import android.Manifest

object MyConstants {
    var LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    const val ADMIN = "admin"
}