package com.example.getirbootcampproject.domain.model

data class ProductSuggestionsResponse(
    val id: String,
    val imageURL: String,
    val name: String,
    val price: Double,
    val priceText: String,
    val shortDescription:String)