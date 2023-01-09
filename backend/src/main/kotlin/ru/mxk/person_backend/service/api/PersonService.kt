package ru.mxk.person_backend.service.api


import ru.mxk.person_backend.model.Person
import java.util.*

interface PersonService {
    fun getAll() : List<Person>

    fun findById(personId: UUID): Person

    fun save(person: Person): Person

    fun remove(personId: UUID): Person

}