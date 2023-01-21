package com.policyboss.demoandroidapp.UI.AutoComplete

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.policyboss.demoandroidapp.APIState
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.DataModel.BankEntity
import com.policyboss.demoandroidapp.Repository.LoginRepository
import com.policyboss.demoandroidapp.RetrofitHelper
import com.policyboss.demoandroidapp.UI.AutoComplete.baseAdapter.AutocompleteAdapter
import com.policyboss.demoandroidapp.ViewModel.LoginViewModel
import com.policyboss.demoandroidapp.ViewModel.LoginViewModelFactory
import com.policyboss.demoandroidapp.databinding.ActivityAutoCompleteBinding
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch

class AutoCompleteActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAutoCompleteBinding

    lateinit var viewModel: LoginViewModel

    lateinit var autoCompleteAdapter : AutocompleteAdapter

    var autoSlectedItem = false
    private var textWatcher : TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAutoCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        init()
        bindAdapter()
        getTextWatcherListener()

        binding.autoComplete.addTextChangedListener(textWatcher)  // always call after textWatch listener

        lifecycleScope.launch{

            repeatOnLifecycle(Lifecycle.State.STARTED){

                viewModel.LoginStateFlow.buffer().collect{

                    when(it){

                        is APIState.Loading -> {
                            // showDialog()
                        }

                        is APIState.Success -> {
                            // cancelDialog()

                            it?.data?.let {



                                //bankAdapter = ArrayAdapter<String>(this@MainActivity, R.layout.simple_spinner_item, mapBank.keys.toList())
//                                autoCompleteAdapter = AutocompleteAdapter(
//                                    this@AutoCompleteActivity,
//                                    it.data
//                                )
//                                binding.autoComplete.setAdapter(autoCompleteAdapter)


                            }




                        }
                        is APIState.Failure -> {

                            Log.d(Constant.TAG, it.errorMessage.toString())

                        }
                        is APIState.Empty -> {
                            // cancelDialog()

                        }

                    }
                }
            }
        }

    }

    private fun init(){


        var loiginRepository = LoginRepository(RetrofitHelper.retrofitLoginApi)
        var viewModelFactory = LoginViewModelFactory(loiginRepository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(LoginViewModel::class.java)


    }

    private fun bindAdapter() {


        autoCompleteAdapter = AutocompleteAdapter(
            this@AutoCompleteActivity,
            ArrayList()){  bankEntity: BankEntity ->


            getSelectedItemFomList(bankEntity)


        }

        binding.autoComplete.setAdapter(autoCompleteAdapter)

        binding.autoComplete.threshold = 2

        // binding.autoText.threshold = 2




        // commented

//        binding.autoComplete.setOnItemClickListener { parent, view, position, id ->
//
////            binding.autoComplete.removeTextChangedListener(textWatcher)
////            autoCompleteAdapter.setData(ArrayList())
//
//            var bankEntity   = parent.adapter.getItem(position)
//
//            Log.d(Constant.TAG, ""+ bankEntity)
////            binding.autoComplete.setText(bankEntity?.bankname?: "")
////            binding.autoComplete.setSelection(0)
//        }

       // endregion

    }

    private fun getSelectedItemFomList(bankEntity : BankEntity){

        binding.autoComplete.removeTextChangedListener(textWatcher)
        autoCompleteAdapter.setData(ArrayList())
        binding.autoComplete.setText(""+ bankEntity.bankname)
        binding.txtData.text = ""+ bankEntity.bankname + " "+ bankEntity.bankid
        binding.autoComplete.setSelection(0)

        binding.autoComplete.addTextChangedListener(textWatcher)
        autoCompleteAdapter.setData(getList())



    }

    private fun getTextWatcherListener(){

        textWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(str: Editable?) {

                if(str.toString().length >= 2 ){

                    if(!autoSlectedItem){

                        binding.txtData.text = str.toString()

                        // autoCompleteAdapter. = CityList
                        autoCompleteAdapter.setData(getList())
                        autoCompleteAdapter.getFilter().filter(str.toString())                        }

                }
            }


        }
    }

    fun getList() : List<BankEntity>{

    return   listOf<BankEntity>(
            BankEntity(bankcode = "1", bankid = 1, bankname =  "KOTAK1", status = 0),
                    BankEntity(bankcode = "2", bankid = 1, bankname =  "UNION1", status = 0),
        BankEntity(bankcode = "1", bankid = 3, bankname =  "HDFC", status = 0),
        BankEntity(bankcode = "1", bankid = 4, bankname =  "KOTAK2", status = 0),
        BankEntity(bankcode = "1", bankid = 5, bankname =  "KOTAK3", status = 0)  ,
        BankEntity(bankcode = "1", bankid = 6, bankname =  "UNION2", status = 0),
        BankEntity(bankcode = "1", bankid = 7, bankname =  "UNION3", status = 0),
        BankEntity(bankcode = "1", bankid = 8, bankname =  "HDFC ABC", status = 0),
        BankEntity(bankcode = "1", bankid = 9, bankname =  "HDFC AKL", status = 0)  ,
        BankEntity(bankcode = "1", bankid = 10, bankname =  "HDFC LL", status = 0),
        BankEntity(bankcode = "1", bankid = 10, bankname =  "HDFC OP", status = 0),
        BankEntity(bankcode = "1", bankid = 10, bankname =  "HDFC L1", status = 0),
        BankEntity(bankcode = "1", bankid = 10, bankname =  "HDFC L2", status = 0),


        )

    }

}