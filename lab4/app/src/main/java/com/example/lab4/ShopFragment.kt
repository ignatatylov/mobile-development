package com.example.lab4

import ClickerViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.databinding.FragmentShopBinding
import kotlinx.coroutines.launch

class ShopFragment : Fragment() {
    private val viewModel: ClickerViewModel by activityViewModels()

    private lateinit var buildingAdapter: BuildingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentShopBinding.inflate(inflater, container, false)

        val recyclerView: RecyclerView = binding.recyclerView

        buildingAdapter = BuildingAdapter { building ->
            viewModel.buyBuilding(building)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = buildingAdapter

        lifecycleScope.launchWhenStarted {

            launch {
                viewModel.state.collect { state ->
                    val buildings = state.buildingList
                    buildingAdapter.submitList(buildings)
                    buildingAdapter.notifyDataSetChanged()
                }
            }
        }

        return binding.root
    }
}
