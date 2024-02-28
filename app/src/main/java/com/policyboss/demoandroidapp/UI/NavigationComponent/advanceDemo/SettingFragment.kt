package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.policyboss.demoandroidapp.NavGraphDirections
import com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo.dataModel.SampleData
import com.policyboss.demoandroidapp.Utility.ExtensionFun.showSnackbar
import com.policyboss.demoandroidapp.databinding.FragmentSettingBinding

/*********** Global Action **************
 * we use global action when we no need to use specifict action. hence any one fragment  can move to global  fragment
 * Use : Navigation graph id : ie nav_graph
 * hence we got NavGraphDirections and fragment name which has global action eg. actionGlobalAboutAppFragment
 *
 */
class SettingFragment : Fragment() {

    private var _binding : FragmentSettingBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_setting, container, false)

        _binding = FragmentSettingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val default_amount = SampleData.defaultAmount

        binding.etAmount.setText(default_amount.value.toString())

        binding.btnSave.setOnClickListener {


            SampleData.defaultAmount.value = binding.etAmount.text.toString().toLong()

            binding.root.showSnackbar("Data Saved")
        }

        binding.aboutApp.setOnClickListener{

            /***** For Global Action :********************
             * 1>  SettingFragmentDirections from where to start
             *  and actionGlobalAboutAppFragment to move . AboutAppFragment has
             *  as Global action
             *  2>
             *  use NavGr aphDirections.actionGlobalAboutAppFragment()
             *  where : NavGraphDirections indicate nav_graph is our id of Navigation graph
             *  actionGlobalAboutAppFragment : AboutAppFragment has Global action
             */
           // val action = SettingFragmentDirections.actionGlobalAboutAppFragment()
            val action = NavGraphDirections.actionGlobalAboutAppFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}