package com.ksnk.dictionary.ui.listFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.ksnk.dictionary.FilterValues
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("eeee", listViewModel.getFilterValue().toString())
        listRecyclerView = view.findViewById(R.id.listRecyclerView)
        radioGroupSort = view.findViewById(R.id.rgSort)

        radioGroupSort?.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbLast -> {
                    listViewModel.setFilterValue(FilterValues.FILTER_LAST.toString().toInt())
                    listViewModel.getAllDesc().observe(viewLifecycleOwner, Observer {
                        if (it.isNotEmpty()) {
                            wordList.clear()
                            wordList.addAll(it)
                            listAdapter.notifyDataSetChanged()
                        } else {
                            wordList.clear()
                            listAdapter.notifyDataSetChanged()

                        }
                    })

                }
                R.id.rbEngAsc -> {
                    listViewModel.setFilterValue(FilterValues.FILTER_ENG_ASC.value)
                    listViewModel.getAllEngAsc().observe(viewLifecycleOwner, Observer {
                        if (it.isNotEmpty()) {
                            wordList.clear()
                            wordList.addAll(it)
                            listAdapter.notifyDataSetChanged()
                        } else {
                            wordList.clear()
                            listAdapter.notifyDataSetChanged()

                        }
                    })
                }
                R.id.rbEngDesc -> {
                    listViewModel.setFilterValue(FilterValues.FILTER_ENG_DESC.toString().toInt())
                    listViewModel.getAllEngDesc().observe(viewLifecycleOwner, Observer {
                        if (it.isNotEmpty()) {
                            wordList.clear()
                            wordList.addAll(it)
                            listAdapter.notifyDataSetChanged()
                        } else {
                            wordList.clear()
                            listAdapter.notifyDataSetChanged()

                        }
                    })
                }
                R.id.rbUkrAsc -> {
                    listViewModel.setFilterValue(FilterValues.FILTER_UKR_ASC.toString().toInt())
                    listViewModel.getAllUkrAsc().observe(viewLifecycleOwner, Observer {
                        if (it.isNotEmpty()) {
                            wordList.clear()
                            wordList.addAll(it)
                            listAdapter.notifyDataSetChanged()
                        } else {
                            wordList.clear()
                            listAdapter.notifyDataSetChanged()

                        }
                    })

                }
                R.id.rbUkrDesc -> {
                    listViewModel.setFilterValue(FilterValues.FILTER_UKR_DESC.toString().toInt())
                    listViewModel.getAllUkrDesc().observe(viewLifecycleOwner, Observer {
                        if (it.isNotEmpty()) {
                            wordList.clear()
                            wordList.addAll(it)
                            listAdapter.notifyDataSetChanged()
                        } else {
                            wordList.clear()
                            listAdapter.notifyDataSetChanged()

                        }
                    })
                }


            }
        }


        wordList = ArrayList()
        mGridLayoutManager = GridLayoutManager(activity, 1)
        listAdapter = ListAdapter(wordList, requireContext())
        listRecyclerView?.adapter = listAdapter
        listRecyclerView?.layoutManager = mGridLayoutManager
        listRecyclerView?.setHasFixedSize(true)
        listViewModel.getAllDesc().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                wordList.clear()
                wordList.addAll(it)
                listAdapter.notifyDataSetChanged()
            } else {
                wordList.clear()
                listAdapter.notifyDataSetChanged()

            }
        })

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
            Log.d("ddddd", it.toString())
            if (it.isNotEmpty()) {
                wordList.clear()
                wordList.addAll(it)
                listAdapter.notifyDataSetChanged()
            } else {
                wordList.clear()
                listAdapter.notifyDataSetChanged()


            }
        }
    }

    companion object

    fun newInstance(): ListWordFragment {
        return ListWordFragment()
    }

}