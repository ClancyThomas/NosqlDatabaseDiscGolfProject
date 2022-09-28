import oracle.nosql.driver.NoSQLHandle;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CloudConnection cloudConnection = new CloudConnection();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Disc Golf Stat Pal Program!");
        System.out.print("Enter the name of the table in the database to work with: ");
        String tableName = scanner.nextLine();

        try (NoSQLHandle handle = cloudConnection.getHandle()) {
            DataHandler dataHandler = new DataHandler(handle, tableName);
            CloudActions databaseWorker = new CloudActions(handle, tableName);
            Boolean printMenu = true;
            while(printMenu) {
                System.out.print("1. Upload a .csv file to the database \n" +
                        "2. Find all rounds for a player by name \n" +
                        "3. Find the average scores on each course for a player by name  \n" +
                        "4. Find the best score on each course for a player by name\n" +
                        "5. Get the weekly rankings of all players\n"+
                        "6. Exit the program\n" +
                        "Enter a number (1-6): ");
                int option = Integer.parseInt(scanner.nextLine());
                switch(option) {
                    case 1:
                        System.out.print("Enter the file path to the .csv file: ");
                        String filePath = scanner.nextLine();
                        System.out.print("Enter the letter to use in the primary key: ");
                        String keyLetter = scanner.nextLine();
                        System.out.print("Enter the course name: ");
                        String courseName = scanner.nextLine();
                        System.out.print("Enter the week (1-52): ");
                        String week = scanner.nextLine();
                        System.out.print("Enter the year: ");
                        String year = scanner.nextLine();
                        System.out.print("Enter the format (singles/doubles): ");
                        String format = scanner.nextLine();

                        dataHandler.uploadCsvFileToDatabase(filePath, keyLetter, courseName, week, year, format);
                        break;
                    case 2:
                        System.out.print("Enter the first name of the player: ");
                        String firstNameTwo = scanner.nextLine();
                        System.out.print("Enter the last name of the player: ");
                        String lastNameTwo = scanner.nextLine();
                        String queryTwo = "SELECT concat(firstName, \" \",lastName), scoreRelativeToPar, scoreTotal, " +
                                 "course, format FROM " + tableName + " WHERE firstName=\"" + firstNameTwo + "\" AND lastName=\""+lastNameTwo+"\"";
                        String queryDescriptionTwo = "Name, Score Relative to Par, Total Score, Course, Format";
                        System.out.print("Print the results to the console (1) or to a .csv file (2): ");
                        int printRequestTwo = Integer.parseInt(scanner.nextLine());
                        if (printRequestTwo == 1) {
                            dataHandler.writeQueryToConsole(databaseWorker.query(queryTwo), queryDescriptionTwo);
                        } else {
                            dataHandler.writeQueryToCsvFile(databaseWorker.query(queryTwo), queryDescriptionTwo);
                        }
                        break;
                    case 3:
                        System.out.print("Enter the first name of the player: ");
                        String firstNameThree = scanner.nextLine();
                        System.out.print("Enter the last name of the player: ");
                        String lastNameThree = scanner.nextLine();
                        String queryThree = "SELECT firstName, lastName, avg(scoreRelativeToPar)," +
                                "course FROM " + tableName + " WHERE firstName=\"" + firstNameThree +
                                "\" AND lastName=\""+lastNameThree+"\" GROUP BY firstName, lastName, course";
                        String queryDescriptionThree = "First Name, Last Name, Course, Average Score Relative to Par";
                        System.out.print("Print the results to the console (1) or to a .csv file (2): ");
                        int printRequestThree = Integer.parseInt(scanner.nextLine());
                        if (printRequestThree == 1) {
                            dataHandler.writeQueryToConsole(databaseWorker.query(queryThree), queryDescriptionThree);
                        } else {
                            dataHandler.writeQueryToCsvFile(databaseWorker.query(queryThree), queryDescriptionThree);
                        }
                        break;
                    case 4:
                        System.out.print("Enter the first name of the player: ");
                        String firstNameFour = scanner.nextLine();
                        System.out.print("Enter the last name of the player: ");
                        String lastNameFour = scanner.nextLine();
                        String queryFour = "SELECT firstName, lastName, min(scoreRelativeToPar)," +
                                "course FROM " + tableName + " WHERE firstName=\"" + firstNameFour +
                                "\" AND lastName=\""+lastNameFour+"\" GROUP BY firstName, lastName, course";
                        String queryDescriptionFour = "First Name, Last Name, Course, Best Score Relative to Par";
                        System.out.print("Print the results to the console (1) or to a .csv file (2): ");
                        int printRequestFour = Integer.parseInt(scanner.nextLine());
                        if (printRequestFour == 1) {
                            dataHandler.writeQueryToConsole(databaseWorker.query(queryFour), queryDescriptionFour);
                        } else {
                            dataHandler.writeQueryToCsvFile(databaseWorker.query(queryFour), queryDescriptionFour);
                        }
                        break;
                    case 5:
                        String queryFive = "SELECT firstName, lastName, avg(scoreTotal), avg(scoreRelativeToPar)," +
                            "count(scoreTotal), min(scoreRelativeToPar) FROM TetonRiverWeeklyStats WHERE course = \"Nature Park\" " +
                            "GROUP BY firstName, lastName ORDER BY avg(scoreRelativeToPar), firstName, lastName";
                        String queryDescriptionFive = "First Name, Last Name, Average Score Relative to Par, Average Total Score, Best Score, Weeks Played";
                        System.out.print("Print the results to the console (1) or to a .csv file (2): ");
                        int printRequestFive = Integer.parseInt(scanner.nextLine());
                        if (printRequestFive == 1) {
                            dataHandler.writeQueryToConsole(databaseWorker.query(queryFive), queryDescriptionFive);
                        } else {
                            dataHandler.writeQueryToCsvFile(databaseWorker.query(queryFive), queryDescriptionFive);
                        }
                        break;
                    case 6:
                        printMenu = false;
                        break;
                }

            }
        } catch (Exception e) {
            System.err.println("Error with the database: "+e);
        }

    }
}
