package ru.mxk.userslist.fragment.person_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import ru.mxk.userslist.R
import ru.mxk.userslist.databinding.FragmentPersonDetailsBinding
import ru.mxk.userslist.dto.result.ResultStatus
import ru.mxk.userslist.fragment.util.factory
import ru.mxk.userslist.fragment.util.navigator
import ru.mxk.userslist.model.Person
import java.util.UUID

class PersonDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPersonDetailsBinding
    private val viewModel: PersonDetailsViewModel by viewModels { this.factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val personId = UUID.fromString(requireArguments().getString(ARG_PERSON_ID))
        viewModel.loadPerson(personId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonDetailsBinding.inflate(layoutInflater, container, false)

        viewModel.personLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                progressBar.visibility = View.GONE
                scrollView.visibility = View.GONE
                errorTextView.visibility = View.GONE

                when(it.status) {
                    ResultStatus.PENDING -> progressBar.visibility = View.VISIBLE
                    ResultStatus.DONE -> {
                        scrollView.visibility = View.VISIBLE
                        showPersonDetails(it.data)
                    }
                    ResultStatus.FAIL -> errorTextView.visibility = View.VISIBLE
                }
            }
        }

        binding.deleteButton.setOnClickListener {
            viewModel.removePerson()
            this.navigator().toast(R.string.user_was_deleted)
            this.navigator().goBack()
        }

        return binding.root
    }

    private fun showPersonDetails(person: Person) {
        binding.personNameTextView.text = person.name
        binding.personDetailsTextView.text = person.details

        Glide.with(this)
            .load(person.photo)
            .circleCrop()
            .error(R.drawable.ic_person)
            .placeholder(R.drawable.ic_person)
            .into(binding.photoImageView)
    }

    companion object {
        private const val ARG_PERSON_ID = "ARG_PERSON_ID"

        fun newInstance(personId: UUID): PersonDetailsFragment {
            val fragment = PersonDetailsFragment()
            fragment.arguments = bundleOf(ARG_PERSON_ID to personId.toString())

            return fragment
        }
    }
}