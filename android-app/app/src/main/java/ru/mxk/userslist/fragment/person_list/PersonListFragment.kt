package ru.mxk.userslist.fragment.person_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mxk.userslist.adapter.PersonActionListener
import ru.mxk.userslist.adapter.PersonAdapter
import ru.mxk.userslist.databinding.FragmentPersonListBinding
import ru.mxk.userslist.dto.result.ResultStatus
import ru.mxk.userslist.enumeration.Direction
import ru.mxk.userslist.fragment.util.factory
import ru.mxk.userslist.fragment.util.navigator
import ru.mxk.userslist.model.Person

class PersonListFragment : Fragment() {

    private lateinit var binding: FragmentPersonListBinding
    private lateinit var adapter: PersonAdapter

    private val viewModel: PersonListViewModel by viewModels { this.factory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonListBinding.inflate(inflater, container, false)
        adapter = PersonAdapter(object : PersonActionListener {
            override fun onShowDetails(person: Person) {
                this@PersonListFragment.navigator().showDetails(person)
            }

            override fun onPersonLike(person: Person) {
                viewModel.likePerson(person)
            }

            override fun onPersonRemove(person: Person) {
                viewModel.removePerson(person)
            }

            override fun onPersonMove(person: Person, direction: Direction) {
                viewModel.movePerson(person, direction)
            }

            override fun onPersonFire(person: Person) {
                viewModel.firePerson(person)
            }

            override fun onPersonActivate(person: Person) {
                viewModel.activatePerson(person)
            }
        })

        viewModel.personsLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                recyclerView.visibility = View.GONE
                progressBar.visibility = View.GONE
                tryAgainContainer.visibility = View.GONE

                when (it.status) {
                    ResultStatus.DONE -> {
                        recyclerView.visibility = View.VISIBLE
                        adapter.setItems(it.data)
                    }
                    ResultStatus.PENDING -> progressBar.visibility = View.VISIBLE
                    ResultStatus.FAIL -> tryAgainContainer.visibility = View.VISIBLE
                }
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        binding.tryAgainButton.setOnClickListener{
            viewModel.loadPersons()
        }

        return binding.root
    }

}