package com.ksnk.dictionary.ui.listFragment.adapter

import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ksnk.dictionary.R
import com.ksnk.dictionary.data.entity.Word
import java.util.*

class ListAdapter(private val wordList: List<Word>, context: Context) :
    RecyclerView.Adapter<ListViewHolder>() {
    var textToSpeech: TextToSpeech? = null

    init {
        textToSpeech = TextToSpeech(context) { i ->
            if (i == TextToSpeech.SUCCESS) {
                textToSpeech!!.language = Locale.UK
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.word_items, parent, false)
        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.textViewEng.text = wordList[position].wordEng
        holder.textViewTranslate.text = wordList[position].wordTranslate
        holder.imageButtonPlay.setOnClickListener {
            textToSpeech?.speak(wordList[position].wordEng, TextToSpeech.QUEUE_FLUSH, null, null)
        }
        holder.itemView.setOnClickListener {
            textToSpeech?.speak(wordList[position].wordEng, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun getItemCount(): Int = wordList.size

}