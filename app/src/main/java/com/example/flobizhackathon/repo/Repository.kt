package com.example.flobizhackathon.repo

import android.util.Log
import com.example.flobizhackathon.ApiService
import com.example.flobizhackathon.model.Response
import io.realm.Realm
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import java.lang.Exception
import javax.inject.Inject


class Repository @Inject constructor(
    val apiService: ApiService,
    val realm: Realm
) {
    private var localData = false


    suspend fun getRemoteData(): Flow<Response> {
        return flow {
            try {


                try {


                    withTimeout(5000) {
                        val data = apiService.getData()
                        emit(data)
                        if (!localData) {
                            localData = true
                            insertData(response = data)

                        }
                    }
                } catch (ex: Exception) {

                    Log.d("ankit", "getRemoteData: ${ex.message}")

                    getLocalData().collect {
                        emit(it!!)
                    }
                }


            } catch (ex: Exception) {
                Log.d("ankit", "getData: ${ex.message} ")
            }
        }
    }

    suspend fun insertData(response: Response) {
        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            realmTransaction.insertOrUpdate(response)
        }

    }

    suspend fun getLocalData(): Flow<Response?> {
        return flow {
            try {
                val temp: Response? = realm.where(Response::class.java).findFirst()
                Log.d("ankit", "getLocalData: $temp ")
                emit(temp)

            } catch (ex: Exception) {

            }

        }
    }

}