package com.example.getirbootcampproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import com.example.getirbootcampproject.databinding.FragmentItemListBinding
import com.wada811.viewbindingktx.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemListFragment : Fragment(R.layout.fragment_item_list) {

    private val binding by viewBinding(FragmentItemListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("ItemListFragment", "onViewCreated")
    }
}