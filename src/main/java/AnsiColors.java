import static org.fusesource.jansi.Ansi.ansi;

public class AnsiColors {
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_GREEN = "\u001B[32m";

    public static void ansiRedError(String message) {
        System.out.println(ansi().render(AnsiColors.ANSI_RED + message + AnsiColors.ANSI_RESET));
    }

    public static void ansiGreenMessage(String message) {
        System.out.println(ansi().render(AnsiColors.ANSI_GREEN + message + AnsiColors.ANSI_RESET));
    }
}
