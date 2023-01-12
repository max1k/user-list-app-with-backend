package ru.mxk.userslist.fragment.person_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mxk.userslist.dto.result.RequestResult
import ru.mxk.userslist.dto.result.ResultStatus
import ru.mxk.userslist.enumeration.Direction
import ru.mxk.userslist.model.Person
import ru.mxk.userslist.model.item.PersonItem
import ru.mxk.userslist.servce.PersonListener
import ru.mxk.userslist.servce.PersonService
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class PersonListViewModel(
    private val personService: PersonService
) : ViewModel(), CoroutineScope {

    private val personsMutableLiveData = MutableLiveData<RequestResult<out List<PersonItem>>>()
    val personsLiveData: LiveData<RequestResult<out List<PersonItem>>> = personsMutableLiveData

    private var persons: MutableList<Person> = mutableListOf()

    private val listener: PersonListener = {
        if (it.status == ResultStatus.DONE) {
            persons = it.data
        }
        launch {
            refreshPersonItems(it)
        }
    }

    private suspend fun refreshPersonItems(requestResult: RequestResult<MutableList<Person>>) {
        withContext(Dispatchers.Main) {
            personsMutableLiveData.value = requestResult.map { persons ->
                persons.map { person ->
                    PersonItem(person, processingItems.contains(person.id))
                }
            }
        }
    }

    private val processingItems: MutableSet<UUID> = mutableSetOf()

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
            markAsProcessing(person)
            personService.movePerson(person.id, direction)
            markAsProcessed(person)
        }
    }

    fun removePerson(person: Person) {
        launch {
            markAsProcessing(person)
            personService.removePerson(person.id)
            markAsProcessed(person)
        }
    }

    override fun onCleared() {
        super.onCleared()
        personService.removeListener(listener)
        job.cancel()
    }

    fun likePerson(person: Person) {
        launch {
            markAsProcessing(person)
            personService.likePerson(person.id)
            markAsProcessed(person)
        }
    }

    fun firePerson(person: Person) {
        launch {
            markAsProcessing(person)
            personService.firePerson(person.id)
            markAsProcessed(person)
        }
    }

    fun activatePerson(person: Person) {
        launch {
            markAsProcessing(person)
            personService.activatePerson(person.id)
            markAsProcessed(person)
        }
    }

    private suspend fun markAsProcessing(person: Person) {
        processingItems.add(person.id)
        refreshPersonItems(RequestResult.ofSuccess(persons))
    }

    private suspend fun markAsProcessed(person: Person) {
        processingItems.remove(person.id)
        refreshPersonItems(RequestResult.ofSuccess(persons))
    }
}