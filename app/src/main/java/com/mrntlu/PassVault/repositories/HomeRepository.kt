package com.mrntlu.PassVault.repositories

import androidx.room.withTransaction
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.services.ParseDao
import com.mrntlu.PassVault.services.ParseDatabase
import com.mrntlu.PassVault.utils.Response
import com.mrntlu.PassVault.utils.networkBoundResource
import com.mrntlu.PassVault.utils.toPasswordItem
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val parseDao: ParseDao,
    private val parseDatabase: ParseDatabase
) {

    private lateinit var user: ParseUser
    private val parseQuery = ParseQuery.getQuery<ParseObject>("Account")

    init {
        if (ParseUser.getCurrentUser() != null)
            user = ParseUser.getCurrentUser()
    }

    private fun getParseObjectFromPasswordItem(parseID: String, onParseObjectRetrieved: (ParseObject) -> Unit, onError: (ParseException) -> Unit) {
        parseQuery.getInBackground(parseID) { parseObject, error ->
            if (error == null) {
                onParseObjectRetrieved(parseObject)
            } else {
                onError(error)
            }
        }
    }

    fun deletePassword(passwordItem: PasswordItem): Flow<Response<Boolean>> = callbackFlow {
        var response: Response<Boolean> = Response.Loading

        try {
            trySend(response)

            getParseObjectFromPasswordItem(
                passwordItem.parseID,
                onParseObjectRetrieved = {
                    it.deleteInBackground { err ->
                        response = if (err == null) {
                            Response.Success(true)
                        } else {
                            Response.Failure(err.message ?: err.toString())
                        }

                        trySend(response)
                    }
                },
                onError = {
                    trySend(Response.Failure(it.message ?: it.toString()))
                }
            )
        } catch (error: Exception) {
            response = Response.Failure(error.message ?: error.toString())
            trySend(response)
        }

        awaitClose()
    }

    //TODO: Check update, if field same don't update.
    fun editPassword(
        passwordItem: PasswordItem, title: String, username: String, password: String, note: String?, isEncrypted: Boolean
    ): Flow<Response<ParseObject>> = callbackFlow {
        var response: Response<ParseObject> = Response.Loading

        try {
            trySend(response)

            getParseObjectFromPasswordItem(
                passwordItem.parseID,
                onParseObjectRetrieved = {
                    it.apply {
                        put("Title", title)
                        put("Username", username)
                        put("Password", password)
                        put("Note", note ?: "")
                        put("IsEncrypted", isEncrypted)
                    }

                    it.saveInBackground { err ->
                        response = if (err == null) {
                            Response.Success(it)
                        } else {
                            Response.Failure(err.message ?: err.toString())
                        }

                        trySend(response)
                    }
                },
                onError = {
                    trySend(Response.Failure(it.message ?: it.toString()))
                }
            )
        } catch (error: Exception) {
            response = Response.Failure(error.message ?: error.toString())
            trySend(response)
        }

        awaitClose()
    }

    fun addPassword(title: String, username: String, password: String, note: String?, isEncrypted: Boolean) = callbackFlow {
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

    //TODO: Finish caching
    // https://www.youtube.com/watch?v=h9XKb4iGM-4&ab_channel=CodinginFlow
    // https://github.com/codinginflow/SimpleCachingExample/tree/Part-4_Room-Cache/app/src/main/java/com/codinginflow/simplecachingexample
    // https://github.com/nameisjayant/Dagger-hilt-with-RoomDatabase-and-Retrofit-in-Android/tree/master/app/src/main/java/com/example/roomwithretrofit
    fun getPasswordsOrCache() = networkBoundResource(
        query = {
            parseDao.getPasswords()
        },
        fetch = {
            val query = ParseQuery.getQuery<ParseObject>("Account")

            if (!::user.isInitialized && ParseUser.getCurrentUser() != null) {
                user = ParseUser.getCurrentUser()
            }

            query.whereEqualTo("ParseUser", user.username)

            Pair(query.find(), query)
        },
        saveFetchResult = { objects ->
             parseDatabase.withTransaction {
                parseDao.deletePasswords()
                parseDao.addPasswords(objects.map { it.toPasswordItem() })
                parseDao.getPasswords()
            }
        }
    )

    fun getPasswords(): Flow<Response<ArrayList<ParseObject>>> = callbackFlow {
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