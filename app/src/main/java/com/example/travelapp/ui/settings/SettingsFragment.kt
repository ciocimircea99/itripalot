package com.example.travelapp.ui.settings

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.travelapp.R
import com.example.travelapp.ui.AfterTextChangedWatcher
import com.example.travelapp.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {

    private lateinit var viewModel: SettingsViewModel

    private val minPriceObserver = Observer<Float> {
        settings_min_price.setText(it.toString())
    }

    private val maxPriceObserver = Observer<Float> {
        settings_max_price.setText(it.toString())
    }

    private val currencyObserver = Observer<String> {
        settings_currency.setText(it)
    }

    private val messageObserver = Observer<String> {
        Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel::class.java)

        viewModel.minPrice.observe(viewLifecycleOwner, minPriceObserver)
        viewModel.maxPrice.observe(viewLifecycleOwner, maxPriceObserver)
        viewModel.currency.observe(viewLifecycleOwner, currencyObserver)
        viewModel.message.observe(viewLifecycleOwner, messageObserver)

        settings_save_bt.setOnClickListener { viewModel.saveSettings() }

        settings_currency.addTextChangedListener(object : AfterTextChangedWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.currencyChanged(s.toString())
            }
        })
        settings_min_price.addTextChangedListener(object : AfterTextChangedWatcher {
            override fun afterTextChanged(s: Editable?) {
                val float = try {
                    s.toString().toFloat()
                } catch (e: Exception) {
                    0f
                }
                viewModel.minPriceChanged(float)
            }
        })
        settings_max_price.addTextChangedListener(object : AfterTextChangedWatcher {
            override fun afterTextChanged(s: Editable?) {
                val float = try {
                    s.toString().toFloat()
                } catch (e: Exception) {
                    0f
                }
                viewModel.maxPriceChanged(float)
            }
        })
    }

}
