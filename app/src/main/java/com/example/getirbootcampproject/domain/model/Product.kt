package com.example.getirbootcampproject.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
data class Product(
    val id: String="0",
    val name: String="",
    @SerializedName("attribute", alternate = ["shortDescription"])
    val attribute: String="",
    val thumbnailURL: String="",
    val imageURL: String="",
    val price: Double=0.00,
    val priceText: String="₺"
):Serializable

