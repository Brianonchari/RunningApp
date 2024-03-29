package co.studycode.runbitapp.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "running_table")
data class Run(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    var img: Bitmap? = null,
    var timeStamp: Long = 0L,
    var avgSpeedInKMH: Float = 0f,
    var distanceInMeters: Int = 0,
    var timeInMilis: Long = 0L,
    var caloriesBurned: Int = 0
):Serializable