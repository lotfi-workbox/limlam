@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.saeedlotfi.limlam.data.repository

import com.saeedlotfi.limlam.data.database.RealmManager
import com.saeedlotfi.limlam.data.mapper.asDatabaseModel
import com.saeedlotfi.limlam.data.mapper.asDomainModel
import com.saeedlotfi.limlam.data.model.ThemeDbModel
import com.saeedlotfi.limlam.domain.model.ThemeDoModel
import com.saeedlotfi.limlam.domain.repository.ThemeRepository
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepositoryImpl @Inject constructor(private val realm: RealmManager) : ThemeRepository {

    override fun getThemeFromDb(id: Long, onComplete: (ThemeDoModel?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(ThemeDbModel::class.java).equalTo(ThemeDbModel::id.name, id)

            if (query.findFirst()?.also {
                    db.copyFromRealm(it).also { fm ->
                        onComplete(fm.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override fun getThemesFromDb(onComplete: (List<ThemeDoModel>?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(ThemeDbModel::class.java)

            if (query.findAll()?.also {
                    db.copyFromRealm(it).also { pll ->
                        onComplete(pll.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override suspend fun saveThemesInDb(themes: List<ThemeDoModel>, onComplete: () -> Unit) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                it.insertOrUpdate(themes.asDatabaseModel())
            }
        }
    }

    override suspend fun removeTheme(id: Long, onComplete: () -> Unit) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                it.where(ThemeDbModel::class.java).equalTo(ThemeDbModel::id.name, id)
                    .findAll()
                    ?.deleteAllFromRealm()
            }
        }
    }

}