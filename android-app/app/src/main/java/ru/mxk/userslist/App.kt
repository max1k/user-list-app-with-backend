package ru.mxk.userslist

import android.app.Application
import ru.mxk.userslist.converter.PersonConverter
import ru.mxk.userslist.servce.PersonService

class App : Application() {
    val personService = PersonService(PersonConverter())
}