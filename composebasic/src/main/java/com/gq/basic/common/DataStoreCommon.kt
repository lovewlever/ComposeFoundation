package com.gq.basic.common

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.reflect.TypeToken
import com.gq.basic.AppContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

object DataStoreCommon {

    /**
     * 是否可以申请权限的弹窗
     * @see com.gq.basic.compose.PermissionConfirmAgree
     * @see com.gq.basic.compose.PermissionConfirmReject
     * @see com.gq.basic.compose.PermissionConfirmNotSet
     */
    val DSK_PERMISSION_CONFIRM = intPreferencesKey("PermissionConfirm")
    // 隐私政策弹窗 1：已同意，0：未同意
    val DSK_PRIVACY_POLICY = intPreferencesKey("privacyPolicy")
    val sp by lazy {
        AppContext.application.getSharedPreferences(
            AppContext.application.packageName,
            Activity.MODE_PRIVATE
        )
    }

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "navigationDataStore")


    @Deprecated("")
    fun <T> getEntityListBySP(key: String): List<T>? {
        return GsonCommon.gson.fromJson(
            sp.getString(key, ""),
            object : TypeToken<MutableList<T>>() {}.type
        )
    }

    @Deprecated(
        "", ReplaceWith(
            "GsonCommon.gson.fromJson(sp.getString(key, \"\"), T::class.java)",
            "com.gq.basic.common.DataStoreCommon.sp"
        )
    )
    inline fun <reified T> getEntityBySP(key: String): T? {
        return GsonCommon.gson.fromJson(
            sp.getString(key, ""),
            T::class.java
        )
    }

    @Deprecated(
        "", ReplaceWith(
            "GsonCommon.gson.fromJson(sp.getString(key, \"\"), T::class.java)",
            "com.gq.basic.common.DataStoreCommon.sp"
        )
    )
    fun <T> putEntityBySP(key: String, t: T) {
        sp.edit {
            putString(key, GsonCommon.gson.toJson(t))
        }
    }

    @Deprecated(
        "", ReplaceWith(
            "GsonCommon.gson.fromJson(sp.getString(key, \"\"), T::class.java)",
            "com.gq.basic.common.DataStoreCommon.sp"
        )
    )
    inline fun <reified T> getBasicTypeBySP(key: String, default: T): T {
        return when (T::class) {
            Int::class -> {
                sp.getInt(key, default as Int) as T
            }
            Float::class -> {
                sp.getFloat(key, default as Float) as T
            }
            Boolean::class -> {
                sp.getBoolean(key, default as Boolean) as T
            }
            String::class -> {
                sp.getString(key, default as String) as T
            }
            Long::class -> {
                sp.getLong(key, default as Long) as T
            }
            else -> {
                throw TypeCastException("不支持的类型")
            }
        }

    }

    @Deprecated(
        "", ReplaceWith(
            "GsonCommon.gson.fromJson(sp.getString(key, \"\"), T::class.java)",
            "com.gq.basic.common.DataStoreCommon.sp"
        )
    )
    fun <T> putBasicTypeBySP(key: String, t: T) {
        sp.edit {
            when (t) {
                is Int -> putInt(key, t)
                is Float -> putFloat(key, t)
                is Boolean -> putBoolean(key, t)
                is String -> putString(key, t)
                is Long -> putLong(key, t)
            }
        }
    }

    @Deprecated(
        "", ReplaceWith(
            "GsonCommon.gson.fromJson(sp.getString(key, \"\"), T::class.java)",
            "com.gq.basic.common.DataStoreCommon.sp"
        )
    )
    suspend fun clearDataAndSP() {
        clearData()
        clearDataBySP()
    }

    @Deprecated(
        "", ReplaceWith(
            "GsonCommon.gson.fromJson(sp.getString(key, \"\"), T::class.java)",
            "com.gq.basic.common.DataStoreCommon.sp"
        )
    )
    suspend fun clearDataBySP() {
        sp.edit { clear() }
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
        crossinline callback: (T?) -> Unit
    ) {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.collect { t: T? ->
            withContext(Dispatchers.Main) {
                callback(t)
            }
        }
    }


    suspend inline fun <reified T> getBasicType(
        key: Preferences.Key<T>,
        default: T,
        crossinline callback: (T) -> Unit
    ) {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.collect { t: T? ->
            withContext(Dispatchers.Main) {
                callback(t ?: default)
            }
        }
    }

    suspend inline fun <reified T> getBasicType(
        key: Preferences.Key<T>,
        default: T
    ): T {
        return AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.first() ?: default
    }

    suspend fun <T> putEntity(key: Preferences.Key<String>, t: T) {
        AppContext.application.dataStore.edit { dataStore ->
            dataStore[key] = GsonCommon.gson.toJson(t)
        }
    }

    suspend fun <T> putGenerics(key: Preferences.Key<T>, t: T) {
        AppContext.application.dataStore.edit { dataStore ->
            dataStore[key] = t
        }
    }

    suspend fun <T> getGenerics(key: Preferences.Key<T>) {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.first()
    }

    suspend fun <T> getGenerics(key: Preferences.Key<T>, callback: (T?) -> Unit) {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.collect { t ->
            callback(t)
        }
    }

    suspend inline fun <reified T> getEntity(
        key: Preferences.Key<String>,
        crossinline callback: (T?) -> Unit
    ) {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.collect {
            val fromJson: T? =
                GsonCommon.gson.fromJson(it, T::class.java)
            withContext(Dispatchers.Main) {
                callback(fromJson)
            }
        }
    }

    suspend inline fun <reified T> getEntity(
        key: Preferences.Key<String>
    ): T? {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.first().let {
            val fromJson: T? =
                GsonCommon.gson.fromJson(it, T::class.java)
            return fromJson
        }
    }

    suspend inline fun <T> getEntityList(
        key: Preferences.Key<String>,
        crossinline callback: (MutableList<T>) -> Unit
    ) {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.collect {
            val fromJson: MutableList<T>? =
                GsonCommon.gson.fromJson(it, object : TypeToken<MutableList<T>>() {}.type)
            withContext(Dispatchers.Main) {
                callback(fromJson ?: mutableListOf())
            }
        }
    }

    suspend inline fun <T> getEntityList(
        key: Preferences.Key<String>
    ): MutableList<T> {
        AppContext.application.dataStore.data.map { preferences: Preferences ->
            preferences[key]
        }.first().let {
            val fromJson: MutableList<T>? =
                GsonCommon.gson.fromJson(it, object : TypeToken<MutableList<T>>() {}.type)
            return fromJson ?: mutableListOf()
        }
    }
}