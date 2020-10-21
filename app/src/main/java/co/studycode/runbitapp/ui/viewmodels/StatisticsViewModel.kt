package co.studycode.runbitapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import co.studycode.runbitapp.repositories.MainRepository

class StatisticsViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
) : ViewModel() {
    val totalRunTime = mainRepository.getTotalTimeInMillis()
    val totalDistance = mainRepository.getTotalDistance()
    val totalKclBurned = mainRepository.getTotalCalloriesBurned()
    val totalAvgSpeed = mainRepository.getTotalAvgSpeed()
    val runsSortedByDate = mainRepository.getAllRunsSortedByDate()

}