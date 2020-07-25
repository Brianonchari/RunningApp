package co.studycode.runningapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.studycode.runningapp.db.Run
import co.studycode.runningapp.repositories.MainRepository
import co.studycode.runningapp.utils.SortType
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
) : ViewModel() {

    private val runSortedByDate = mainRepository.getAllRunsSortedByDate()
    private val runSortedByDistance = mainRepository.getAllRunsSortedByDistance()
    private val runsSortedByAvgSpeed = mainRepository.getAllRunsSortedByAvgSpeed()
    private val runsSortedByCalloriesBurned = mainRepository.getAllRunsSortedByCalloriesBurned()
    private val runSortedByTimeInMillis = mainRepository.getAllRunsSortedByTimeInMilis()

    val runs  = MediatorLiveData<List<Run>>()
    var sortType = SortType.DATE

    init {
        runs.addSource(runSortedByDate){result ->
            if(sortType==SortType.DATE){
                result.let { runs.value = it }
            }
        }

        runs.addSource(runsSortedByAvgSpeed){result ->
            if(sortType==SortType.AVG_SPEED){
                result.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByCalloriesBurned){result ->
            if(sortType==SortType.CALLORIES_BURNED){
                result.let { runs.value = it }
            }
        }

        runs.addSource(runSortedByDistance){result ->
            if(sortType==SortType.DISTANCE){
                result.let { runs.value = it }
            }
        }

        runs.addSource(runSortedByTimeInMillis){result ->
            if(sortType==SortType.RUNNING_TIME){
                result.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType)=when(sortType){
        SortType.DATE -> runSortedByDate.value?.let { runs.value=it }
        SortType.RUNNING_TIME -> runSortedByTimeInMillis.value?.let { runs.value=it }
        SortType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value=it }
        SortType.DISTANCE -> runSortedByDistance.value?.let { runs.value=it }
        SortType.CALLORIES_BURNED -> runsSortedByCalloriesBurned.value?.let { runs.value=it }
    }.also {
        this.sortType = sortType
    }

    fun insertRun(run:Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }


}