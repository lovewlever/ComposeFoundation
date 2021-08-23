package com.gq.basic.common

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gq.basic.AppContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

object DataStoreCommon {

    val DSK_USER_ID = stringPreferencesKey("userId")
    val DSK_USER_TOKEN = stringPreferencesKey("token")
    // 隐私政策弹窗 1：已同意，0：未同意
    val DSK_PRIVACY_POLICY = intPreferencesKey("privacyPolicy")
    val sp by lazy {
        AppContext.application.getSharedPreferences(AppContext.application.packageName, Activity.MODE_PRIVATE)
    }

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "navigationDataStore")

    fun <T> getEntityBySP(key: Preferences.Key<T>): T? {
        return GsonCommon.gson.fromJson(sp.getString(key.name,""), object : TypeToken<MutableList<T>>() {}.type)
    }

    fun <T> putEntityBySP(key: Preferences.Key<T>, t: T) {
        sp.edit {
            putString(key.name, GsonCommon.gson.toJson(t))
        }
    }

    /*fun <T> getBasicTypeBySP(key: Preferences.Key<T>): T {

    }*/

    fun <T> putBasicTypeBySP(key: Preferences.Key<T>, t: T) {
        sp.edit {
            when(t) {
                is Int -> putInt(key.name, t)
                is Float -> putFloat(key.name, t)
                is Boolean -> putBoolean(key.name, t)
                is String -> putString(key.name, t)
                is Long -> putLong(key.name, t)
            }
        }
    }

    suspend fun clearData() {
        AppContext.application.dataStore.edit {
            it.clear()
        }
    }

    suspend fun <T> putBasicType(key: Preferences.Key<T>, t: T) {
        AppContext.application.dataStore.edit { dataStore ->
            dataStore[key] = t
        }
    }

    suspend inline fun <reified T> getBasicType(
        key: Preferences.Key<T>,
        crossinline callback: (T?) -> Unit = {}
    ) {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.collect { t: T? ->
            withContext(Dispatchers.Main) {
                callback(t)
            }
        }
    }

    suspend fun <T> putEntity(key: Preferences.Key<String>, t: T) {
        AppContext.application.dataStore.edit { dataStore ->
            dataStore[key] = GsonCommon.gson.toJson(t)
        }
    }

    suspend inline fun <reified T> getEntity(
        key: Preferences.Key<String>,
        crossinline callback: (T) -> Unit
    ) {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.collect {
            val fromJson: T = GsonCommon.gson.fromJson(it, T::class.java) ?: T::class.java.newInstance()
            withContext(Dispatchers.Main) {
                callback(fromJson)
            }
        }
    }

    suspend inline fun <T> getEntityList(
        key: Preferences.Key<String>,
        crossinline callback: (MutableList<T>?) -> Unit
    ) {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.collect {
            val fromJson: MutableList<T>? =
                GsonCommon.gson.fromJson(it, object : TypeToken<MutableList<T>>() {}.type)
            withContext(Dispatchers.Main) {
                callback(fromJson)
            }
        }
    }
}