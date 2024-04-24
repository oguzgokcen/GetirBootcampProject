package com.example.getirbootcampproject.domain.usecase

import com.example.getirbootcampproject.domain.NoParaMeterUseCase
import com.example.getirbootcampproject.domain.model.BaseResponse
import com.example.getirbootcampproject.domain.model.Product
import com.example.getirbootcampproject.domain.model.remote.RespData
import com.example.getirbootcampproject.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SuggestedProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
): NoParaMeterUseCase<Flow<BaseResponse<RespData<Product>>>> {
    override fun execute(): Flow<BaseResponse<RespData<Product>>> = productsRepository.getSuggestedProducts()

}