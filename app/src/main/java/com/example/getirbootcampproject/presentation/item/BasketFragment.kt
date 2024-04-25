package com.example.getirbootcampproject.presentation.item

import android.animation.LayoutTransition
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.getirbootcampproject.R
import com.example.getirbootcampproject.databinding.FragmentBasketBinding
import com.example.getirbootcampproject.databinding.ItemCardBinding
import com.example.getirbootcampproject.databinding.ItemPaymentBinding
import com.example.getirbootcampproject.domain.model.CardItem
import com.example.getirbootcampproject.domain.model.Product
import com.example.getirbootcampproject.presentation.adapter.SingleRecylerAdapter
import com.wada811.viewbindingktx.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasketFragment : Fragment(R.layout.fragment_basket) {

    private val viewModel: ItemViewModel by activityViewModels()
    private val binding by viewBinding(FragmentBasketBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    fun bind() {
        binding.apply {
            basketItemAdapter.data = viewModel.cardItems
            rvBasketItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvBasketItems.adapter = basketItemAdapter

            itemSuggestedAdapter.data = viewModel.suggestedProducts
            rvItemListHorizontal.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvItemListHorizontal.adapter = itemSuggestedAdapter
        }
    }
    private val basketItemAdapter = SingleRecylerAdapter<ItemPaymentBinding, CardItem>(  //for suggested items linear layout and item.ImageURL instead of item.thumbnailURL
        { inflater, _, _ ->
            ItemPaymentBinding.inflate(
                inflater,
                binding.rvBasketItems,
                false
            )
        },
        { cardItemBinding, item ->
            with(cardItemBinding) {
                context?.let {
                    Glide.with(it)
                        .load(item.imageUrl)
                        .into(ivProductImage)
                }
                val count = viewModel.getItemCountById(item.id)
                tvProductName.text = item.name
                tvProductPrice.text = buildString {
                    append("₺")
                    append(String.format("%.2f", item.totalPrice))
                }
                tvProductAttribute.text = item.attribute
                if(count > 0){
                    tvItemCount.text = count.toString()
                }
                if (count > 1){
                    ivRemoveFromCard.setImageDrawable(
                        ResourcesCompat.getDrawable(resources,
                        R.drawable.ic_item_decrease, null))
                }
                ivAddToCart.setOnClickListener{
                    val count = viewModel.addToCard(Product(item.id))
                    tvItemCount.text = count.toString()
                    if (count > 1){
                        ivRemoveFromCard.setImageDrawable(
                            ResourcesCompat.getDrawable(resources,
                            R.drawable.ic_item_decrease, null))
                    }
                    mcvProductImage.strokeColor = ResourcesCompat.getColor(resources,
                        R.color.primary_color, null)
                }
                ivRemoveFromCard.setOnClickListener{
                    val count = viewModel.decreaseFromCard(Product(item.id))
                    if(count > 0)
                        tvItemCount.text = count.toString()
                    if (count==1){
                        ivRemoveFromCard.setImageDrawable(
                            ResourcesCompat.getDrawable(resources,
                                R.drawable.ic_item_delete, null))
                    }
                }
            }
        }
    )

    private val itemSuggestedAdapter = SingleRecylerAdapter<ItemCardBinding, Product>(  //for suggested items linear layout and item.ImageURL instead of item.thumbnailURL
        { inflater, _, _ ->
            ItemCardBinding.inflate(
                inflater,
                binding.rvItemListHorizontal,
                false
            )
        },
        { cardItemBinding, item ->
            with(cardItemBinding) {
                context?.let {
                    Glide.with(it)
                        .load(item.imageURL)
                        .into(ivProductImage)
                }
                val count = viewModel.getItemCount(item)
                tvProductName.text = item.name
                tvProductPrice.text = item.priceText
                tvProductAttribute.text = item.attribute
                lladdToCart.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                if(count > 0){
                    tvItemCount.text = count.toString()
                    tvItemCount.visibility = View.VISIBLE
                    ivRemoveFromCard.visibility = View.VISIBLE
                    mcvProductImage.strokeColor = ResourcesCompat.getColor(resources,
                        R.color.primary_color, null)
                }
                if (count > 1){
                    ivRemoveFromCard.setImageDrawable(ResourcesCompat.getDrawable(resources,
                        R.drawable.ic_item_decrease, null))
                }
                ivAddToCart.setOnClickListener{
                    Log.v("ItemListFragment", "Add to cart clicked")
                    if(tvItemCount.visibility == View.GONE){
                        val v = View.VISIBLE
                        tvItemCount.visibility = v
                        ivRemoveFromCard.visibility = v
                    }
                    val count = viewModel.addToCard(item)
                    tvItemCount.text = count.toString()
                    if (count > 1){
                        ivRemoveFromCard.setImageDrawable(ResourcesCompat.getDrawable(resources,
                            R.drawable.ic_item_decrease, null))
                    }
                    mcvProductImage.strokeColor = ResourcesCompat.getColor(resources,
                        R.color.primary_color, null)
                }
                ivRemoveFromCard.setOnClickListener{
                    Log.v("ItemListFragment", "Remove from cart clicked")
                    val count = viewModel.decreaseFromCard(item)
                    if(count > 0)
                        tvItemCount.text = count.toString()
                    if (count==1){
                        ivRemoveFromCard.setImageDrawable(
                            ResourcesCompat.getDrawable(resources,
                                R.drawable.ic_item_delete, null))
                    }
                    if (count == 0){
                        tvItemCount.visibility = View.GONE
                        ivRemoveFromCard.visibility = View.GONE
                        mcvProductImage.strokeColor = ResourcesCompat.getColor(resources,
                            R.color.bg_primary_subtle, null)
                    }
                }
                cardLayout.setOnClickListener{
                    val direction = ItemListFragmentDirections.actionItemListFragmentToDetailFragment(item)
                    findNavController().navigate(direction)
                }
            }
        }
    )


}