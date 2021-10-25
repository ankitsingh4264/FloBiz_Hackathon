package com.example.flobizhackathon

import android.util.Log
import android.widget.SearchView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebouncingSearch(
    private val scope: CoroutineScope,
    private val onDebounceQueryTextChange:(String?)->Unit
) : SearchView.OnQueryTextListener {
    var debouncePeriod: Long = 500
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
     }

    private var searchJob: Job? = null
    override fun onQueryTextChange(newText: String?): Boolean {

//        Log.d("ankit", "onQueryTextChange: $newText ")

        searchJob?.cancel()
       searchJob= scope.launch {
            delay(debouncePeriod)
//           Log.d("ankit", "onQueryexecuted:$newText ")

               onDebounceQueryTextChange(newText)

        }
        return false

    }
}