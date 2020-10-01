package com.pcodelight.quadrant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pcodelight.quadrant.repository.UserRepository

class ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) ->
                return DashboardViewModel() as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                return LoginViewModel(UserRepository()) as T
            modelClass.isAssignableFrom(HomeSectionViewModel::class.java) ->
                return HomeSectionViewModel() as T
            modelClass.isAssignableFrom(LocationListViewModel::class.java) ->
                return LocationListViewModel() as T
            modelClass.isAssignableFrom(MonthlyDataViewModel::class.java) ->
                return MonthlyDataViewModel() as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}