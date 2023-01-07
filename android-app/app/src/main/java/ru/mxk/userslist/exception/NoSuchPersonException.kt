package ru.mxk.userslist.exception

import java.util.UUID

class NoSuchPersonException(personId: UUID) : NoSuchElementException("Person with id:'$personId' is not found")
