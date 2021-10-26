package com.example.flobizhackathon.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.flobizhackathon.Adapter.TagsAdapter
import com.example.flobizhackathon.Adapter.onClick
import com.example.flobizhackathon.databinding.BottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class BottomSheetTags : BottomSheetDialogFragment() , onClick{
    private lateinit var binding: BottomSheetLayoutBinding
    private val viewModel : MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=
        BottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "BottomSheetTags"
    }
  private lateinit var list: List<String>
    private lateinit var adapter: TagsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list=viewModel.tagList
        Log.d("ankit", "onCreateF: $viewModel")

        adapter= TagsAdapter(list,this,viewModel.clickedTag)
        binding.rvTags.apply {
            adapter=this@BottomSheetTags.adapter
            layoutManager=StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        }


    }
    private lateinit var click: bsTagClick

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {

            click = context as bsTagClick
        }catch (ex:Exception){
            Log.d(TAG, "onAttach: ${ex.message}")
        }
    }
    override fun itemClicked(position: Int) {
        this.dismiss()
        viewModel.clickedTag=list[position]
        click.tagClicked(list[position])

    }
    interface bsTagClick{
        fun tagClicked(tag :String)
    }
}