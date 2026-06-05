package persistence

import tools.jackson.databind.SerializationFeature

interface Serializer {
    fun write(obj: Any?)
    fun <T> read(classType: Class<T>): T
}