package com.nhlynn.care_giver.delegate

import com.nhlynn.care_giver.model.AlarmVO

interface AlarmDelegate {
    fun onCloseAlarm(alarm: AlarmVO)
}