package ru.mxk.userslist.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.mxk.userslist.model.Person

class PersonDiffUtil(
    private val oldList: List<Person>,
    private val newList: List<Person>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size


    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPerson = oldList[oldItemPosition]
        val newPerson = newList[newItemPosition]

        return oldPerson.id == newPerson.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPerson = oldList[oldItemPosition]
        val newPerson = newList[newItemPosition]

        return oldPerson == newPerson
    }
}