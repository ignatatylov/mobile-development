package com.example.lab3

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentThreeBinding

class ThirdFragment : Fragment(R.layout.fragment_three)
{
    private lateinit var binding: FragmentThreeBinding

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentThreeBinding.bind(view)

        val year = arguments?.getInt("YEAR")
        val month = arguments?.getInt("MONTH")
        val day = arguments?.getInt("DAY")
        val firstName = arguments?.getString("FIRST_NAME")
        val lastName = arguments?.getString("LAST_NAME")

        if (year != null && month != null && day != null) {
            val dateText = "Selected date: $day/${month + 1}/$year\n$firstName $lastName"
            binding.thirdText.text = dateText
        } else {
            binding.thirdText.text = "No date selected"
        }

        binding.buttonBack.setOnClickListener {
            val arguments = Bundle().apply {
                putString("FIRST_NAME", firstName)
                putString("LAST_NAME", lastName)
            }
            findNavController().navigate(R.id.action_thirdFragment_to_firstFragment, arguments)
        }
    }
}