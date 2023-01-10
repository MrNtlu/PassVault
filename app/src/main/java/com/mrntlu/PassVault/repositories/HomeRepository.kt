package com.mrntlu.PassVault.repositories

import androidx.room.withTransaction
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.services.ParseDao
import com.mrntlu.PassVault.services.ParseDatabase
import com.mrntlu.PassVault.utils.Response
import com.mrntlu.PassVault.utils.SortType
import com.mrntlu.PassVault.utils.networkBoundResource
import com.mrntlu.PassVault.utils.toPasswordItem
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val parseDao: ParseDao,
    private val parseDatabase: ParseDatabase
) {
    private val parseQuery = ParseQuery.getQuery<ParseObject>("Account")

    private fun getParseObjectFromPasswordItem(
        parseID: String,
        onParseObjectRetrieved: (ParseObject) -> Unit,
        onError: (ParseException) -> Unit
    ) {
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

    fun editPassword(
        passwordItem: PasswordItem, title: String, username: String,
        password: String, note: String?, isEncrypted: Boolean,
        imageUri: String?, imageColor: String,
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
                        put("Note", note ?: JSONObject.NULL)
                        put("IsEncrypted", isEncrypted)
                        put("ImageUri", imageUri ?: JSONObject.NULL)
                        put("ImageColor", imageColor)
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

    fun addPassword(
        title: String, username: String, password: String,
        note: String?, isEncrypted: Boolean, imageUri: String?,
        imageColor: String,
    ) = callbackFlow {
        var response: Response<ParseObject> = Response.Loading
        val parseObject = ParseObject.create("Account")

        try {
            parseObject.apply {
                put("ParseUser", ParseUser.getCurrentUser().username)
                put("Title", title)
                put("Username", username)
                put("Password", password)
                if (note != null)
                    put("Note", note)
                put("IsEncrypted", isEncrypted)
                if (imageUri != null)
                    put("ImageUri", imageUri)
                put("ImageColor", imageColor)
            }

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

    private fun getSortedPasswordsFromCache(sortType: SortType): MutableList<PasswordItem> {
        return if (sortType != SortType.Default) {
            parseDao.getPasswordsSorted(sortType.sort!!, sortType.isDescending)
        } else {
            parseDao.getPasswords()
        }
    }

    fun getSortedPasswords(sortType: SortType): Response.Success<MutableList<PasswordItem>> {
        return Response.Success(getSortedPasswordsFromCache(sortType))
    }

    fun getPasswordsOrCache(isNetworkAvailable: Boolean, sortType: SortType) = networkBoundResource(
        query = {
            getSortedPasswordsFromCache(sortType)
        },
        fetch = {
            val query = ParseQuery.getQuery<ParseObject>("Account")
            query.whereEqualTo("ParseUser", ParseUser.getCurrentUser().username)

            Pair(query.find(), query)
        },
        saveFetchResult = { objects ->
            parseDatabase.withTransaction {
                parseDao.deletePasswords()
                parseDao.addPasswords(objects.map { it.toPasswordItem() })
                getSortedPasswordsFromCache(sortType)
            }
        },
        shouldFetch = { isNetworkAvailable && ParseUser.getCurrentUser() != null }
    )
}