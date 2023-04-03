package com.saeedlotfi.limlam.userInterface._common

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.*
import com.saeedlotfi.limlam.userInterface.R
import com.saeedlotfi.limlam.userInterface.dependencyInjection.factory.UiComponentFactory
import com.saeedlotfi.limlam.userInterface._common.BaseViewHolder.Companion.from
import com.saeedlotfi.limlam.userInterface.layouts._commom.ThemeManager.getDrawableId
import com.saeedlotfi.limlam.userInterface.layouts._commom.ThemeManager.getModeByModelClass
import com.saeedlotfi.limlam.userInterface.layouts.components.BaseRvItemUiComponent
import com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts.RvGridUiNormal
import com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts.RvLinearFourPicUiNormal
import com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts.RvLinearOnePicUiNormal
import com.saeedlotfi.limlam.userInterface.layouts.rvItemLayouts.RvListHeaderUiNormal
import java.util.*
import javax.inject.Inject

open class MultiViewRvAdapter @Inject constructor(
    private val userInterface: UiComponentFactory<BaseRvItemUiComponent<*, *>>
) : ListAdapter<com.saeedlotfi.limlam.domain.model.AbstractDoModel, RecyclerView.ViewHolder>(HomeDiffCallback()), Filterable {

    protected lateinit var onItemClickListener: (position: Int) -> Unit

    private lateinit var mode: Mode

    private var header: Boolean = false

    private var selectionMode: Boolean = false

    var inputList: MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel> = mutableListOf()
        private set

    var selectedItems: MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel> = mutableListOf()

    protected var backStack: MutableList<Pair<com.saeedlotfi.limlam.domain.model.AbstractDoModel, Int>> = mutableListOf()

    private var recyclerView: RecyclerView? = null

    fun init(
        inputList: MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel>,
        mode: Mode,
        header: Boolean,
        selectionMode: Boolean,
        onItemClickListener: (position: Int) -> Unit
    ) {
        this.mode = mode
        this.header = header
        this.selectionMode = selectionMode
        this.onItemClickListener = onItemClickListener
        this.inputList = inputList
        backStack.clear()
        backStack.add(
            index = 0,
            element = Pair(first = com.saeedlotfi.limlam.domain.model.AbstractDoModel.createInstance(subList = inputList), second = 0)
        )
    }

    fun backToPreviousSelection() {
        if (selectionMode) {
            selectionMode = false
            selectedItems.clear()
        }
        if (backStack.size > 1) {
            val item = backStack.removeFirst()
            showItem(backStack.first().first, item.second, false)
        }
    }

    fun isMainList(): Boolean {
        return backStack.size < 2
    }

    private fun showItem(item: com.saeedlotfi.limlam.domain.model.AbstractDoModel, position: Int, addToBackStack: Boolean) {
        if (item.subList.isNullOrEmpty()) {
            onItemClickListener(position)
        } else {
            submitList(null)
            if (addToBackStack) backStack.add(0, Pair(item, position))
            inputList = item.subList as MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel>
            mode = getModeByModelClass(item.subList?.firstOrNull()?.javaClass)
            header = !isMainList()
            submitList(item.subList)
            recyclerView?.adapter = this
            if (!addToBackStack) {
                recyclerView?.scrollToPosition(position)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
        val displayMetrics = recyclerView.context.resources.displayMetrics
        val density = (displayMetrics.widthPixels / displayMetrics.density).toInt()

        recyclerView.layoutManager = when {
            mode == Mode.GRID -> GridLayoutManager(recyclerView.context, (density / 100)).also {
                if (header) it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        if (position == VIEW_TYPE_HEADER) {
                            return density / GRID_LAYOUT_DENSITY_PER_SPAN
                        }
                        return NORMAL_SCREEN_SPAN_SIZE
                    }
                }
            }

            userInterface.screenSize == UiComponentFactory.ScreenSize.LARGE ->
                GridLayoutManager(recyclerView.context, LARGE_SCREEN_SPAN_SIZE).also {
                    if (header) it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            if (position == VIEW_TYPE_HEADER) {
                                return LARGE_SCREEN_SPAN_SIZE
                            }
                            return NORMAL_SCREEN_SPAN_SIZE
                        }
                    }
                }

            else -> LinearLayoutManager(recyclerView.context)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val def = holder.layoutPosition - position
        fun getRealPosition(pos: Int) = if (header && pos > 0) (pos - def) - 1 else (pos - def)

        if (holder is HeaderViewHolder) return holder.bind(backStack.first().first) {}

        var item = getItem(getRealPosition(holder.layoutPosition))

        (holder as BaseViewHolder).bind(item) { click ->

            val itemPosition = getRealPosition(holder.layoutPosition)
            item = getItem(itemPosition)

            if (!selectionMode && click) return@bind showItem(item, itemPosition, true)

            if (!selectionMode) {
                selectionMode = true
                backStack.add(0, backStack.first().copy())
            }

            if (!holder.view.selected) {
                selectedItems.add(item)
                holder.view.select()
            } else {
                selectedItems.removeAll { it.id == item.id }
                holder.view.unselect()
            }

        }

        if (selectionMode && selectedItems.any { it.id == item.id })
            holder.view.select() else holder.view.unselect()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> from<HeaderViewHolder, RvListHeaderUiNormal>(userInterface, parent)

            VIEW_TYPE_ITEM_GRID -> from<GridViewHolder, RvGridUiNormal>(userInterface, parent)

            VIEW_TYPE_ITEM_LINEAR_ONE_PIC ->
                from<LinearOnePicViewHolder, RvLinearOnePicUiNormal>(userInterface, parent)

            VIEW_TYPE_ITEM_LINEAR_FOUR_PIC ->
                from<LinearFourPicViewHolder, RvLinearFourPicUiNormal>(userInterface, parent)

            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            header && position == 0 -> VIEW_TYPE_HEADER
            else -> {
                when (mode) {
                    Mode.LINEAR_ONE_PICTURE -> VIEW_TYPE_ITEM_LINEAR_ONE_PIC
                    Mode.LINEAR_FOUR_PICTURE -> VIEW_TYPE_ITEM_LINEAR_FOUR_PIC
                    Mode.GRID -> VIEW_TYPE_ITEM_GRID
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (header) super.getItemCount() + 1 else super.getItemCount()
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        when (holder) {
            is HeaderViewHolder -> holder.view.cmptThumbnail.recycleImage()
            is LinearOnePicViewHolder -> holder.view.cmptThumbnail.recycleImage()
            is LinearFourPicViewHolder -> {
                holder.view.cmptPicGroup.cmptThumbnail.recycleImage()
                holder.view.cmptPicGroup.cmptThumbnail.recycleImage()
                holder.view.cmptPicGroup.cmptThumbnail.recycleImage()
                holder.view.cmptPicGroup.cmptThumbnail.recycleImage()
            }
            is GridViewHolder -> holder.view.cmptThumbnail.recycleImage()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filteredList: MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel> = mutableListOf()

                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(inputList)
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.getDefault()).trim()
                    inputList.forEach {
                        if (it.title?.lowercase(Locale.getDefault())
                                ?.contains(filterPattern) == true
                        ) {
                            filteredList.add(it)
                        }
                    }
                }

                return FilterResults().also {
                    it.values = filteredList
                }

            }

            override fun publishResults(
                constraint: CharSequence?, results: FilterResults?
            ) {
                @Suppress("UNCHECKED_CAST")
                submitList(results?.values as MutableList<com.saeedlotfi.limlam.domain.model.AbstractDoModel>)
            }

        }
    }

    class HeaderViewHolder(
        override val view: RvListHeaderUiNormal
    ) : BaseViewHolder(view) {

        override fun bind(
            item: com.saeedlotfi.limlam.domain.model.AbstractDoModel,
            onItemClickListener: (click: Boolean) -> Unit
        ) {
            view.cmptTitle.tvTitle1.text = item.title
            view.cmptTitle.tvTitle2.text = view.rootView.context.getString(
                R.string.list_size, item.subList?.size.toString()
            )
            view.cmptThumbnail.loadThumbnailFromCache(
                item.subList?.firstOrNull()?.id ?: 0,
                getDrawableId(item)
            )
        }
    }

    class LinearOnePicViewHolder(
        override val view: RvLinearOnePicUiNormal
    ) : BaseViewHolder(view) {
        override fun bind(
            item: com.saeedlotfi.limlam.domain.model.AbstractDoModel,
            onItemClickListener: (click: Boolean) -> Unit
        ) {
            view.cmptTitle.tvTitle1.text = item.title
            if (item.details.isNullOrEmpty()) view.cmptTitle.justOneTitle()
            else view.cmptTitle.tvTitle2.text = item.details
            view.cmptThumbnail.loadThumbnailFromCache(item.id, getDrawableId(item))
            view.rootView.setOnClickListener {
                onItemClickListener(true)
            }
            view.rootView.setOnLongClickListener {
                onItemClickListener(false)
                true
            }
        }
    }

    class LinearFourPicViewHolder(
        override val view: RvLinearFourPicUiNormal
    ) : BaseViewHolder(view) {
        override fun bind(
            item: com.saeedlotfi.limlam.domain.model.AbstractDoModel,
            onItemClickListener: (click: Boolean) -> Unit
        ) {
            view.cmptTitle.justOneTitle()
            view.cmptTitle.tvTitle1.text = item.title
            item.subList?.takeIf { it.isNotEmpty() }?.firstOrNull()?.also {
                view.cmptPicGroup.cmptThumbnail.loadThumbnailFromCache(
                    it.id,
                    getDrawableId(item.subList?.firstOrNull())
                )
            }
            item.subList?.takeIf { it.size > 1 }?.get(1)?.also {
                view.cmptPicGroup.cmptThumbnail1.loadThumbnailFromCache(
                    it.id,
                    getDrawableId(item.subList?.firstOrNull())
                )
            }
            item.subList?.takeIf { it.size > 2 }?.get(2)?.also {
                view.cmptPicGroup.cmptThumbnail2.loadThumbnailFromCache(
                    it.id,
                    getDrawableId(item.subList?.firstOrNull())
                )
            }
            item.subList?.takeIf { it.size > 3 }?.get(3)?.also {
                view.cmptPicGroup.cmptThumbnail3.loadThumbnailFromCache(
                    it.id,
                    getDrawableId(item.subList?.firstOrNull())
                )
            }
            view.rootView.setOnClickListener {
                onItemClickListener(true)
            }
            view.rootView.setOnLongClickListener {
                onItemClickListener(false)
                true
            }

        }
    }

    class GridViewHolder(
        override val view: RvGridUiNormal
    ) : BaseViewHolder(view) {
        override fun bind(
            item: com.saeedlotfi.limlam.domain.model.AbstractDoModel,
            onItemClickListener: (click: Boolean) -> Unit
        ) {
            view.textView1.text = item.title
            view.cmptThumbnail.loadThumbnailFromCache(
                item.subList?.firstOrNull()?.id ?: 0,
                getDrawableId(item.subList?.firstOrNull())
            )
            view.rootView.setOnClickListener {
                onItemClickListener(true)
            }
            view.rootView.setOnLongClickListener {
                onItemClickListener(false)
                true
            }
        }
    }

    class HomeDiffCallback : DiffUtil.ItemCallback<com.saeedlotfi.limlam.domain.model.AbstractDoModel>() {

        override fun areItemsTheSame(oldItem: com.saeedlotfi.limlam.domain.model.AbstractDoModel, newItem: com.saeedlotfi.limlam.domain.model.AbstractDoModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: com.saeedlotfi.limlam.domain.model.AbstractDoModel,
            newItem: com.saeedlotfi.limlam.domain.model.AbstractDoModel
        ): Boolean {
            return oldItem.subList == newItem.subList
        }

    }

    enum class Mode {
        LINEAR_ONE_PICTURE, LINEAR_FOUR_PICTURE, GRID
    }

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM_LINEAR_ONE_PIC = 1
        const val VIEW_TYPE_ITEM_LINEAR_FOUR_PIC = 2
        const val VIEW_TYPE_ITEM_GRID = 3
        const val NORMAL_SCREEN_SPAN_SIZE = 1
        const val GRID_LAYOUT_DENSITY_PER_SPAN = 100
        const val LARGE_SCREEN_SPAN_SIZE = 2
    }
}

