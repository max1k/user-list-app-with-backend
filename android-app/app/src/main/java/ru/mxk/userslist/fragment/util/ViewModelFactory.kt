package ru.mxk.userslist.fragment.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mxk.userslist.App
import ru.mxk.userslist.Navigator
import ru.mxk.userslist.fragment.person_details.PersonDetailsViewModel
import ru.mxk.userslist.fragment.person_list.PersonListViewModel

class ViewModelFactory(private val app: App) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            PersonListViewModel::class.java -> PersonListViewModel(app.personService) as T
            PersonDetailsViewModel::class.java -> PersonDetailsViewModel(app.personService) as T
            else -> throw IllegalArgumentException("Unexpected class ${modelClass.simpleName}")
        }
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator