@file:Suppress("unused")

package com.saeedlotfi.limlam.data.repository

import com.saeedlotfi.limlam.data.database.RealmManager
import com.saeedlotfi.limlam.data.model.AlbumDbModel
import com.saeedlotfi.limlam.data.mapper.asDatabaseModel
import com.saeedlotfi.limlam.data.mapper.asDomainModel
import com.saeedlotfi.limlam.domain.model.AlbumDoModel
import com.saeedlotfi.limlam.domain.repository.AlbumRepository
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepositoryImpl @Inject constructor(private val realm: RealmManager) : AlbumRepository {

    override fun getAlbumFromDb(id: Long, onComplete: (AlbumDoModel?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(AlbumDbModel::class.java)
                .equalTo(AlbumDoModel::id.name, id)

            if (query.findFirst()?.also {
                    db.copyFromRealm(it).also { am ->
                        onComplete(am.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override fun getAlbumsFromDb(onComplete: (List<AlbumDoModel>?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(AlbumDbModel::class.java)
                .notEqualTo(AlbumDbModel::visibility.name, false)

            if (query.findAll()?.also {
                    db.copyFromRealm(it).also { aml ->
                        onComplete(aml.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override suspend fun saveAlbumsInDb(
        albums: List<AlbumDoModel>,
        onComplete: () -> Unit
    ) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                val newAlbums = albums.map { am ->
                    val query = it.where(AlbumDbModel::class.java)
                        .equalTo(AlbumDbModel::id.name, am.id).findFirst()

                    query?.apply { musics?.addAll(am.musics!!.asDatabaseModel()) }
                        ?: am.asDatabaseModel()
                }
                it.insertOrUpdate(newAlbums)
            }
        }
    }

    override suspend fun removeAlbum(id: Long, onComplete: () -> Unit) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                it.where(AlbumDbModel::class.java).equalTo(AlbumDbModel::id.name, id).findAll()
                    ?.deleteAllFromRealm()
            }
        }
    }

}