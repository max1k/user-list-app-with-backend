package ru.mxk.userslist.fragment.person_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mxk.userslist.enumeration.Direction
import ru.mxk.userslist.model.Person
import ru.mxk.userslist.servce.PersonListener
import ru.mxk.userslist.servce.PersonService

class PersonListViewModel(
    private val personService: PersonService
) : ViewModel() {

    private val personsMutableLiveData = MutableLiveData<List<Person>>()
    val personsLiveData: LiveData<List<Person>> = personsMutableLiveData

    private val listener: PersonListener = {
        personsMutableLiveData.value = it
    }

    init {
        loadPersons()
    }

    private fun loadPersons() {
        personService.addListener(listener)
    }

    fun movePerson(person: Person, direction: Direction) {
        personService.movePerson(person.id, direction)
    }

    fun removePerson(person: Person) {
        return personService.removePerson(person.id)
    }

    override fun onCleared() {
        super.onCleared()
        personService.removeListener(listener)
    }

    fun likePerson(person: Person) {
        personService.likePerson(person.id)
    }

    fun firePerson(person: Person) {
        personService.firePerson(person.id)
    }

    fun activatePerson(person: Person) {
        personService.activatePerson(person.id)
    }
}