package com.example.ethweb3j

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.ethweb3j.databinding.ActivityMainBinding
import com.example.ethweb3j.entity.Wallet
import com.example.ethweb3j.viewModel.MainViewModel
import kotlinx.coroutines.*
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import java.io.File
import java.security.Provider
import java.security.Security

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var file : File


    val viewModel : MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.wallet = Wallet(
            "test-address",
            "test-name",
            "0",
            "test-password"

        )



        file = File(filesDir.toString()+"/"+"ethwallet")

        if (!file!!.mkdirs()) {
            file!!.mkdirs()
        } else {

            Log.d("log_onCreate", "wallet dirs already created")

        }

        viewModel.setupBouncyCastle()

        viewModel.getBalance.observe(this, Observer {

            binding.apply {

                binding.wallet!!.balance = textBalance.text.toString()
            }
        })

        binding.buttonCreateWallet.setOnClickListener {

            binding.apply {

                binding.wallet!!.name =
                    editWalletPath.text.toString()

                binding.wallet!!.password =
                    editPassword.text.toString()


                viewModel.createWallet(

                    editPassword.text.toString(),
                    file
                )
            }


        }

        binding.buttonConnectToNode.setOnClickListener {

            viewModel.connectToEthNetwork(this)

        }






    }



}