package com.example.getirbootcampproject.presentation.item

import android.animation.LayoutTransition
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.getirbootcampproject.R
import com.example.getirbootcampproject.databinding.FragmentDetailBinding
import com.example.getirbootcampproject.domain.model.Product
import com.wada811.viewbindingktx.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val binding by viewBinding(FragmentDetailBinding::bind)
    private val viewModel: ItemViewModel by activityViewModels()

    private val args: DetailFragmentArgs by navArgs()
    lateinit var item: Product
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item = args.item
        bindItem(item)
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.getCardTotal().observe(viewLifecycleOwner){
            binding.tvCardTotal.text = buildString {
                append("₺")
                append(String.format("%.2f", it))
            }
        }
    }

    fun initListeners() {

        binding.apply {
            cvBasket.setOnClickListener { findNavController().navigate(R.id.action_detailFragment_to_basketFragment) }
            llBottomLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            btnAddToCart.setOnClickListener {
                Log.v("ItemListFragment", "First add to card")
                val count = viewModel.addToCard(item)
                tvItemCount.text = count.toString()
                btnAddToCart.visibility = View.GONE
                cvItemFooter.visibility = View.VISIBLE
            }
            ivAddToCart.setOnClickListener{
                Log.v("ItemListFragment", "Add to cart clicked")
                val count = viewModel.addToCard(item)
                tvItemCount.text = count.toString()
                if (count > 1){
                    ivRemoveFromCard.setImageDrawable(ResourcesCompat.getDrawable(resources,
                        R.drawable.ic_item_decrease, null))
                }
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
                if (count==0){
                    cvItemFooter.visibility = View.GONE
                    btnAddToCart.visibility = View.VISIBLE
                }
                if (count < 0) {
                    cvItemFooter.visibility = View.GONE
                    btnAddToCart.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Ürün bulunamadı", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun bindItem(item: Product) {
        binding.apply {
            tvDetailName.text = item.name
            tvDetailPrice.text = item.priceText
            tvDetailAttribute.text = item.attribute
            val count = viewModel.getItemCount(item).toString()
            Glide.with(requireContext()).load(item.imageURL).into(ivItem)
            if (count.toInt() > 0) {
                btnAddToCart.visibility = View.GONE
                cvItemFooter.visibility = View.VISIBLE
                tvItemCount.text = count
                if (count.toInt() > 1) {
                    ivRemoveFromCard.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_item_decrease, null
                        )
                    )
                }
            }
        }
    }

}