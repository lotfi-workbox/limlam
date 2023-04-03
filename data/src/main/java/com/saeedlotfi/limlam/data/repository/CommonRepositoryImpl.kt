@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.saeedlotfi.limlam.data.repository

import com.saeedlotfi.limlam.data.database.RealmManager
import com.saeedlotfi.limlam.data.mapper.asDatabaseModel
import com.saeedlotfi.limlam.data.mapper.asDomainModel
import com.saeedlotfi.limlam.data.model.StatesDbModel
import com.saeedlotfi.limlam.domain.model.StateDoModel
import com.saeedlotfi.limlam.domain.repository.CommonRepository
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommonRepositoryImpl @Inject constructor(private val realm: RealmManager) : CommonRepository {

    override fun getStateFromDb(id: Long, onComplete: (StateDoModel?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(StatesDbModel::class.java).equalTo(StatesDbModel::id.name, id)

            if (query.findFirst()?.also {
                    db.copyFromRealm(it).also { fm ->
                        onComplete(fm.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override suspend fun saveStateInDb(state: StateDoModel, onComplete: (StateDoModel) -> Unit) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete(state)
            }
            db.executeTransactionAwait {
                it.insertOrUpdate(state.asDatabaseModel())
            }
        }
    }

}