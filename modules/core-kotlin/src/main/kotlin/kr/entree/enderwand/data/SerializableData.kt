@file:Suppress("EXPERIMENTAL_API_USAGE")

package kr.entree.enderwand.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File
import java.io.Reader
import java.io.Writer
import java.util.logging.Logger

val JsonConfiguration.Companion.IO get() = Stable.copy(useArrayPolymorphism = true)

// TODO: Redesign
inline fun <T> serializableDataOf(
    serializer: KSerializer<T>,
    file: File,
    logger: Logger,
    noinline patcher: (T) -> Unit,
    noinline provider: () -> T,
    format: StringFormat = Json(JsonConfiguration.IO),
    configure: StandardData.() -> Unit = {}
) = dataOf(file, SerializableData(serializer, patcher, provider, format), logger).apply(configure)

class SerializableData<T>(
    var serializer: KSerializer<T>,
    var patcher: (T) -> Unit,
    var provider: () -> T,
    val format: StringFormat
) : DynamicData {
    override fun load(reader: Reader) {
        JsonConfiguration.Stable
        val text = reader.readText()
        if (text.isNotBlank()) {
            patcher(format.parse(serializer, text))
        }
    }

    override fun save(writer: Writer) {
        writer.write(format.stringify(serializer, provider()))
    }
}