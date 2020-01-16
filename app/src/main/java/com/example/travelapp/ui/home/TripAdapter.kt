package com.example.travelapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.travelapp.R
import com.example.travelapp.model.Trip
import com.example.travelapp.model.formatPrice
import kotlinx.android.synthetic.main.item_trip.view.*


class TripAdapter(
    private var trips: List<Trip>,
    private val tripAdapterListener: TripAdapterListener?
) : RecyclerView.Adapter<TripViewHolder>(),
    TripViewHolder.TripViewHolderListener {

    override fun itemLongClickedOn(adapterPosition: Int) {
        tripAdapterListener?.editTrip(trips[adapterPosition])
    }

    override fun itemClickedOn(adapterPosition: Int) {
        tripAdapterListener?.detailsTrip(trips[adapterPosition])
    }

    override fun favouriteClicked(adapterPosition: Int) {
        if (adapterPosition != -1) {
            val favoriteStatus = trips[adapterPosition].favourite
            trips[adapterPosition].favourite = !favoriteStatus
            notifyItemChanged(adapterPosition)
            tripAdapterListener?.favoredTrip(trips[adapterPosition])
        }
    }

    fun setTrips(trips: List<Trip>) {
        this.trips = trips
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val viewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.item_trip, parent, false)

        return TripViewHolder(viewHolder, this)
    }

    override fun getItemCount() = trips.size

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.setData(trips[position])
    }

    interface TripAdapterListener {
        fun editTrip(trip: Trip)
        fun detailsTrip(trip: Trip)
        fun favoredTrip(trip: Trip)
    }
}

class TripViewHolder(
    itemView: View,
    private val viewHolderListener: TripViewHolderListener?
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            viewHolderListener?.itemClickedOn(adapterPosition)
        }
        itemView.setOnLongClickListener {
            viewHolderListener?.itemLongClickedOn(adapterPosition)
            true
        }
        itemView.item_trip_favourite_ib.setOnClickListener {
            viewHolderListener?.favouriteClicked(adapterPosition)
        }
    }

    fun setData(trip: Trip) {
        itemView.item_trip_name_tv.text = trip.name
        itemView.item_trip_destination_tv.text = trip.destination

        if (trip.picture != null) {
            itemView.item_trip_picture_iv.setImageBitmap(trip.picture)
        }

        itemView.item_trip_price_tv.text = trip.price.formatPrice(trip.currency)

        val favouriteTint = if (trip.favourite) {
            ContextCompat.getColor(itemView.context, R.color.colorAccent)
        } else {
            ContextCompat.getColor(itemView.context, R.color.colorWhite)
        }

        itemView.item_trip_favourite_ib.setColorFilter(
            favouriteTint,
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    interface TripViewHolderListener {
        fun itemLongClickedOn(adapterPosition: Int)
        fun itemClickedOn(adapterPosition: Int)
        fun favouriteClicked(adapterPosition: Int)
    }
}

