package com.example.travelapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelapp.R
import com.example.travelapp.model.Trip
import com.example.travelapp.ui.BaseFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : BaseFragment(), TripAdapter.TripAdapterListener {
    override fun detailsTrip(trip: Trip) {
        val action = HomeFragmentDirections.actionTripDetails(trip.id)
        findNavController().navigate(action)
    }

    override fun favoredTrip(trip: Trip) {
        viewModel.updateTrip(trip)
    }

    override fun editTrip(trip: Trip) {
        val action = HomeFragmentDirections.actionAddEditTrip(trip.id)
        findNavController().navigate(action)
    }

    private lateinit var viewModel: HomeViewModel

    private val tripsObserver = Observer<List<Trip>> { trips ->
        (home_trips_rv.adapter as TripAdapter).setTrips(trips)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        viewModel.trips.observe(viewLifecycleOwner, tripsObserver)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView = root.home_trips_rv
        recyclerView.adapter = TripAdapter(
            arrayListOf(),
            this
        )
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(TripItemDecorator(20))
        val fab: FloatingActionButton = root.home_add_trip_fab
        fab.setOnClickListener {
            val action = HomeFragmentDirections.actionAddEditTrip()
            findNavController().navigate(action)
        }
        return root
    }
}