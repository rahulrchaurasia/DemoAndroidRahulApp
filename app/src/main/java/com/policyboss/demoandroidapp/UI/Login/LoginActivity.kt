package com.policyboss.demoandroidapp.UI.Login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.policyboss.demoandroidapp.APIState
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.UI.HomeActivity
import com.policyboss.demoandroidapp.Utility.ExtensionFun.showSnackbar
import com.policyboss.demoandroidapp.Utility.showSnackbar
import com.policyboss.demoandroidapp.ViewModel.UserViewModel
import com.policyboss.demoandroidapp.databinding.ActivityLoginBinding
import com.policyboss.demoandroidapp.request.LoginRequestModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity() , OnClickListener{

    private lateinit var binding: ActivityLoginBinding

    private val mUserViewModel by viewModels<UserViewModel>()

    lateinit var myNullableString: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(this)

       // responseListner()
    }


    private fun responseListner(){

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED){

                mUserViewModel.loginResponse.collect{

                    when(it){
                        is APIState.Empty -> {

                        }
                        is APIState.Failure -> {
                            hideLoading()
                            showSnackbar(binding.root, it.errorMessage)
                        }
                        is APIState.Loading -> {

                            displayLoadingWithText(binding.root, "Loading..", null, false)
                        }
                        is APIState.Success -> {
                            hideLoading()
                            showSnackbar(binding.root, it.data?.data?.validaty?:"")
                        }
                    }
                }
            }
        }
    }
    override fun onClick(view: View?) {
        when(view?.id){

            binding.btnLogin.id -> {

               // mUserViewModel.login(LoginRequestModel("SUPERADMIN", "IAVOgPjZsdnJM4ou71YAqg=="))

                this.finish()
                startActivity(Intent(this, HomeActivity::class.java))
            }

        }
    }
}