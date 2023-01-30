package com.example.ethweb3j.repo

import com.example.ethweb3j.model.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Provider
import java.security.Security

class MainRepositoryImpl : MainRepository {

    override suspend fun setupBouncyCastle(): Response<Int> {


        TODO("Not yet implemented")
    }

    override suspend fun createWallet(password: String): Response<Int> {


        TODO("Not yet implemented")
    }


}