package ru.mxk.userslist.dto.result

data class RequestResult<T>(val status: ResultStatus, private val value: T?) {

    val data: T get() = value ?: throw NoSuchElementException("Value is not set")

    companion object {
        fun <T> ofPending(): RequestResult<T> {
            return RequestResult(ResultStatus.PENDING, null)
        }

        fun <T> ofFail(): RequestResult<T> {
            return RequestResult(ResultStatus.FAIL, null)
        }

        fun <T> ofSuccess(data: T): RequestResult<T> {
            return RequestResult(ResultStatus.DONE, data)
        }
    }
}

enum class ResultStatus {
    PENDING, DONE, FAIL
}