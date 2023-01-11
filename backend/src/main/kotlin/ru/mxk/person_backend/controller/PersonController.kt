package ru.mxk.person_backend.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import ru.mxk.person_backend.model.Person
import ru.mxk.person_backend.service.api.PersonService
import java.time.Duration
import java.util.*

@RestController
@RequestMapping("/person")
class PersonController(
    private val personService: PersonService,
    @Value("#{T(java.time.Duration).parse('\${app.response.delay}')}") private val responseDelay: Duration
) {
    private val logger = LoggerFactory.getLogger(PersonController::class.java)

    @PostMapping
    fun save(@RequestBody person: Person): Person {
        preprocess("save request. id=${person.id}")
        return personService.save(person)
    }

    @GetMapping("/all")
    fun getAll(): List<Person> {
        preprocess("getAll request")
        return personService.getAll()
    }

    @GetMapping("/{personId}")
    fun getById(@PathVariable personId: UUID): Person {
        preprocess("findById request. id=$personId")
        return personService.findById(personId)
    }

    @DeleteMapping("/{personId}")
    fun delete(@PathVariable personId: UUID): Person {
        preprocess("remove request. id=$personId")
        return personService.remove(personId)
    }

    private fun preprocess(description: String) {
        val logString = if (responseDelay.isZero) description else "$description. Delaying $responseDelay"
        logger.info(logString)

        if (!responseDelay.isZero) {
            Thread.sleep(responseDelay.toMillis())
        }
    }

}