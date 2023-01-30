package com.example.ethweb3j.repo

import com.example.ethweb3j.model.Response

interface MainRepository {

    suspend fun setupBouncyCastle () : Response<Int>
    suspend fun createWallet (password : String) : Response<Int>


}