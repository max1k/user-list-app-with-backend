package ru.mxk.userslist.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Person(
    val id: UUID,
    val name: String,
    @SerializedName("company")
    val companyName: String,
    @SerializedName("avatar")
    val photo: String,
    val liked: Boolean,
    val fired: Boolean,
    val active: Boolean,
    val details: String
)