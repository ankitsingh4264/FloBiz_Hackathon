package com.example.flobizhackathon.di

import com.example.flobizhackathon.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule  {
   private val baseUrl="https://api.stackexchange.com/2.2/"

    @Provides
    fun providesRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    fun providesApiService(retrofit: Retrofit):ApiService{
        return retrofit.create(ApiService::class.java)
    }
    private val realmVersion = 2L

    @Singleton
    @Provides
    fun providesRealmConfig(): RealmConfiguration =

        RealmConfiguration.Builder()
            .schemaVersion(realmVersion)
            .build()

    @Provides
    @Singleton
    fun providesRealm(realmConfiguration: RealmConfiguration):Realm{
       return Realm.getInstance(realmConfiguration)
    }
}