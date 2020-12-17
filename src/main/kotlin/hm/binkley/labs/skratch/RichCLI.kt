package hm.binkley.labs.skratch

import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.impl.completer.NullCompleter
import org.jline.terminal.MouseEvent
import org.jline.terminal.Size
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.jline.widget.AutosuggestionWidgets
import picocli.CommandLine
import kotlin.system.exitProcess

class RichCLI<T>(
    name: String,
    val options: T, // picocli needs a union type: not (yet) a Kotlin thing
    private val terminal: Terminal = TerminalBuilder.builder()
        .name(name)
        .build(),
    completer: Completer = NullCompleter.INSTANCE,
    private val lineReader: LineReader = LineReaderBuilder.builder()
        .completer(completer)
        .terminal(terminal)
        .build(),
) : AnsiRenderStream(terminal.output()),
    Terminal by terminal,
    LineReader by lineReader {
    init {
        AnsiConsole.systemInstall()
        AutosuggestionWidgets(lineReader).enable()
    }

    val err get() = AnsiRenderStream(System.err)

    fun parse(args: Array<String>) = (CommandLine(options)).apply {
        val code = execute(*args)
        // TODO: How to tie this to @Command settings for exit codes?
        when {
            isUsageHelpRequested || isVersionHelpRequested -> exitProcess(0)
            0 != code -> exitProcess(code)
        }
    }

    /**
     * For more control over the terminal
     *
     * @todo Contrast jansi & jline, possibly picking just jline
     */
    fun ansi() = Ansi.ansi()

    // Conflict between Terminal and LineReader
    override fun close() = terminal.close()
    override fun flush() = terminal.flush()
    override fun readMouseEvent(): MouseEvent = terminal.readMouseEvent()
}

/** @todo This is a hack */
fun Terminal.isTty() = Size(0, 0) != size
