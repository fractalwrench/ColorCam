package com.fractalwrench.colorcam.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

class IntentUtils {

    companion object {

        fun launchPlayStoreListing(context: Context) {
            // see http://stackoverflow.com/questions/10816757

            val packageName = context.packageName
            val uri = Uri.parse("market://details?id=" + packageName)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)))
            }
        }
    }

}
