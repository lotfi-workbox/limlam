package com.saeedlotfi.limlam.userInterface.fragments.queue

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.saeedlotfi.limlam.userInterface._common.BaseDialogFragment
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface._common.ItemTouchHelperAdapter
import com.saeedlotfi.limlam.userInterface._common.MediaPlayerCore
import com.saeedlotfi.limlam.userInterface.dependencyInjection.provider.getUserInterfaceComponent
import com.saeedlotfi.limlam.userInterface.layouts.RvListDialogUiNormal
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.UiContext
import com.saeedlotfi.limlam.userInterface.layouts._commom.core.toast
import java.util.*
import javax.inject.Inject

class QueueDialog : BaseDialogFragment() {

    @Inject
    lateinit var userInterface: UiComponentFactory<RvListDialogUiNormal>

    @Inject
    lateinit var adapter: QueueRvAdapter

    @Inject
    lateinit var mediaPlayerCore: MediaPlayerCore

    private var itemTouchHelper: ItemTouchHelper =
        ItemTouchHelper(QueueTouchHelper(object : ItemTouchHelperAdapter {
            override fun onItemMove(fromPosition: Int, toPosition: Int) {
                if (fromPosition < toPosition) {
                    for (i in fromPosition until toPosition) {
                        swapQueueItems(fromPosition, toPosition, true)
                        userInterface.view.rvList.adapter?.notifyItemMoved(fromPosition, toPosition)
                    }
                } else {
                    for (i in fromPosition downTo toPosition + 1) {
                        swapQueueItems(fromPosition, toPosition, false)
                        userInterface.view.rvList.adapter?.notifyItemMoved(fromPosition, toPosition)
                    }
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onItemDismiss(position: Int) {
                if (removeQueueItemAt(position)) {
                    userInterface.view.rvList.adapter?.notifyItemRemoved(position)
                } else {
                    userInterface.view.rvList.adapter?.notifyDataSetChanged()
                }
            }
        }))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        getUserInterfaceComponent().inject(this)
        userInterface.createComponent(UiContext.create(requireContext()), RvListDialogUiNormal::class)
        userInterface.view.btAccept.visibility = View.GONE
        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        configRecyclerView()
        return userInterface.view.rootView
    }

    private fun configRecyclerView() {
        userInterface.view.rvList.layoutManager = LinearLayoutManager(requireContext())
        adapter.onItemClickAction = { key, index ->
            when (key) {
                "playItem" -> {
                    mediaPlayerCore.initMediaPlayer(mediaPlayerCore.queue, index, true)
                }
                "startDrag" -> {
                    itemTouchHelper.startDrag(
                        userInterface.view.rvList.findViewHolderForLayoutPosition(index)!!
                    )
                }
            }
        }
        userInterface.view.rvList.adapter = adapter
        adapter.submitList(mediaPlayerCore.queue)
        itemTouchHelper.attachToRecyclerView(userInterface.view.rvList)
        userInterface.view.rvList.scrollToPosition(mediaPlayerCore.playingIndex)
    }

    private fun swapQueueItems(index1: Int, index2: Int, flag: Boolean) {
        mediaPlayerCore.queue?.also {
            when (mediaPlayerCore.playingIndex) {
                index1 -> {
                    when (flag) {
                        true -> {
                            Collections.swap(mediaPlayerCore.queue!!, index1, index2)
                            mediaPlayerCore.playingIndex += 1
                        }
                        false -> {
                            Collections.swap(mediaPlayerCore.queue!!, index1, index2)
                            mediaPlayerCore.playingIndex -= 1
                        }
                    }
                }
                index2 -> {
                    when (flag) {
                        true -> {
                            Collections.swap(mediaPlayerCore.queue!!, index1, index2)
                            mediaPlayerCore.playingIndex -= 1
                        }
                        false -> {
                            Collections.swap(mediaPlayerCore.queue!!, index1, index2)
                            mediaPlayerCore.playingIndex += 1
                        }
                    }
                }
                else -> {
                    Collections.swap(mediaPlayerCore.queue!!, index1, index2)
                }
            }
        }
    }

    private fun removeQueueItemAt(index: Int): Boolean {
        if (index != mediaPlayerCore.playingIndex) {
            if (index < mediaPlayerCore.playingIndex) {
                mediaPlayerCore.playingIndex--
            }
            mediaPlayerCore.queue?.removeAt(index)
            return true
        }
        userInterface.view.rootView.toast("Track is playing")
        return false
    }

}