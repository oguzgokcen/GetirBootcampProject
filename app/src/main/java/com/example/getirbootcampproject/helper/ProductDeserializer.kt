package com.example.getirbootcampproject.helper

import android.util.Log
import com.example.getirbootcampproject.domain.model.remote.RespData
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ProductDeserializer<T> : JsonDeserializer<RespData<T>> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): RespData<T> {
        val productsArray = json?.asJsonArray
        val productsElement= productsArray?.get(0)?.asJsonObject?.get("products")?.asJsonArray
        if (productsElement == null || !productsElement.isJsonArray) {
            throw JsonParseException("Invalid JSON format: 'products' array not found.")
        }

        // Determine the inner type of RespData
        val innerType: Type = when (typeOfT) {
            is ParameterizedType -> typeOfT.actualTypeArguments.firstOrNull() ?: throw JsonParseException("Inner type not found.")
            else -> throw JsonParseException("ParameterizedType expected.")
        }

        var productsList:List<T> = ArrayList()
        if (productsElement != null) {
            for(i in 0 until productsElement.size()){
                val productElement = productsElement.get(i)
                val product = context.deserialize<T>(productElement, innerType)
                (productsList as ArrayList).add(product)
            }
        }
        return RespData(productsList)
    }

}