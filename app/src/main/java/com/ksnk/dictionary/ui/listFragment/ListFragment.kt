package com.ksnk.dictionary.ui.listFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.ksnk.dictionary.FragmentSettingListener
import com.ksnk.dictionary.R
import com.ksnk.dictionary.data.entity.Word
import com.ksnk.dictionary.ui.listFragment.adapter.ListAdapter
import com.ksnk.dictionary.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListFragment : Fragment(), FragmentSettingListener {
    private val listViewModel: ListViewModel by viewModel()
    private lateinit var wordList: MutableList<Word>
    private lateinit var listAdapter: ListAdapter
    private var mGridLayoutManager: GridLayoutManager? = null
    private var listRecyclerView: RecyclerView? = null
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
                    "Deleted " + deletedWord.wordID,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            wordList.add(position, deletedWord)
                            CoroutineScope(Dispatchers.IO).launch {
                                listViewModel.addWord(
                                    deletedWord
                                )}
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
        (activity as MainActivity?)?.setSettingListener(this@ListFragment)
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


}