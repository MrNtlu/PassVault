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
    val data = query()

    val flow = if (shouldFetch(data)) {
        trySend(Response.Loading)

        try {
            Response.Success(saveFetchResult(fetch().first))
        } catch (throwable: Throwable) {
            Response.Success(data)
//            Response.Failure(throwable.message ?: throwable.toString())
        }
    } else {
        Response.Success(data)
    }

    trySend(flow)

    awaitClose {
        fetch().second.cancel()
    }
}