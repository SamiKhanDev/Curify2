package com.saim.domain.repositories

import com.saim.domain.entities.MyCartData
import kotlinx.coroutines.flow.Flow

interface MyCartRepositoryContract {
    suspend fun savemycart(uid: String, mycart: MyCartData): Result<Boolean>
    suspend fun deletemycart(uid: String, cartId: String): Result<Boolean>
    fun getmycart(uid: String): Flow<List<MyCartData>>
}

