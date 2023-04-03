@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.saeedlotfi.limlam.data.repository

import com.saeedlotfi.limlam.data.database.RealmManager
import com.saeedlotfi.limlam.data.mapper.asDatabaseModel
import com.saeedlotfi.limlam.data.mapper.asDomainModel
import com.saeedlotfi.limlam.data.model.FolderDbModel
import com.saeedlotfi.limlam.domain.model.FolderDoModel
import com.saeedlotfi.limlam.domain.repository.FolderRepository
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepositoryImpl @Inject constructor(private val realm: RealmManager) : FolderRepository {

    override fun getFolderFromDb(id: Long, onComplete: (FolderDoModel?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(FolderDbModel::class.java).equalTo(FolderDbModel::id.name, id)

            if (query.findFirst()?.also {
                    db.copyFromRealm(it).also { fm ->
                        onComplete(fm.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override fun getFoldersFromDb(onComplete: (List<FolderDoModel>?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(FolderDbModel::class.java)
                .notEqualTo(FolderDbModel::visibility.name, false)

            if (query.findAll()?.also {
                    db.copyFromRealm(it).also { pll ->
                        onComplete(pll.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override suspend fun saveFoldersInDb(folders: List<FolderDoModel>, onComplete: () -> Unit) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                val newFolders = folders.map { fm ->
                    val query = it.where(FolderDbModel::class.java)
                        .equalTo(FolderDbModel::id.name, fm.id).findFirst()

                    query?.apply { musics?.addAll(fm.musics!!.asDatabaseModel()) }
                        ?: fm.asDatabaseModel()
                }
                it.insertOrUpdate(newFolders)
            }
        }
    }

    override suspend fun removeFolder(id: Long, onComplete: () -> Unit) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                it.where(FolderDbModel::class.java).equalTo(FolderDbModel::id.name, id)
                    .findAll()
                    ?.deleteAllFromRealm()
            }
        }
    }

}