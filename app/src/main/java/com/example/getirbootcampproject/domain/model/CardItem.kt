package com.example.getirbootcampproject.domain.model

class CardItem(
    val id: String,
    val name: String,
    val basePrice: Double,
    val imageUrl: String,
    var quantity: Int =0,
    var totalPrice: Double = basePrice * quantity,
    val attribute:String=""
) {
    init {
        increaseQuantity()
    }
    fun increaseQuantity() {
        quantity++
        totalPrice = basePrice * quantity
    }
    fun decreaseQuantity() {
        if (quantity > 0) {
            quantity--
            totalPrice = basePrice * quantity
        }
    }
}
