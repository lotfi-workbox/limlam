@file:Suppress("unused")

package com.saeedlotfi.limlam.data.repository

import com.saeedlotfi.limlam.data.database.RealmManager
import com.saeedlotfi.limlam.data.mapper.asDatabaseModel
import com.saeedlotfi.limlam.data.mapper.asDomainModel
import com.saeedlotfi.limlam.data.model.ArtistDbModel
import com.saeedlotfi.limlam.domain.model.ArtistDoModel
import com.saeedlotfi.limlam.domain.repository.ArtistRepository
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtistRepositoryImpl @Inject constructor(private val realm: RealmManager) : ArtistRepository {

    override fun getArtistFromDb(id: Long, onComplete: (ArtistDoModel?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(ArtistDbModel::class.java).equalTo(ArtistDbModel::id.name, id)

            if (query.findFirst()?.also {
                    db.copyFromRealm(it).also { pl ->
                        onComplete(pl.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override fun getArtistsFromDb(onComplete: (List<ArtistDoModel>?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(ArtistDbModel::class.java)
                .notEqualTo(
                    ArtistDoModel::visibility.name,
                    false
                )

            if (query.findAll()?.also {
                    db.copyFromRealm(it).also { aml ->
                        onComplete(aml.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }


    override suspend fun saveArtistsInDb(
        artists: List<ArtistDoModel>,
        onComplete: () -> Unit
    ) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                val newArtists = artists.map { am ->
                    val query = it.where(ArtistDbModel::class.java)
                        .equalTo(ArtistDbModel::id.name, am.id).findFirst()

                    query?.apply { albums?.addAll(am.albums!!.asDatabaseModel()) }
                        ?: am.asDatabaseModel()
                }
                it.insertOrUpdate(newArtists)
            }
        }
    }

    override suspend fun removeArtist(id: Long, onComplete: () -> Unit) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                it.where(ArtistDbModel::class.java).equalTo(ArtistDbModel::id.name, id)
                    .findAll()
                    ?.deleteAllFromRealm()
            }
        }
    }

}