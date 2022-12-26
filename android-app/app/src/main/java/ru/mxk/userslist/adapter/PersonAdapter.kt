package ru.mxk.userslist.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.mxk.userslist.R
import ru.mxk.userslist.databinding.ItemPersonBinding
import ru.mxk.userslist.enumeration.ActionType
import ru.mxk.userslist.model.Person

class PersonAdapter(private val actionListener: PersonActionListener) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>(), View.OnClickListener {

    var data = mutableListOf<Person>()
        set(newValue) {
            val diff = DiffUtil.calculateDiff(PersonDiffUtil(field, newValue))

            field.clear()
            field.addAll(newValue)

            diff.dispatchUpdatesTo(this)
        }

    class PersonViewHolder(val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPersonBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.moreImageViewButton.setOnClickListener(this)
        binding.likedImageView.setOnClickListener(this)
        binding.firedImageView.setOnClickListener(this)
        binding.activeImageView.setOnClickListener(this)

        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = data[position]
        val context = holder.itemView.context

        holder.itemView.tag = person
        with(holder.binding) {
            likedImageView.tag = person
            firedImageView.tag = person
            moreImageViewButton.tag = person
            activeImageView.tag = person

            val likedColor = if (person.liked) R.color.red else R.color.grey
            val firedColor = if (person.fired) R.color.green else R.color.grey
            val activeColor = if (person.active) R.color.orange else R.color.grey

            userNameTextView.text = person.name
            userCompanyTextView.text = person.companyName
            likedImageView.setColorFilter(
                ContextCompat.getColor(context, likedColor),
                PorterDuff.Mode.SRC_IN
            )
            firedImageView.setColorFilter(
                ContextCompat.getColor(context, firedColor),
                PorterDuff.Mode.SRC_IN
            )
            activeImageView.setColorFilter(
                ContextCompat.getColor(context, activeColor),
                PorterDuff.Mode.SRC_IN
            )

            Glide.with(context)
                .load(person.photo)
                .circleCrop()
                .error(R.drawable.ic_person)
                .placeholder(R.drawable.ic_person)
                .into(photoImageView)
        }
    }

    override fun onClick(view: View) {
        val person: Person = view.tag as Person

        when (view.id) {
            R.id.more_image_view_button -> showPopupMenu(view)
            R.id.liked_image_view -> actionListener.onPersonLike(person)
            R.id.fired_image_view -> actionListener.onPersonFire(person)
            R.id.active_image_view -> actionListener.onPersonActivate(person)
            else -> actionListener.onPersonGetId(person)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val person = view.tag as Person
        val position = data.indexOfFirst { it.id == person.id }

        popupMenu.menu.add(0, ActionType.UP.id, Menu.NONE, "Up").apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ActionType.DOWN.id, Menu.NONE, "Down").apply {
            isEnabled = position < data.size - 1
        }
        popupMenu.menu.add(0, ActionType.REMOVE.id, Menu.NONE, "Remove")
        popupMenu.menu.add(0, ActionType.LIKE.id, Menu.NONE, "Like")
        popupMenu.menu.add(0, ActionType.FIRE.id, Menu.NONE, "Fire")
        popupMenu.menu.add(0, ActionType.OPTIONS.id, Menu.NONE, "Show id")

        popupMenu.setOnMenuItemClickListener {
            when (val actionType = ActionType.of(it.itemId)) {
                ActionType.UP, ActionType.DOWN -> {
                    val direction = actionType.direction ?: throw IllegalArgumentException()
                    actionListener.onPersonMove(person, direction)
                }
                ActionType.REMOVE -> actionListener.onPersonRemove(person)
                ActionType.OPTIONS -> actionListener.onPersonGetId(person)
                ActionType.LIKE -> actionListener.onPersonLike(person)
                ActionType.FIRE -> actionListener.onPersonFire(person)
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }
}