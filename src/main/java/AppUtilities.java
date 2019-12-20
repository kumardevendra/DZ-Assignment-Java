import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppUtilities {

    Connection connector;
    Properties properties;
    public AppUtilities() {

    }

    public AppUtilities(Connection connector, Properties properties) {
        this.connector = connector;
        this.properties = properties;
    }

    public Connection getConnector() {
        try {
            SQL_Assignment.logger.info(new Date() + "Getting the Driver Class");
            Class.forName("com.mysql.jdbc.Driver");

            SQL_Assignment.logger.info(new Date() + "Getting the properties");
            properties = getProperties();
            SQL_Assignment.logger.info(new Date() + "Properties : <"+properties.propertyNames()+">");
            connector = DriverManager.getConnection(properties.getProperty("connectionString"),
                    properties.getProperty("user"),
                    properties.getProperty("password"));
            SQL_Assignment.logger.info(new Date() + "Connetion established");

        } catch (ClassNotFoundException e) {
            SQL_Assignment.logger.error(new Date() + "Driver class not found : " + e.getMessage());
        } catch (SQLException e) {
            SQL_Assignment.logger.error(new Date() + "Something went wrong while connecting : " + e.getMessage());
        }

        return connector;
    }

    public void setConnector(Connection connector) {
        this.connector = connector;
    }

    public Properties getProperties() {

        SQL_Assignment.logger.info(new Date() + " Inside Properties.");
        Properties properties = new Properties();

        try {
            InputStream inputStream = new FileInputStream("C:\\Users\\kumadeve\\IdeaProjects\\Dataeaze-Assignment-SQL\\config.properties");
            SQL_Assignment.logger.info(new Date() + " Properties file loaded.");
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            SQL_Assignment.logger.info(new Date() + " Properties File not found : " + e.getMessage());
        } catch (IOException e) {
            SQL_Assignment.logger.info(new Date() + " Properties File not found : " + e.getMessage());

        }

        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void Log(String logLevel, String logMsg) {

    }
}
