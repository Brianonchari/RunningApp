package co.studycode.runningapp.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var profileImg: Bitmap? = null,
    var userName: String,
    var weight: Float
) {
    constructor(
        userName: String,
        weight: Float,
        profileImg: Bitmap?
    ):this(null, profileImg,userName, weight)
}
