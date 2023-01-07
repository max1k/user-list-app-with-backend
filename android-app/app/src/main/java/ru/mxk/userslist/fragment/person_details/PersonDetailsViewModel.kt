package ru.mxk.userslist.fragment.person_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mxk.userslist.exception.NoSuchPersonException
import ru.mxk.userslist.model.Person
import ru.mxk.userslist.servce.PersonService
import java.util.UUID

class PersonDetailsViewModel(private val personService: PersonService) : ViewModel() {

    private val personMutableLiveData = MutableLiveData<Person>()
    val personLiveData: LiveData<Person> = personMutableLiveData

    fun loadPerson(personId: UUID) {
        if (personMutableLiveData.value != null) return

        try {
            personMutableLiveData.value = personService.findPersonById(personId)
        } catch (exception: NoSuchPersonException) {
            exception.printStackTrace()
        }

    }

    fun removePerson() {
        val personId = personLiveData.value?.id

        requireNotNull(personId) {
            "Person is not set"
        }

        personService.removePerson(personId)
    }

}