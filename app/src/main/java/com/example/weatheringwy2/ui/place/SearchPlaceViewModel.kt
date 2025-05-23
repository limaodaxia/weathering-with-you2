package com.example.weatheringwy2.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.weatheringwy2.logic.Repository
import com.example.weatheringwy2.logic.model.Place

class SearchPlaceViewModel:ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    fun saveSharedPreferencesPlace(place: Place) = Repository.saveSharedPreferencesPlace(place)

    fun getSharedPreferencesPlace() = Repository.getSharedPreferencesPlace()

    fun isSharedPreferencesPlaceSaved() = Repository.isSharedPreferencesPlaceSaved()

    val placeList = ArrayList<Place>()

    val placeLiveData /*:LiveData<Result<List<Place>>>*/ = searchLiveData.switchMap{ query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query:String){
        searchLiveData.value = query//这里调用了setValue
    }
}