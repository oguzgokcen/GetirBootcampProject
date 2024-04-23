package com.example.getirbootcampproject.domain.model

import retrofit2.http.Url

data class CardItem(
    val id: String,
    val name: String,
    val attribute: String,
    val thumbnailURL: String,
    val imageURL: String,
    val price: Double,
    val priceText: String
)

