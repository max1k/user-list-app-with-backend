package ru.mxk.userslist.enumeration

enum class ActionType(val id: Int, val direction: Direction?) {
    UP(1, Direction.UP),
    DOWN(2, Direction.DOWN),
    LIKE(3, null),
    OPTIONS(4, null),
    REMOVE(5, null),
    FIRE(6, null);

    companion object {
        fun of(id: Int): ActionType = values().first{ it.id == id }
    }
}