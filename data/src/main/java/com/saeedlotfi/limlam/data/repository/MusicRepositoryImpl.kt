@file:Suppress("unused")

package com.saeedlotfi.limlam.data.repository

import com.saeedlotfi.limlam.data.database.RealmManager
import com.saeedlotfi.limlam.data.mapper.asDatabaseModel
import com.saeedlotfi.limlam.data.mapper.asDomainModel
import com.saeedlotfi.limlam.data.model.MusicDbModel
import com.saeedlotfi.limlam.domain.model.MusicDoModel
import com.saeedlotfi.limlam.domain.repository.MusicRepository
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepositoryImpl @Inject constructor(private val realm: RealmManager) : MusicRepository {

    override fun getMusicFromDb(id: Long, onComplete: (MusicDoModel?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(MusicDbModel::class.java).equalTo(MusicDbModel::id.name, id)

            if (query.findFirst()?.also {
                    db.copyFromRealm(it).also { mm ->
                        onComplete(mm.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override fun getMusicsFromDb(onComplete: (List<MusicDoModel>?) -> Unit) {
        realm.localInstance.also { db ->
            val query = db.where(MusicDbModel::class.java)
                .notEqualTo(MusicDbModel::visibility.name, false)

            if (query.findAll()?.also {
                    db.copyFromRealm(it).also { mml ->
                        onComplete(mml.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override suspend fun saveMusicsInDb(
        musics: List<MusicDoModel>,
        onComplete: (newSaved : MutableList<MusicDoModel>) -> Unit
    ) {
        realm.localInstance.also { db ->
            val inserted: MutableList<MusicDoModel> = mutableListOf()
            db.executeTransactionAwait {
                musics.forEach { mm ->
                    try {
                        it.insert(mm.asDatabaseModel())
                        inserted.add(mm)
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
                onComplete(inserted)
            }
        }
    }

    override suspend fun removeMusic(id: Long, onComplete: () -> Unit) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                it.where(MusicDbModel::class.java).equalTo(MusicDbModel::id.name, id).findAll()
                    ?.deleteAllFromRealm()
            }
        }
    }

}