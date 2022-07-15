package com.dicoding.storyapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.adapter.ListStoryAdapter
import com.dicoding.storyapp.databinding.ActivityStoryBinding
import com.dicoding.storyapp.model.User
import com.dicoding.storyapp.viewmodel.StoryViewModel
import com.google.android.material.snackbar.Snackbar

class StoryActivity : AppCompatActivity() {
    private var _binding: ActivityStoryBinding? = null
    private val binding get() = _binding

    private lateinit var user: User
    private lateinit var adapter: ListStoryAdapter

    private val viewModel by viewModels<StoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.apply {
            title = getString(R.string.user_story)
            setDisplayHomeAsUpEnabled(true)
        }

        progressBar()
        noStory()
        showSnackBar()
        uploadStory()

        user = intent.getParcelableExtra(EXTRA_USER)!!
        setListStory()

        adapter = ListStoryAdapter()
        binding?.rvStory?.layoutManager = LinearLayoutManager(this)
        binding?.rvStory?.setHasFixedSize(true)
        binding?.rvStory?.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun progressBar() {
        viewModel.isLoading.observe(this) {
            binding?.apply {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    rvStory.visibility = View.INVISIBLE
                } else {
                    progressBar.visibility = View.GONE
                    rvStory.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun noStory(){
        viewModel.isGetData.observe(this){
            binding?.apply {
                if (it) {
                    noStory.visibility = View.VISIBLE
                } else {
                    noStory.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun showSnackBar() {
        viewModel.snackBarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    findViewById(R.id.rv_story),
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setListStory() {
        viewModel.showListStory(user.token)
        viewModel.itemStory.observe(this) {
            adapter.setListStory(it)
        }
    }

    override fun onResume() {
        super.onResume()
        setListStory()
    }

    private fun uploadStory(){
        binding?.uploadStory?.setOnClickListener {
            val intent = Intent(this, UploadStoryActivity::class.java)
            intent.putExtra(UploadStoryActivity.EXTRA_USER, user)
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_USER = "user"
    }
}