package com.saeedlotfi.limlam.userInterface.fragments.queue

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.UiContext
import com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts.RvQueueUiNormal
import javax.inject.Inject

class QueueRvAdapter @Inject constructor(
    private var userInterface: UiComponentFactory<RvQueueUiNormal>
) : ListAdapter<com.saeedlotfi.limlam.domain.model.MusicDoModel, QueueRvAdapter.ViewHolder>(HomeDiffCallback()) {

    lateinit var onItemClickAction: (key: String, index: Int) -> Unit

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(userInterface, parent)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.view.cmptMain.cmptThumbnail.recycleImage()
        super.onViewRecycled(holder)
    }

    class ViewHolder(
        val view: RvQueueUiNormal
    ) : RecyclerView.ViewHolder(view.rootView) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(
            item: com.saeedlotfi.limlam.domain.model.MusicDoModel,
            onItemClickAction: (key: String, index: Int) -> Unit
        ) {

            if (item.title != "Unknown") {
                view.cmptMain.cmptTitle.tvTitle1.text = item.title
            } else {
                view.cmptMain.cmptTitle.tvTitle1.text = item.name
            }
            view.cmptMain.cmptTitle.tvTitle2.text = view.rootView.context.getString(
                R.string.artist_album, item.artist, item.album
            )
            view.cmptMain.cmptThumbnail.loadThumbnailFromCache(item.id, R.drawable.ic_note)
            view.rootView.setOnClickListener {
                onItemClickAction("playItem", layoutPosition)
            }
            view.cmtDrag.rootView.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        onItemClickAction("startDrag", layoutPosition)
                    }
                }
                true
            }
        }

        companion object {
            fun from(
                userInterface: UiComponentFactory<RvQueueUiNormal>, parent: ViewGroup
            ): ViewHolder {
                return ViewHolder(
                    userInterface.createComponent(
                        UiContext.create(parent.context), RvQueueUiNormal::class
                    )
                )
            }
        }
    }

    class HomeDiffCallback : DiffUtil.ItemCallback<com.saeedlotfi.limlam.domain.model.MusicDoModel>() {

        override fun areItemsTheSame(oldItem: com.saeedlotfi.limlam.domain.model.MusicDoModel, newItem: com.saeedlotfi.limlam.domain.model.MusicDoModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: com.saeedlotfi.limlam.domain.model.MusicDoModel, newItem: com.saeedlotfi.limlam.domain.model.MusicDoModel): Boolean {
            return oldItem.id == newItem.id
        }

    }

}
