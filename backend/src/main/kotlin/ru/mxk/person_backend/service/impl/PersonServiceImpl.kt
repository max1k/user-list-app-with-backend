package ru.mxk.person_backend.service.impl

import org.springframework.stereotype.Service
import ru.mxk.person_backend.model.Person
import ru.mxk.person_backend.repository.PersonRepository
import ru.mxk.person_backend.service.api.PersonService
import java.util.*

@Service
class PersonServiceImpl(
    private val personRepository: PersonRepository
) : PersonService {

    override fun getAll(): List<Person> = personRepository.findAll().toList()

    override fun findById(personId: UUID): Person = personRepository.findById(personId).orElseThrow()

    override fun save(person: Person): Person = personRepository.save(person)

    override fun remove(personId: UUID): Person {
        val person = findById(personId)
        personRepository.delete(person)
        return person
    }
}