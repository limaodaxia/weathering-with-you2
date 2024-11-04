package com.example.weatheringwy2.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.weatheringwy2.logic.Repository
import com.example.weatheringwy2.logic.model.Place

class MyPlaceViewModel:ViewModel() {

    val myPlaceList = ArrayList<Place>()

    fun saveSharedPreferencesPlace(place: Place) = Repository.saveSharedPreferencesPlace(place)

    fun getSharedPreferencesPlace() = Repository.getSharedPreferencesPlace()

    private val refreshLiveData = MutableLiveData<Any?>()

    fun refresh(){
        refreshLiveData.value = refreshLiveData.value
    }

    val refreshResult:LiveData<List<Place>> = refreshLiveData.switchMap{
        loadAllMyPlaces()
    }

    fun loadAllMyPlaces(): LiveData<List<Place>> {
        return Repository.loadAllMyPlaces()
    }

    fun deleteMyPlace(place: Place){
        Repository.deleteMyPlace(place)
    }

}