package com.example.travelapp.ui.addedittrip

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.travelapp.R
import com.example.travelapp.model.Trip
import com.example.travelapp.model.TripType
import com.example.travelapp.ui.AfterTextChangedWatcher
import com.example.travelapp.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_edit_trip.*

class AddEditTripFragment : BaseFragment() {

    companion object {
        const val GALLERY_PICTURE_RC = 234
        const val CAMERA_PICTURE_RC = 432

        const val IMAGE_INTENT_TYPE = "image/*"
        const val IMAGE_INTENT_TITLE = "Select Picture"

        const val CAMERA_PICTURE_DATA = "data"
    }

    private lateinit var viewModel: AddEditTripViewModel
    private val args: AddEditTripFragmentArgs by navArgs()

    private val messageObserver = Observer<String> { message ->
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private val tripObserver = Observer<Trip> { trip ->
        add_edit_trip_picture_iv.setImageBitmap(trip.picture)

        add_edit_trip_name.setText(trip.name)

        add_edit_trip_destination.setText(trip.destination)

        when (trip.type) {
            TripType.CITY_BREAK -> add_edit_trip_type_rg.check(add_edit_trip_type_city_rb.id)
            TripType.MOUNTAIN -> add_edit_trip_type_rg.check(add_edit_trip_type_mountain_rb.id)
            TripType.SEA_SIDE -> add_edit_trip_type_rg.check(add_edit_trip_type_sea_rb.id)
            else -> add_edit_trip_type_rg.clearCheck()
        }

        add_edit_trip_min_price_tv.text = viewModel.minPrice.toString()
        add_edit_trip_max_price_tv.text = viewModel.maxPrice.toString()

        add_edit_trip_price_tv.text = trip.price.toString()

        add_edit_trip_price_sb.progress =
            trip.price.priceToProgress(viewModel.minPrice, viewModel.maxPrice)

        add_edit_trip_start_date_dp.updateDate(
            trip.startDate.year,
            trip.startDate.monthValue,
            trip.startDate.dayOfMonth
        )

        add_edit_trip_end_date_dp.updateDate(
            trip.endDate.year,
            trip.endDate.monthValue,
            trip.endDate.dayOfMonth
        )

        add_edit_trip_rating_bar.rating = trip.rating
    }

    private val actionFinishedObserver = Observer<Boolean> { actionFinished ->
        if (actionFinished) {
            findNavController().navigateUp()
        }
    }

    //VEZI CE CALLBACKURI SE APELEAZA IN HOME FRAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_trip, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AddEditTripViewModel::class.java)

        val tripId = args.tripid
//        if (tripId != -1L) {
//            (activity as? MainActivity)?.setToolbarTitle(trip.name)
//        } else {
//            add_edit_trip_delete_fab.visibility = View.GONE
//        }

        viewModel.initTrip(tripId)

        viewModel.tripMutableLive.observe(viewLifecycleOwner, tripObserver)
        viewModel.messageMutableLive.observe(viewLifecycleOwner, messageObserver)
        viewModel.actionFinished.observe(viewLifecycleOwner, actionFinishedObserver)

        add_edit_trip_name.addTextChangedListener(object : AfterTextChangedWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.nameChanged(s.toString())
            }
        })
        add_edit_trip_destination.addTextChangedListener(object : AfterTextChangedWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.destinationChanged(s.toString())
            }
        })

        add_edit_trip_type_rg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                add_edit_trip_type_city_rb.id -> viewModel.setTripType(TripType.CITY_BREAK)
                add_edit_trip_type_sea_rb.id -> viewModel.setTripType(TripType.SEA_SIDE)
                add_edit_trip_type_mountain_rb.id -> viewModel.setTripType(TripType.MOUNTAIN)
            }
        }

        add_edit_trip_price_sb.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                val newPrice = progress.progressToPrice(viewModel.minPrice, viewModel.maxPrice)
                add_edit_trip_price_tv.text = newPrice.toString()
                viewModel.priceChanged(newPrice)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        add_edit_trip_start_date_dp.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            viewModel.startDateChanged(year, monthOfYear + 1, dayOfMonth)
        }
        add_edit_trip_end_date_dp.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            viewModel.endDateChanged(year, monthOfYear + 1, dayOfMonth)
        }

        add_edit_trip_rating_bar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                viewModel.ratingChanged(rating)
            }
        }

        add_edit_trip_camera_bt.setOnClickListener { cameraButtonClicked() }
        add_edit_trip_gallery_bt.setOnClickListener { galleryButtonClicked() }
        add_edit_trip_save_fab.setOnClickListener { viewModel.upsertTrip() }
        add_edit_trip_delete_fab.setOnClickListener { viewModel.deleteTrip() }

    }

    private fun cameraButtonClicked() {
        val intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        intent.resolveActivity(activity?.packageManager!!)
        startActivityForResult(intent, CAMERA_PICTURE_RC)
    }

    private fun galleryButtonClicked() {
        val intent = Intent()
        intent.type = IMAGE_INTENT_TYPE
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, IMAGE_INTENT_TITLE),
            GALLERY_PICTURE_RC
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_PICTURE_RC && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }
            val inputStream = requireContext().contentResolver.openInputStream(data.data!!)
            val imageBitmap = BitmapFactory.decodeStream(
                inputStream,
                null,
                BitmapFactory.Options().also {
                    it.outHeight = 150
                    it.outWidth = 267
                }
            )
            if (imageBitmap != null) {
                add_edit_trip_picture_iv.setImageBitmap(imageBitmap)
                viewModel.setTripImage(imageBitmap)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.gallery_sel_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (requestCode == CAMERA_PICTURE_RC && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get(CAMERA_PICTURE_DATA) as? Bitmap
            if (imageBitmap != null) {
                add_edit_trip_picture_iv.setImageBitmap(imageBitmap)
                viewModel.setTripImage(imageBitmap)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.camera_sel_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
