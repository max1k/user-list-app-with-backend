package ru.mxk.userslist.servce


import ru.mxk.userslist.converter.PersonConverter
import ru.mxk.userslist.enumeration.Direction
import ru.mxk.userslist.model.Person
import java.util.*
import kotlin.NoSuchElementException
import kotlin.collections.ArrayList

typealias PersonListener = (persons: MutableList<Person>) -> Unit

class PersonService(private val personConverter: PersonConverter) {
    private val persons : MutableList<Person> = ArrayList((1 .. 50).map(personConverter::createPerson))

    private var listeners = mutableListOf<PersonListener>()

    fun likePerson(id: Long) {
        val (index, person) = getPersonWithIndexById(id)
        persons[index] = person.copy(liked = !person.liked)

        notifyChanges()
    }

    fun firePerson(id: Long) {
        val (index, person) = getPersonWithIndexById(id)
        persons[index] = person.copy(fired = !person.fired)

        notifyChanges()
    }

    fun activatePerson(id: Long) {
        val (index, person) = getPersonWithIndexById(id)
        persons[index] = person.copy(active = !person.active)

        notifyChanges()
    }

    fun removePerson(id: Long) {
        val (index, _) = getPersonWithIndexById(id)
        persons.removeAt(index)

        notifyChanges()
    }

    fun movePerson(id: Long, direction: Direction) {
        val (oldIndex, _) = getPersonWithIndexById(id)

        val newIndex = oldIndex + direction.value
        Collections.swap(persons, oldIndex, newIndex)

        notifyChanges()
    }

    private fun getPersonWithIndexById(id: Long): Pair<Int, Person> {
        val index = persons.indexOfFirst { it.id == id }
        if (index == -1) throw NoSuchElementException()
        val person = persons[index]

        return Pair(index, person)
    }

    fun addListener(listener: PersonListener) {
        listeners.add(listener)
        listener.invoke(persons)
    }

    fun removeListener(listener: PersonListener) {
        listeners.remove(listener)
        listener.invoke(persons)
    }

    private fun notifyChanges() = listeners.forEach { it.invoke(persons) }
}