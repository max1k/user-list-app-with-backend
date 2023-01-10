package ru.mxk.userslist.repository

import retrofit2.Response
import retrofit2.http.*
import ru.mxk.userslist.model.Person
import java.util.*

interface PersonRepository {

    @GET("person/all")
    suspend fun findAll(): Response<List<Person>>

    @GET("person/{personId}")
    suspend fun findById(@Path("personId") personId: UUID): Response<Person?>

    @DELETE("person/{personId}")
    suspend fun delete(@Path("personId") personId: UUID): Response<Person>

    @POST("person")
    suspend fun save(@Body person: Person): Response<Person>
}