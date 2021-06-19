package view.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import java.util.*
import kotlin.Comparator


class SimpleSectionedRecyclerViewAdaper(context: Context, sectionResourceId: Int, textResourceId: Int, baseAdapter: SimpleAdapter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mContext: Context? = null
    private val SECTION_TYPE = 0

    private var mValid = true
    private var mSectionResourceId = 0
    private var mTextResourceId = 0
    private var mLayoutInflater: LayoutInflater? = null
    private var mBaseAdapter: SimpleAdapter
    private val mSections = SparseArray<Section?>()
/*
    fun SimpleSectionedRecyclerViewAdaper(
        context: Context, sectionResourceId: Int, textResourceId: Int,
        baseAdapter: RecyclerView.Adapter<*>?
    ) {
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        mSectionResourceId = sectionResourceId
        mTextResourceId = textResourceId
        mBaseAdapter = baseAdapter
        mContext = context
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
    }
*/
    init {
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        mSectionResourceId = sectionResourceId
        mTextResourceId = textResourceId
        mBaseAdapter = baseAdapter
        mContext = context
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
    }

    class SectionViewHolder(view: View, mTextResourceid: Int) : RecyclerView.ViewHolder(view) {
        var title: TextView

        init {
            title = view.findViewById(mTextResourceid)
        }
    }

    override fun onBindViewHolder(sectionViewHolder: RecyclerView.ViewHolder, position: Int) {
        if (isSectionHeaderPosition(position)) {
            (sectionViewHolder as SectionViewHolder).title.text = mSections[position]!!.title
        } else {
            mBaseAdapter!!.onBindViewHolder(
                    (sectionViewHolder as SimpleAdapter.SimpleViewHolder),
                sectionedPositionToPosition(position)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isSectionHeaderPosition(position)) SECTION_TYPE else mBaseAdapter!!.getItemViewType(
            sectionedPositionToPosition(position)
        ) + 1
    }


    class Section(var firstPosition: Int, var title: CharSequence) {
        var sectionedPosition = 0
    }

    fun setSections(sections: Array<Section>) {
        mSections.clear()
        Arrays.sort(sections, object : Comparator<Section?> {
            override fun compare(o1: Section?, o2: Section?): Int {
                return if (o1!!.firstPosition == o2!!.firstPosition) 0 else if (o1.firstPosition < o2.firstPosition) -1 else 1
            }
        })
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
        return mSections[position] != null
    }

    override fun getItemId(position: Int): Long {
        return if (isSectionHeaderPosition(position)) (Int.MAX_VALUE - mSections.indexOfKey(position)).toLong() else mBaseAdapter!!.getItemId(
            sectionedPositionToPosition(position)
        )
    }

    override fun getItemCount(): Int {
        return if (mValid) mBaseAdapter!!.itemCount + mSections.size() else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SECTION_TYPE) {
            val view: View =
                LayoutInflater.from(mContext).inflate(mSectionResourceId, parent, false)
            SectionViewHolder(view, mTextResourceId)
        } else {
            mBaseAdapter!!.onCreateViewHolder(parent!!, viewType - 1)
        }
    }
}