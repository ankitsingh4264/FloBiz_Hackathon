package com.example.flobizhackathon.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flobizhackathon.adapter.QuestionsAdapter
import com.example.flobizhackathon.adapter.onClick
import com.example.flobizhackathon.util.DebouncingSearch
import com.example.flobizhackathon.databinding.ActivityMainBinding
import com.example.flobizhackathon.model.Items
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Intent
import android.net.Uri
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomSheetTags.bsTagClick , onClick {
    private lateinit var binding: ActivityMainBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var adapter: QuestionsAdapter

    private val viewModel: MainActivityViewModel by viewModels()

    private val list = ArrayList<Items?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgFilter.setOnClickListener {
            setTags()
        }
        orgList.add(Items())
        manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = QuestionsAdapter(list, this)
        binding.rvItem.adapter = adapter
        binding.rvItem.layoutManager = manager
        loadData()
        observeViewModel()

        with(binding) {
            rvItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        //load data
                        loadData()
                    }
                }
            })


            //search functionality
            searchBar.setOnQueryTextListener(DebouncingSearch(lifecycleScope) {
                if (it.isNullOrEmpty()) {
                    if (filter) {
                        filter = false
                        return@DebouncingSearch
                    }
                    search = false
                    list.clear()
                    list.addAll(orgList)
                    adapter.notifyDataSetChanged()

                } else {
                    filter=false
                    search = true
                    filterData(it)
                }
            })
        }

    }


    private fun setTags() {
        lifecycleScope.launch {
            val set = HashSet<String>()

            for (item in orgList) {
                item.tags?.let {
                    for (tag in item.tags!!) {
                        if (tag != null) {
                            set.add(tag)
                        }
                    }
                }

            }
            val temp = ArrayList<String>(set)
            viewModel.tagList = temp
            BottomSheetTags().show(supportFragmentManager, BottomSheetTags.TAG)


        }
    }

    private var orgList: ArrayList<Items> = ArrayList()
    private var search = false
    private var filter = false
    private fun filterData(search: String?) {
        val filterList = ArrayList<Items>();

        CoroutineScope(Dispatchers.Main).launch {
            for (item in orgList) {
                if (item.tags == null) continue
                if (item.tags!!.contains(search) || item.title!!.contains(
                        search.toString(),
                        true
                    )
                ) {
                    filterList.add(item)
                }
            }
            list.clear()
            list.add(Items())
            list.addAll(filterList)
            calculateAvg()
            list[0]?.answerCount = avgAnsCount
            list[0]?.viewCount = avgViewCount

            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }

        }
    }


    private fun loadData() {
        if (search) return
        binding.pb.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.getData()
        }
    }


    private fun observeViewModel() {
        viewModel.data.observe(this, Observer {
            for (temp in it) temp?.let { it1 -> orgList.add(it1) }

            lifecycleScope.launch {
                calculateAvg()
                orgList[0].answerCount = avgAnsCount
                orgList[0].viewCount = avgViewCount
                list.clear()
                list.addAll(orgList.filterNotNull())

                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()

                }
                binding.pb.visibility = View.GONE

            }

        })
    }

    private var avgViewCount = 0L
    private var avgAnsCount = 0L
    fun calculateAvg() {
        if (orgList.size == 1) return
        var totViewCount = 0L;
        var totAnsCount = 0L;
        for (item in orgList) {
            item.viewCount?.let {
                totViewCount += it
            }
            item.answerCount?.let {
                totAnsCount += it
            }

        }
        val totElement = orgList.size - 1;
        avgViewCount = totViewCount / totElement;
        avgAnsCount = (totAnsCount / totElement)


    }

    override fun tagClicked(tag: String) {
        binding.txtTag.text = tag.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        filterData(tag)
    }

    override fun itemClicked(position: Int) {
        if (position==0) return
        val url = list[position]?.link
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

}