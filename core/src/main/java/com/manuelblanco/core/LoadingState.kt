package com.manuelblanco.core

/**
 * Loading state for the calls in coroutines. Using in Viewmodels and fragments.
 */
@Suppress("DataClassPrivateConstructor")
data class LoadingState private constructor(val status: Status, val msg: String? = null) {
    companion object {
        val LOADED = LoadingState(Status.SUCCESS)
        val LOADING = LoadingState(Status.RUNNING)
        val NETWORK = LoadingState(Status.NETWORK)
        val SUCCESS = LoadingState(Status.SUCCESS)
        val EMPTY_OR_NULL = LoadingState(Status.EMPTY_OR_NULL)
        fun error(msg: String?) = LoadingState(Status.FAILED, msg)
    }

    enum class Status {
        RUNNING,
        SUCCESS,
        NETWORK,
        EMPTY_OR_NULL,
        FAILED
    }
}