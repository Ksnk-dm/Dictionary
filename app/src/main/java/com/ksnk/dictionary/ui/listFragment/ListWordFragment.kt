package com.ksnk.dictionary.ui.listFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.ksnk.dictionary.enums.FilterValues
import com.ksnk.dictionary.listeners.FragmentSettingListener
import com.ksnk.dictionary.R
import com.ksnk.dictionary.data.entity.Word
import com.ksnk.dictionary.ui.listFragment.adapter.ListAdapter
import com.ksnk.dictionary.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListWordFragment : Fragment(), FragmentSettingListener {
    private val listViewModel: ListViewModel by viewModel()
    private lateinit var wordList: MutableList<Word>
    private lateinit var listAdapter: ListAdapter
    private var mGridLayoutManager: GridLayoutManager? = null
    private var listRecyclerView: RecyclerView? = null
    private var radioGroupSort: RadioGroup? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(it: List<Word>) {
        if (it.isNotEmpty()) {
            wordList.clear()
            wordList.addAll(it)
            listAdapter.notifyDataSetChanged()
        } else {
            wordList.clear()
            listAdapter.notifyDataSetChanged()

        }
    }

    private fun loadAndSetLast() {
        listViewModel.setFilterValue(FilterValues.FILTER_LAST.value)
        listViewModel.getAllDesc().observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
    }

    private fun loadAndSetEngAsc() {
        listViewModel.setFilterValue(FilterValues.FILTER_ENG_ASC.value)
        listViewModel.getAllEngAsc().observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
    }

    private fun loadAndSetEngDesc() {
        listViewModel.setFilterValue(FilterValues.FILTER_ENG_DESC.value)
        listViewModel.getAllEngDesc().observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
    }

    private fun loadAndSetUkrAsc() {
        listViewModel.setFilterValue(FilterValues.FILTER_UKR_ASC.value)
        listViewModel.getAllUkrAsc().observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
    }

    private fun loadAndSetUkrDesc() {
        listViewModel.setFilterValue(FilterValues.FILTER_UKR_DESC.value)
        listViewModel.getAllUkrDesc().observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listRecyclerView = view.findViewById(R.id.listRecyclerView)
        radioGroupSort = view.findViewById(R.id.rgSort)
        when (listViewModel.getFilterValue()) {
            0 -> {
                radioGroupSort?.check(R.id.rbLast)
                loadAndSetLast()
            }
            1 -> {
                radioGroupSort?.check(R.id.rbEngAsc)
                loadAndSetEngAsc()
            }
            2 -> {
                radioGroupSort?.check(R.id.rbEngDesc)
                loadAndSetEngDesc()
            }
            3 -> {
                radioGroupSort?.check(R.id.rbUkrAsc)
                loadAndSetUkrAsc()
            }
            4 -> {
                radioGroupSort?.check(R.id.rbUkrDesc)
                loadAndSetUkrDesc()
            }
        }




        wordList = ArrayList()
        mGridLayoutManager = GridLayoutManager(activity, 1)
        listAdapter = ListAdapter(wordList, requireContext())
        listRecyclerView?.adapter = listAdapter
        listRecyclerView?.layoutManager = mGridLayoutManager
        listRecyclerView?.setHasFixedSize(true)
        radioGroupSort?.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbLast -> {
                    loadAndSetLast()
                }
                R.id.rbEngAsc -> {
                    loadAndSetEngAsc()
                }
                R.id.rbEngDesc -> {
                    loadAndSetEngDesc()
                }
                R.id.rbUkrAsc -> {
                    loadAndSetUkrAsc()
                }
                R.id.rbUkrDesc -> {
                    loadAndSetUkrDesc()
                }
            }
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val deletedWord: Word =
                    wordList[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition
                wordList.removeAt(viewHolder.adapterPosition)
                CoroutineScope(Dispatchers.IO).launch {
                    listViewModel.deleteWord(
                        deletedWord
                    )
                }
                listAdapter.notifyItemRemoved(viewHolder.adapterPosition)

                Snackbar.make(
                    listRecyclerView!!,
                    "Deleted " + deletedWord.wordEng,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            wordList.add(position, deletedWord)
                            CoroutineScope(Dispatchers.IO).launch {
                                listViewModel.addWord(
                                    deletedWord
                                )
                            }
                            listAdapter.notifyItemInserted(position)
                        }).show()
            }
        }).attachToRecyclerView(listRecyclerView)


    }

    override fun search(textSearch: String?) {
        searchDatabase(textSearch!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity?)?.setSettingListener(this@ListWordFragment)
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        listViewModel.searchDatabase(searchQuery).observe(this) {
        updateUI(it)
            }
        }

    companion object

    fun newInstance(): ListWordFragment {
        return ListWordFragment()
    }

}