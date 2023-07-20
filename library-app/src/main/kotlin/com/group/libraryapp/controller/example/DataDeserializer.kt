package com.group.libraryapp.controller.example

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.context.annotation.Bean

object DataDeserializer : SimpleModule() {
    init {
        addDeserializer(TestId::class.java, Deserializer)
        addSerializer(TestId::class.java, Serializer)
    }

    object Deserializer : StdDeserializer<TestId>(TestId::class.java) {
        val objectMapper = ObjectMapper()
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): TestId {
            val codec = p.codec
            val node = codec.readTree<JsonNode>(p)

            val value = node["value"]
            val version = node["version"]
            return TestId(value.toString(), version.longValue())
        }
    }

    object Serializer : StdSerializer<TestId>(TestId::class.java) {
        override fun serialize(value: TestId?, gen: JsonGenerator, provider: SerializerProvider?) {
            gen.writeString(value?.toString())
        }
    }
}