package ru.mxk.userslist.model

import java.util.UUID

data class Person(
    val id: UUID,
    val name: String,
    val companyName: String,
    val photo: String,
    val liked: Boolean,
    val fired: Boolean,
    val active: Boolean,
    val details: String
)