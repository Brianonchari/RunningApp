package co.studycode.runningapp.repositories

import co.studycode.runningapp.db.Profile
import co.studycode.runningapp.db.ProfileDao
import co.studycode.runningapp.db.Run
import co.studycode.runningapp.db.RunDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDao: RunDao,
    val profileDao: ProfileDao
) {
    suspend fun insertRun(run: Run) = runDao.insertRun(run)
    suspend fun saveProfile(profile: Profile) = profileDao.insertProfile(profile)
    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)
    fun getAllRunsSortedByDate() = runDao.getAllRunsSortedByDate()
    fun getAllRunsSortedByDistance() = runDao.getAllRunsSortedByDistanceInMeters()
    fun getAllRunsSortedByTimeInMilis() = runDao.getAllRunsSortedByTimeInMilis()
    fun getAllRunsSortedByAvgSpeed() = runDao.getAllRunsSortedByAvgSpeed()
    fun getAllRunsSortedByCalloriesBurned() = runDao.getAllRunsSortedByCalloriesBurned()

    fun getTotalAvgSpeed() = runDao.getAvgSpeed()
    fun getTotalCalloriesBurned() = runDao.getTotalCalloriesBurned()
    fun getTotalDistance() = runDao.getTotalDistance()
    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()


}