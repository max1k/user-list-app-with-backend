package ru.mxk.userslist.servce


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import ru.mxk.userslist.enumeration.Direction
import ru.mxk.userslist.exception.NoSuchPersonException
import ru.mxk.userslist.model.Person
import ru.mxk.userslist.repository.PersonRepository
import java.util.*

typealias PersonListener = (persons: MutableList<Person>) -> Unit



class PersonService(private val personRepository: PersonRepository) {

    private val persons : MutableList<Person> = mutableListOf()

    private var listeners = mutableListOf<PersonListener>()

    suspend fun loadAll() {
        persons.clear()

        personRepository.findAll()
            .process {
                persons.addAll(it ?: emptyList())
                notifyChanges()
            }
    }

    suspend fun likePerson(id: UUID) {
        val (index, person) = getPersonWithIndexById(id)
        val copiedPerson = person.copy(liked = !person.liked)

        personRepository.save(copiedPerson)
            .process {
                if (it != null) {
                    persons[index] = it
                    notifyChanges()
                }
            }
    }

    private suspend fun<T> Response<T>.process(consumer: (T?) -> Unit) {
        val response = this
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                consumer.invoke(response.body())
            }
        }
    }

    suspend fun firePerson(id: UUID) {
        val (index, person) = getPersonWithIndexById(id)
        val copiedPerson = person.copy(fired = !person.fired)

        personRepository.save(copiedPerson)
            .process {
                if (it != null) {
                    persons[index] = it
                    notifyChanges()
                }
            }
    }

    suspend fun activatePerson(id: UUID) {
        val (index, person) = getPersonWithIndexById(id)
        val copiedPerson = person.copy(active = !person.active)

        personRepository.save(copiedPerson)
            .process {
                if (it != null) {
                    persons[index] = it
                    notifyChanges()
                }
            }
    }

    suspend fun removePerson(id: UUID) {
        personRepository.delete(id)
            .process {
                if (it != null) {
                    val (index, _) = getPersonWithIndexById(it.id)
                    persons.removeAt(index)
                    notifyChanges()
                }
            }
    }

    fun movePerson(id: UUID, direction: Direction) {
        val (oldIndex, _) = getPersonWithIndexById(id)

        val newIndex = oldIndex + direction.value
        Collections.swap(persons, oldIndex, newIndex)

        notifyChanges()
    }

    suspend fun findPersonById(id: UUID): Person {
        val response = personRepository.findById(id)
        val person = response.body()
        if (!response.isSuccessful || person == null) {
            throw NoSuchPersonException(id)
        }
        return person
    }

    private fun getPersonWithIndexById(id: UUID): Pair<Int, Person> {
        val index = persons.indexOfFirst { it.id == id }
        if (index == -1) throw NoSuchPersonException(id)
        val person = persons[index]

        return Pair(index, person)
    }

    fun addListener(listener: PersonListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: PersonListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() = listeners.forEach { it.invoke(persons) }
}