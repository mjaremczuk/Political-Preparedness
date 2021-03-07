package com.github.mjaremczuk.politicalpreparedness.repository

sealed class Result<out T> {
    data class Failure<T>(val exception: Exception) : Result<T>()
    data class Success<T>(val elections: T) : Result<T>()
    class Loading<T>() : Result<T>()
}