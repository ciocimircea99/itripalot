package com.example.travelapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.travelapp.R
import com.example.travelapp.loadImage
import com.example.travelapp.model.Trip
import com.example.travelapp.model.TripType
import com.example.travelapp.model.formatPrice
import com.example.travelapp.networking.formatDescription
import com.example.travelapp.networking.formatHumidity
import com.example.travelapp.networking.formatTemperature
import com.example.travelapp.networking.getWeatherIconURL
import com.example.travelapp.networking.response.CurrentWeatherResponse
import com.example.travelapp.ui.BaseFragment
import com.example.travelapp.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_trip_details.*
import java.util.*

class TripDetailsFragment : BaseFragment() {

    private lateinit var viewModel: TripDetailsViewModel
    private val args: TripDetailsFragmentArgs by navArgs()

    private val tripObserver = Observer<Trip> { trip ->
        (activity as? MainActivity)?.setToolbarTitle(trip.name)

        trip_details_picture_iv.setImageBitmap(trip.picture)

        trip_details_destination_tv.text = trip.destination

        trip_details_type_label.text = when (trip.type) {
            TripType.CITY_BREAK -> {
                getString(R.string.city_break)
            }
            TripType.SEA_SIDE -> {
                getString(R.string.sea_side)
            }
            TripType.MOUNTAIN -> {
                getString(R.string.mountains)
            }
            else -> {
                ""
            }
        }

        trip_details_picture_favourite_iv.visibility =
            if (trip.favourite) View.VISIBLE else View.GONE

        trip_details_price_tv.text = trip.price.formatPrice(trip.currency)

        val startMonthLowerCase = trip.endDate.month.toString().toLowerCase(Locale.ROOT)
        val startMonthText = startMonthLowerCase[0].toUpperCase() + startMonthLowerCase.substring(1)

        val startDateString =
            "${trip.endDate.dayOfMonth} $startMonthText ${trip.endDate.year}"

        trip_details_start_date_tv.text = startDateString

        val endMonthLowerCase = trip.endDate.month.toString().toLowerCase(Locale.ROOT)
        val endMonthText = endMonthLowerCase[0].toUpperCase() + endMonthLowerCase.substring(1)

        val endDateString =
            "${trip.endDate.dayOfMonth} $endMonthText ${trip.endDate.year}"
        trip_details_end_date_tv.text = endDateString

        trip_details_rating_bar.rating = trip.rating
    }

    private val currentWeatherObserver = Observer<CurrentWeatherResponse> { response ->
        if (response == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.weather_unavailable),
                Toast.LENGTH_SHORT
            ).show()
            return@Observer
        }

        val currentWeather = response.weather[0]
        val mainWeather = response.main

        val iconUrl = getWeatherIconURL(currentWeather.icon)
        val description = currentWeather.description
        val temperature = mainWeather.temp
        val humidity = mainWeather.humidity

        trip_details_weather_iv.loadImage(iconUrl)
        trip_details_weather_description_tv.text = formatDescription(description)
        trip_details_weather_temp_tv.text = formatTemperature(temperature)
        trip_details_weather_humidity_tv.text = formatHumidity(humidity)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trip_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(TripDetailsViewModel::class.java)

        val tripId = args.tripid

        viewModel.initTrip(tripId)

        viewModel.tripMutableLive.observe(viewLifecycleOwner, tripObserver)
        viewModel.weatherMutableLive.observe(viewLifecycleOwner, currentWeatherObserver)
    }
}
