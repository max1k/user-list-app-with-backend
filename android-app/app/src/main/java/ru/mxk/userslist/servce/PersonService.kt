package ru.mxk.userslist.servce


import retrofit2.Response
import ru.mxk.userslist.dto.result.RequestResult
import ru.mxk.userslist.dto.result.ResultStatus
import ru.mxk.userslist.enumeration.Direction
import ru.mxk.userslist.exception.NoSuchPersonException
import ru.mxk.userslist.model.Person
import ru.mxk.userslist.repository.PersonRepository
import java.net.SocketTimeoutException
import java.util.Collections
import java.util.UUID

typealias PersonListener = (persons: RequestResult<MutableList<Person>>) -> Unit


class PersonService(private val personRepository: PersonRepository) {

    private var persons: RequestResult<MutableList<Person>> = RequestResult(ResultStatus.PENDING, mutableListOf())

    private val listeners = mutableListOf<PersonListener>()

    suspend fun loadAll() {
        persons = RequestResult.ofPending()
        notifyChanges()

        try {
            personRepository.findAll()
                .process {
                    persons = RequestResult.ofSuccess(it?.toMutableList() ?: mutableListOf())
                }
        } catch (exception: SocketTimeoutException) {
            persons = RequestResult.ofFail()
            notifyChanges()
        }
    }

    suspend fun likePerson(id: UUID) {
        val (index, person) = getPersonWithIndexById(id)
        val copiedPerson = person.copy(liked = !person.liked)

        personRepository.save(copiedPerson)
            .process {
                if (it != null) {
                    persons.data[index] = it
                }
            }
    }

    private fun<T> Response<T>.process(consumer: (T?) -> Unit) {
        if (isSuccessful) {
            consumer.invoke(body())
            notifyChanges()
        }
    }

    suspend fun firePerson(id: UUID) {
        val (index, person) = getPersonWithIndexById(id)
        val copiedPerson = person.copy(fired = !person.fired)

        personRepository.save(copiedPerson)
            .process {
                if (it != null) {
                    persons.data[index] = it
                }
            }
    }

    suspend fun activatePerson(id: UUID) {
        val (index, person) = getPersonWithIndexById(id)
        val copiedPerson = person.copy(active = !person.active)

        personRepository.save(copiedPerson)
            .process {
                if (it != null) {
                    persons.data[index] = it
                }
            }
    }

    suspend fun removePerson(id: UUID) {
        personRepository.delete(id)
            .process {
                if (it != null) {
                    val (index, _) = getPersonWithIndexById(it.id)
                    persons.data.removeAt(index)
                }
            }
    }

    fun movePerson(id: UUID, direction: Direction) {
        val (oldIndex, _) = getPersonWithIndexById(id)

        val newIndex = oldIndex + direction.value
        Collections.swap(persons.data, oldIndex, newIndex)

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
        val index = persons.data.indexOfFirst { it.id == id }
        if (index == -1) throw NoSuchPersonException(id)
        val person = persons.data[index]

        return Pair(index, person)
    }

    fun addListener(listener: PersonListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: PersonListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(persons) }
    }
}