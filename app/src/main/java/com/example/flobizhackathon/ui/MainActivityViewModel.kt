package com.example.flobizhackathon.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flobizhackathon.model.Items
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.flobizhackathon.repo.Repository


@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _data=MutableLiveData<List<Items?>>()
    val data:LiveData<List<Items?>> = _data
    private val set=HashSet<String>()
    var tagList=ArrayList<String>()
    var clickedTag:String=""

     fun getData(){
         val list=ArrayList<Items>()


         viewModelScope.launch {
            repository.getRemoteData().collect {
                if (it.items==null) return@collect
                for (temp in it.items!!){
                    if (!set.contains(temp!!.questionId)){
                       list.add(temp)
                        temp.questionId?.let { it1 -> set.add(it1) }
                    }
                }
                _data.value=list
            }
        }
    }

}