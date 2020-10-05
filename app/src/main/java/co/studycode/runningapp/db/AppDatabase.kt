package co.studycode.runningapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Run::class, Profile::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase :RoomDatabase(){
    abstract fun getDao():RunDao
    abstract fun getProfileao():ProfileDao
}