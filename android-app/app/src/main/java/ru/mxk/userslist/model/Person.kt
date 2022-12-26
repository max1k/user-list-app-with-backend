package ru.mxk.userslist.model

data class Person(
    val id: Long,
    val name: String,
    val companyName: String,
    val photo: String,
    val liked: Boolean,
    val fired: Boolean,
    val active: Boolean
)