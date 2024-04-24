package com.example.getirbootcampproject.presentation.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
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
    private val viewModel: ItemViewModel by viewModels()

    private val args: DetailFragmentArgs by navArgs()
    lateinit var item: Product
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item = args.item
        bindItem(item)
    }

    override fun onResume() {
        super.onResume()

    }

    fun bindItem(item: Product) {
        binding.apply {
            tvDetailName.text = item.name
            tvDetailPrice.text = item.priceText
            tvDetailAttribute.text = item.attribute
            Glide.with(requireContext()).load(item.imageURL).into(ivItem)
        }
    }

}