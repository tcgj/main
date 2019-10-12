package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.HashMap;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class CommandLineParser {
    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Contains a map of command names to parsers.
     */
    private final HashMap<String, CommandParser> commandParsers;

    /**
     * Creates a command line parser. Commands are found by searching for
     * all classes implementing the interface {@link CommandParser}.
     */
    public CommandLineParser() {
        commandParsers = loadParsers();
    }

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        CommandParser parser = commandParsers.get(commandWord.toLowerCase());
        if (parser == null) {
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }

        return parser.parse(arguments);
        /*
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }*/
    }

    /**
     * Loads all command parsers reachable from the system class loader.
     *
     * @return A map of command names to parsers
     */
    private static HashMap<String, CommandParser> loadParsers() {
        HashMap<String, CommandParser> ret = new HashMap<>();
        for (CommandParser parser : ServiceLoader.load(CommandParser.class, null)) {
            String name = parser.name().toLowerCase();
            if (ret.containsKey(name)) {
                throw new IllegalArgumentException("Duplicate command name.");
            }
            ret.put(name, parser);
        }
        return ret;
    }
}
