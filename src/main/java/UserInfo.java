import notion.api.v1.model.databases.Database;
import notion.api.v1.model.databases.Databases;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserInfo {

    public static String askToken() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Interaction token: ");
        return sc.nextLine();
    }

    //always returns
    public static Database askDatabaseIDIfNeededReturnDB(List<Database> databasesList) {
        Scanner sc = new Scanner(System.in);
        Database database = null;
        if (databasesList.size() == 1) {
            database = databasesList.get(0);
        } else {
            System.out.print("Database ID (you have multiply databases with this interaction): ");
            String id = sc.nextLine();
            try {
                database = databasesList.stream().filter(x -> x.getId().replaceAll("-", "").equals(id)).findFirst().orElseThrow();
            } catch (NoSuchElementException e) {
                System.out.println("Database not found");
            }
        }
        return database;
    }

    public static String askFirstColumnName() {
        Scanner sc = new Scanner(System.in);
        System.out.print("First column title(front of the card):");
        return sc.nextLine();
    }

    public static String askSecondColumnName() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Second column title(back of the card):");
        return sc.nextLine();
    }

    public static String askIfCreateDeck() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Create a new deck?");
        return sc.nextLine();
    }

    public static String askForDeckID() {
        Scanner sc = new Scanner(System.in);
        System.out.print("To get the deck-id of the deck you want to add the card to, right click on the deck in " +
                "the sidebar in the app and choose \"Copy ID\". " +
                "\nThat will copy some markdown to your clipboard that will include the UUID for that deck. \n" +
                "It will look something like this: 17d379f1-650f-45d3-bac1-591a1e302786.\n" +
                "Note that the @deck/ is not part of the ID." +
                "Insert deck ID:");
        return sc.nextLine();
    }

    public static String askForMochiEmail() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Mochi account email:");
        return sc.nextLine();
    }
    public static String askForMochiPassword() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Mochi account password:");
        return sc.nextLine();
    }
}
