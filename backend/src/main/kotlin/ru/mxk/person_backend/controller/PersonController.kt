package ru.mxk.person_backend.controller

import org.springframework.web.bind.annotation.*
import ru.mxk.person_backend.model.Person
import ru.mxk.person_backend.service.api.PersonService
import java.util.*

@RestController
@RequestMapping("/person")
class PersonController(private val personService: PersonService) {

    @PostMapping
    fun addPerson(@RequestBody person: Person): Person = personService.add(person)

    @GetMapping("/all")
    fun getAll(): List<Person> = personService.getAll()

    @GetMapping("/{personId}")
    fun getById(@PathVariable personId: UUID): Person {
        return personService.findById(personId)
    }

    @DeleteMapping("/{personId}")
    fun delete(@PathVariable personId: UUID): Person = personService.remove(personId)

}