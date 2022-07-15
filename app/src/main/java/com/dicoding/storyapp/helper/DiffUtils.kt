package com.dicoding.storyapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.storyapp.model.DataListStory

class DiffUtils(private val mOldList: List<DataListStory>, private val mNewList: List<DataListStory>) : DiffUtil.Callback() {
    override fun getOldListSize() = mOldList.size
    override fun getNewListSize() = mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        mOldList[oldItemPosition].id == mNewList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = mOldList[oldItemPosition]
        val newItem = mNewList[newItemPosition]
        return oldItem.id == newItem.id
    }
}