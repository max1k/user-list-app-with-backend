package ru.mxk.userslist

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mxk.userslist.repository.PersonRepository
import ru.mxk.userslist.servce.PersonService


class App : Application() {
    private val personRepository: PersonRepository = createPersonRepository()
    val personService = PersonService(personRepository)

    private fun createPersonRepository(): PersonRepository {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.224.9.136:8088/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        return retrofit.create(PersonRepository::class.java)
    }
}