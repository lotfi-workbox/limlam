package com.saeedlotfi.limlam.userInterface.fragments.playlists

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface._common.MultiViewRvAdapter
import com.saeedlotfi.limlam.userInterface.layouts.components.BaseRvItemUiComponent
import javax.inject.Inject

class PlayListsRvAdapter @Inject constructor(
    userInterface: UiComponentFactory<BaseRvItemUiComponent<*, *>>
) : MultiViewRvAdapter(userInterface) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        fun createItemAndBind(holder: LinearOnePicViewHolder, title: String) {
            holder.view.cmptOption.rootView.visibility = View.GONE
            holder.bind(com.saeedlotfi.limlam.domain.model.AbstractDoModel.createInstance(title)) {
                onItemClickListener(position)
            }
        }

        if (isMainList() && position == DEFAULT_ITEM_POS_CREATE_PLAYLIST)
            createItemAndBind(holder as LinearOnePicViewHolder, "Create Play List")
        else if (isMainList()) super.onBindViewHolder(holder, position - DEFAULT_ITEMS_COUNTS)
        else super.onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isMainList() && position == DEFAULT_ITEM_POS_CREATE_PLAYLIST)
            VIEW_TYPE_ITEM_LINEAR_ONE_PIC
        else if (isMainList()) super.getItemViewType(position - DEFAULT_ITEMS_COUNTS)
        else super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return if (isMainList()) super.getItemCount() + DEFAULT_ITEMS_COUNTS else super.getItemCount()
    }

    fun updateOneItemFormParents(item: com.saeedlotfi.limlam.domain.model.AbstractDoModel) {
        val parentListIndex = backStack.indexOfFirst { it.first.javaClass == item.javaClass } + 1
        val itemIndex = backStack[parentListIndex].first.subList?.indexOfFirst { it.id == item.id }
        if (itemIndex != null && itemIndex != -1)
            (backStack[parentListIndex].first.subList as? MutableList)?.set(itemIndex, item)
    }

    fun getCurrentlyDisplayedItem() = backStack.firstOrNull()?.first

    companion object {
        const val DEFAULT_ITEM_POS_CREATE_PLAYLIST = 0
        const val DEFAULT_ITEMS_COUNTS = 1
    }

}

