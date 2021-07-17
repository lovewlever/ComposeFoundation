package com.gq.basic.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.reflect.TypeToken
import com.gq.basic.AppContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BasicDataStore @Inject constructor(
    val basicGson: BasicGson
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataStore")

    fun clearData() {
        coroutineScope.launch {
            AppContext.application.dataStore.edit {
                it.clear()
            }
        }
    }

    suspend fun <T> putEntity(key: Preferences.Key<String>, t: T) {
        AppContext.application.dataStore.edit { dataStore ->
            dataStore[key] = basicGson.gson.toJson(t)
        }
    }


    suspend inline fun <reified T> getEntity(
        key: Preferences.Key<String>,
        crossinline callback: (T?) -> Unit
    ) {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.collect { json ->
            val entity = basicGson.gson.fromJson(json, T::class.java)
            withContext(Dispatchers.Main) {
                callback(entity)
            }
        }
    }

    suspend inline fun <T> getEntityList(
        key: Preferences.Key<String>,
        crossinline callback: (MutableList<T>) -> Unit
    ) {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.collect { json ->
            val fromJson: MutableList<T> =
                basicGson.gson.fromJson(json, object : TypeToken<MutableList<T>>() {}.type) ?: mutableListOf()
            withContext(Dispatchers.Main) {
                callback(fromJson)
            }
        }
    }
}