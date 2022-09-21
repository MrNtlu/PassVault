package com.mrntlu.PassVault.repositories

import com.mrntlu.PassVault.services.ParseService
import com.mrntlu.PassVault.utils.Response
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class HomeRepository(): ParseService {

    private lateinit var user: ParseUser

    init {
        if (ParseUser.getCurrentUser() != null)
            user = ParseUser.getCurrentUser()
    }

    override fun getPasswords(): Flow<Response<List<ParseObject>>> = callbackFlow {
        var response: Response<List<ParseObject>> = Response.Loading
        val query = ParseQuery.getQuery<ParseObject>("Account")

        try{
            trySend(response)

            query.whereEqualTo("ParseUser", user.username)

            query.findInBackground { objects, error ->
                response = if (error == null) {
                    Response.Success(objects)
                } else {
                    Response.Failure(error)
                }

                trySend(response)
            }
        } catch (error: Exception) {
            response = Response.Failure(error)
            trySend(response)
        }

        awaitClose {
            query.cancel()
        }
    }

    override fun searchPasswords(): Flow<Response<List<ParseObject>>> {
        TODO("Not yet implemented")
    }
}