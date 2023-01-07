package ru.mxk.userslist

import ru.mxk.userslist.model.Person

interface Navigator {

    fun showDetails(person: Person)

    fun goBack()

    fun toast(messageRes: Int)

}