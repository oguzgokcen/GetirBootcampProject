package com.example.getirbootcampproject.presentation.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.getirbootcampproject.domain.ViewState
import com.example.getirbootcampproject.domain.model.BaseResponse
import com.example.getirbootcampproject.domain.model.Product
import com.example.getirbootcampproject.domain.model.remote.RespData
import com.example.getirbootcampproject.domain.usecase.ProductsUseCase
import com.example.getirbootcampproject.domain.usecase.SuggestedProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val productsUseCase: ProductsUseCase,
    private val suggestedProductsUseCase: SuggestedProductsUseCase
):ViewModel() {
    private val _uiStateItemList: MutableStateFlow<ViewState<BaseResponse<RespData<Product>>>> =
        MutableStateFlow(ViewState.Loading)
    val uiStateItemList = _uiStateItemList.asStateFlow()

    private val _uiStateSuggestions: MutableStateFlow<ViewState<BaseResponse<RespData<Product>>>> =
        MutableStateFlow(ViewState.Loading)
    val uiStateSuggestions = _uiStateSuggestions.asStateFlow()

    var cardTotal = 0.0

    fun addToCard(price: Double) {
        cardTotal += price
    }
    fun getCardTotal(): String {
        return "₺"+String.format("%.2f", cardTotal)
    }
    fun getProducts() {
        productsUseCase.execute().map {
            when(val responseData: BaseResponse<RespData<Product>> = it) {
                is BaseResponse.Success -> {
                    ViewState.Success(responseData)
                }
                is BaseResponse.Error -> {
                    ViewState.Error(responseData.message)
                }
            }
        }.onEach { data ->
            _uiStateItemList.emit(data)
        }.catch {
            _uiStateItemList.emit(ViewState.Error(it.message.toString()))
        }.launchIn(viewModelScope)

    }

    fun getSuggestedProducts() {
        suggestedProductsUseCase.execute().map {
            when(val responseData: BaseResponse<RespData<Product>> = it) {
                is BaseResponse.Success -> {
                    ViewState.Success(responseData)
                }
                is BaseResponse.Error -> {
                    ViewState.Error(responseData.message)
                }
            }
        }.onEach { data ->
            _uiStateSuggestions.emit(data)
        }.catch {
            _uiStateSuggestions.emit(ViewState.Error(it.message.toString()))
        }.launchIn(viewModelScope)

    }
}