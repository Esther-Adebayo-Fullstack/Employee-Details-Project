import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class AddEmployee extends JFrame implements ActionListener {
    JButton addEmployeeButton; // Button to add employee
    JLabel firstNameLabel, lastNameLabel, ageLabel, salaryLabel, departmentLabel, addressLabel, cityLabel, phoneNumberLabel; // Labels for input fields
    String firstName, lastName, ageStr, basicSalaryStr, addressStr, cityStr, phoneNumber; // Variables to store input values

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy"); // Date format for date of joining
    JTextField firstNameField, lastNameField, ageField, salaryField, addressField, cityField, phoneNumberField; // Text fields for input
    Random random; // Random number generator
    JComboBox<String> departmentDropdown; // Dropdown for department selection

    public AddEmployee() {
        setLayout(null); // Using null layout for manual component positioning

        // Font and color settings
        Font font = new Font("Arial", Font.PLAIN, 14);
        Color textColor = Color.BLACK;
        Color placeholderColor = new Color(180, 180, 180);

        // First Name input
        firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(font);
        firstNameLabel.setForeground(textColor);
        firstNameLabel.setBounds(10, 20, 100, 20); // x, y, width, height
        add(firstNameLabel);
        firstNameField = new JTextField();
        firstNameField.setFont(font);
        firstNameField.setBounds(120, 20, 200, 20); // x, y, width, height
        add(firstNameField);

        // Last Name input
        lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(font);
        lastNameLabel.setForeground(textColor);
        lastNameLabel.setBounds(10, 60, 100, 20); // x, y, width, height
        add(lastNameLabel);
        lastNameField = new JTextField();
        lastNameField.setFont(font);
        lastNameField.setBounds(120, 60, 200, 20); // x, y, width, height
        add(lastNameField);

        // Age input
        ageLabel = new JLabel("Age:");
        ageLabel.setFont(font);
        ageLabel.setForeground(textColor);
        ageLabel.setBounds(10, 100, 100, 20); // x, y, width, height
        add(ageLabel);
        ageField = new JTextField();
        ageField.setFont(font);
        ageField.setBounds(120, 100, 200, 20); // x, y, width, height
        add(ageField);

        // Basic Salary input
        salaryLabel = new JLabel("Basic Salary:");
        salaryLabel.setFont(font);
        salaryLabel.setForeground(textColor);
        salaryLabel.setBounds(10, 140, 100, 20); // x, y, width, height
        add(salaryLabel);
        salaryField = new JTextField();
        salaryField.setFont(font);
        salaryField.setBounds(120, 140, 200, 20); // x, y, width, height
        add(salaryField);

        // Department selection dropdown
        departmentLabel = new JLabel("Department:");
        departmentLabel.setFont(font);
        departmentLabel.setForeground(textColor);
        departmentLabel.setBounds(10, 180, 100, 20); // x, y, width, height
        add(departmentLabel);
        String[] departments = {"Engineering Department", "IT", "Science", "Infrastructure", "Development", "Human Affairs"};
        departmentDropdown = new JComboBox<>(departments);
        departmentDropdown.setFont(font);
        departmentDropdown.setBounds(120, 180, 200, 20); // x, y, width, height
        add(departmentDropdown);

        // Address input
        addressLabel = new JLabel("Address:");
        addressLabel.setFont(font);
        addressLabel.setForeground(textColor);
        addressLabel.setBounds(10, 220, 100, 20); // x, y, width, height
        add(addressLabel);
        addressField = new JTextField();
        addressField.setFont(font);
        addressField.setBounds(120, 220, 200, 20); // x, y, width, height
        add(addressField);

        // City input
        cityLabel = new JLabel("City:");
        cityLabel.setFont(font);
        cityLabel.setForeground(textColor);
        cityLabel.setBounds(10, 260, 100, 20); // x, y, width, height
        add(cityLabel);
        cityField = new JTextField();
        cityField.setFont(font);
        cityField.setBounds(120, 260, 200, 20); // x, y, width, height
        add(cityField);

        // Phone Number input
        phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setFont(font);
        phoneNumberLabel.setForeground(textColor);
        phoneNumberLabel.setBounds(10, 300, 100, 20); // x, y, width, height
        add(phoneNumberLabel);
        phoneNumberField = new JTextField("+234...");
        phoneNumberField.setFont(font);
        phoneNumberField.setForeground(placeholderColor);
        phoneNumberField.setBounds(120, 300, 200, 20); // x, y, width, height
        phoneNumberField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                //if inpt placeholder is +234..... it should automatically put +23 when ser wants to  iput
                if (phoneNumberField.getText().equals("+234...")) {
                    phoneNumberField.setText("+234");
                    phoneNumberField.setForeground(textColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                //change placeholder color and add placeholder if input is empty
                if (phoneNumberField.getText().isEmpty()) {
                    phoneNumberField.setText("+234...");
                    phoneNumberField.setForeground(placeholderColor);
                }
            }
        });
        add(phoneNumberField);

        addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.setSize(150, 30);
        addEmployeeButton.setLocation(160, 340);
        addEmployeeButton.setFont(font);
        addEmployeeButton.setBackground(new Color(8, 32, 107));
        addEmployeeButton.setForeground(Color.WHITE);
        addEmployeeButton.setFocusPainted(false);
        addEmployeeButton.addActionListener(this);
        add(addEmployeeButton);

        setSize(400, 450);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addEmployeeButton) {
            /** getting the  texts in the form fields and trim method
            to remove extra spaces **/

            /** Now ... A user might put commas in their data and the database is in csv format which when included(a comma),
            it would scatter the data arrangement in the database; so it is removed **/
            firstName = firstNameField.getText().trim().replace(",", "");
            lastName = lastNameField.getText().trim().replace(",", "");
            ageStr = ageField.getText().trim().replace(",", "");
            basicSalaryStr = salaryField.getText().trim().replace(",", "");
            addressStr = addressField.getText().trim().replace(",", "");
            cityStr = cityField.getText().trim().replace(",", "");
            phoneNumber = phoneNumberField.getText().trim().replace(",", "");


            // Validation to check if any of the fields is Empty;
            if (firstName.isEmpty() || lastName.isEmpty() || ageStr.isEmpty() || basicSalaryStr.isEmpty() || addressStr.isEmpty() || phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            int age;
            double basicSalary;

            //Age validation;
            try {
                age = Integer.parseInt(ageStr);
                if (age < 16 || age >=65) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age.");
                return;
            }

            //  Salary validation
            try {
                basicSalary = Double.parseDouble(basicSalaryStr);
                if (basicSalary <= 0 || basicSalary >5000000) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid basic salary. Max salary is 5million");
                return;
            }

            // Phone Number Validation
            // Regular expression to match a typical phone number format
            String phoneRegex = "^\\+?[0-9]{8,15}$"; // Allows optional '+' sign and requires at least 8 digits

            if (!phoneNumber.matches(phoneRegex)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid phone number.");
                return;
            }

            // Write employee data to the Database.txt file
            String filename = "src/Database.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(toString());
                writer.newLine(); // Add a newline after each employee entry;
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving data.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Employee data saved successfully.");
            clearFields();
            dispose();
        }
    }


    //The info that will be transferred to the database;
    public String toString() {
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date dateOfJoining = new Date();
        String formattedDate = outputFormat.format(dateOfJoining);
        Random random = new Random();
        int employeeNumber = 0;
            //To give a unique Employee Number ...if the random number already exists in the database it gives it another random number till it is unique;
            try (BufferedReader reader = new BufferedReader(new FileReader("src/Database.txt"))) {
                String line;

                while ((line = reader.readLine()) != null) {

                    employeeNumber  = random.nextInt(100000) + 1;
                    String[] record = line.split(",");
                    if(employeeNumber == Integer.parseInt(record[0])){
                        employeeNumber  = random.nextInt(1000000) + 1;
                    }
                    else if(employeeNumber != Integer.parseInt(record[0])){
                        break;
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // A return statement to wite the data in csv format so it can be sent to the database;
        return employeeNumber + ", " + firstName + ", " + lastName + ", " + ageStr + ", " + basicSalaryStr +
                ", " + departmentDropdown.getSelectedItem() + ", " + formattedDate + ", " + addressStr + ", " + cityStr + ", " + phoneNumber;
    }

//    clear fields after all operations
    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        ageField.setText("");
        salaryField.setText("");
        phoneNumberField.setText("+123...");
        phoneNumberField.setForeground(new Color(180, 180, 180));
    }
}
