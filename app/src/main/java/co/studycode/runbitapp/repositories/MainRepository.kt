package co.studycode.runbitapp.repositories

import co.studycode.runbitapp.db.Run
import co.studycode.runbitapp.db.RunDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDao: RunDao
) {
    suspend fun insertRun(run: Run) = runDao.insertRun(run)
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