package com.example.getirbootcampproject

import android.animation.LayoutTransition
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.getirbootcampproject.databinding.FragmentItemListBinding
import com.example.getirbootcampproject.databinding.ItemCardBinding
import com.example.getirbootcampproject.domain.model.CardItem
import com.example.getirbootcampproject.presentation.adapter.SingleRecylerAdapter
import com.wada811.viewbindingktx.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemListFragment : Fragment(R.layout.fragment_item_list) {

    private val binding by viewBinding(FragmentItemListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("ItemListFragment", "onViewCreated")
        val cardItems = listOf(
            CardItem(
                id = "66055119e6d3728ea0a686a0",
                name = "Kızılay Erzincan & Misket Limonu ve Nane Aromalı İçecek İkilisi",
                attribute = "2 Products",
                thumbnailURL = "https://market-product-images-cdn.getirapi.com/product/62a59d8a-4dc4-4b4d-8435-643b1167f636.jpg",
                imageURL = "https://market-product-images-cdn.getirapi.com/product/62a59d8a-4dc4-4b4d-8435-643b1167f636.jpg",
                price = 65.3,
                priceText = "₺65,30"
            ),
            CardItem(
                id = "5d6d2c696deb8b00011f7665",
                name = "Kuzeyden",
                attribute = "2 x 5 L",
                thumbnailURL = "http://cdn.getir.com/product/5d6d2c696deb8b00011f7665_tr_1617795578982.jpeg",
                imageURL = "http://cdn.getir.com/product/5d6d2c696deb8b00011f7665_tr_1617795578982.jpeg",
                price = 59.2,
                priceText = "₺59,20"
            ),
            CardItem(
                id = "645a08cc4d357e68122d74b1",
                name = "Sırma Lemon & Sırma Black Mulberry & Blackcurrant Mineral Water",
                attribute = "2 Products",
                thumbnailURL = "https://market-product-images-cdn.getirapi.com/product/ff43e9c8-a6a0-4444-923b-4972b2915284.png",
                imageURL = "https://market-product-images-cdn.getirapi.com/product/ff43e9c8-a6a0-4444-923b-4972b2915284.png",
                price = 43.9,
                priceText = "₺43,90"
            )
        )

        itemAdapter.data = cardItems
        binding.rvItemList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvItemList.adapter = itemAdapter
        initListener()
    }

    private fun initListener() {

    }

    private val itemAdapter = SingleRecylerAdapter<ItemCardBinding, CardItem>(
        { inflater, _, _ ->
            ItemCardBinding.inflate(
                inflater,
                binding.rvItemList,
                false
            )
        },
        { binding, item ->
            with(binding) {
                context?.let {
                    Glide.with(it)
                        .load(item.thumbnailURL)  //başlık resmini kullanıyoruz
                        .into(ivProductImage)
                }
                tvProductName.text = item.name
                tvProductPrice.text = item.priceText
                tvProductAttribute.text = item.attribute
                lladdToCart.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                ivAddToCart.setOnClickListener{
                    Log.v("ItemListFragment", "Add to cart clicked")
                    if(tvItemCount.visibility == View.GONE){
                        val v = View.VISIBLE
                        tvItemCount.visibility = v
                        ivRemoveFromCard.visibility = v
                    }
                    val currentValue = tvItemCount.text.toString().toInt()
                    val newValue = currentValue + 1
                    tvItemCount.text = newValue.toString()
                }
                ivRemoveFromCard.setOnClickListener{
                    Log.v("ItemListFragment", "Remove from cart clicked")
                    if(tvItemCount.text.toString().toInt() > 0)
                        tvItemCount.text = (tvItemCount.text.toString().toInt() - 1).toString()
                    if (tvItemCount.text.toString().toInt() == 0){
                        tvItemCount.visibility = View.GONE
                        ivRemoveFromCard.visibility = View.GONE
                    }
                }
            }
        }

    )
}