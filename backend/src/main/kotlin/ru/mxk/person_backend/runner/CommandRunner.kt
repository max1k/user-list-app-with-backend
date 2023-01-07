package ru.mxk.person_backend.runner

import com.github.javafaker.Faker
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ru.mxk.person_backend.model.Person
import ru.mxk.person_backend.service.api.PersonService

@Component
class CommandRunner(private val personService: PersonService) : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(CommandRunner::class.java)
    private val faker = Faker()

    override fun run(vararg args: String?) {
        if (personService.getAll().isNotEmpty()) {
            logger.info("Database is filled")
            return
        }

        for (i in 1..100) {
            val person = Person(
                id = null,
                name = faker.name().name(),
                company = faker.company().name(),
                avatar = getAvatarUrl(i),
                liked = false,
                fired = false,
                active = true,
                details = faker.lorem().paragraphs(3).joinToString("\n")
            )

            personService.add(person)
        }
    }

    private fun getAvatarUrl(id: Int): String {
        return listOf(
            "https://randomuser.me/api/portraits/women/$id.jpg",
            "https://randomuser.me/api/portraits/men/$id.jpg"
        ).random()
    }
}