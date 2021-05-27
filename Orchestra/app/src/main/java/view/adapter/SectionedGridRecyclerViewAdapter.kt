package view.adapter

import java.util.*
import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver


class SectionedGridRecyclerViewAdapter(context: Context, sectionResourceId: Int, textResourceId: Int, recyclerView: RecyclerView?,
                                       baseAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mContext: Context? = null
    private val SECTION_TYPE = 0

    private var mValid = true
    private var mSectionResourceId = 0
    private var mTextResourceId = 0
    private var mLayoutInflater: LayoutInflater? = null
    private var mBaseAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    private val mSections = SparseArray<Section?>()
    private var mRecyclerView: RecyclerView? = null

    init {
    mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
    mSectionResourceId = sectionResourceId
    mTextResourceId = textResourceId
    mBaseAdapter = baseAdapter
    mContext = context
    mRecyclerView = recyclerView
    mBaseAdapter!!.registerAdapterDataObserver(object : AdapterDataObserver() {
        override fun onChanged() {
            mValid = mBaseAdapter!!.itemCount > 0
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            mValid = mBaseAdapter!!.itemCount > 0
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            mValid = mBaseAdapter!!.itemCount > 0
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            mValid = mBaseAdapter!!.itemCount > 0
            notifyItemRangeRemoved(positionStart, itemCount)
        }
    })
    val layoutManager = mRecyclerView!!.layoutManager as GridLayoutManager?
    layoutManager!!.spanSizeLookup = object : SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            // return if (isSectionHeaderPosition(position)) layoutManager.spanCount else 1
            if (isSectionHeaderPosition(position)) {
                return layoutManager.spanCount
            } else {
                return 1
            }
        }
    }
    }

    class SectionViewHolder(view: View, mTextResourceid: Int) : RecyclerView.ViewHolder(view) {
        var title: TextView

        init {
            title = view.findViewById(mTextResourceid)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, typeView: Int): RecyclerView.ViewHolder {
        return if (typeView == SECTION_TYPE) {
            val view: View =
                LayoutInflater.from(mContext).inflate(mSectionResourceId, parent, false)
            SectionViewHolder(view, mTextResourceId)
        } else {
            mBaseAdapter!!.onCreateViewHolder(parent!!, typeView - 1)
        }
    }

    override fun onBindViewHolder(sectionViewHolder: RecyclerView.ViewHolder, position: Int) {
        if (isSectionHeaderPosition(position)) {
            (sectionViewHolder as SectionViewHolder).title.text = mSections[position]!!.title
        } else {
            mBaseAdapter!!.onBindViewHolder(
                sectionViewHolder,
                sectionedPositionToPosition(position)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        /*
        return if (isSectionHeaderPosition(position)) SECTION_TYPE else mBaseAdapter!!.getItemViewType(
            sectionedPositionToPosition(position)
        ) + 1

         */

        if (isSectionHeaderPosition(position)) {
            return SECTION_TYPE
        } else {
            return mBaseAdapter!!.getItemViewType(sectionedPositionToPosition(position)) + 1
        }
    }


    class Section(var firstPosition: Int, var title: CharSequence) {
        var sectionedPosition = 0
    }


    fun setSections(sections: Array<Section>) {
        mSections.clear()
        Arrays.sort(sections) { o1, o2 -> if (o1!!.firstPosition == o2!!.firstPosition) 0 else if (o1.firstPosition < o2.firstPosition) -1 else 1 }
        var offset = 0 // offset positions for the headers we're adding
        for (section in sections) {
            section.sectionedPosition = section.firstPosition + offset
            mSections.append(section.sectionedPosition, section)
            ++offset
        }
        notifyDataSetChanged()
    }

    fun positionToSectionedPosition(position: Int): Int {
        var offset = 0
        for (i in 0 until mSections.size()) {
            if (mSections.valueAt(i)!!.firstPosition > position) {
                break
            }
            ++offset
        }
        return position + offset
    }

    fun sectionedPositionToPosition(sectionedPosition: Int): Int {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return RecyclerView.NO_POSITION
        }
        var offset = 0
        for (i in 0 until mSections.size()) {
            if (mSections.valueAt(i)!!.sectionedPosition > sectionedPosition) {
                break
            }
            --offset
        }
        return sectionedPosition + offset
    }

    fun isSectionHeaderPosition(position: Int): Boolean {
        if (mSections[position] != null) {
            return true
        } else {
            return false
        }
        // return mSections[position] != null
    }


    override fun getItemId(position: Int): Long {
        /*
        return if (isSectionHeaderPosition(position)) (Int.MAX_VALUE - mSections.indexOfKey(position)).toLong() else mBaseAdapter!!.getItemId(
            sectionedPositionToPosition(position)
        )
         */

        if (isSectionHeaderPosition(position)) {
            return (Int.MAX_VALUE - mSections.indexOfKey(position)).toLong()
        } else {
            return mBaseAdapter!!.getItemId(sectionedPositionToPosition(position))
        }
    }

    override fun getItemCount(): Int {
        //return if (mValid) mBaseAdapter!!.itemCount + mSections.size() else 0
        if (mValid) {
            return mBaseAdapter!!.itemCount + mSections.size()
        } else {
            return 0
        }
    }
}