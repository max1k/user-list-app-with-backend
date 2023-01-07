package ru.mxk.userslist.servce


import ru.mxk.userslist.converter.PersonConverter
import ru.mxk.userslist.enumeration.Direction
import ru.mxk.userslist.exception.NoSuchPersonException
import ru.mxk.userslist.model.Person
import java.util.*

typealias PersonListener = (persons: MutableList<Person>) -> Unit

class PersonService(private val personConverter: PersonConverter) {
    private val persons : MutableList<Person> = ArrayList((1 .. 50).map(personConverter::createPerson))

    private var listeners = mutableListOf<PersonListener>()

    fun likePerson(id: UUID) {
        val (index, person) = getPersonWithIndexById(id)
        persons[index] = person.copy(liked = !person.liked)

        notifyChanges()
    }

    fun firePerson(id: UUID) {
        val (index, person) = getPersonWithIndexById(id)
        persons[index] = person.copy(fired = !person.fired)

        notifyChanges()
    }

    fun activatePerson(id: UUID) {
        val (index, person) = getPersonWithIndexById(id)
        persons[index] = person.copy(active = !person.active)

        notifyChanges()
    }

    fun removePerson(id: UUID) {
        val (index, _) = getPersonWithIndexById(id)
        persons.removeAt(index)

        notifyChanges()
    }

    fun movePerson(id: UUID, direction: Direction) {
        val (oldIndex, _) = getPersonWithIndexById(id)

        val newIndex = oldIndex + direction.value
        Collections.swap(persons, oldIndex, newIndex)

        notifyChanges()
    }

    fun findPersonById(id: UUID): Person {
        return getPersonWithIndexById(id).second
    }

    private fun getPersonWithIndexById(id: UUID): Pair<Int, Person> {
        val index = persons.indexOfFirst { it.id == id }
        if (index == -1) throw NoSuchPersonException(id)
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