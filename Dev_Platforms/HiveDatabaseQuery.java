package com.smit.twitterbdw;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;

public class HiveDatabaseQuery {
private static String driverName = "org.apache.hive.jdbc.HiveDriver";
Statement stmt;
Connection con;
public void setConnection() throws SQLException , Exception
{
Class.forName(driverName);
// get connection
Connection con = DriverManager.getConnection(
"jdbc:hive2://localhost:10000/default", "", "");
Statement stmt = con.createStatement();
//stmt.execute("add jar /home/hduser/hadooplearn/csv-serde.jar");
}
public void closeConnection() throws SQLException{
con.close();
}
public ResultSet QueryME(String args) throws SQLException, Exception{
//setConnection();
Class.forName(driverName);
// get connection
Connection con = DriverManager.getConnection(
"jdbc:hive2://localhost:10000/default", "", "");
Statement stmt = con.createStatement();

stmt.execute("add jar /home/eldor4do/Documents/TwitterPython/json-serde-1.3-jar-with-dependencies.jar");
ResultSet res = stmt.executeQuery(args);
//while(res.next()){
//System.out.println(res.getString(1));
// entrya.setText(res.getString(1));
//}
return res;
}
public static void main(String[] args) throws SQLException, Exception {
// Register driver and create driver instance
//HiveDatabaseQuery q = new HiveDatabaseQuery();
//q.QueryME("select * from tweets limit 3");
}
}