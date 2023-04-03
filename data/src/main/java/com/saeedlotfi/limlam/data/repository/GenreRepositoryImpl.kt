@file:Suppress("unused")

package com.saeedlotfi.limlam.data.repository

import com.saeedlotfi.limlam.data.database.RealmManager
import com.saeedlotfi.limlam.data.mapper.asDatabaseModel
import com.saeedlotfi.limlam.data.mapper.asDomainModel
import com.saeedlotfi.limlam.data.model.GenreDbModel
import com.saeedlotfi.limlam.domain.model.GenreDoModel
import com.saeedlotfi.limlam.domain.repository.GenreRepository
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepositoryImpl @Inject constructor(private val realm: RealmManager) : GenreRepository {

    override fun getGenreFromDb(id: Long, onComplete: (GenreDoModel?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(GenreDbModel::class.java).equalTo(GenreDbModel::id.name, id)

            if (query.findFirst()?.also {
                    db.copyFromRealm(it).also { am ->
                        onComplete(am.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override fun getGenresFromDb(onComplete: (List<GenreDoModel>?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(GenreDbModel::class.java)
                .notEqualTo(GenreDbModel::visibility.name, false)

            if (query.findAll()?.also {
                    db.copyFromRealm(it).also { gml ->
                        onComplete(gml.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override suspend fun saveGenresInDb(
        genres: List<GenreDoModel>,
        onComplete: () -> Unit
    ) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                val newGenres = genres.map { gm ->
                    val query = it.where(GenreDbModel::class.java)
                        .equalTo(GenreDbModel::id.name, gm.id).findFirst()

                    query?.apply { musics?.addAll(gm.musics!!.asDatabaseModel()) }
                        ?: gm.asDatabaseModel()
                }
                it.insertOrUpdate(newGenres)
            }
        }
    }

    override suspend fun removeGenre(id: Long, onComplete: () -> Unit) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                it.where(GenreDbModel::class.java).equalTo(GenreDbModel::id.name, id).findAll()
                    ?.deleteAllFromRealm()
            }
        }
    }

}