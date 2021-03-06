@file:Suppress("UNCHECKED_CAST", "UNUSED_PARAMETER")

package kr.entree.enderwand.command.argument

import kr.entree.enderwand.collection.Reader
import kr.entree.enderwand.command.ArgumentParseException
import kr.entree.enderwand.command.CommandBuilder
import kr.entree.enderwand.command.NotDoubleException
import kr.entree.enderwand.command.NotIntException
import kr.entree.enderwand.command.sender.Sender
import kr.entree.enderwand.time.DURATION_PARSER_ENGLISH
import kr.entree.enderwand.time.DURATION_PARSER_KOREAN
import kr.entree.enderwand.time.DurationParser
import java.time.Duration

/**
 * Created by JunHyung Lim on 2020-01-28
 */
val PARSER_STRING: Parser = { it.read() }
val PARSER_INT: Parser = {
    it.read().run { toIntOrNull() ?: throw NotIntException(this) }
}
val PARSER_DOUBLE: Parser = {
    it.read().run { toDoubleOrNull() ?: throw NotDoubleException(this) }
}

class DurationArgumentParser(private val parser: DurationParser) : Parser {
    override fun invoke(reader: Reader<String>) = parser.parse(reader.read())
}

val PARSER_DURATION_KOREAN: Parser = {
    DURATION_PARSER_KOREAN.parse(it.read())
}

val PARSER_STRING_LIST: Parser = {
    mutableListOf<String>().apply {
        while (it.canRead()) {
            add(it.read())
        }
    }
}

fun Iterable<Argument<*>>.parse(reader: Reader<String>): List<Any> {
    val ret = mutableListOf<Any>()
    forEachIndexed { index, argument ->
        if (!reader.canRead()) {
            if (argument.optional) {
                argument.default?.let { ret.add(it) }
                return@forEachIndexed
            }
            throw ArgumentParseException(index)
        }
        try {
            ret += argument.parse(reader)
        } catch (ex: Exception) {
            throw ArgumentParseException(index, ex)
        }
    }
    return ret
}

inline fun <S : Sender> CommandBuilder<S>.string(
    description: String = "string",
    configure: Argument<String>.() -> Unit = {}
) = Argument<String>(description, parser = PARSER_STRING).apply(configure)

inline fun <S : Sender> CommandBuilder<S>.strings(
    description: String = "strings",
    configure: Argument<List<String>>.() -> Unit = {}
) = Argument<List<String>>(description, parser = PARSER_STRING_LIST).apply(configure)

inline fun <S : Sender> CommandBuilder<S>.int(
    description: String = "int",
    configure: Argument<Int>.() -> Unit = {}
) = Argument<Int>(description, parser = PARSER_INT).apply(configure)

inline fun <S : Sender> CommandBuilder<S>.double(
    description: String = "double",
    configure: Argument<Double>.() -> Unit = {}
) = Argument<Double>(description, parser = PARSER_DOUBLE).apply(configure)

inline fun <S : Sender> CommandBuilder<S>.duration(
    description: String = "duration",
    parser: DurationParser = DURATION_PARSER_ENGLISH,
    configure: Argument<Duration>.() -> Unit = {}
) = Argument<Duration>(description, parser = DurationArgumentParser(parser))

inline fun <S : Sender> CommandBuilder<S>.durationKor(
    description: String = "시간",
    configure: Argument<Duration>.() -> Unit = {}
) = Argument<Duration>(description, parser = PARSER_DURATION_KOREAN)