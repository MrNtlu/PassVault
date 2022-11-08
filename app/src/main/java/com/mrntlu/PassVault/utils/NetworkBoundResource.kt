package com.mrntlu.PassVault.utils

import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Response.Loading)

        try {
            saveFetchResult(fetch())
            query().map { Response.Success(it) }
        } catch (throwable: Throwable) {
            query().map { Response.Failure(throwable.message ?: throwable.toString()) }
        }
    } else {
        query().map { Response.Success(it) }
    }

    emitAll(flow)
}