package co.studycode.runningapp.utils

import android.graphics.Color

object Constants {
    //Database Constants
    const val APP_DATABASE_NAME = "runningapp_db"
    const val REQUEST_CODE_LOCATION_PERMISSION = 0
    //Service constants
    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"
    //Notification Constants
    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_NAME = "Tracking"
    const val NOTIFICATION_ID = 1
    //Polyline
    const val POLYLINE_COLOR = Color.BLACK
    const val POLYLINE_WIDTH= 8f
    //Location Consts
    const val  LOCATION_UPDATES_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L
    const val MAP_ZOOM = 15f
    const val TIMER_UPDATE_INTERVAL = 50L
    const val SHARED_PREF_NAME = "SharedPref"
    const val KET_FIRST_TIME_TOGLE = "KET_FIRST_TIME_TOGLE"
    const val KEY_NAME = "KEY_NAME"
    const val KEY_WEIGHT="KEY_WEIGHT"
    const val KEY_IMAGE = "KEY_IMAGE"
    const val CANCEL_TRACKING_DIALOG="CANCEL_TRACKING_DIALOG"
    //Camera
    const val REQUEST_PERMISSION = 100
    const val REQUEST_IMAGE_CAPTURE = 1
    const val REQUEST_PICK_IMAGE = 2
}
