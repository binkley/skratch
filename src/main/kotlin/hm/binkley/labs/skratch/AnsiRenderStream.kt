package hm.binkley.labs.skratch

import org.fusesource.jansi.Ansi
import org.fusesource.jansi.Ansi.Attribute
import org.fusesource.jansi.Ansi.Color
import org.fusesource.jansi.Ansi.ansi
import java.io.OutputStream
import java.io.PrintStream
import java.util.Locale.ENGLISH

// Copied from 1.x tag of Jansi.  2.x dropped this functionality

private const val BEGIN_TOKEN = "@|"
private const val END_TOKEN = "|@"
private const val CODE_TEXT_SEPARATOR = " "
private const val CODE_LIST_SEPARATOR = ","
private const val BEGIN_TOKEN_LEN = 2
private const val END_TOKEN_LEN = 2

open class AnsiRenderStream(
    out: OutputStream,
    autoFlush: Boolean = false,
) : PrintStream(out, autoFlush) {
    override fun print(s: String?) {
        if (null != s && s.contains(BEGIN_TOKEN)) super.print(render(s))
        else super.print(s)
    }

    fun println(format: String, vararg args: Any?) =
        println(format.format(*args))
}

private fun render(ansi: Ansi, vararg codes: String): Ansi {
    for (code in codes) render(ansi, code)
    return ansi
}

private fun render(input: String) =
    render(input, StringBuilder()).toString()

private fun render(text: String, vararg codes: String) =
    render(ansi(), *codes).a(text).reset().toString()

private fun render(ansi: Ansi, name: String): Ansi {
    val code = Code.valueOf(name.toUpperCase(ENGLISH))

    if (code.isColor) {
        if (code.isBackground) ansi.bg(code.getColor())
        else ansi.fg(code.getColor())
    } else if (code.isAttribute) {
        ansi.a(code.getAttribute())
    }

    return ansi
}

private fun render(input: String, target: Appendable): Appendable {
    var i = 0
    var j: Int
    var k: Int
    while (true) {
        j = input.indexOf(BEGIN_TOKEN, i)
        if (-1 == j) {
            if (0 == i) {
                target.append(input)
                return target
            }
            target.append(input.substring(i, input.length))
            return target
        }
        target.append(input.substring(i, j))
        k = input.indexOf(END_TOKEN, j)
        if (-1 == k) {
            target.append(input)
            return target
        }
        j += BEGIN_TOKEN_LEN
        val spec = input.substring(j, k)
        val items =
            spec.split(CODE_TEXT_SEPARATOR.toRegex(), 2).toTypedArray()
        if (1 == items.size) {
            target.append(input)
            return target
        }
        val replacement: String = render(items[1], *items[0].split(
            CODE_LIST_SEPARATOR.toRegex()).toTypedArray())
        target.append(replacement)
        i = k + END_TOKEN_LEN
    }
}

private enum class Code constructor(
    private val n: Enum<*>,
    val isBackground: Boolean = false,
) {
    // Colors
    BLACK(Color.BLACK),
    RED(Color.RED),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    BLUE(Color.BLUE),
    MAGENTA(Color.MAGENTA),
    CYAN(Color.CYAN),
    WHITE(Color.WHITE),

    // Foreground Colors
    FG_BLACK(Color.BLACK, false),
    FG_RED(Color.RED, false),
    FG_GREEN(Color.GREEN, false),
    FG_YELLOW(Color.YELLOW, false),
    FG_BLUE(Color.BLUE, false),
    FG_MAGENTA(Color.MAGENTA, false),
    FG_CYAN(Color.CYAN, false),
    FG_WHITE(Color.WHITE, false),

    // Background Colors
    BG_BLACK(Color.BLACK, true),
    BG_RED(Color.RED, true),
    BG_GREEN(Color.GREEN, true),
    BG_YELLOW(Color.YELLOW, true),
    BG_BLUE(Color.BLUE, true),
    BG_MAGENTA(Color.MAGENTA, true),
    BG_CYAN(Color.CYAN, true),
    BG_WHITE(Color.WHITE, true),

    // Attributes
    RESET(Attribute.RESET),
    INTENSITY_BOLD(Attribute.INTENSITY_BOLD),
    INTENSITY_FAINT(Attribute.INTENSITY_FAINT),
    ITALIC(Attribute.ITALIC),
    UNDERLINE(Attribute.UNDERLINE),
    BLINK_SLOW(Attribute.BLINK_SLOW),
    BLINK_FAST(Attribute.BLINK_FAST),
    BLINK_OFF(Attribute.BLINK_OFF),
    NEGATIVE_ON(Attribute.NEGATIVE_ON),
    NEGATIVE_OFF(Attribute.NEGATIVE_OFF),
    CONCEAL_ON(Attribute.CONCEAL_ON),
    CONCEAL_OFF(Attribute.CONCEAL_OFF),
    UNDERLINE_DOUBLE(Attribute.UNDERLINE_DOUBLE),
    UNDERLINE_OFF(Attribute.UNDERLINE_OFF),

    // Aliases
    BOLD(Attribute.INTENSITY_BOLD),
    FAINT(Attribute.INTENSITY_FAINT);

    val isColor get() = n is Color

    fun getColor(): Color = n as Color

    val isAttribute get() = n is Attribute

    fun getAttribute() = n as Attribute
}
