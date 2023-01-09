package ru.mxk.person_backend.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import ru.mxk.person_backend.model.Person
import ru.mxk.person_backend.service.api.PersonService
import java.util.*

@RestController
@RequestMapping("/person")
class PersonController(private val personService: PersonService) {
    private val logger = LoggerFactory.getLogger(PersonController::class.java)

    @PostMapping
    fun save(@RequestBody person: Person): Person {
        logger.info("save request. id=${person.id}")
        return personService.save(person)
    }

    @GetMapping("/all")
    fun getAll(): List<Person> {
        logger.info("getAll request")
        return personService.getAll()
    }

    @GetMapping("/{personId}")
    fun getById(@PathVariable personId: UUID): Person {
        logger.info("findById request. id=$personId")
        return personService.findById(personId)
    }

    @DeleteMapping("/{personId}")
    fun delete(@PathVariable personId: UUID): Person {
        logger.info("remove request. id=$personId")
        return personService.remove(personId)
    }

}