package com.example.lab3

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentOneBinding

class FirstFragment : Fragment(R.layout.fragment_one)
{
    private lateinit var binding: FragmentOneBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOneBinding.bind(view)

        val firstNameArg = arguments?.getString("FIRST_NAME")
        val lastNameArg = arguments?.getString("LAST_NAME")

        if (firstNameArg != null && lastNameArg != null)
        {
            binding.firstName.text = Editable.Factory.getInstance().newEditable(firstNameArg)
            binding.lastName.text = Editable.Factory.getInstance().newEditable(lastNameArg)
        }

        binding.openSecondFragmentButton.setOnClickListener {
            val firstName:String = binding.firstName.text.toString()
            val lastName:String = binding.lastName.text.toString()

            val arguments = Bundle().apply {
                putString("FIRST_NAME", firstName)
                putString("LAST_NAME", lastName)
            }

            findNavController()
                .navigate(R.id.action_firstFragment_to_secondFragment, arguments)
        }

        binding.openForthFragmentButton.setOnClickListener {
            findNavController().navigate(R.id.action_firstFragment_to_forthFragment)
        }
    }
}