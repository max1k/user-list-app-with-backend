package ru.mxk.userslist.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.mxk.userslist.model.item.PersonItem

class PersonItemDiffUtil(
    private val oldList: List<PersonItem>,
    private val newList: List<PersonItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size


    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.person.id == newItem.person.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }
}