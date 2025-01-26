import java.sql.*;
import java.util.Scanner;

public class CRUD_JDBC {
    public static void main(String[] args) {
        // Database connection details
        String jdbcDriver = "com.mysql.cj.jdbc.Driver";
        String dbUrl = "jdbc:mysql://localhost:3306/university";
        String username = "root";
        String password = "root123";

        try {
            // Register JDBC driver
            Class.forName(jdbcDriver);
            
            // Establish connection to the database
            Connection con = DriverManager.getConnection(dbUrl, username, password);
            
            // Create statement
            Statement st = con.createStatement();
            
            // Create EMPLOYEE table if not exists
            String createTableQuery = "CREATE TABLE IF NOT EXISTS EMPLOYEE (" +
                                      "ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                                      "FIRSTNAME VARCHAR(255), " +
                                      "LASTNAME VARCHAR(255));";
            st.executeUpdate(createTableQuery);
            
            // Initialize Scanner for user input
            Scanner sc = new Scanner(System.in);
            
            // Main menu loop
            while (true) {
                // Display menu options
                System.out.println("\n1. INSERT\n2. UPDATE\n3. DELETE\n4. RETRIEVE\n5. TRUNCATE\n6. EXIT");
                System.out.print("Enter your choice: ");
                
                // Get user's choice
                int choice = sc.nextInt();
                sc.nextLine();  // Consume newline character
                
                // Exit the loop if user chooses option 6
                if (choice == 6) {
                    System.out.println("Exiting...");
                    break;
                }

                // Handle user choices
                switch (choice) {
                    case 1: // Insert record
                        System.out.print("Enter First Name: ");
                        String firstName = sc.nextLine();
                        System.out.print("Enter Last Name: ");
                        String lastName = sc.nextLine();
                        
                        // Use prepared statement to prevent SQL injection
                        String insertQuery = "INSERT INTO EMPLOYEE (FIRSTNAME, LASTNAME) VALUES (?, ?)";
                        try (PreparedStatement pst = con.prepareStatement(insertQuery)) {
                            pst.setString(1, firstName);
                            pst.setString(2, lastName);
                            pst.executeUpdate();
                            System.out.println("Record inserted successfully.");
                        } catch (SQLException e) {
                            System.out.println("Error during insertion: " + e.getMessage());
                        }
                        break;
                        
                    case 2: // Update record
                        System.out.print("Enter Column Name to Update (FIRSTNAME/LASTNAME): ");
                        String columnName = sc.nextLine();
                        System.out.print("Enter the New Value: ");
                        String newValue = sc.nextLine();
                        System.out.print("Enter the ID of the Employee to Update: ");
                        int updateId = sc.nextInt();
                        sc.nextLine();
                        
                        // Use prepared statement to prevent SQL injection
                        String updateQuery = "UPDATE EMPLOYEE SET " + columnName + " = ? WHERE ID = ?";
                        try (PreparedStatement pst = con.prepareStatement(updateQuery)) {
                            pst.setString(1, newValue);
                            pst.setInt(2, updateId);
                            pst.executeUpdate();
                            System.out.println("Record updated successfully.");
                        } catch (SQLException e) {
                            System.out.println("Error during update: " + e.getMessage());
                        }
                        break;

                    case 3: // Delete record
                        System.out.print("Enter the ID of the Employee to Delete: ");
                        int deleteId = sc.nextInt();
                        sc.nextLine();
                        
                        // Use prepared statement to prevent SQL injection
                        String deleteQuery = "DELETE FROM EMPLOYEE WHERE ID = ?";
                        try (PreparedStatement pst = con.prepareStatement(deleteQuery)) {
                            pst.setInt(1, deleteId);
                            pst.executeUpdate();
                            System.out.println("Record deleted successfully.");
                        } catch (SQLException e) {
                            System.out.println("Error during deletion: " + e.getMessage());
                        }
                        break;
                        
                    case 4: // Retrieve records
                        String retrieveQuery = "SELECT * FROM EMPLOYEE";
                        try (ResultSet rs = st.executeQuery(retrieveQuery)) {
                            System.out.println("ID\tFirst Name\tLast Name");
                            while (rs.next()) {
                                System.out.println(rs.getInt("ID") + "\t" + rs.getString("FIRSTNAME") + "\t" + rs.getString("LASTNAME"));
                            }
                        } catch (SQLException e) {
                            System.out.println("Error during retrieval: " + e.getMessage());
                        }
                        break;

                    case 5: // Truncate table
                        String truncateQuery = "TRUNCATE TABLE EMPLOYEE";
                        try {
                            st.executeUpdate(truncateQuery);
                            System.out.println("Table truncated successfully.");
                        } catch (SQLException e) {
                            System.out.println("Error during truncation: " + e.getMessage());
                        }
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter a value between 1 and 6.");
                        break;
                }
            }

            // Close resources
            sc.close();
            con.close();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }
}
