package com.flex.forensics

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult

class KYCProcessor {

    public fun extractKYCText(activity: Activity, context: Context){
        var intent =Intent(context,StillImageActivity::class.java)
        startActivityForResult(activity,intent,1003,null)
    }
}