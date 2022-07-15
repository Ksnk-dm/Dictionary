package com.ksnk.dictionary.ui.listFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ksnk.dictionary.R
import com.ksnk.dictionary.data.entity.Word
import com.ksnk.dictionary.ui.listFragment.adapter.ListAdapter
import com.ksnk.dictionary.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListFragment : Fragment() {
    private val listViewModel: ListViewModel by viewModel()
    private lateinit var wordList: MutableList<Word>
    private lateinit var listAdapter: ListAdapter
    private var mGridLayoutManager: GridLayoutManager?=null
    private var listRecyclerView:RecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listRecyclerView = view.findViewById(R.id.listRecyclerView)
        wordList = ArrayList()
        mGridLayoutManager = GridLayoutManager(activity, 1)
        listAdapter = ListAdapter(wordList, requireContext())
        listRecyclerView?.adapter = listAdapter
        listRecyclerView?.layoutManager = mGridLayoutManager
        listRecyclerView?.setHasFixedSize(true)
        listViewModel.getAllWords().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                wordList.clear()
                wordList.addAll(it)
                listAdapter.notifyDataSetChanged()
            } else {
                wordList.clear()
                listAdapter.notifyDataSetChanged()

            }
        })
//        CoroutineScope(Dispatchers.IO).launch {
//            listViewModel.addWord(
//                Word(
//                    0,
//                    wordEng = "read",
//                    wordTranslate = "читать"
//                )
//            )
//        }

    }


}