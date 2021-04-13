package com.inventoryapp.mobile.util

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    sealed class Status {
        object Success : Status()
        object Error : Status()
        object Loading : Status()
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.Success, data, null)
        }

        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(Status.Error, data, message)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.Loading, data, null)
        }
    }
}