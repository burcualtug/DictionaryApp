package com.orion.dictionarymvvm.ui.addedwords

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orion.dictionarymvvm.R
import com.orion.dictionarymvvm.data.firebase.Words
import com.orion.dictionarymvvm.ui.addword.AddWordFragment

class CustomAdapterAddedWords(
    private val context: AddedWordsFragment,
    private val Wordlist: ArrayList<Words>,
    private val listener: AddedWordsFragment,
) :
    RecyclerView.Adapter<CustomAdapterAddedWords.ItemViewHolder>(), Filterable {
    private lateinit var mListener: ItemClickListener

    interface ItemClickListener {
        fun onItemClicked(item: Words)
        fun onItemClick(position: Int)
    }

    //temporary list for search
    private var WordSearchlist = arrayListOf<Words>()

    fun setItemClickListener(clickListener: ItemClickListener) {
        mListener = clickListener
    }
    fun submitList(wordList: ArrayList<Words>) {
        Wordlist.clear()
        Wordlist.addAll(wordList)
        WordSearchlist = wordList
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Words? {
        return WordSearchlist.get(position)
    }


    inner class ItemViewHolder(itemView: View, clickListener: ItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        private val turkish = itemView.findViewById<TextView>(R.id.rv_turkish)
        private val english = itemView.findViewById<TextView>(R.id.rv_english)
        fun bind(wordlist: Words, context: AddedWordsFragment) {
            turkish.text = wordlist.turkish;
            english.text = wordlist.english;
        }

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    init {
        this.WordSearchlist = Wordlist
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false);
        return ItemViewHolder(view, mListener);
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(WordSearchlist[position], context)
    }

    override fun getItemCount(): Int {
        return WordSearchlist.size;
    }

    //function to filter
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    WordSearchlist = Wordlist
                } else {
                    val filteredList = ArrayList<Words>()
                    //The part to search for the desired data
                    for (word in Wordlist) {
                        if (word.english.toLowerCase().contains(charString.toLowerCase()) ||
                            word.turkish.toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(word)
                        }
                    }
                    WordSearchlist = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = WordSearchlist
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                WordSearchlist = filterResults.values as ArrayList<Words>
                notifyDataSetChanged()
            }

        }
    }


}