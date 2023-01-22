package com.mrntlu.PassVault.utils

import com.parse.ParseQuery
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> ResultType,
    crossinline fetch: () -> Pair<RequestType, ParseQuery<*>>,
    crossinline saveFetchResult: suspend (RequestType) -> ResultType,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = callbackFlow<Response<ResultType>> {
    try {
        val data = query()

        val flow = if (shouldFetch(data)) {
            trySend(Response.Loading)

            try {
                Response.Success(saveFetchResult(fetch().first))
            } catch (throwable: Throwable) {
                if (throwable.message != null && throwable.message!! == "i/o failure") {
                    Response.Success(data)
                } else {
                    Response.Failure(throwable.message ?: throwable.toString())
                }
            }
        } else {
            Response.Success(data)
        }

        trySend(flow)
    } catch (throwable: Throwable) {
        trySend(Response.Failure(throwable.message ?: throwable.toString()))
    }

    awaitClose {
        fetch().second.cancel()
    }
}