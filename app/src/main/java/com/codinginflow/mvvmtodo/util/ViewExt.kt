package com.codinginflow.mvvmtodo.util

import androidx.appcompat.widget.SearchView

// inLine is for efficiency
// crossLine is necessary
inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit){
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
           return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
          listener(newText.orEmpty())
            return true
        }
    })
}