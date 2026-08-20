/**
 * The main entry point for SlotBot.
 */
public class SlotBot {
    public static void main(String[] args) {
        String banner = "  ____  _       _   ____        _   \n"
                + " / ___|| | ___ | |_ | __ )  ___ | |_ \n"
                + " \\___ \\| |/ _ \\| __||  _ \\ / _ \\| __|\n"
                + "  ___) | | (_) | |_ | |_) | (_) | |_ \n"
                + " |____/|_|\\___/ \\__||____/ \\___/ \\__|\n";
        System.out.println(banner);
        String message = """
                ____________________________________________________________
                Hello! I'm SlotBot.
                What can I do for you?
                ____________________________________________________________
                Bye. Hope to see you again soon!
                ____________________________________________________________
                """;

        System.out.println(message);
    }
}
