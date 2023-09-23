package com.example.journalog.datastore

interface DataStoreRepository {
    suspend fun putUsername(username: String)
    suspend fun getUsername(): String?
    suspend fun putUserId(userId: Int)
    suspend fun getUserId(): Int?
    suspend fun clearUserData()
}