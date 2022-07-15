package com.ksnk.dictionary.ui.listFragment.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ksnk.dictionary.R


class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textViewEng: TextView = itemView.findViewById(R.id.textViewEng)
    var textViewTranslate: TextView = itemView.findViewById(R.id.textViewTranslate)
    var imageButtonPlay: ImageButton = itemView.findViewById(R.id.imageButtonPlay)
}