package com.mrntlu.PassVault.repositories

import com.mrntlu.PassVault.services.ParseService
import com.mrntlu.PassVault.utils.Response
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class HomeRepository : ParseService {

    private lateinit var user: ParseUser

    init {
        if (ParseUser.getCurrentUser() != null)
            user = ParseUser.getCurrentUser()
    }

    override fun deletePassword(parseObject: ParseObject): Flow<Response<Boolean>> = callbackFlow {
        var response: Response<Boolean> = Response.Loading

        try {
            trySend(response)

            parseObject.deleteInBackground { error ->
                response = if (error == null) {
                    Response.Success(true)
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

    //TODO: Check update, if field same don't update.
    override fun editPassword(
        parseObject: ParseObject, title: String, username: String, password: String, note: String?, isEncrypted: Boolean
    ): Flow<Response<ParseObject>> = callbackFlow {
        var response: Response<ParseObject> = Response.Loading

        parseObject.apply {
            put("Title", title)
            put("Username", username)
            put("Password", password)
            put("Note", note ?: "")
            put("IsEncrypted", isEncrypted)
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

    override fun addPassword(title: String, username: String, password: String, note: String?, isEncrypted: Boolean) = callbackFlow {
        var response: Response<ParseObject> = Response.Loading
        val parseObject = ParseObject.create("Account")

        parseObject.apply {
            put("ParseUser", user.username)
            put("Title", title)
            put("Username", username)
            put("Password", password)
            if (note != null)
                put("Note", note)
            put("IsEncrypted", isEncrypted)
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

    override fun getPasswords(): Flow<Response<ArrayList<ParseObject>>> = callbackFlow {
        var response: Response<ArrayList<ParseObject>> = Response.Loading
        val query = ParseQuery.getQuery<ParseObject>("Account")

        if (!::user.isInitialized && ParseUser.getCurrentUser() != null) {
            user = ParseUser.getCurrentUser()
        }

        try{
            trySend(response)

            query.whereEqualTo("ParseUser", user.username)

            query.findInBackground { objects, error ->
                response = if (error == null) {
                    Response.Success(ArrayList(objects))
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
}