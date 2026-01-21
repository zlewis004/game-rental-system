/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class GameRental {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of GameRental store
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public GameRental(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end GameRental

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            GameRental.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      GameRental esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the GameRental object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new GameRental (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Profile");
                System.out.println("2. Update Profile");
                System.out.println("3. View Catalog");
                System.out.println("4. Place Rental Order");
                System.out.println("5. View Full Rental Order History");
                System.out.println("6. View Past 5 Rental Orders");
                System.out.println("7. View Rental Order Information");
                System.out.println("8. View Tracking Information");

                //the following functionalities basically used by employees & managers
                System.out.println("9. Update Tracking Information");

                //the following functionalities basically used by managers
                System.out.println("10. Update Catalog");
                System.out.println("11. Update User");

                System.out.println(".........................");
                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: viewProfile(esql, authorisedUser); break;
                   case 2: updateProfile(esql, authorisedUser); break;
                   case 3: viewCatalog(esql); break;
                   case 4: placeOrder(esql, authorisedUser); break;
                   case 5: viewAllOrders(esql, authorisedUser); break;
                   case 6: viewRecentOrders(esql, authorisedUser); break;
                   case 7: viewOrderInfo(esql, authorisedUser); break;
                   case 8: viewTrackingInfo(esql, authorisedUser); break;
                   case 9: updateTrackingInfo(esql); break;
                   case 10: updateCatalog(esql); break;
                   case 11: updateUser(esql); break;



                   case 20: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(GameRental esql){
   try {
      System.out.print("\tEnter Username: ");
      String username = in.readLine();
      System.out.print("\tEnter Password: ");
      String password = in.readLine();
      System.out.print("\tEnter Phone Number: ");
      String phoneNumber = in.readLine();

      String query = String.format("INSERT INTO Users(login, password, role, phoneNum, numOverDueGames) VALUES('%s', '%s','customer', '%s', 0)", username, password, phoneNumber);  
      System.out.println(query);
           esql.executeUpdate(query);
      System.out.println("User successfully created!");
   }
   catch (Exception e) {
      System.err.println(e.getMessage());
   }
}//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(GameRental esql){
   try {
      System.out.print("\tEnter username: ");
      String username = in.readLine();
      System.out.print("\tEnter password: ");
      String password = in.readLine();

      String query = String.format("SELECT * FROM Users WHERE login = '%s' AND password = '%s'", username, password);
      int userNum = esql.executeQuery(query);

      if (userNum > 0) {
         System.out.println("Successful Log in!");
         return username;
      } else {
         System.out.println("Invalid username or password.");
         return null;
      }
   } catch (Exception e) {
      System.err.println(e.getMessage());
      return null;
   }
   }//end

// Rest of the functions definition go in here

   public static void viewProfile(GameRental esql, String username) {
        try {
            String query = String.format("SELECT * FROM Users WHERE login = '%s'", username);
            esql.executeQueryAndPrintResult(query);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void updateProfile(GameRental esql, String username) {
      try {
         System.out.println("What would you like to update?");
         System.out.println("1. Password");
              System.out.println("2. Phone Number");
         System.out.println("3. Favorite Games");
         int choice = readChoice();
         String query = "";

      switch(choice) {
         case 1:
           System.out.println("\tEnter new Password: ");
           String newPassword = in.readLine();
           query = String.format("Update Users SET password = '%s' WHERE login = '%s'", newPassword, username);
           break;
              case 2:
           System.out.println("\tEnter new Phone Number: ");
           String newPhoneNum = in.readLine();
           query = String.format("Update Users SET phoneNum = '%s' WHERE login = '%s'", newPhoneNum, username);
           break;
              case 3:
           System.out.println("\tEnter new Favorite Game: ");
           String newFavGames = in.readLine();
           query = String.format("Update Users SET favGames = '%s' WHERE login = '%s'", newFavGames, username);
           break;
              default: 
           System.out.println("\tUnrecognized Choice!");
           return;
      }
      esql.executeUpdate(query);
      System.out.println("Profile was updated successfully!");
      } catch (Exception e) {
         System.err.println(e.getMessage());
        }
   }

    public static void viewCatalog(GameRental esql) {
      try {
         System.out.println("Search catalog by: ");
         System.out.println("1. Genre");
         System.out.println("2. Price");
         System.out.println("3. Sort by Price (High to Low)");
         System.out.println("4. Sort by Price (Low to High)");
         int choice = readChoice();
         String query = "";

         switch(choice) {
            case 1:
               System.out.print("\tEnter genre: ");
               String genre = in.readLine();
               query = String.format("SELECT * FROM Catalog WHERE genre = '%s'", genre);
               break;
                 case 2:
               System.out.print("\tEnter maximum price: ");
               String price = in.readLine();
               query = String.format("SELECT * FROM Catalog Where price = '%s'", price);
               break;
                 case 3:
               query = "SELECT * FROM Catalog ORDER BY price DESC";
               break;
                 case 4:
               query = "SELECT * FROM Catalog ORDER BY price ASC";
               break;
            default:
               System.out.println("Unrecognized Choice!");
               return;
         }
         esql.executeQueryAndPrintResult(query);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

  public static void placeOrder(GameRental esql, String username) {
       try {
            System.out.print("\tEnter Game ID: ");
            String gameID = in.readLine();
            System.out.print("\tEnter number of units: ");
            int unitsOrdered = Integer.parseInt(in.readLine());

            // Generate unique rentalOrderID
            String rentalOrderID = " " + System.currentTimeMillis();
            // Calculate total price
            String priceQuery = String.format("SELECT price FROM Catalog WHERE gameID = '%s'", gameID);
            List<List<String>> priceResult = esql.executeQueryAndReturnResult(priceQuery);
            double totalPrice = unitsOrdered * Double.parseDouble(priceResult.get(0).get(0));
            // Get current timestamp and due date (7 days later)
            String orderTimestamp = "now()";
            String dueDate = "now() + interval '7 days'";

            // Insert into RentalOrder
            String insertOrderQuery = String.format("INSERT INTO RentalOrder (rentalOrderID, login, noOfGames, totalPrice, orderTimestamp, dueDate) VALUES ('%s', '%s', %d, %f, %s, %s)", rentalOrderID, username, unitsOrdered, totalPrice, orderTimestamp, dueDate);
            esql.executeUpdate(insertOrderQuery);

            // Insert into GamesInOrder
            String insertGamesQuery = String.format("INSERT INTO GamesInOrder (rentalOrderID, gameID, unitsOrdered) VALUES ('%s', '%s', %d)", rentalOrderID, gameID, unitsOrdered);
            esql.executeUpdate(insertGamesQuery);

            // Generate unique trackingID
            System.out.print("\tEnter your unique tracking ID: ");
            String trackingID = in.readLine();
            // Insert into TrackingInfo
            String insertTrackingQuery = String.format("INSERT INTO TrackingInfo (trackingID, rentalOrderID, status, currentLocation, courierName, lastUpdateDate) VALUES ('%s', '%s', 'Pending', 'Warehouse', 'Courier Service', %s)", trackingID, rentalOrderID, orderTimestamp);
            esql.executeUpdate(insertTrackingQuery);

            System.out.println("Order successfully placed!");

      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

 public static void viewAllOrders(GameRental esql, String username) {
      try {
         String query = String.format("SELECT * FROM RentalOrder WHERE login = '%s'", username);
         esql.executeQueryAndPrintResult(query);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static void viewRecentOrders(GameRental esql, String username) {
       try {
         String query = String.format("SELECT * FROM RentalOrder WHERE login = '%s' ORDER BY dueDate DESC LIMIT 5", username);
         esql.executeQueryAndPrintResult(query);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static void viewOrderInfo(GameRental esql, String username) {
      try {
         System.out.print("Enter Rental Order ID: ");
         String rentalOrderID = in.readLine();
         String query = String.format("SELECT * FROM GamesInOrder WHERE rentalOrderID = '%s'", rentalOrderID, username);
         esql.executeQueryAndPrintResult(query);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static void viewTrackingInfo(GameRental esql, String username) {
      try {
         System.out.print("Enter Tracking ID: ");
         String trackingID = in.readLine();
         String query = String.format("SELECT * FROM TrackingInfo WHERE trackingID = '%s'", trackingID, username);
         esql.executeQueryAndPrintResult(query);
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static void updateTrackingInfo(GameRental esql) {
      try {
         System.out.print("\tEnter Tracking ID: ");
         String trackingID = in.readLine();
         System.out.print("\tEnter new status: ");
         String status = in.readLine();
         System.out.print("\tEnter new courier name: ");
         String courierName = in.readLine();
         System.out.print("\tEnter additional comments: ");
         String additionalComments = in.readLine();

         String query = String.format("UPDATE TrackingInfo SET status = '%s', courierName = '%s', additionalComments = '%s' WHERE trackingID = '%s'", status, courierName, additionalComments, trackingID);
         esql.executeUpdate(query);
         System.out.println("Tracking information updated successfully!");
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static void updateCatalog(GameRental esql) {
      try {
         System.out.print("Enter game ID: ");
         String gameID = in.readLine();
         System.out.print("Enter new price: ");
         double price = Double.parseDouble(in.readLine());
         System.out.print("Enter description: ");
         String descrip = in.readLine();

         String query = String.format("UPDATE Catalog SET description = '%s', price = '%s' WHERE gameID = '%s'", descrip, price, gameID);
         esql.executeUpdate(query);
         System.out.println("Catalog updated successfully!");
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   
   }

   public static void updateUser(GameRental esql) {
      try {
         System.out.print("Enter username: ");
         String username = in.readLine();
         System.out.print("Enter new role: ");
         String role = in.readLine();
         System.out.print("Enter new number of Overdue Games: ");
         int numOverdueGames = Integer.parseInt(in.readLine());

         String query = String.format("UPDATE Users SET role = '%s', numOverDueGames = %d WHERE login = '%s'", role, numOverdueGames, username);
         esql.executeUpdate(query);
         System.out.println("User information has been updated!");
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

}//end GameRental

