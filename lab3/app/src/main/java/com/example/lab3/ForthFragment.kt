package com.example.lab3

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentFourBinding

class ForthFragment : Fragment(R.layout.fragment_four)
{
    private lateinit var binding: FragmentFourBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFourBinding.bind(view)

        binding.openFirstFragmentButton.setOnClickListener {
            findNavController()
                .popBackStack()
        }

        binding.openFifthFragmentButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_forthFragment_to_fifthFragment)
        }
    }
}