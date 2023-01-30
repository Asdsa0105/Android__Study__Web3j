package com.example.ethweb3j.model

sealed class Response<out T> {

    object loading : Response<Nothing>()

    data class Success<out T> (
        val data : T
            ) : Response<T>()

    data class Failure<out T> (

        val data : T

        ) : Response<T>()

    data class Error (

        val exception: java.lang.Exception?

    ) : Response<Nothing>()

    companion object {

        const val SUCCESS : Int = 1
        const val FAILURE : Int =0

        const val SUCCESS_STRING = "SUCCESS"
    }
}
