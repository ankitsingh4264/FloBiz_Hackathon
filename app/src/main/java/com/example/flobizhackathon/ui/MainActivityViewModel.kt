package com.example.flobizhackathon.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flobizhackathon.model.Items
import com.example.flobizhackathon.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

    private val _data = MutableLiveData<List<Items?>>()
    val data: LiveData<List<Items?>> = _data
    private val set = HashSet<String>()
    var tagList = ArrayList<String>()
    var clickedTag: String = ""
    private var job: Job? = null
    var id = 0;

    suspend fun getData() {
        val list = ArrayList<Items>()
        job?.let {
            if (it.isActive)
                return
        }
        job = viewModelScope.launch {
            dataRepository.getRemoteData().collect {
                if (it.items == null) return@collect
                it.items?.forEachIndexed { _, temp ->
                    temp?.questionId?.let {
                        if (!set.contains(it)) {
                            list.add(temp)
                            set.add(it)
                        }
                    }
                }
                _data.value = list
            }
        }
    }

}