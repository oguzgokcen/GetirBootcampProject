package com.example.getirbootcampproject.domain.model

data class Product(
    val id: String,
    val name: String,
    val attribute: String,
    val thumbnailURL: String,
    val imageURL: String,
    val price: Double,
    val priceText: String
)

