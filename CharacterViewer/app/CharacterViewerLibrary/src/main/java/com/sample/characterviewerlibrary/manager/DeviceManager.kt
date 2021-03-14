package com.sample.characterviewerlibrary.manager

import android.content.Context
import android.telephony.TelephonyManager
import java.util.*

object DeviceManager {

    fun isTabletDevice(context: Context) : Boolean {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return Objects.requireNonNull(manager).phoneType == TelephonyManager.PHONE_TYPE_NONE
    }
}