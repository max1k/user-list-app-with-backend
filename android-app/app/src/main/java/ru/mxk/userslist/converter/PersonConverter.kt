package ru.mxk.userslist.converter

import com.github.javafaker.Faker
import ru.mxk.userslist.model.Person

class PersonConverter {
    private val faker = Faker()

    fun createPerson(id: Int): Person {
        val photo = listOf(
            "https://randomuser.me/api/portraits/women/$id.jpg",
            "https://randomuser.me/api/portraits/men/$id.jpg"
        ).random()

        return Person(
            id = id.toLong(),
            name = faker.name().fullName(),
            companyName = faker.company().name(),
            photo = photo,
            liked = false,
            fired = false,
            active = false
        )
    }
}