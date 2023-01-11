package ru.mxk.userslist.fragment.person_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.mxk.userslist.dto.result.RequestResult
import ru.mxk.userslist.exception.NoSuchPersonException
import ru.mxk.userslist.model.Person
import ru.mxk.userslist.servce.PersonService
import java.util.*
import kotlin.coroutines.CoroutineContext

class PersonDetailsViewModel(private val personService: PersonService) : ViewModel(), CoroutineScope {

    private val personMutableLiveData = MutableLiveData<RequestResult<Person>>()
    val personLiveData: LiveData<RequestResult<Person>> = personMutableLiveData

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    fun loadPerson(personId: UUID) {
        if (personMutableLiveData.value != null) return

        launch {
            updatePersonData(RequestResult.ofPending())

            try {
                val person = personService.findPersonById(personId)
                updatePersonData(RequestResult.ofSuccess(person))
            } catch (exception: NoSuchPersonException) {
                updatePersonData(RequestResult.ofFail())
            }
        }

    }

    private suspend fun updatePersonData(requestResult: RequestResult<Person>) {
        withContext(Dispatchers.Main) {
            personMutableLiveData.value = requestResult
        }
    }

    fun removePerson() {
        val personId = personLiveData.value?.data?.id

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