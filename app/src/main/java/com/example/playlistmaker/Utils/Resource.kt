package com.example.playlistmaker.Utils

sealed class Resource<T>(val data: T? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T> : Resource<T>()
}