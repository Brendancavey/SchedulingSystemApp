/** Author: Brendan Thoeung | ID: 007494550 | Date: 9/19/2022
 * */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    /** This is the openConnection method.
     * This method is primarily called in the main method to open
     * the connection to the sql data base.*/
    public static void openConnection() {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        } catch(Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
    /** This is the closeConnection method.
     * This method is primarily called in the main method to close
     * the connection to the sql data base. */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }
    /** This is the getConnection method.
     * This method is primarily called when making a connection to the database
     * to execute a sql query.*/
    public static Connection getConnection(){
        return connection;
    }
}
