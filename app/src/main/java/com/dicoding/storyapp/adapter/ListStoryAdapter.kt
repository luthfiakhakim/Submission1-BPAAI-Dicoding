package com.dicoding.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ItemRowStoryBinding
import com.dicoding.storyapp.helper.DiffUtils
import com.dicoding.storyapp.model.DataListStory
import com.dicoding.storyapp.view.DetailStoryActivity

class ListStoryAdapter: RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {

    private val listStory = ArrayList<DataListStory>()

    fun setListStory(itemStory: List<DataListStory>) {
        val diffUtils = DiffUtils(this.listStory, itemStory)
        val diffResult = DiffUtil.calculateDiff(diffUtils)

        this.listStory.clear()
        this.listStory.addAll(itemStory)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount() = listStory.size

    inner class ViewHolder(private var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: DataListStory) {
            with(binding) {
                Glide.with(userStory)
                    .load(story.photoUrl)
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .centerCrop()
                    .into(userStory)
                name.text = story.name
                description.text = story.description
                posted.text = binding.root.resources.getString(R.string.posted_on, story.createdAt)

                userStory.setOnClickListener {
                    val intent = Intent(it.context, DetailStoryActivity::class.java)
                    intent.putExtra(DetailStoryActivity.EXTRA_STORY, story)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            it.context as Activity,
                            Pair(userStory, "story"),
                            Pair(name, "name"),
                            Pair(description, "description"),
                            Pair(posted, "posted")
                        )
                    it.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }
}