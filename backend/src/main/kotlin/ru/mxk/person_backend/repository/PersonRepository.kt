package ru.mxk.person_backend.repository

import org.springframework.data.repository.CrudRepository
import ru.mxk.person_backend.model.Person
import java.util.*

interface PersonRepository : CrudRepository<Person, UUID>