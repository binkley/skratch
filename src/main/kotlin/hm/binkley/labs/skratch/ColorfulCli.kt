package hm.binkley.labs.skratch

import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.MouseEvent
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import picocli.CommandLine
import kotlin.system.exitProcess

class ColorfulCli<T>(
    name: String,
    val options: T, // picocli needs a union type, not a Kotlin thing (yet)
    private val terminal: Terminal = TerminalBuilder.builder()
        .name(name)
        .build(),
    private val lineReader: LineReader = LineReaderBuilder.builder()
        .terminal(terminal)
        .build(),
) : AnsiRenderStream(terminal.output()),
    Terminal by terminal,
    LineReader by lineReader {
    init {
        AnsiConsole.systemInstall()
    }

    val err get() = AnsiRenderStream(System.err)

    fun parse(args: Array<String>) = with(CommandLine(options)) {
        val code = execute(*args)
        // TODO: How to tie this to @Command settings for exit codes?
        // TODO: How to automate picocli to exit for me?
        if (isUsageHelpRequested || isVersionHelpRequested)
            exitProcess(0)
        code
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
