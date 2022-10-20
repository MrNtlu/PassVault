package com.mrntlu.PassVault.repositories

import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.services.ParseService
import com.mrntlu.PassVault.utils.Response
import com.mrntlu.PassVault.utils.parseObjectToPasswordItem
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

    override fun addPassword(title: String, username: String, password: String, note: String) = callbackFlow {
        var response: Response<ParseObject> = Response.Loading
        val parseObject = ParseObject.create("Account")

        parseObject.apply {
            put("ParseUser", user.username)
            put("Title", title)
            put("Username", username)
            put("Password", password)
            put("Note", note)
        }

        try {
            trySend(response)

            parseObject.saveInBackground { error ->
                response = if (error == null) {
                    Response.Success(parseObject)
                } else {
                    Response.Failure(error.message ?: error.toString())
                }

                trySend(response)
            }
        } catch (error: Exception) {
            response = Response.Failure(error.message ?: error.toString())
            trySend(response)
        }

        awaitClose()
    }

    override fun getPasswords(): Flow<Response<List<PasswordItem>>> = callbackFlow {
        var response: Response<List<PasswordItem>> = Response.Loading
        val query = ParseQuery.getQuery<ParseObject>("Account")

        try{
            trySend(response)

            query.whereEqualTo("ParseUser", user.username)

            query.findInBackground { objects, error ->
                response = if (error == null) {
                    Response.Success(objects.map { parseObjectToPasswordItem(it) })
                } else {
                    Response.Failure(error.message ?: error.toString())
                }

                trySend(response)
            }
        } catch (error: Exception) {
            response = Response.Failure(error.message ?: error.toString())
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