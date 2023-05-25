package com.policyboss.demoandroidapp.UI.AutoCompleteDemo2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.policyboss.demoandroidapp.APIState
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.DataModel.BankModel.BankEntity
import com.policyboss.demoandroidapp.Repository.LoginRepository
import com.policyboss.demoandroidapp.RetrofitHelper
import com.policyboss.demoandroidapp.UI.AutoComplete.AutoCompleteActivity
import com.policyboss.demoandroidapp.Utility.hideKeyboard
import com.policyboss.demoandroidapp.ViewModel.LoginViewModel
import com.policyboss.demoandroidapp.ViewModel.LoginViewModelFactory
import com.policyboss.demoandroidapp.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch


class AutoCompDemo2Activity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: LoginViewModel

    lateinit var bankDetailAdapter: BankDetailAdapter
    lateinit var bankAdapter :  ArrayAdapter<BankEntity>
    var bankList  = ArrayList<String>()
    var mapBank  = emptyMap<String,Int>()

    var autoFlag : Boolean = false

    private var textWatcher : TextWatcher? = null
  lateinit var layout : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        layout = binding.root
        init()
       // initUI()
       bindRecyclerView()

        hideKeyboard(layout)
        setListener()

        binding.autoText.addTextChangedListener(textWatcher)
        binding.customerTextView.addTextChangedListener(textWatcher)

       // setListenerForRecycler()

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


                               // var tempbankList  = it.data.map { it.bankname }.toList()

                                //region BindByArrayList Demo

//                                mapBank = it.data.associateBy({ it.bankname  }, { it.bankid })
//
//                                Log.d(Constant.TAG, "MAP"+ mapBank.toString())
//
//                                //bankAdapter = ArrayAdapter<String>(this@MainActivity, R.layout.simple_spinner_item, mapBank.keys.toList())
//                                bankAdapter = BankAdapter(
//                                    this@MainActivity,
//                                    binding.root.id, it.data
//                                )
//                                binding.customerTextView.setAdapter(bankAdapter)

                                //endregion

                                ////////////  other///

                                bankDetailAdapter.setData(it.data)
                                binding.rvBank.visibility = View.VISIBLE



                            }




                        }
                        is APIState.Failure -> {

//                            cancelDialog()
//
//                            showSnackBar(viewParent,it.errorMessage?: Constant.ErrorMessage)
                            Log.d(Constant.TAG, it.errorMessage.toString())
                            //mapBank  = emptyMap<String,Int>()
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





    private fun bindRecyclerView(){

        bankDetailAdapter = BankDetailAdapter(this,ArrayList()){ it : BankEntity ->

            // Here we'll receive callback of
            // every recyclerview item click
            // Now, perform any action here.
            // for ex: navigate to different screen

            //navigateDashboardMenu(it)

            Log.d(Constant.TAG, "HHHHHHHH " + it.bankid + it.bankname)

             binding.txtData.text = it.bankname + "" + it.bankid


            getSelectedItemFomList(bankEntity = it)



        }

        binding.rvBank.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@AutoCompDemo2Activity)
            adapter = bankDetailAdapter
        }
    }

    private fun getSelectedItemFomList(bankEntity : BankEntity){

        binding.autoText.removeTextChangedListener(textWatcher)
        bankDetailAdapter.setData(ArrayList())
        binding.autoText.setText(""+ bankEntity.bankname)
        binding.txtData.text = ""+ bankEntity.bankname + " "+ bankEntity.bankid
        binding.autoText.setSelection(0)

        binding.autoText.addTextChangedListener(textWatcher)

        binding.autoLayout.setEndIconDrawable(com.google.android.material.R.drawable.abc_ic_clear_material)


        //
        binding.rvBank.visibility = GONE

    }



    fun  setListener(){

         binding.btnMoveToAutoText.setOnClickListener{



             startActivity(Intent(this, AutoCompleteActivity::class.java))

         }

      //  binding.autoLayout.set


        textWatcher = object : TextWatcher{
          override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

          }

          override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

          }

          override fun afterTextChanged(editable: Editable?) {

              if(editable.toString().length >= 2 ){

                 // binding.txtData.text = editable.toString()
                  viewModel.getLoginUsingFlow(strSerach = editable.toString().lowercase())
              }
          }


      }
    }


    // region Not in Used
    private fun initUI() {

        //  bankAdapter = ArrayAdapter<String>(this@MainActivity, R.layout.simple_spinner_item, bankList)

        bankAdapter = BankAdapter(
            this@AutoCompDemo2Activity,
            binding.root.id, ArrayList()
        )

        binding.customerTextView.setAdapter(bankAdapter)

        binding.customerTextView.threshold = 2

        // binding.autoText.threshold = 2



    }

    //endregion

}