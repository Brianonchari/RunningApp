package co.studycode.runningapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProfile(profile: Profile)

    @Query("SELECT * FROM Profile")
    suspend fun getUserProfile():Profile
}