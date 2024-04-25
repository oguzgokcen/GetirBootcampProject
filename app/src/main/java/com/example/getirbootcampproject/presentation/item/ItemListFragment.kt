package com.example.getirbootcampproject.presentation.item

import android.animation.LayoutTransition
import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.getirbootcampproject.R
import com.example.getirbootcampproject.databinding.FragmentItemListBinding
import com.example.getirbootcampproject.databinding.ItemCardBinding
import com.example.getirbootcampproject.domain.ViewState
import com.example.getirbootcampproject.domain.model.BaseResponse
import com.example.getirbootcampproject.domain.model.Product
import com.example.getirbootcampproject.presentation.adapter.SingleRecylerAdapter
import com.wada811.viewbindingktx.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemListFragment : Fragment(R.layout.fragment_item_list) {

    private val binding by viewBinding(FragmentItemListBinding::bind)
    private val viewModel: ItemViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("ItemListFragment", "onViewCreated")

        viewModel.getSuggestedProducts()
        viewModel.getProducts()
        initListener()
        initObserver()
    }
    private fun initListener() =with(binding){
        cvBasket.setOnClickListener {
            findNavController().navigate(R.id.action_itemListFragment_to_basketFragment)
        }
    }

    private fun initObserver() = with(viewModel){
        viewLifecycleOwner.lifecycleScope.launch {
            uiStateItemList.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { viewState ->
                    when (viewState) {
                        is ViewState.Success -> {
                            val response = viewState.result as BaseResponse.Success
                            //binding.loadingView.visibility = View.GONE
                            itemAdapter.data = response.data.products
                            binding.rvItemListGrid.layoutManager = GridLayoutManager(context, 3)
                            binding.rvItemListGrid.adapter = itemAdapter
                            Log.v("ViewState.Success", response.data.toString())
                        }

                        is ViewState.Error -> {
                            val responseError = viewState.error
                            Log.v("ViewState.Error", responseError)
                        }

                        is ViewState.Loading -> {
                            Log.v("ViewState.Loading", "ViewState.Loading")
                        }
                    }
                }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            uiStateSuggestions.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { viewState ->
                    when (viewState) {
                        is ViewState.Success -> {
                            val response = viewState.result as BaseResponse.Success
                            itemSuggestedAdapter.data = response.data.products
                            binding.rvItemListHorizontal.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            binding.rvItemListHorizontal.adapter = itemSuggestedAdapter
                            setSuggestedProducts(response.data.products)
                            Log.v("ViewState.Success", response.data.toString())
                        }

                        is ViewState.Error -> {
                            val responseError = viewState.error
                            Log.v("ViewState.Error", responseError)
                        }

                        is ViewState.Loading -> {
                            Log.v("ViewState.Loading", "ViewState.Loading")
                        }
                    }
                }
        }
        getCardTotal().observe(viewLifecycleOwner){
            binding.tvCardTotal.text = buildString {
                append("₺")
                append(String.format("%.2f", it))
            }
        }
    }

    private val itemAdapter = SingleRecylerAdapter<ItemCardBinding, Product>(
        { inflater, _, _ ->
            ItemCardBinding.inflate(
                inflater,
                binding.rvItemListGrid,
                false
            )
        },
        { cardItemBinding, item ->
            with(cardItemBinding) {
                context?.let {
                    Glide.with(it)
                        .load(item.thumbnailURL)  //başlık resmini kullanıyoruz
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