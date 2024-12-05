import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main extends Application {

    private ArrayList<Map<String, String>> excelData;

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(10);
        Label statusLabel = new Label("Welcome to Disc Golf Stat Pal!");

        // Button to connect to the Excel file
        Button connectButton = new Button("Connect to Database");
        connectButton.setOnAction(e -> {
            try {
                String excelFilePath = "C:\\Users\\andxa\\DiscGolfQueryResults.xlsx";
        
                // Debug: Print current working directory
                System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
        
                // Check if file exists
                File file = new File(excelFilePath);
                if (!file.exists()) {
                    statusLabel.setText("Error: Excel file not found at " + excelFilePath);
                    return;
                }
        
                // Load data from Excel file
                excelData = readExcelFile(excelFilePath);
                statusLabel.setText("Successfully connected to Excel sheet!");
                System.out.println("Data loaded from Excel:");
                excelData.forEach(System.out::println); // Print data to console
            } catch (Exception ex) {
                statusLabel.setText("Failed to connect to Excel sheet: " + ex.getMessage());
            }
        });
        


        // Button to upload a CSV file
        Button uploadButton = new Button("Upload CSV File");
        uploadButton.setOnAction(e -> {
            statusLabel.setText("Upload functionality is currently disabled.");
        });

        // Button to execute a query and save results to a CSV
        Button queryButton = new Button("Run Query");
        queryButton.setOnAction(e -> {
            if (excelData == null) {
                statusLabel.setText("Connect to the Excel sheet first!");
                return;
            }
            try {
                System.out.println("Query Results:");
                excelData.forEach(System.out::println); // Print query results to console
                statusLabel.setText("Query executed. Results displayed in console.");
            } catch (Exception ex) {
                statusLabel.setText("Query execution failed: " + ex.getMessage());
            }
        });

        // Add all components to the layout
        layout.getChildren().addAll(statusLabel, connectButton, uploadButton, queryButton);

        // Set up the scene and stage
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Disc Golf Stat Pal");
        primaryStage.show();
    }

    // Method to read data from an Excel file
    public ArrayList<Map<String, String>> readExcelFile(String filePath) throws IOException {
        ArrayList<Map<String, String>> dataList = new ArrayList<>();
    
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
    
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
    
            // Get headers
            org.apache.poi.ss.usermodel.Row headerRow = rowIterator.next();
            ArrayList<String> headers = new ArrayList<>();
            for (org.apache.poi.ss.usermodel.Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }
    
            // Get data rows
            while (rowIterator.hasNext()) {
                org.apache.poi.ss.usermodel.Row row = rowIterator.next();
                Map<String, String> rowData = new HashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    org.apache.poi.ss.usermodel.Cell cell = row.getCell(i, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.put(headers.get(i), cell.toString());
                }
                dataList.add(rowData);
            }
        }
    
        return dataList;
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
