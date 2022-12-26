package ru.mxk.userslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mxk.userslist.adapter.PersonActionListener
import ru.mxk.userslist.adapter.PersonAdapter
import ru.mxk.userslist.databinding.ActivityMainBinding
import ru.mxk.userslist.enumeration.Direction
import ru.mxk.userslist.model.Person
import ru.mxk.userslist.servce.PersonService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter: PersonAdapter = PersonAdapter(object : PersonActionListener {
        override fun onPersonGetId(person: Person) =
            Toast.makeText(this@MainActivity, "Person ID: ${person.id}", Toast.LENGTH_SHORT).show()

        override fun onPersonLike(person: Person) = personService.likePerson(person.id)

        override fun onPersonRemove(person: Person) = personService.removePerson(person.id)

        override fun onPersonMove(person: Person, direction: Direction) =
            personService.movePerson(person.id, direction)

        override fun onPersonFire(person: Person) = personService.firePerson(person.id)

        override fun onPersonActivate(person: Person) = personService.activatePerson(person.id)
    })

    private val listener: (persons: MutableList<Person>) -> Unit = { adapter.data = it }

    private val personService: PersonService
        get() = (applicationContext as App).personService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = LinearLayoutManager(this) // LayoutManager


        personService.addListener(listener)
        binding.recyclerView.layoutManager = manager // Назначение LayoutManager для RecyclerView
        binding.recyclerView.adapter = adapter // Назначение адаптера для RecyclerView
    }

    override fun onDestroy() {
        super.onDestroy()
        personService.removeListener(listener)
    }
}