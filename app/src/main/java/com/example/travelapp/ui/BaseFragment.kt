package com.example.travelapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.travelapp.TravelApp

open class BaseFragment : Fragment() {
    protected lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = (activity?.application as? TravelApp)?.getViewModelFactory()!!
    }
}