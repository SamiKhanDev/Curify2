package com.saim.data.repositories

import com.saim.domain.entities.MyCartData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import com.saim.domain.repositories.MyCartRepositoryContract

class MyCartRepository @javax.inject.Inject constructor() : MyCartRepositoryContract {
    private fun userCartCollection(uid: String) =
        FirebaseFirestore.getInstance().collection("users").document(uid).collection("cart")


    override suspend fun savemycart(uid: String, mycart: MyCartData): Result<Boolean> {
        try {
            val document = userCartCollection(uid).document()
            mycart.id = document.id
            document.set(mycart).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)

        }
    }
    override suspend fun deletemycart(uid: String, cartId: String): Result<Boolean> {
        return try {
            userCartCollection(uid).document(cartId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override fun getmycart(uid: String) = userCartCollection(uid)
        .snapshots()
        .map {it.toObjects(MyCartData::class.java) }
}
