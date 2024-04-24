package com.example.getirbootcampproject.domain.repository

import com.example.getirbootcampproject.data.remote.ApiService
import com.example.getirbootcampproject.data.remote.CallBack
import com.example.getirbootcampproject.di.IoDispatcher
import com.example.getirbootcampproject.domain.model.BaseResponse
import com.example.getirbootcampproject.domain.model.Product
import com.example.getirbootcampproject.domain.model.ProductSuggestionsResponse
import com.example.getirbootcampproject.domain.model.remote.RespData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val apiService: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
    ){
    fun getProducts(): Flow<BaseResponse<RespData<Product>>> = callbackFlow {
        apiService.getProducts().enqueue(CallBack(this.channel))
        awaitClose{close()}
    }.flowOn(ioDispatcher)

    fun getSuggestedProducts(): Flow<BaseResponse<RespData<ProductSuggestionsResponse>>> = callbackFlow{
        apiService.getSuggestedProducts().enqueue(CallBack(this.channel))
        awaitClose{close()}
    }.flowOn(ioDispatcher)
}