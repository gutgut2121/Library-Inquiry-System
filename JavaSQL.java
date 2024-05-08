
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

        
public class JavaSQL {
    public static void main(String[] args) {
        // TODO code application logic here
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db40";
        String dbUsername = "Group40";
        String dbPassword = "CSCI3170";
        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress,dbUsername,dbPassword);
            System.out.println("Welcome to Library Inquiry System!");
            main_menu(con);  
            con.close();
        }
        catch (ClassNotFoundException e){
            System.out.println("[Error]: Java Mysql DB Driver not found!!");
            System.exit(0);
        }
        catch (SQLException e){
            System.out.println(e);
        }   
    }
    
    public static void main_menu(Connection con){
        int user_choice;
        System.out.println("\n-----Main menu-----");     
        System.out.println("What kinds of operations would you like to perform?");
        System.out.println("1. Operations for Administrator");
        System.out.println("2. Operations for Library User");
        System.out.println("3. Operations for Librarian");
        System.out.println("4. Exit this program");
        Scanner scan_main_menu = new Scanner(System.in);
        OUTER:
        do {
            System.out.println("Enter Your Choice: ");
            user_choice = scan_main_menu.nextInt(); 
            switch (user_choice) {
                case 1: admin_menu(con);
                case 2: libraryUser_menu(con);
                case 3: librarian_menu(con);
                case 4: {
                    System.out.println("GoodBye!");
                    System.exit(1);
                }
                default: {
                    System.out.println("Please enter 1,2,3,4");
                }
            }
        }while (user_choice <1 || user_choice>4); 
    }
    
    public static void admin_menu(Connection con){
        int admin_choice;
        System.out.println("\n-----Operations for administrator menu-----");
        System.out.println("What kinds of operations would you like to perform?");
        System.out.println("1. Create all tables");
        System.out.println("2. Delete all tables");
        System.out.println("3. Load from datafile");
        System.out.println("4. Show number of records in each table");
        System.out.println("5. Return to the main menu");
        Scanner scan_admin_menu = new Scanner(System.in);
        do {
           System.out.print("Enter Your Choice: ");
           admin_choice = scan_admin_menu.nextInt();
           }while (admin_choice < 1 || admin_choice > 5);
        if(admin_choice == 1)
        {
            createALLtables(con);
            admin_menu(con);
        }
        else if(admin_choice == 2)
        {
            deleteALLtables(con);
            admin_menu(con);
        }
        else if(admin_choice == 3)
        {
            loadFROMdatafile(con);
            admin_menu(con);
        }
        else if(admin_choice == 4)
        {
            recordINeachTABLE(con);
            admin_menu(con);
        }
        else if(admin_choice == 5)
        {
            main_menu(con);
        }
    }
    
    public static void createALLtables(Connection con){
        System.out.println("Processing...");
        try{
             //create table User Categories
            String sql_uc = "CREATE TABLE user_category(" + "ucid integer(1) primary key,"
            + "max integer(2) not null," + "period integer(2) not null)";
            Statement stmt_uc = con.createStatement();
            stmt_uc.executeUpdate(sql_uc);
            
            //create table Library Users
            String sql_lu = "CREATE TABLE libuser(" + "libuid varchar(10) primary key,"
            + "name varchar(25) not null," + "age integer(3) not null," 
                    + "address varchar(100) not null," + "ucid integer(1) not null)";
            Statement stmt_lu = con.createStatement();
            stmt_lu.executeUpdate(sql_lu);
             
            //create table Book Categories
            String sql_bc = "CREATE TABLE book_category(" + "bcid integer(1) primary key,"
            + "bcname varchar(30) not null)";
            Statement stmt_bc = con.createStatement();
            stmt_bc.executeUpdate(sql_bc);
            
            //create table Books
            String sql_b = "CREATE TABLE book(" + "callnum varchar(8) primary key,"
             + "title varchar(30) not null," 
            + "publish varchar(10)," +
            "rating varchar(5)," + "tborrowed integer(2) not null," + "bcid integer(1) not null)";
            Statement stmt_b = con.createStatement();
            stmt_b.executeUpdate(sql_b);
            
            //create table Book Copies
            String sql_c = "CREATE TABLE copy(" + "callnum varchar(8) not null,"
            + "copynum integer(1) not null," + "PRIMARY KEY(callnum,copynum),"
            + "FOREIGN KEY(callnum) REFERENCES book(callnum))";
            Statement stmt_c = con.createStatement();
            stmt_c.executeUpdate(sql_c);
            
            //create table Authors
            String sql_a = "CREATE TABLE authorship(" + "aname varchar(25) not null,"
            + "callnum varchar(8) not null," + "PRIMARY KEY(aname,callnum),"
            + "FOREIGN KEY(callnum) REFERENCES book(callnum))";
            Statement stmt_a = con.createStatement();
            stmt_a.executeUpdate(sql_a);
            
            //create table Checked-Out Records
            String sql_borrow = "CREATE TABLE borrow(" + "callnum varchar(8) not null," 
            + "copynum integer(1) not null," + "libuid varchar(10) not null,"
            + "checkout varchar(10) not null," + "returnd varchar(10),"
            + "PRIMARY KEY(callnum,copynum,libuid,checkout),"
            + "FOREIGN KEY(callnum,copynum) REFERENCES copy(callnum,copynum))";
            // + "FOREIGN KEY(libuid) REFERENCES libuser(libuid),";
            Statement stmt_borrow = con.createStatement();
            stmt_borrow.executeUpdate(sql_borrow);

            System.out.print("Done. Database is initialized.");       
        }
        catch(SQLException e){
            System.out.println("Error:"+e);
        }
        
    }
    
    public static void deleteALLtables(Connection con){
        System.out.println("Processing...");
        try{
            //drop table User Categories
            String sql_uc_del = "DROP TABLE IF EXISTS user_category";
            Statement stmt_uc_del = con.createStatement();
            stmt_uc_del.executeUpdate(sql_uc_del);
            
            //drop table Library Users
            String sql_lu_del = "DROP TABLE IF EXISTS libuser";
            Statement stmt_lu_del = con.createStatement();
            stmt_lu_del.executeUpdate(sql_lu_del);
            
            //drop table Book Categories
            String sql_bc_del = "DROP TABLE IF EXISTS book_category";
            Statement stmt_bc_del = con.createStatement();
            stmt_bc_del.executeUpdate(sql_bc_del);
            
            //drop table Authors
            String sql_a_del = "DROP TABLE IF EXISTS authorship";
            Statement stmt_a_del = con.createStatement();
            stmt_a_del.executeUpdate(sql_a_del);
            
            //drop table Checked-Out Records
            String sql_borrow_del = "DROP TABLE IF EXISTS borrow";
            Statement stmt_borrow_del = con.createStatement();
            stmt_borrow_del.executeUpdate(sql_borrow_del);  

            //drop table Book Copies
            String sql_c_del = "DROP TABLE IF EXISTS copy";
            Statement stmt_c_del = con.createStatement();
            stmt_c_del.executeUpdate(sql_c_del);

            //drop table Books
            String sql_b_del = "DROP TABLE IF EXISTS book";
            Statement stmt_b_del = con.createStatement();
            stmt_b_del.executeUpdate(sql_b_del);

            System.out.print("Done. Database is removed.");
        } 
        catch(SQLException e){
            System.out.println("Error:"+e);
        }
    }
    
    public static void loadFROMdatafile(Connection con){
        Scanner scan = new Scanner(System.in);
        System.out.print("Type in the Source Data Folder Path:");
        String path = scan.nextLine();
        try{
            System.out.println("Processing...");
            //input data to table User Categories
            File file_uc = new File(path + "/" + "user_category.txt");
            Scanner scan_uc = new Scanner(file_uc);
            PreparedStatement pstmt_uc = con.prepareStatement("INSERT INTO user_category (ucid,max,period) VALUES (?,?,?)");
            while (scan_uc.hasNextLine()) {
                String data = scan_uc.nextLine();
                String[] attribute = data.split("\t");
                for(int i=0;i<attribute.length;i++)
                    pstmt_uc.setString(i+1,attribute[i]);
                pstmt_uc.execute();
            }
            
            //input data to table Library Users
            File file_lu = new File(path + "/" + "user.txt");
            Scanner scan_lu = new Scanner(file_lu);
            PreparedStatement pstmt_lu = con.prepareStatement("INSERT INTO libuser (libuid,name,age,address,ucid) VALUES (?,?,?,?,?)");
            while (scan_lu.hasNextLine()) {
                String data = scan_lu.nextLine();
                String[] attribute = data.split("\t");
                for(int i=0;i<attribute.length;i++)
                   pstmt_lu.setString(i+1,attribute[i]);
                pstmt_lu.execute();  
            }
            
            //input data to table Book Categories
            File file_bc = new File(path + "/" + "book_category.txt");
            Scanner scan_bc = new Scanner(file_bc);
            PreparedStatement pstmt_bc = con.prepareStatement("INSERT INTO book_category (bcid,bcname) VALUES (?,?)");
            while (scan_bc.hasNextLine()) {
                String data = scan_bc.nextLine();
                String[] attribute = data.split("\t");
                for(int i=0;i<attribute.length;i++)
                   pstmt_bc.setString(i+1,attribute[i]);
                pstmt_bc.execute();  
            }

             //input data to table Books 
             File file_b = new File(path + "/" + "book.txt");
             Scanner scan_b = new Scanner(file_b);
             PreparedStatement pstmt_b = con.prepareStatement("INSERT INTO book (callnum,title,publish,rating,tborrowed,bcid) VALUES (?,?,?,?,?,?)");
             while (scan_b.hasNextLine()) {
                 String data = scan_b.nextLine();
                 String[] attribute = data.split("\t");
                 pstmt_b.setString(1,attribute[0]);
                 pstmt_b.setString(2,attribute[2]);
                 pstmt_b.setString(3,attribute[4]);
                 pstmt_b.setString(4,attribute[5]);
                 pstmt_b.setString(5,attribute[6]);
                 pstmt_b.setString(6,attribute[7]);
                 pstmt_b.execute(); 
                 }

            //input data to table Book Copies
            File file_copy = new File(path + "/" + "book.txt");
            Scanner scan_copy = new Scanner(file_copy);
            PreparedStatement pstmt_copy = con.prepareStatement("INSERT INTO copy (callnum,copynum) VALUES (?,?)");
            while (scan_copy.hasNextLine()) {
                String data = scan_copy.nextLine();
                String[] attribute = data.split("\t");
                int noOfcopy = Integer.parseInt(attribute[1]);
                for(int i = 1;i <= noOfcopy ;i++){
                    pstmt_copy.setString(1,attribute[0]);
                    String x = String.valueOf(i);
                    pstmt_copy.setString(2,x);
                    pstmt_copy.execute();
                } 
            }

             //input data to table Authors
             File file_a = new File(path + "/" + "book.txt");
             Scanner scan_a = new Scanner(file_a);
             PreparedStatement pstmt_a = con.prepareStatement("INSERT INTO authorship (aname,callnum) VALUES (?,?)");
             while (scan_a.hasNextLine()) {
                 String data = scan_a.nextLine();
                 String[] attribute = data.split("\t");
                 String[] attribute_author = attribute[3].split(",");
                 for(int i=0;i<attribute_author.length;i++){
                     pstmt_a.setString(1,attribute_author[i]);
                     pstmt_a.setString(2,attribute[0]);
                     pstmt_a.execute(); 
                 }
             }

              //input data to table Checked-Out Records
            File file_borrow = new File(path + "/" + "check_out.txt");
            Scanner scan_borrow = new Scanner(file_borrow);
            PreparedStatement pstmt_borrow = con.prepareStatement("INSERT INTO borrow (callnum,copynum,libuid,checkout,returnd) VALUES (?,?,?,?,?)");
            while (scan_borrow.hasNextLine()) {
                String data = scan_borrow.nextLine();
                String[] attribute = data.split("\t");
                for(int i=0;i<attribute.length;i++)
                   pstmt_borrow.setString(i+1,attribute[i]);
                pstmt_borrow.execute();   
            }

            System.out.println("Done. Data is inputted to the database.");
        }
        catch(FileNotFoundException | SQLException e){
            System.out.println("Error: " + e);
        }
    }
    
    public static void recordINeachTABLE(Connection con){
        try{
       //Show  the number of record in table User Categories
        System.out.println("Number of records in each table:");
        String sql_uc_record = "SELECT COUNT(*) FROM user_category";
        Statement stmt_uc_record = con.createStatement();
        ResultSet result_uc = stmt_uc_record.executeQuery(sql_uc_record);
        int count_uc = 0;
        while(result_uc.next()){
            count_uc= result_uc.getInt("COUNT(*)");
        }
        System.out.println("user_category: "+count_uc);
        
        //Show  the number of record in table Library Users
        String sql_lu_record = "SELECT COUNT(*) FROM libuser";
        Statement stmt_lu_record = con.createStatement();
        stmt_lu_record.executeQuery(sql_lu_record);
        ResultSet result_lu = stmt_lu_record.executeQuery(sql_lu_record);
        int count_lu = 0;
        while(result_lu.next()){
            count_lu= result_lu.getInt("COUNT(*)");
        }
        System.out.println("libuser: "+count_lu);
        
        //Show  the number of record in table Book Categories
        String sql_bc_record = "SELECT COUNT(*) FROM book_category";
        Statement stmt_bc_record = con.createStatement();
        stmt_bc_record.executeQuery(sql_bc_record);
        ResultSet result_bc = stmt_uc_record.executeQuery(sql_bc_record);
        int count_bc = 0;
        while(result_bc.next()){
            count_bc= result_bc.getInt("COUNT(*)");
        }
        System.out.println("book_category: "+count_bc);
        
        //Show  the number of record in table Books
        String sql_b_record = "SELECT COUNT(*) FROM book";
        Statement stmt_b_record = con.createStatement();
        stmt_b_record.executeQuery(sql_b_record);
        ResultSet result_b = stmt_b_record.executeQuery(sql_b_record);
        int count_b = 0;
        while(result_b.next()){
            count_b= result_b.getInt("COUNT(*)");
        }
        System.out.println("book: "+count_b);
        
        //Show  the number of record in table Book Copies
        String sql_copy_record = "SELECT COUNT(*) FROM copy";
        Statement stmt_copy_record = con.createStatement();
        stmt_copy_record.executeQuery(sql_copy_record);
        ResultSet result_copy = stmt_copy_record.executeQuery(sql_copy_record);
        int count_copy = 0;
        while(result_copy.next()){
            count_copy= result_copy.getInt("COUNT(*)");
        }
        System.out.println("copy: "+count_copy);
        
        //Show  the number of record in table Authors
        String sql_a_record = "SELECT COUNT(*) FROM authorship";
        Statement stmt_a_record = con.createStatement();
        stmt_a_record.executeQuery(sql_a_record);
        ResultSet result_a = stmt_a_record.executeQuery(sql_a_record);
        int count_a = 0;
        while(result_a.next()){
            count_a= result_a.getInt("COUNT(*)");
        }
        System.out.println("authorship: "+count_a);
        
        //Show  the number of record in table Records
        String sql_borrow_record = "SELECT COUNT(*) FROM borrow";
        Statement stmt_borrow_record = con.createStatement();
        stmt_borrow_record.executeQuery(sql_borrow_record);
        ResultSet result_borrow = stmt_borrow_record.executeQuery(sql_borrow_record);
        int count_borrow = 0;
        while(result_borrow.next()){
            count_borrow= result_borrow.getInt("COUNT(*)");
        }
        System.out.println("borrow: "+count_borrow);
        }
        catch(SQLException e){
             System.out.println("Error: " + e);
        }
    }
        
	public static void libraryUser_menu(Connection con){
		System.out.println("\n-----Operations for library user menu-----");
		System.out.println("What kinds of operations would you like to perform?");
		System.out.println("1. Search for books");
		System.out.println("2. Show checkout records of a library user");
		System.out.println("3. Return to the main menu");
		int input;
		Scanner scan = new Scanner(System.in);
		do {
		    System.out.print("Enter Your Choice: ");
		    input = scan.nextInt();
		} while (input < 1 || input > 3);
		if (input == 1)
		    searchBook(con);
		else if (input == 2)
		    userRecord(con);
		else if (input == 3)
		    main_menu(con);
		libraryUser_menu(con);  
		} 
	
	public static void searchBook(Connection con) {
		String callnumb, searchWord ;
		int input; int columns = 10000;
		System.out.println("Choose the search criteria:");
		System.out.println("1. Call Number");
		System.out.println("2. Title");
		System.out.println("3. Author");
		Scanner scan = new Scanner(System.in);
		
		String sql_count;
        PreparedStatement pstmt_count;
		
		// the no. of record in book
		try{
		    sql_count = "SELECT COUNT(*) FROM book";
		    pstmt_count = con.prepareStatement(sql_count);
		    ResultSet rs = pstmt_count.executeQuery();
		    rs.next();
		    columns = rs.getInt("count(*)");
		}
		catch (Exception e){
			System.out.println("An error had occured: " + e);
		}
		// Input the searching criteria
		do {
		    System.out.print("Choose the search criteria: ");
		    input = scan.nextInt();
		} while (input < 1 || input > 3);
		Boolean get_result = false; 
		try {
		// Searching keyword
		    System.out.print("Type in the search keyword: ");
		    Scanner keyword = new Scanner(System.in);
		    String sql_search;  
		    PreparedStatement pstmt_search;  
		    // Search based on method
		if (input == 1) {
			callnumb = keyword.nextLine();
			sql_search = "SELECT * FROM book, authorship, book_category WHERE book.bcid = book_category.bcid AND book.callnum = authorship.callnum AND book.callnum = ? ORDER BY book.callnum ASC";
			pstmt_search = con.prepareStatement(sql_search);
			pstmt_search.setString(1, callnumb);
		} else if (input == 2) {
			searchWord = keyword.nextLine();
			sql_search = "SELECT * FROM book, authorship, book_category WHERE book.bcid = book_category.bcid AND book.callnum = authorship.callnum AND book.title LIKE BINARY ? ORDER BY book.callnum ASC";
			pstmt_search = con.prepareStatement(sql_search);
			searchWord = "%" + searchWord + "%";
			pstmt_search.setString(1, searchWord);
		} else {
			searchWord = keyword.nextLine();
			sql_search = "SELECT * FROM book, authorship, book_category WHERE book.bcid = book_category.bcid AND book.callnum = authorship.callnum AND authorship.aname LIKE BINARY ? ORDER BY book.callnum ASC";
			pstmt_search = con.prepareStatement(sql_search);
			searchWord = "%" + searchWord + "%";
			pstmt_search.setString(1, searchWord);
		}
        
        ResultSet rs = pstmt_search.executeQuery();
        System.out.println("| Call Num | Title | Book Category | Author | Rating | Available No. of Copy |");
        int copyResult = 0;
        String callResult = "", titleResult = "", bookCatResult= "", authorResult = "", ratingResult = "";
        String[][] resultSet = new String[columns][5];
        int[] copyNumSet = new int[columns];
        int count = 0;
        while (rs.next()) {
        get_result = true;
        Boolean contained = false;
        String call_numb = rs.getString("callnum");
		
		//Record the author by searching callnum and title
		if (input == 1 || input == 2) {
        
		for(int i=0; i<count; i++){
            if(resultSet[i][0].equals(call_numb)){
        	    contained = true;
        	    resultSet[i][2] = resultSet[i][2] + ", " + rs.getString("aname");
          }
        }
        if(contained)
          continue;       
	        resultSet[count][2] = rs.getString("aname");
		
		//Record the author by searching author	
		}else if (input == 3 ){
			
			resultSet[count][2]="";
			
		    String sqlstmt = "SELECT * FROM authorship where callnum = ? ";
			PreparedStatement authorstmt = con.prepareStatement(sqlstmt);
			authorstmt.setString(1, call_numb);
			ResultSet authorset = authorstmt.executeQuery();
			
			int countauthor = 0;
			while(authorset.next()){
				
				resultSet[count][2] = resultSet[count][2] + ", " + authorset.getString("aname");			
			}
			resultSet[count][2] = resultSet[count][2].substring(1);
		}
        // Record result
        resultSet[count][0] = call_numb;
        resultSet[count][1] = rs.getString("title");
		resultSet[count][3] = rs.getString("rating");
		resultSet[count][4] = rs.getString("bcname");
        
		callResult = call_numb;
        
		// Count the number of copies
		String sql_copy = "SELECT count(copynum) FROM copy WHERE callnum = ? group by callnum";
        PreparedStatement copyPstmt = con.prepareStatement(sql_copy);
        copyPstmt.setString(1, callResult);
        ResultSet copySet = copyPstmt.executeQuery();
        if(copySet.next()){
          copyNumSet[count] = copySet.getInt("count(copynum)");
        }
		
        // Count the number of unreturn book
        String sql_un = "SELECT count(returnd) FROM borrow WHERE returnd = 'null' and callnum = ? group by callnum";
        PreparedStatement pstmt_un = con.prepareStatement(sql_un);
        pstmt_un.setString(1, callResult);
        ResultSet set_un = pstmt_un.executeQuery();
        if(set_un.next()){
          int returnedbk = set_un.getInt("count(returnd)");
          copyNumSet[count] -= returnedbk;
        }
		
		count += 1;
        } //End while loop
		
		//Get the result
        if (!get_result)
        throw new Exception("no output");
        else{
        for(int i=0; i<count; i++)
          System.out.println("| " + resultSet[i][0] + " | " + resultSet[i][1] + " | " + resultSet[i][2] + " | " + resultSet[i][3] + " | " + resultSet[i][4] + " | " + copyNumSet[i] + "  |");
		  
		}
        } catch (Exception e) {
        System.out.println("An error had occured: " + e);
        }
		System.out.println("End of Query");
        }
	
	public static void userRecord(Connection con) {
		String user_id;
		try {
			//Type in ID
		  System.out.print("Enter The User ID: ");
		  Scanner scan = new Scanner(System.in);
		  String sqlState;
		  PreparedStatement pstmt;
		  // Search borrow book
		  user_id = scan.nextLine();
		  sqlState = "SELECT borrow.libuid, borrow.callnum, authorship.aname, book.title, copy.copynum, borrow.returnd, STR_TO_DATE(borrow.checkout, '%d/%m/%Y') AS checkout FROM " + "book, copy, authorship, borrow WHERE "
			  + "book.callnum = copy.callnum AND " + "book.callnum = authorship.callnum AND "
			  + "authorship.callnum = copy.callnum AND " + "borrow.callnum = book.callnum AND borrow.callnum = copy.callnum AND borrow.callnum = authorship.callnum AND borrow.copynum = copy.copynum AND "
			  + "borrow.libuid = ?" +"ORDER BY checkout DESC";
		  //prepare statement
		  pstmt = con.prepareStatement(sqlState);
		  pstmt.setString(1, user_id);

		  // Show the result
		  ResultSet rs = pstmt.executeQuery();
		  System.out.println("Loan Record");
		  System.out.println("| Call Number | Copy Number | Title | Author | Check-out | Returned? |");
		  Boolean get_result = false;
		  int copyResult = 0;
		  String titleResult = "", authorResult = "", callResult = "", checkoutResult = "", returnResult = "";
		  //Print out result
		  while (rs.next()) {
			get_result = true;
			String callTemp = rs.getString("callnum");
			if (callTemp.equals(callResult)) {
			  authorResult = authorResult + ", " + rs.getString("aname");
			} else {
			  if (!callResult.equals("")){
				System.out.println("| " + callResult + " | " + copyResult + " | " + titleResult + " | " + authorResult
					+ " | " + checkoutResult + " | " + returnResult + "  |");
				
			  }
			  callResult = callTemp;
			  copyResult = rs.getInt("copynum");
			  titleResult = rs.getString("title");
			  authorResult = rs.getString("aname");
			  checkoutResult = rs.getString("checkout");
			  returnResult = rs.getString("returnd");
			  // Whether user return books
			  if (returnResult.equals("null")){
			   returnResult = "No";}
			  else{
			   returnResult = "Yes";}
			}
		  }
		  //Print out result
		  if (!get_result){
			throw new Exception("no output");}
		  else{
			System.out.println("| " + callResult + " | " + copyResult + " | " + titleResult + " | " + authorResult + " | "
				+ checkoutResult + " | " + returnResult + "  |");
			
		  }
		} catch (Exception e) {
		  System.out.println("An error had occured: " + e);
		}
		System.out.println("End of Query");
    } 
    
    // Librarian operation
    public static void librarian_menu(Connection con){
        System.out.println("\n-----Operations for librarian menu-----");
        System.out.println("What kinds of operations would you like to perform?");
        System.out.println("1. Book Borrowing");
        System.out.println("2. Book Returning");
        System.out.println("3. List all un-returned book copies which are checked-out within a period");
        System.out.println("4. Return to the main menu");
        int input;
        Scanner scan = new Scanner(System.in);
        do {
          System.out.print("Enter Your Choice: ");
          input = scan.nextInt();
        } while (input < 1 || input > 4);
        if (input == 1)
          bookBorrowing(con);
        else if (input == 2)
          bookReturning(con);
        else if (input == 3)
          listAllUnreturnedBooks(con);
        else if (input == 4)
          main_menu(con);
        librarian_menu(con);
    } 
    
    public static void bookBorrowing(Connection con) {
        // allow input the user id, call number and copy number
        String user_id;
        String call_number;
        int copy_number;
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter The User ID: ");
        user_id = scan.nextLine();
        System.out.print("Enter The Call Number: ");
        call_number = scan.nextLine();
        System.out.print("Enter The Copy Number: ");
        copy_number = scan.nextInt();
        
        // check whether the book copy is available to be borrowed
        String sql_check;
        PreparedStatement pstmt_check;
        try {
            sql_check = "SELECT * FROM borrow WHERE callnum = ? AND copynum = ? AND return = 'null'";
            pstmt_check = con.prepareStatement(sql_check);
            pstmt_check.setString(1, call_number);
            pstmt_check.setInt(2, copy_number);
            ResultSet rs_check = pstmt_check.executeQuery();

            // If the result is empty, the book can be borrowed
            if (!rs_check.next()) {
                // Insert a new checkout record 
                String sql_borrow;
                PreparedStatement pstmt_borrow;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate today = LocalDate.now();
                sql_borrow = "INSERT INTO borrow (callnum, copynum, libuid, checkout, return) VALUES (?, ?, ?, ?, 'null')";
                pstmt_borrow = con.prepareStatement(sql_borrow);
                pstmt_borrow.setString(1, call_number);
                pstmt_borrow.setInt(2, copy_number);
                pstmt_borrow.setString(3, user_id);
                pstmt_borrow.setString(4, dtf.format(today));

                pstmt_borrow.execute();
                // Informative messages
                System.out.println("Book borrowing performed successfully.");
            } else {
                System.out.println("The Book is not available to be borrowed.");
            }
        } catch (Exception e) {
            System.out.println("An Error had occured: " + e);
        }
    }
    
    public static void bookReturning(Connection con) {
        // allow input the user id, call number and copy number
        String user_id;
        String call_number;
        int copy_number;
        int rating;
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter The User ID: ");
        user_id = scan.nextLine();
        System.out.print("Enter The Call Number: ");
        call_number = scan.nextLine();
        System.out.print("Enter The Copy Number: ");
        copy_number = scan.nextInt();
        System.out.print("Enter Your Rating of the Book: ");
        rating = scan.nextInt();
        
        // check if the record exists
        String sql_check;
	    PreparedStatement pstmt_check;
	    try {
	        sql_check = "SELECT * FROM borrow WHERE libuid = ? AND callnum = ? AND copynum = ? AND return = 'null'";
            pstmt_check = con.prepareStatement(sql_check);
            pstmt_check.setString(1, user_id);
            pstmt_check.setString(2, call_number);
            pstmt_check.setInt(3, copy_number);
            ResultSet rs_check = pstmt_check.executeQuery();
            // If the result exists, return the book
            if (rs_check.next()) {
                // return date is updated to current date
                String sql_return;
                PreparedStatement pstmt_return;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate today = LocalDate.now();
                sql_return = "UPDATE borrow SET return = ? WHERE libuid = ? AND callnum = ? AND copynum = ?";
                pstmt_return = con.prepareStatement(sql_return);
                pstmt_return.setString(1, dtf.format(today));
                pstmt_return.setString(2, user_id);
                pstmt_return.setString(3, call_number);
                pstmt_return.setInt(4, copy_number);
                pstmt_return.execute();
                
                // update the rating of the book
                String sql_rating;
                PreparedStatement pstmt_rating;
                float new_rating;
                sql_rating = "SELECT rating, tborrowed FROM book WHERE callnum = ? AND copynum = ?";
                pstmt_rating = con.prepareStatement(sql_rating);
                pstmt_rating.setString(1, call_number);
                pstmt_rating.setInt(2, copy_number);
                ResultSet rs_rating = pstmt_rating.executeQuery();
                int tborrowed = -10;
                float old_rating = -10;
                while (rs_rating.next()) {
                    tborrowed = rs_rating.getInt("tborrowed");
                    old_rating = rs_rating.getFloat("rating");
                }
                new_rating = (old_rating*tborrowed + rating)/(tborrowed+1);
                
                String sql_updateRating;
                PreparedStatement pstmt_updateRating;
                sql_updateRating = "UPDATE book SET rating = ? WHERE callnum = ? AND copynum = ?";
                pstmt_updateRating = con.prepareStatement(sql_updateRating);
                pstmt_updateRating.setFloat(1, new_rating);
                pstmt_updateRating.setString(2, call_number);
                pstmt_updateRating.setInt(3, copy_number);
                pstmt_updateRating.execute();
                
                // increase the number of times borrowed by one
                String sql_updatetborrowed;
                PreparedStatement pstmt_updatetborrowed;
                sql_updatetborrowed = "UPDATE book SET tborrowed = ? WHERE callnum = ? AND copynum = ?";
                pstmt_updatetborrowed= con.prepareStatement(sql_updatetborrowed);
                int new_tborrowed = tborrowed + 1;
                pstmt_updatetborrowed.setInt(1, new_tborrowed);
                pstmt_updatetborrowed.setString(2, call_number);
                pstmt_updatetborrowed.setInt(3, copy_number);
                pstmt_updatetborrowed.execute();
                
                // Informative message
                System.out.println("Book returning performed successfully.");
            } else {
                // The record does not exist
                System.out.println("The record does not exist. Book returning failed to perform.");
            }
        } catch (Exception e) {
            System.out.println("An error had occured: " + e);
        }
    }
    
    public static void listAllUnreturnedBooks(Connection con) {
        // list all un-returned book copies which are checked-out within a period
        String start_date;
        String end_date;
        int nrow = 10;
        
        // get the no. of record in borrow
        try {
            String sql = "SELECT COUNT(*) FROM borrow";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            nrow = rs.getInt("count(*)");
        }
        catch (Exception e){
            System.out.println("An error had occured: " + e);
        }
        
        Scanner scan = new Scanner(System.in);
        System.out.print("Type in the starting date [dd/mm/yyyy]: ");
        start_date = scan.nextLine();
        System.out.print("Type in the ending date [dd/mm/yyyy]: ");
        end_date = scan.nextLine();

        // Get unreturn record and store it in an array
        String sql_unreturn;
        PreparedStatement pstmt_unreturn;
        String[][] result = new String[nrow][4];
        int row = 0;
        try {
            sql_unreturn = "SELECT libuid, callnum, copynum, checkout FROM borrow WHERE return = 'null' ORDER BY checkout DESC;";
            pstmt_unreturn = con.prepareStatement(sql_unreturn);
            ResultSet rs_unreturn = pstmt_unreturn.executeQuery();
            
            Boolean no_result = true;
            while (rs_unreturn.next()) {
                // store the record that is within the specified period
                String date = rs_unreturn.getString("checkout");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Boolean check_date = sdf.parse(date).before(sdf.parse(end_date));
                check_date = check_date && sdf.parse(date).after(sdf.parse(start_date));
                if(check_date){
                    no_result = false;
                    result[row][0] = rs_unreturn.getString("libuid");
                    result[row][1] = rs_unreturn.getString("callnum");
                    result[row][2] = rs_unreturn.getString("copynum");
                    result[row][3] = date;
                    row += 1;
                }
            }

            if(no_result){
                System.out.println("No record found in the specified period");
            } else {
                // print the result
                System.out.println("| LibUID | CallNum | CopyNum | Checkout |");
                for(int i=0; i<row; i++) {
                    System.out.println("| " + result[i][0] + " | " + result[i][1] + " | " + result[i][2] + " | " + result[i][3] + " | ");
                }
                System.out.println("End of Query");
            }
        } catch (Exception e) {
            System.out.println("An error had occured: " + e);
        }
    }
}
