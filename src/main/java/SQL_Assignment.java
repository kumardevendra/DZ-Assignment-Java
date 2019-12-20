

import com.opencsv.CSVWriter;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQL_Assignment {
    static Logger logger = Logger.getLogger(SQL_Assignment.class);
    static String d;
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date cur_date = new Date();
        d = dateFormat.format(cur_date);
    }


    public static void main(String[] args) {

        SimpleLayout layout = new SimpleLayout();
        FileAppender appender = null;
        try {
            appender = new FileAppender(layout,"C:\\Users\\kumadeve\\IdeaProjects\\Dataeaze-Assignment-SQL\\logs\\SQL-Assignment_"+ d +".log",true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.addAppender(appender);
        logger.setLevel(Level.DEBUG);
        logger.info(args.length);
        if( args.length == 0) {
            logger.error(new Date() + "Invalid Arguments must provide\n" +
                    "jar <jar_file_name> <db_name> <table_name> <output_file_name_with_path>");
            System.exit(0);
        }
        else if (args[0] == null ) {
            logger.error(new Date() + "Arg[1] : database is null");
            System.exit(0);
        }
        else if (args[1] == null ) {
            logger.error(new Date() + "Arg[2] : table is null");
            System.exit(0);
        }
        else if (args[2] == null) {
            logger.error(new Date() + "Arg[3] : file path with name is null");
        }
        else if (args.length > 3) {
            logger.error(new Date() + "Invalid Arguments must provide\n" +
                    "jar <jar_file_name> <db_name> <table_name> <output_file_name_with_path>");
            System.exit(0);
        }


        logger.info(new Date() + " Start: getting the arguments");
        String tableName = args[1];
        String database = args[0];
        String outPutFile = args[2];
        logger.info(new Date() + " End: getting the arguments ");

        logger.info("Table: <"+ tableName+">");
        logger.info("Database : <"+ database +">");
        logger.info("Out Put File Path : <"+ outPutFile +">");

        logger.info(new Date() + " Getting App Utilities");
        AppUtilities appUtilities = new AppUtilities();

        logger.info(new Date() + " Establishing the connection with the Database");
        Connection conn = appUtilities.getConnector();

        String filename = outPutFile;
        String query;
        try {

            logger.info(new Date() + " Creating output file.");
            Writer writer = Files.newBufferedWriter(Paths.get(filename));
            logger.info(new Date() + " Outputh file created <" + filename + ">");


            CSVWriter csvWriter = new CSVWriter( writer,
                    '\t',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END
            );

            logger.info(new Date() + " CSV writer properties < " + csvWriter.toString() +" >");


            logger.info(new Date() + " Creating statement");
            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            query = "select * from "+ database +"."+ tableName;
            logger.info(new Date() + "Query : <"+ query+">");
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                csvWriter.writeNext(new String[]{resultSet.getString("emp_id"), resultSet.getString("emp_name")});
                logger.info(new Date() + " Inserting Data : <"+resultSet.getString("emp_id") + "," + resultSet.getString("emp_name"));
            }

            writer.close();
            logger.info(new Date() + " Closing the out put file <"+filename+">");
            conn.close();
            logger.info(new Date() + " Closing the connection");
        }
        catch (SQLException e) {
            logger.error("SQL Exception : " + e.getMessage());
        } catch (IOException e) {
            logger.error("File Not Found : " + e.getMessage());
        }
    }
}
