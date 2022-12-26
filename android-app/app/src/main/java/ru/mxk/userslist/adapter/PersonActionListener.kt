package ru.mxk.userslist.adapter

import ru.mxk.userslist.enumeration.Direction
import ru.mxk.userslist.model.Person

interface PersonActionListener {

    fun onPersonGetId(person: Person)

    fun onPersonLike(person: Person)

    fun onPersonRemove(person: Person)

    fun onPersonMove(person: Person, direction: Direction)

    fun onPersonFire(person: Person)

    fun onPersonActivate(person: Person)
}