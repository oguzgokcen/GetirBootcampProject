package com.example.getirbootcampproject.data.remote
import com.example.getirbootcampproject.domain.model.Product
import com.example.getirbootcampproject.domain.model.remote.RespData
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("products")
    fun getProducts(): Call<RespData<Product>>

    @GET("suggestedProducts")
    fun getSuggestedProducts(): Call<RespData<Product>>
}