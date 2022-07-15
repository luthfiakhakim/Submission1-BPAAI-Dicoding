package com.dicoding.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.model.DataListStory

class DetailStoryViewModel: ViewModel() {
    lateinit var listStory: DataListStory

    fun setDetailStory(story: DataListStory) : DataListStory{
        listStory = story
        return listStory
    }
}