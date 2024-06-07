import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeRecords extends JFrame {
    // File path for the database
    String filePath = "src/Database.txt";
    // Error message for validation
    String errorMessage;
    // Current date
    Date currentDate = new Date();

    // Buttons for UI
    JButton Add, Delete, Edit, Save, Count, Search, Refresh, Exit;
    // Text fields for search criteria
    JTextField searchOne, searchTwo;
    // Array for table columns
    String[] columns;
    // Array for table data
    String[][] data;
    // Array for department options
    String[] departments = {"Engineering Department", "IT", "Science", "Infrastructure", "Development", "Human Affairs"};
    // List to store and manipulate the data
    ArrayList<String[][]> dataArr = new ArrayList<>();
    // List to store edited cells
    java.util.List<int[]> editedCells = new ArrayList<>();

    // Boolean flags
    Boolean isEditable, isValid;

    // Table model
    DefaultTableModel model;
    // Table component
    private JTable dataTable;
    // Scroll pane for the table
    private JScrollPane scrollPane;

    // Array for search options
    String[] options;
    // Dropdown menus for search criteria
    JComboBox<String> dropdown, dropdown2;

    // Constructor
    public EmployeeRecords() {
        // Frame setup
        setTitle("Admin Page");
        setSize(1350, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane ().setBackground(Color.GRAY);

        // Fetch data from the database
        fetch();

        // Font setup
        Font serifFont = new Font("Dialog", Font.BOLD, 16);

        // Panel for top navigation
        JPanel navbar = new JPanel();
        JLabel Greetings = new JLabel("Hello Admin");
        JLabel Title = new JLabel("Employee Records");
        JPanel dateTimePanel = new JPanel();
        dateTimePanel.setLayout(new BorderLayout());
        JLabel date = new JLabel(getDate());
        JLabel time = new JLabel(getTime());
        Exit = new JButton("Exit");
        Exit.addActionListener(e->{
            exit();
        });

        dateTimePanel.add(date, BorderLayout.WEST);
        dateTimePanel.add(time, BorderLayout.CENTER);
        dateTimePanel.add(Exit, BorderLayout.EAST);
        Title.setFont(serifFont);
        Greetings.setFont(serifFont);
        date.setFont(serifFont);
        time.setFont(serifFont);

        // Timer to update time
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the time label
                time.setText(" " + getTime());
            }
        });

        // Start the timer
        timer.start();

        // Styling for labels
        Title.setForeground(Color.WHITE);
        Greetings.setForeground(Color.WHITE);

        // Styling for title alignment
        Title.setHorizontalAlignment(JLabel.CENTER);

        // Styling for navbar
        navbar.setBackground(new Color(0x071559));
        navbar.setLayout(new BorderLayout());
        navbar.setBorder(new EmptyBorder(20, 20, 20, 20));
        navbar.add(Greetings, BorderLayout.WEST);
        navbar.add(Title, BorderLayout.CENTER);
        navbar.add(dateTimePanel, BorderLayout.EAST);
        add(navbar, BorderLayout.NORTH);

        // Body panel
        JPanel Body = new JPanel();
        Body.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Sidebar panel
        JPanel Sidebar = new JPanel();
        Sidebar.setLayout(null);
        Sidebar.setPreferredSize(new Dimension(200, 600));
        Sidebar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Buttons for sidebar
        Save = new JButton("Save");
        Add = new JButton("Add");
        Delete = new JButton("Delete");
        Edit = new JButton("Edit Mode");
        Count = new JButton("Sum of Records");
        Refresh = new JButton("Refresh/Display all");

        // Setting bounds for buttons
        Save.setBounds(30, 80, 130, 40);
        Add.setBounds(30, 160, 130, 40);

        Delete.setBounds(30, 240, 130, 40);
        Edit.setBounds(30, 320, 130, 40);
        Count.setBounds(30, 400, 130, 40);
        Refresh.setBounds(20, 480, 150, 40);

        // Styling for buttons
        Save.setBackground(new Color(8, 32, 107));
        Add.setBackground(new Color(8, 32, 107));
        Delete.setBackground(new Color(8, 32, 107));
        Edit.setBackground(new Color(8, 32, 107));
        Count.setBackground(new Color(8, 32, 107));
        Refresh.setBackground(new Color(8, 32, 107));

        // Styling for button text
        Save.setForeground(Color.WHITE);
        Add.setForeground(Color.WHITE);
        Delete.setForeground(Color.WHITE);
        Edit.setForeground(Color.WHITE);
        Count.setForeground(Color.WHITE);
        Refresh.setForeground(Color.WHITE);

        // Disabling focus painting for buttons
        Save.setFocusPainted(false);
        Add.setFocusPainted(false);
        Delete.setFocusPainted(false);
        Edit.setFocusPainted(false);
        Count.setFocusPainted(false);
        Refresh.setFocusPainted(false);

        // Adding action listeners for buttons in the sidebar;
        Save.setEnabled(false);
        Save.addActionListener(e->{
            saveChanges();
        });
        Add.addActionListener(e->{
            add();
        });

        Edit.addActionListener(e->{
            editData();
        });
        Delete.addActionListener(e->{
            deleteData();
        });
        Count.addActionListener(e->{
            countRecords();
        });
        Refresh.addActionListener(e->{
            refreshTable();
        });

        // Adding buttons to sidebar
        Sidebar.add(Save);
        Sidebar.add(Add);
        Sidebar.add(Delete);
        Sidebar.add(Edit);
        Sidebar.add(Count);
        Sidebar.add(Refresh);

        // Main section panel
        JPanel MainSection = new JPanel();
        MainSection.setLayout(new BorderLayout());

        // Search panel
        JPanel SearchPanel = new JPanel();
        SearchPanel.setLayout(new FlowLayout());

        // Dropdown menus and text fields for search
        options = new String[]{"Employee Number", "First Name", "Last Name", "Department"};
        dropdown = new JComboBox<>(options);
        dropdown2 = new JComboBox<>(options);
        searchOne = new JTextField("", 15);
        searchTwo = new JTextField("", 20);
        dropdown.setSelectedIndex(0);
        dropdown2.setSelectedIndex(1);

        // Search button
        Search = new JButton("Search");
        Search.setBackground(new Color(8, 32, 107));
        Search.setForeground(Color.WHITE);

        // Action listener for search button
        Search.addActionListener(e->{
            search();
        });

        // Adding components to search panel
        SearchPanel.add(searchOne);
        SearchPanel.add(dropdown);
        SearchPanel.add(searchTwo);
        SearchPanel.add(dropdown2);
        SearchPanel.add(Search);

        // Adding search panel to main section
        MainSection.add(SearchPanel, BorderLayout.NORTH);

        // Panel for table display
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tablePanel.setPreferredSize(new Dimension(1100, 500));

        columns = new String[]{ // For the table headers;
                "Employee Number",
                "First Name",
                "Last Name",
                "Age",
                "Basic Salary",
                "Department",
                "Date of Joining",
                "Address",
                "City",
                "Phone Number"
        };
        isEditable = false;
        model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return  isEditable;
            }
        };

        // Table model listener for detecting cell edits
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {

                if (isEditable && e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    int[] editedCell = {row, col};
                    if (!editedCells.contains(editedCell)) {
                        editedCells.add(editedCell);
                    }
                    Save.setEnabled(!editedCells.isEmpty());
                }
            }
        });
        dataTable = new JTable(model);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        dataTable.setFillsViewportHeight(true);

        // Customizing the table header
        JTableHeader header = dataTable.getTableHeader();
        header.setBackground(new Color(8, 32, 107));
        header.setBorder(new EmptyBorder(30, 0, 30, 0));
        header.setForeground(Color.WHITE);
        header.setFont(serifFont);

        // Creating a JScrollPane for the table
        scrollPane = new JScrollPane(dataTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        MainSection.add(tablePanel, BorderLayout.CENTER);

        // Adding components to body panel
        Body.add(Sidebar);
        Body.add(MainSection);

        // Adding body panel to frame
        add(Body, BorderLayout.CENTER);
        setUndecorated(true);
        setVisible(true);
    }

    public String getDate() {
        // Format for date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return dateFormat.format(currentDate);
    }

    public String getTime() {
        // Get the current time
        Date currentTime = new Date();

        // Format for time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(currentTime);
    }


    public static void main(String[] args) {
        new EmployeeRecords();
    }

    public ArrayList<String[][]> fetch() {
        ArrayList<String[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Process each line of data here
                String[] record = line.split(",");
                rows.add(record);
            }

            // Initialize the data array based on the number of rows and columns
            int rowCount = rows.size();
            int columnCount = rows.isEmpty() ? 0 : rows.get(0).length;
            data = new String[rowCount][columnCount];

            // Populate the data array with values from the ArrayList
            for (int row = 0; row < rowCount; row++) {
                String[] record = rows.get(row);
                for (int col = 0; col < columnCount; col++) {
                    data[row][col] = record[col];
                }
            }

            // Clear dataArr before adding new data
            dataArr.clear();
            // Add data array to dataArr
            dataArr.add(data);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataArr;
    }


    public void search() {
        String searchTerm = searchOne.getText().trim();
        String searchColumn = (String) Objects.requireNonNull(dropdown.getSelectedItem());
        String secondSearchTerm = searchTwo.getText().trim();
        String secondSearchColumn = (String) Objects.requireNonNull(dropdown2.getSelectedItem());

        // If both search fields are empty, display a message and return
        if (searchTerm.isEmpty() && secondSearchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter search criteria");
            return;
        }

        ArrayList<String[]> matchedRows = new ArrayList<>();

        // Iterate through the data to find matches
        for (String[] row : data) {
            if ((!searchTerm.isEmpty() && row[Arrays.asList(columns).indexOf(searchColumn)].equalsIgnoreCase(searchTerm)) ||
                    (!secondSearchTerm.isEmpty() && row[Arrays.asList(columns).indexOf(secondSearchColumn)].equalsIgnoreCase(secondSearchTerm))) {
                matchedRows.add(row);
                System.out.println(row);
                isEditable = false;
            }
        }

        if (matchedRows.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No matching records found");
        } else {
            // Display the matched rows in a new window or dialog
            displayMatchedRecords(matchedRows);
        }
    }

    private void displayMatchedRecords(ArrayList<String[]> matchedRows) {
        // Clear the existing table data
        model.setRowCount(0);

        // Add the matched rows to the table model
        for (String[] row : matchedRows) {
            model.addRow(row);
        }
    }


    public void add() {
        new AddEmployee();
    }

    public void editData() {
        isEditable = !isEditable;
        if (isEditable) {
            Edit.setText("Readonly");
        } else {
            Edit.setText("Edit Mode");
        }

        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                model.fireTableCellUpdated(row, col); // Refresh the table cells
            }
        }
    }

    // Method to check if a new employee ID is unique
    private boolean isNewIdUnique(String newId) {
        String[][] tableArr = dataArr.get(0); // Get the data array from the dataArr list
        for (String[] row : tableArr) { // Loop through each row in the data array
            if (row[0].equalsIgnoreCase(newId)) { // Check if the new ID matches any existing ID (case-insensitive)
                return false; // Return false if the ID already exists
            }
        }
        return true; // Return true if the ID is unique
    }

    // Method to save changes made to the database
    public void saveChanges() {
        // Check if the new ID is unique before saving changes
        updateDatabase(); // Call the updateDatabase method to apply changes to the database
    }

    // Method to check if a new employee ID is unique (overloaded for edit mode)
    private boolean isNewIdUnique(String newId, int currentRow) {
        String[][] tableArr = dataArr.get(0); // Get the data array from the dataArr list
        for (int row = 0; row < tableArr.length; row++) { // Loop through each row index in the data array
            if (row != currentRow && tableArr[row][0].equalsIgnoreCase(newId)) { // Check if the new ID matches any existing ID (except the current row)
                return false; // Return false if the ID already exists
            }
        }
        return true; // Return true if the ID is unique
    }

    // Method to update the database with the changes
    private void updateDatabase() {
        isValid = true; // Initialize isValid flag to true
        String[][] tableArr = dataArr.get(0); // Get the data array from the dataArr list

        if (editedCells.isEmpty()) { // Check if there are no edited cells
            JOptionPane.showMessageDialog(null, "No changes were made."); // Show a message if no changes were made
            return; // Exit the method
        }

        // Iterate through the list of edited cells and update the corresponding records
        for (int[] editedCell : editedCells) { // Loop through each edited cell
            int row = editedCell[0]; // Get the row index of the edited cell
            int col = editedCell[1]; // Get the column index of the edited cell
            String columnName = columns[col]; // Get the column name from the columns array

            // For other columns, update the value directly
            Object newValueObject = dataTable.getValueAt(row, col); // Get the new value from the data table
            String newValue = (newValueObject != null) ? newValueObject.toString().trim() : null; // Trim and convert the new value to string

            if (!newValue.isEmpty()) { // Check if the new value is not empty
                if( !newValue.contains(",")){
                    switch (columnName) { // Switch based on the column name
                        case "Employee Number":
                            if (!isNewIdUnique(newValue, row)) { // Check if the new ID is unique (except the current row)
                                isValid = false; // Set isValid flag to false
                                errorMessage = "Employee ID already exists."; // Set the error message
                            }
                            break;
                        case "Age":
                            try {
                                int age = Integer.parseInt(newValue); // Parse the new age value as integer
                                if (age < 16 || age > 65) { // Check if the age is within the valid range
                                    isValid = false; // Set isValid flag to false
                                    errorMessage = "Age must be between 16 and 65."; // Set the error message
                                }
                            } catch (NumberFormatException e) { // Catch NumberFormatException if the age value is not a valid integer
                                isValid = false; // Set isValid flag to false
                                errorMessage = "Invalid age format."; // Set the error message
                            }
                            break;
                        case "Department":
                            // Validate department against the options available in AddEmployee class
                            if (!Arrays.asList(departments).contains(newValue)) { // Check if the new department value is not in the departments array
                                isValid = false; // Set isValid flag to false
                                errorMessage = "Invalid department."; // Set the error message
                            }
                            break;
                        case "Basic Salary":
                            try {
                                double salary = Double.parseDouble(newValue); // Parse the new salary value as double
                                if (salary < 0 || salary > 5000000) { // Check if the salary is within the valid range
                                    isValid = false; // Set isValid flag to false
                                    errorMessage = "Salary must be between 0 and 5,000,000."; // Set the error message
                                }
                            } catch (NumberFormatException e) { // Catch NumberFormatException if the salary value is not a valid double
                                isValid = false; // Set isValid flag to false
                                errorMessage = "Invalid salary format."; // Set the error message
                            }
                            break;
                        case "Date of Joining":
                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                dateFormat.setLenient(false);
                                Date newDate = dateFormat.parse(newValue);
                                Date currentDate = new Date();
                                if (newDate.after(currentDate)) {
                                    throw new IllegalArgumentException("Date of joining cannot be in the future");
                                }
                            } catch (Exception ex) {
                                isValid = false;
                                errorMessage = "Invalid date format.";
                            }
                            break;

                        case "Phone Number":
                            // Validate phone number format using regex
                            String phoneRegex = "^\\+?[0-9]{8,15}$"; // Define the regex pattern for phone numbers
                            if (!newValue.matches(phoneRegex)) { // Check if the new phone number value matches the regex pattern
                                JOptionPane.showMessageDialog(this, "Please enter a valid phone number."); // Show a message if the phone number is invalid
                                return; // Exit the method
                            }
                            break;
                        // Add more cases for other columns if needed
                    }

                    // If validation passes, update the value in the data array
                    if (isValid) {
                        tableArr[row][col] = newValue; // Update the value in the data array
                    } else {
                        // Show error message if validation fails
                        JOptionPane.showMessageDialog(null, errorMessage); // Show the error message
                        return; // Exit the method
                    }
                }else{
                    isValid = false; // Set isValid flag to false
                    errorMessage = "Data fields cannot contain commas"; // Set the error message
                }

            } else {
                isValid = false; // Set isValid flag to false
                errorMessage = "Data fields cannot be empty"; // Set the error message
            }
        }

        // Write the updated data back to the file
        if (isValid) { // Check if isValid flag is true
            try (FileWriter fileWriter = new FileWriter(filePath); // Open a file writer to write to the database file
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) { // Open a buffered writer for efficient writing
                for (String[] row : tableArr) { // Loop through each row in the data array
                    bufferedWriter.write(String.join(",", row)); // Write the row data as a comma-separated string
                    bufferedWriter.newLine(); // Write a new line after each row
                }
            } catch (IOException i) { // Catch any IO exceptions
                i.printStackTrace(); // Print the stack trace for debugging
            }
            editedCells.clear(); // Clear the list of edited cells
            JOptionPane.showMessageDialog(null, "Changes saved to the database"); // Show a message indicating that changes were saved
        }
    }



    // Method to delete selected rows from the database
    public void deleteData(){
        int[] selectedRows = dataTable.getSelectedRows(); // Get the indices of the selected rows in the table

        if (selectedRows.length > 0) { // Check if there are selected rows
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete selected rows?", "Confirm Deletion", JOptionPane.YES_NO_OPTION); // Show a confirmation dialog for deletion

            if (choice == JOptionPane.YES_OPTION) { // Check if the user confirms deletion
                deleteRowsFromDatabase(selectedRows); // Call the method to delete selected rows from the database
                // Refresh the table after deletion
                refreshTable(); // Call the method to refresh the table
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select rows to delete"); // Show a message if no rows are selected for deletion
        }
    }

    // Method to delete selected rows from the database file
    private void deleteRowsFromDatabase(int[] selectedRows) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) { // Open a buffered reader to read from the database file
            // Create a list to hold the remaining lines after deletion
            ArrayList<String> remainingLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) { // Read each line from the database file
                // Process each line of data here
                String[] record = line.split(","); // Split the line into fields using comma as delimiter
                boolean deleteRow = false; // Initialize deleteRow flag to false
                for (int i : selectedRows) { // Loop through each selected row index
                    if (!dataArr.isEmpty()) { // Check if dataArr is not empty
                        String[][] arrLoop = dataArr.get(0); // Get the data array from the dataArr list
                        String employeeNumber = arrLoop[i][0]; // Get the employee number from the selected row
                        if (Objects.equals(employeeNumber, record[0])) { // Check if the employee number matches the current record
                            // If the current line corresponds to a selected row, mark it for deletion
                            deleteRow = true; // Set deleteRow flag to true
                            break; // Exit the loop since the row is marked for deletion
                        }
                    }
                }
                // If the current line is not marked for deletion, add it to the remainingLines list
                if (!deleteRow) { // Check if deleteRow flag is false
                    remainingLines.add(line); // Add the line to the list of remaining lines
                }
            }

            // Write the remaining lines back to the file
            try (FileWriter fileWriter = new FileWriter(filePath); // Open a file writer to write to the database file
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) { // Open a buffered writer for efficient writing
                for (String remainingLine : remainingLines) { // Loop through each remaining line
                    bufferedWriter.write(remainingLine); // Write the line to the database file
                    bufferedWriter.newLine(); // Write a new line after each line
                }
            }

            JOptionPane.showMessageDialog(null, "Selected rows deleted successfully"); // Show a message indicating successful deletion

        } catch (IOException e) { // Catch any IO exceptions
            System.out.println("Error occurred while deleting employee: " + e.getMessage()); // Print error message
        }
    }

    // Method to refresh the table with updated data from the database
    private void refreshTable() {
        model.setRowCount(0); // Clear the table model

        // Fetch data again and add it to the table model
        dataArr = fetch(); // Call the fetch method to retrieve data from the database
        String[][] arrLoop = dataArr.get(0); // Get the data array from the dataArr list
        for (String[] row : arrLoop) { // Loop through each row in the data array
            model.addRow(row); // Add the row to the table model
        }

        // Notify the table that the data has changed
        model.fireTableDataChanged(); // Notify the table model that the data has changed
    }

    // Method to count the total number of records in the table
    public void countRecords(){
        int col = model.getRowCount(); // Get the number of rows in the table
        JOptionPane.showMessageDialog(null,"There are " +  col + " Records for this report"); // Show a message with the total number of records
    }

    // Method to handle the exit action
    public void exit() {
        int option = JOptionPane.showConfirmDialog(this,
                (!editedCells.isEmpty() ? "You have Unsaved edits. Do you wish to save" : "Are you sure you want to Exit? "),
                "Exit", JOptionPane.YES_NO_CANCEL_OPTION); // Show a confirmation dialog for exiting the application

        if (option == JOptionPane.YES_OPTION) { // Check if the user chooses to save changes before exiting
            if (!editedCells.isEmpty()) { // Check if there are unsaved edits
                saveChanges(); // Call the method to save changes
                if(isValid){
                    dispose(); // Close the application window
                }
            }else{
                dispose();
            }

        } else if (option == JOptionPane.NO_OPTION && !editedCells.isEmpty()) { // Check if the user chooses not to save changes but there are unsaved edits
            dispose(); // Close the application window
        }
    }
}



