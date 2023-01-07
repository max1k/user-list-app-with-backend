package ru.mxk.person_backend.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Person(
    @Id
    var id: String?,
    val name: String,
    val company: String,
    val avatar: String,
    val liked: Boolean,
    val fired: Boolean,
    val active: Boolean,
    val details: String
)