package ru.mxk.person_backend.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.mxk.person_backend.model.Person
import ru.mxk.person_backend.repository.PersonRepository
import ru.mxk.person_backend.service.api.PersonService
import java.time.Duration
import java.util.*

@Service
class PersonServiceImpl(
    private val personRepository: PersonRepository,
    @Value("#{T(java.time.Duration).parse('\${app.response.delay}')}") private val responseDelay: Duration
) : PersonService {

    override fun getAll(): List<Person> {
        Thread.sleep(responseDelay.toMillis())
        return personRepository.findAll().toList()
    }

    override fun findById(personId: UUID): Person {
        Thread.sleep(responseDelay.toMillis())
        return personRepository.findById(personId).orElseThrow()
    }

    override fun save(person: Person): Person {
        Thread.sleep(responseDelay.toMillis())
        return personRepository.save(person)
    }

    override fun remove(personId: UUID): Person {
        Thread.sleep(responseDelay.toMillis())
        val person = findById(personId)
        personRepository.delete(person)
        return person
    }
}