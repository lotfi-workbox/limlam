@file:Suppress("unused")

package com.saeedlotfi.limlam.data.repository

import com.saeedlotfi.limlam.data.database.RealmManager
import com.saeedlotfi.limlam.data.mapper.asDatabaseModel
import com.saeedlotfi.limlam.data.mapper.asDomainModel
import com.saeedlotfi.limlam.data.model.PlayListDbModel
import com.saeedlotfi.limlam.domain.model.MusicDoModel
import com.saeedlotfi.limlam.domain.model.PlayListDoModel
import com.saeedlotfi.limlam.domain.repository.PlayListRepository
import com.saeedlotfi.limlam.domain.repository.PlayListRepository.*
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayListRepositoryImpl @Inject constructor(
    private val realm: RealmManager
) : PlayListRepository {

    override fun getPlayListFromDb(id: Long, onComplete: (PlayListDoModel?) -> Unit) {
        realm.localInstance.also { db ->

            val query =
                db.where(PlayListDbModel::class.java).equalTo(PlayListDbModel::id.name, id)

            if (query.findFirst()?.also {
                    db.copyFromRealm(it).also { pl ->
                        onComplete(pl.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override fun getPlayListsFromDb(onComplete: (List<PlayListDoModel>?) -> Unit) {
        realm.localInstance.also { db ->

            val query = db.where(PlayListDbModel::class.java)
                .notEqualTo(PlayListDoModel::visibility.name, false)

            if (query.findAll()?.also {
                    db.copyFromRealm(it).also { pll ->
                        onComplete(pll.asDomainModel())
                    }
                } == null) {
                onComplete(null)
            }
        }
    }

    override suspend fun savePlayListsInDb(
        playlists: List<PlayListDoModel>,
        onComplete: () -> Unit
    ) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                it.insertOrUpdate(playlists.asDatabaseModel())
            }
        }
    }

    override suspend fun removePlayList(id: Long, onComplete: () -> Unit) {
        realm.localInstance.also { db ->
            db.addChangeListener {
                db.removeAllChangeListeners()
                onComplete()
            }
            db.executeTransactionAwait {
                it.where(PlayListDbModel::class.java).equalTo(PlayListDbModel::id.name, id)
                    .findAll()
                    ?.deleteAllFromRealm()
            }
        }
    }

    override suspend fun addToRecentlyPlayed(music: MusicDoModel, onComplete: () -> Unit) {
        var pm: PlayListDoModel? = null

        getPlayListFromDb(Defaults.RecentlyPlayed.name.hashCode().toLong()) {
            pm = it
        }


        if (pm == null) {
            savePlayListsInDb(
                listOf(
                    PlayListDoModel(
                        id = Defaults.RecentlyPlayed.name.hashCode().toLong(),
                        name = Defaults.RecentlyPlayed.displayedName,
                        musics = mutableListOf(music)
                    )
                ),
                onComplete
            )
        } else {
            pm!!.musics?.removeAll { it.id == music.id }
            if ((pm!!.musics?.size ?: 0) == RECENTLY_PLAYED_MAX_SIZE)
                pm!!.musics?.removeAt(49)
            pm!!.musics?.add(0, music)
            savePlayListsInDb(listOf(pm!!), onComplete)
        }
    }

    override suspend fun addOrRemoveFromFavourites(music: MusicDoModel, onComplete: () -> Unit) {
        var pm: PlayListDoModel? = null

        getPlayListFromDb(Defaults.Favourites.name.hashCode().toLong()) {
            pm = it
        }

        if (pm == null) {
            music.favourite = true
            savePlayListsInDb(
                listOf(
                    PlayListDoModel(
                        id = Defaults.Favourites.name.hashCode().toLong(),
                        name = Defaults.Favourites.displayedName,
                        musics = mutableListOf(music)
                    )
                ),
                onComplete
            )
        } else {
            if (music.favourite || pm!!.musics?.any { it.id == music.id } == true) {
                music.favourite = false
                pm!!.musics?.removeAll { it.id == music.id }
            } else {
                music.favourite = true
                pm!!.musics?.add(0, music)
            }
            realm.localInstance.executeTransactionAwait { db ->
                db.insertOrUpdate(music.asDatabaseModel())
            }
            savePlayListsInDb(listOf(pm!!), onComplete)
        }
    }

    companion object {
        const val RECENTLY_PLAYED_MAX_SIZE = 50
    }

}