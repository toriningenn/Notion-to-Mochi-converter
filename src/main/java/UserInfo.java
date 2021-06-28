import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import notion.api.v1.model.databases.Database;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

public class UserInfo {

    private static ConsolePrompt prompt = new ConsolePrompt();

    public static String askToken() {
        try {
            System.out.println(ansi().render("1.Head to https://www.notion.so/my-integrations" +
                    "\n2.Add new internal integration and copy your integration token." +
                    "\n3.Go to the Mochi database you want to convert. " +
                    "\nYou can use any two columns which must be of Text or Title property type." +
                    "\n4.Hit the Share button and add your integration."));
            PromptBuilder promptBuilder = prompt.getPromptBuilder();
            promptBuilder.createInputPrompt()
                    .name("token")
                    .message("Enter your integration token:")
                    .addPrompt();
            HashMap<String, InputResult> result = (HashMap<String, InputResult>) prompt.prompt(promptBuilder.build());
            return result.get("token").getInput();
        } catch (IOException e) {
            System.out.println(ansi().render(AnsiColors.ANSI_RED + "Error" + AnsiColors.ANSI_RESET));
            return askToken();
        }
    }

    public static Database askDatabaseIDReturnDB(List<Database> databasesList) {
        Database database = null;
        try {
            if (databasesList.size() == 1) {
                database = databasesList.get(0);
            } else {
                System.out.println(ansi().render("You have multiply databases with this interaction!" +
                        "\nOpen the database as a full page in Notion. Use the Share menu to Copy link. " +
                        "\nNow paste the link in your text editor so you can take a closer look. The URL uses the following format:" +
                        "\nhttps://www.notion.so/{workspace_name}/{database_id}?v={view_id}"));
                PromptBuilder promptBuilder = prompt.getPromptBuilder();
                promptBuilder.createInputPrompt()
                        .name("db_id")
                        .message("Database ID: ")
                        .addPrompt();
                HashMap<String, InputResult> result = (HashMap<String, InputResult>) prompt.prompt(promptBuilder.build());
                String id = result.get("db_id").getInput();
                database = databasesList.stream().filter(x -> x.getId().replaceAll("-", "").equals(id)).findFirst().orElseThrow();
            }
        } catch (Exception e) {
            System.out.println(ansi().render(AnsiColors.ANSI_RED + "Database not found" + AnsiColors.ANSI_RESET));
            askDatabaseIDReturnDB(databasesList);
        }
        return database;
    }

    public static String askFirstColumnName() {
        try {
            PromptBuilder promptBuilder = prompt.getPromptBuilder();
            promptBuilder.createInputPrompt()
                    .name("first_column")
                    .message("First column title (front side of the card): ")
                    .addPrompt();
            HashMap<String, InputResult> result = (HashMap<String, InputResult>) prompt.prompt(promptBuilder.build());

            return result.get("first_column").getInput();
        } catch (IOException e) {
            System.out.println(ansi().render(AnsiColors.ANSI_RED + "Error" + AnsiColors.ANSI_RESET));
            return askFirstColumnName();
        }
    }

    public static String askSecondColumnName() {
        try {
            PromptBuilder promptBuilder = prompt.getPromptBuilder();
            promptBuilder.createInputPrompt()
                    .name("second_column")
                    .message("Second column title (back side of the card): ")
                    .addPrompt();
            HashMap<String, InputResult> result = (HashMap<String, InputResult>) prompt.prompt(promptBuilder.build());

            return result.get("second_column").getInput();
        } catch (IOException e) {
            System.out.println(ansi().render(AnsiColors.ANSI_RED + "Error" + AnsiColors.ANSI_RESET));
            return askSecondColumnName();
        }
    }

    public static String askForDeckID() {
        try {
            System.out.println(ansi().render("To get the deck-id of the deck you want to add the card to, " +
                    "\nright click on the deck in the sidebar in the app and choose \"Copy ID\"." +
                    "\nThat will copy some markdown to your clipboard that will include the UUID for that deck." +
                    "\nNote that the @deck/ is not part of the ID."));
            PromptBuilder promptBuilder = prompt.getPromptBuilder();
            promptBuilder.createInputPrompt()
                    .name("deck_id")
                    .message("Insert your deck ID:")
                    .addPrompt();
            HashMap<String, InputResult> result = (HashMap<String, InputResult>) prompt.prompt(promptBuilder.build());

            return result.get("deck_id").getInput();
        } catch (Exception e) {
            System.out.println(ansi().render(AnsiColors.ANSI_RED + "Error" + AnsiColors.ANSI_RESET));
            return askForDeckID();
        }
    }

    public static String askForMochiEmail() {
        try {
            PromptBuilder promptBuilder = prompt.getPromptBuilder();
            promptBuilder.createInputPrompt()
                    .name("email")
                    .message("Mochi account email:")
                    .addPrompt();
            HashMap<String, InputResult> result = (HashMap<String, InputResult>) prompt.prompt(promptBuilder.build());
            return result.get("email").getInput();
        } catch (IOException e) {
            System.out.println(ansi().render(AnsiColors.ANSI_RED + "Error" + AnsiColors.ANSI_RESET));
            return askForMochiEmail();
        }
    }

    public static String askForMochiPassword() {
        try {
            PromptBuilder promptBuilder = prompt.getPromptBuilder();
            promptBuilder.createInputPrompt()
                    .name("password")
                    .message("Mochi password:")
                    .mask('*')
                    .addPrompt();
            HashMap<String, InputResult> result = (HashMap<String, InputResult>) prompt.prompt(promptBuilder.build());

            return result.get("password").getInput();
        } catch (IOException e) {
            System.out.println(ansi().render(AnsiColors.ANSI_RED + "Error" + AnsiColors.ANSI_RESET));
            return askForMochiPassword();
        }
    }
}
