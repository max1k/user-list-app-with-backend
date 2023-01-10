package ru.mxk.userslist.fragment.person_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.mxk.userslist.exception.NoSuchPersonException
import ru.mxk.userslist.model.Person
import ru.mxk.userslist.servce.PersonService
import java.util.*
import kotlin.coroutines.CoroutineContext

class PersonDetailsViewModel(private val personService: PersonService) : ViewModel(), CoroutineScope {

    private val personMutableLiveData = MutableLiveData<Person>()
    val personLiveData: LiveData<Person> = personMutableLiveData

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    fun loadPerson(personId: UUID) {
        if (personMutableLiveData.value != null) return

        launch {
            try {
                val person = personService.findPersonById(personId)
                withContext(Dispatchers.Main) {
                    personMutableLiveData.value = person
                }
            } catch (exception: NoSuchPersonException) {
                exception.printStackTrace()
            }
        }

    }

    fun removePerson() {
        val personId = personLiveData.value?.id

        requireNotNull(personId) {
            "Person is not set"
        }
        launch {
            personService.removePerson(personId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}