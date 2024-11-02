package com.example.lab3

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentFiveBinding

class FifthFragment : Fragment(R.layout.fragment_five)
{
    private lateinit var binding: FragmentFiveBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFiveBinding.bind(view)

        binding.openForthFragmentButtonFromFifth.setOnClickListener {
            findNavController()
                .popBackStack()
        }
    }
}