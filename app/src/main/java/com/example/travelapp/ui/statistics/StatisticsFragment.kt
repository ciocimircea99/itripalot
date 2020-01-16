package com.example.travelapp.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.travelapp.R
import com.example.travelapp.model.Trip
import com.example.travelapp.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_statistics.*

class StatisticsFragment : BaseFragment() {

    private lateinit var viewModel: StatisticsViewModel

    private val tripsObserver = Observer<List<Trip>> { trips ->
        val totalTrips = trips.size
        if (totalTrips == 0) {
            statistics_total_trips_tv.text =
                getString(R.string.statistic_total_trips_text, totalTrips)
            statistics_total_favourites_tv.text =
                getString(R.string.statistics_total_favourites_text, 0)
            statistics_total_spent_tv.text = getString(R.string.statistics_total_cost_text, 0f, "")
            statistics_total_cheapest_tv.text = getString(R.string.statistics_cheapest_text, 0f, "")
            statistics_total_priciest_tv.text = getString(R.string.statistics_priciest_text, 0f, "")
            statistics_mean_rating_bar.rating = 2.5f
            return@Observer
        }
        var totalMoneySpent = 0f
        var favourites = 0
        var cheapest = trips[0].price
        var priciest = trips[0].price
        var totalRating = 0f
        val currency = trips[0].currency

        trips.forEach { trip ->
            totalMoneySpent += trip.price
            if (trip.favourite) favourites++
            if (trip.price < cheapest)
                cheapest = trip.price
            if (trip.price > priciest)
                priciest = trip.price
            totalRating += trip.rating
        }

        statistics_total_trips_tv.text = getString(R.string.statistic_total_trips_text, totalTrips)
        statistics_total_favourites_tv.text =
            getString(R.string.statistics_total_favourites_text, favourites)
        statistics_total_spent_tv.text =
            getString(R.string.statistics_total_cost_text, totalMoneySpent, currency)
        statistics_total_cheapest_tv.text =
            getString(R.string.statistics_cheapest_text, cheapest, currency)
        statistics_total_priciest_tv.text =
            getString(R.string.statistics_priciest_text, priciest, currency)
        statistics_mean_rating_bar.rating = totalRating / totalTrips
    }

    private val messageObserver = Observer<String> {
        Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(StatisticsViewModel::class.java)
        viewModel.trips.observe(viewLifecycleOwner, tripsObserver)
        viewModel.message.observe(viewLifecycleOwner, messageObserver)
        statistics_delete_all_bt.setOnClickListener { viewModel.clearAllData() }
    }
}