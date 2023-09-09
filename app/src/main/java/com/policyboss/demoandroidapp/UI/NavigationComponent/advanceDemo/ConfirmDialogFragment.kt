package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.FragmentConfirmDialogBinding
import com.policyboss.demoandroidapp.databinding.FragmentSendCashBinding


class ConfirmDialogFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentConfirmDialogBinding? = null
    private val binding get() = _binding!!

    private val args : ConfirmDialogFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCancelable(false)    // for prevent dialog to dismiss.
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentConfirmDialogBinding.inflate(inflater,container,false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receiverName = args.receivername
        val amount = args.amount



        binding.tvMessage.text = "Do you want to send ${amount} to ${receiverName}"

        binding.btnYes.setOnClickListener{

            Toast.makeText(requireContext(),"${amount} has send to  ${receiverName}", Toast.LENGTH_SHORT).show()
            this.dismiss()
        }
        binding.btnNo.setOnClickListener{

            this.dismiss()
        }
    }


}