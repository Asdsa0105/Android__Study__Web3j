package com.example.ethweb3j.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.core.methods.response.Web3ClientVersion
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.io.File
import java.math.BigDecimal
import java.security.Provider
import java.security.Security

class MainViewModel : ViewModel(

) {


    var web3 : Web3j? = null
    var walletName : String = "wallet-name"
    var credentials: Credentials? = null
    val getBalance : MutableLiveData<String> = MutableLiveData()


    init {

        web3 =
            Web3j.build(HttpService("https://goerli.infura.io/v3/ccd9f2c87d39443b82701a0ef5629d0a"))

    }

    fun connectToEthNetwork (context: Context) {

        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Now connecting...", Toast.LENGTH_SHORT).show()
            }

            try {

                val clientVersion : Web3ClientVersion? =
                    web3?.web3ClientVersion()?.sendAsync()?.get()

                if (!clientVersion?.hasError()!!) {

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "connected!!!", Toast.LENGTH_SHORT).show()

                        Log.d("log_connect", "connected")

                    }

                } else {

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, clientVersion.error.message, Toast.LENGTH_SHORT).show()
                        Log.d("log_connect", "error: "+clientVersion.error.message)

                    }
                }

            } catch (exception: Exception) {


                withContext(Dispatchers.Main) {
                    Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

            }


        }
    }

    fun retrieveBalance ( context: Context ) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val balanceWei : EthGetBalance? = web3?.ethGetBalance(

                    credentials?.address,
                    DefaultBlockParameterName.LATEST

                )?.sendAsync()?.get()



                getBalance.postValue(balanceWei?.balance.toString())


                Log.d("log_balance", "balance: "+balanceWei?.balance.toString())


            } catch (exception : Exception) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "balance error occurred!", Toast.LENGTH_SHORT).show()

                    Log.d("log_balance", "error: "+ exception.message.toString())
                }
            }
        }
    }

    fun createWallet (password : String, file: File?) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                walletName = WalletUtils.generateLightNewWalletFile(password, file)

                Log.d("log_createWallet", "wallet name generated is ---")

                credentials =
                    WalletUtils.loadCredentials(password, file.toString()+"/"+walletName)

                Log.d("log_createWallet", credentials?.address.toString())



            } catch (exception: Exception) {

                Log.d("log_createWallet", "creating a wallet failed")

            }
        }
    }

    fun makeTransaction (value: Double) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val receipt : TransactionReceipt = Transfer.sendFunds(
                    web3,
                    credentials,
                    credentials?.address.toString(),
                    BigDecimal.valueOf(value.toLong()),
                    Convert.Unit.ETHER
                ).send()

                Log.d("log_makeTransaction", "transaction was successful: "+ receipt.transactionHash.toString())


            } catch (exception : Exception) {

                Log.d("log_makeTransaction", "transaction failed: "+ exception.message.toString())

            }
        }
    }

    fun setupBouncyCastle () {

        viewModelScope.launch(Dispatchers.IO) {


            val provider : Provider =
                Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)

                    ?:
                    return@launch
            if (provider.javaClass == BouncyCastleProvider::class.java)
                    return@launch

            Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
            Security.insertProviderAt(BouncyCastleProvider(),1)
        }
    }
}