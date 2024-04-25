package com.example.getirbootcampproject.presentation.item

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.getirbootcampproject.domain.ViewState
import com.example.getirbootcampproject.domain.model.BaseResponse
import com.example.getirbootcampproject.domain.model.CardItem
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
    val cardItems = mutableListOf<CardItem>()
    val suggestedProducts = ArrayList<Product>()
    private val _uiStateItemList: MutableStateFlow<ViewState<BaseResponse<RespData<Product>>>> =
        MutableStateFlow(ViewState.Loading)
    val uiStateItemList = _uiStateItemList.asStateFlow()

    private val _uiStateSuggestions: MutableStateFlow<ViewState<BaseResponse<RespData<Product>>>> =
        MutableStateFlow(ViewState.Loading)
    val uiStateSuggestions = _uiStateSuggestions.asStateFlow()

    private var cardTotal = MutableLiveData(0.00)
    fun getCardTotal():LiveData<Double> = cardTotal

    fun addToCard(item:Product):Int{
        return if (item.id in cardItems.map { it.id }) {
            val cardItem = cardItems.find { it.id == item.id }
            cardItem?.increaseQuantity()
            updateCardTotal()
            cardItem?.quantity ?: 0
        } else {
            cardItems.add(CardItem(item.id, item.name, item.price, item.imageURL, attribute = item.attribute))
            updateCardTotal()
            1
        }
    }
    fun decreaseFromCard(item:Product):Int{
        return if (item.id in cardItems.map { it.id }) {
            val cardItem = cardItems.find { it.id == item.id }
            cardItem?.decreaseQuantity()
            if (cardItem?.quantity == 0) {
                cardItems.remove(cardItem)
            }
            updateCardTotal()
            cardItem?.quantity ?: 0
        } else {
            return -1
        }
    }
    fun getItemCount(item: Product): Int {
        return cardItems.find { it.id == item.id }?.quantity ?: 0
    }
    fun getItemCountById(id: String): Int {
        return cardItems.find { it.id == id }?.quantity ?: 0
    }
    fun updateCardTotal() {
        cardTotal.value = cardItems.sumOf { it.totalPrice }
    }

    fun setSuggestedProducts(products: List<Product>) {
        suggestedProducts.clear()
        suggestedProducts.addAll(products)
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