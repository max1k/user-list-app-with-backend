package ru.mxk.userslist.fragment.person_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.mxk.userslist.dto.result.RequestResult
import ru.mxk.userslist.enumeration.Direction
import ru.mxk.userslist.model.Person
import ru.mxk.userslist.servce.PersonListener
import ru.mxk.userslist.servce.PersonService
import kotlin.coroutines.CoroutineContext

class PersonListViewModel(
    private val personService: PersonService
) : ViewModel(), CoroutineScope {

    private val personsMutableLiveData = MutableLiveData<RequestResult<out List<Person>>>()
    val personsLiveData: LiveData<RequestResult<out List<Person>>> = personsMutableLiveData

    private val listener: PersonListener = {
        personsMutableLiveData.value = it
    }

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    init {
        personService.addListener(listener)
        loadPersons()
    }

    fun loadPersons() {
        launch {
            personService.loadAll()
        }
    }

    fun movePerson(person: Person, direction: Direction) {
        launch {
            personService.movePerson(person.id, direction)
        }
    }

    fun removePerson(person: Person) {
        launch {
            personService.removePerson(person.id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        personService.removeListener(listener)
        job.cancel()
    }

    fun likePerson(person: Person) {
        launch {
            personService.likePerson(person.id)
        }
    }

    fun firePerson(person: Person) {
        launch {
            personService.firePerson(person.id)
        }
    }

    fun activatePerson(person: Person) {
        launch {
            personService.activatePerson(person.id)
        }
    }
}