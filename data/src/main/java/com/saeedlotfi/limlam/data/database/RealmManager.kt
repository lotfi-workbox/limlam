package com.saeedlotfi.limlam.data.database

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

class RealmManager @Inject constructor() {

    companion object {
        fun initAndConfig(context: Context) {
            Realm.init(context)
            val config = RealmConfiguration.Builder()
                .schemaVersion(2)
                .migration(DbMigration())
                .build()
            Realm.setDefaultConfiguration(config)
        }
    }

    private val localRealms = ThreadLocal<Realm?>()

    val localInstance: Realm
        get() = localRealms.get() ?: openInstance()

    private fun openInstance(): Realm {
        checkDefaultConfiguration()
        val realm = Realm.getDefaultInstance()
        if (localRealms.get() == null) {
            localRealms.set(realm)
        }
        return realm
    }

    private fun checkDefaultConfiguration() {
        checkNotNull(Realm.getDefaultConfiguration()) { "No default configuration is set." }
    }
}