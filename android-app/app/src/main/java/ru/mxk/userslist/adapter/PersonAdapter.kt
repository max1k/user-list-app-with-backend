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
import ru.mxk.userslist.model.item.PersonItem

class PersonAdapter(private val actionListener: PersonActionListener) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>(), View.OnClickListener {

    private val items = mutableListOf<PersonItem>()

    fun setItems(newValue: List<PersonItem>) {
        val diff = DiffUtil.calculateDiff(PersonItemDiffUtil(items, newValue))

        items.clear()
        items.addAll(newValue)

        diff.dispatchUpdatesTo(this)
    }

    class PersonViewHolder(val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = items.size

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
        val personItem = items[position]
        val context = holder.itemView.context

        holder.itemView.tag = personItem
        with(holder.binding) {
            setOf(likedImageView, firedImageView, moreImageViewButton, activeImageView)
                .forEach { it.tag = personItem }

            val likedColor = if (personItem.person.liked) R.color.red else R.color.grey
            val firedColor = if (personItem.person.fired) R.color.green else R.color.grey
            val activeColor = if (personItem.person.active) R.color.orange else R.color.grey

            userNameTextView.text = personItem.person.name
            userCompanyTextView.text = personItem.person.companyName

            if (personItem.processing) {
                controlsGroup.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            } else {
                controlsGroup.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }

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
                .load(personItem.person.photo)
                .circleCrop()
                .error(R.drawable.ic_person)
                .placeholder(R.drawable.ic_person)
                .into(photoImageView)
        }
    }

    override fun onClick(view: View) {
        val personItem: PersonItem = view.tag as PersonItem

        when (view.id) {
            R.id.more_image_view_button -> showPopupMenu(view)
            R.id.liked_image_view -> actionListener.onPersonLike(personItem.person)
            R.id.fired_image_view -> actionListener.onPersonFire(personItem.person)
            R.id.active_image_view -> actionListener.onPersonActivate(personItem.person)
            else -> actionListener.onShowDetails(personItem.person)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val personItem = view.tag as PersonItem
        val position = items.indexOfFirst { it.person.id == personItem.person.id }

        popupMenu.menu.add(0, ActionType.UP.id, Menu.NONE, "Up").apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ActionType.DOWN.id, Menu.NONE, "Down").apply {
            isEnabled = position < items.lastIndex
        }
        popupMenu.menu.add(0, ActionType.REMOVE.id, Menu.NONE, "Remove")
        popupMenu.menu.add(0, ActionType.LIKE.id, Menu.NONE, "Like")
        popupMenu.menu.add(0, ActionType.FIRE.id, Menu.NONE, "Fire")
        popupMenu.menu.add(0, ActionType.OPTIONS.id, Menu.NONE, "Show id")

        popupMenu.setOnMenuItemClickListener {
            when (val actionType = ActionType.of(it.itemId)) {
                ActionType.UP, ActionType.DOWN -> {
                    val direction = actionType.direction ?: throw IllegalArgumentException()
                    actionListener.onPersonMove(personItem.person, direction)
                }
                ActionType.REMOVE -> actionListener.onPersonRemove(personItem.person)
                ActionType.OPTIONS -> actionListener.onShowDetails(personItem.person)
                ActionType.LIKE -> actionListener.onPersonLike(personItem.person)
                ActionType.FIRE -> actionListener.onPersonFire(personItem.person)
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }
}