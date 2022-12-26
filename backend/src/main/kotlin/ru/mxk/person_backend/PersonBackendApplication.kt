package ru.mxk.person_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersonBackendApplication

fun main(args: Array<String>) {
    runApplication<PersonBackendApplication>(*args)
}
