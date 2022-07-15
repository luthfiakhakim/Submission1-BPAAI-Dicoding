package com.dicoding.storyapp.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityDetailStoryBinding
import com.dicoding.storyapp.model.DataListStory
import com.dicoding.storyapp.viewmodel.DetailStoryViewModel

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var story: DataListStory
    private lateinit var binding: ActivityDetailStoryBinding

    private val viewModel: DetailStoryViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.detail_story)
            setDisplayHomeAsUpEnabled(true)
        }

        story = intent.getParcelableExtra(EXTRA_STORY)!!
        viewModel.setDetailStory(story)

        showDetailStory()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showDetailStory() {
        with(binding){
            name.text = viewModel.listStory.name
            description.text = viewModel.listStory.description
            postedOn.text = getString(R.string.posted_on, viewModel.listStory.createdAt)

            Glide.with(userStory)
                .load(viewModel.listStory.photoUrl)
                .placeholder(R.drawable.ic_baseline_image_24)
                .centerCrop()
                .into(userStory)
        }
    }

    companion object {
        const val EXTRA_STORY = "story"
    }
}