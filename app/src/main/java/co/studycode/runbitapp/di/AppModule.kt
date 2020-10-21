package co.studycode.runbitapp.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import co.studycode.runbitapp.db.AppDatabase
import co.studycode.runbitapp.utils.Constants.APP_DATABASE_NAME
import co.studycode.runbitapp.utils.Constants.KET_FIRST_TIME_TOGLE
import co.studycode.runbitapp.utils.Constants.KEY_NAME
import co.studycode.runbitapp.utils.Constants.KEY_WEIGHT
import co.studycode.runbitapp.utils.Constants.SHARED_PREF_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext app:Context) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        APP_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRunDao(db:AppDatabase) = db.getDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context)= app.getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPref:SharedPreferences) = sharedPref.getString(KEY_NAME,"")?:""

    @Singleton
    @Provides
    fun provideWeight(sharedPref:SharedPreferences) = sharedPref.getFloat(KEY_WEIGHT,65f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPref:SharedPreferences) = sharedPref.getBoolean(KET_FIRST_TIME_TOGLE,true)
}