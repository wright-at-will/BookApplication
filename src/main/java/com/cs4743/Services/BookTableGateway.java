package com.cs4743.Services;

import com.cs4743.Controller.MenuController;
import com.cs4743.Model.Book;
import com.mysql.cj.jdbc.ConnectionGroup;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BookTableGateway {

    private static Logger logger = LogManager.getLogger(MenuController.class);
    private static MysqlDataSource ds = null;
    private static Connection conn = null;
    private static PreparedStatement stmt = null;
    private static ResultSet rs = null;

    private static int INSERT = 0;
    private static int UPDATE = 1;
    private static int BOOK = 2;
    private static int BOOKLIST = 3;
    private static int DELETE = 4;

    static String url = "jdbc:mysql://easel2.fulgentcorp.com/yby805" +
            "?" +
            "db=yby805" +
            "&useLegacyDatetimeCode=false&useTimezone=true&serverTimezone=GMT ";
    static String user = "yby805";
    static String password = "y9X8yYS2ZsFsuK1Xlzgj";

    //TODO Change queires so that they can take the right keys and values
    private static String[] queries =
            {
                    "INSERT INTO `Book`" +
                            "() " +
                            "VALUES ()",
                    "UPDATE `Book` SET" +
                            "`title`=?,`summary`=?,`year_published`=?,`isbn`=?" +
                            "WHERE `id` = ?",
                    "SELECT * FROM Book WHERE id = ?",
                    "SELECT id, title FROM Book",
                    "DELETE FROM `Book` WHERE `id` = ?"
            };

    //TODO Create should create the query inside it according to the parts of the book that have been filled out
    public static int update(Book book){
        int bookID = book.getBookID();
        if(bookID < 1){
            return create(book);
        }
        ArrayList<String> filledFields = new ArrayList<>();
        ArrayList<Object> params = new ArrayList<>();
        StringBuilder query = new StringBuilder("UPDATE `Book` SET ");
        try {
            //Title cannot be null
                params.add(book.getTitle());
                query.append("`title` = ? ");
            //if(book.getSummary()!=null) {
                params.add(book.getSummary());
                query.append(", `summary` = ? ");
            //}if(book.getPubYear()>0) {
                params.add(book.getPubYear());
                query.append(", `year_published` = ? ");
            //}if(book.getIsbn()!=null) {
                params.add(book.getIsbn());
                query.append(", `isbn` = ? ");
            //}
            query.append("WHERE `id` = ? ");
            params.add(bookID);
            getResultSet(params, query.toString());

        } catch (Exception e){
            e.printStackTrace();
            bookID = 0;
        } finally {
           closeConnection();
        }
        return bookID;
    }

    private static int create(Book book){
        int id = 0;
        ResultSet rs = null;
        try{
            //should be called when the id is 0
            //Title cannot be null
            ArrayList<String> filledFields = new ArrayList<>();
            ArrayList<Object> params = new ArrayList<>();
            params.add(book.getTitle());
            filledFields.add("`title`");
            if(book.getSummary()!=null) {
                params.add(book.getSummary());
                filledFields.add("`summary`");
            }if(book.getPubYear() != null) {
                params.add(book.getPubYear());
                filledFields.add("`year_published`");
            }if(book.getIsbn()!=null) {
                params.add(book.getIsbn());
                filledFields.add("`isbn`");
            }
            String query = "INSERT INTO `Book`("+String.join(", ",filledFields)+") VALUES(" + String.join(", ", Collections.nCopies(params.size(),"?"))+")";
                    //"SELECT LAST_INSERT_ID()";
            getResultSet(params,query);
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            id = rs.getInt(1);
            closeConnection();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return id;
    }

    public static Book read(int bookID){
        ArrayList<Object> params = new ArrayList<>();
        Book book = null;
        try {
            if(bookID < 1) {
                logger.info("Creating new book object");
                return new Book();
                //throw new SQLException("Bad book id given to read");
            }
            params.add(bookID);
            getResultSet(params,queries[BOOK]);
            book = new Book(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            closeConnection();
        }
        return book;
    }



    public void delete(Book book){
        ArrayList<Object> params = new ArrayList<>();
        params.add(book.getBookID());
        try{
            getResultSet(params, queries[DELETE]);
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static ArrayList bookList(){

        ArrayList<Book> books = new ArrayList<>();
        try {
            getResultSet(new ArrayList<>(), queries[BOOKLIST]);
            while(rs.next()) {
                if (rs.getInt("id") < 1 || rs.getString("title") == null){
                    logger.error("Bad object returned by query:\n");
                    continue;
                }
                books.add(new Book(rs.getInt("id"),rs.getString("title")));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return books;
    }

    private static void getResultSet(ArrayList<Object> params, String query) throws SQLException {
        bookTableGatewayFactory();
        conn = ds.getConnection();
        stmt = conn.prepareStatement(query);
        for(int i=0; i<params.size();i++){
                stmt.setObject(i+1,params.get(i));
        }
        stmt.execute();
        rs = stmt.getResultSet();
    }

    public static void bookTableGatewayFactory() {
        if(ds == null) {
            ds = new MysqlDataSource();
            ds.setURL(url);
            ds.setUser(user);
            ds.setPassword(password);
        }
    }

    private static void closeConnection(){
        try {
            if (conn != null) {
                conn.close();
            }
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        } catch (Exception e){

        }
    }

    public static void insertAuditTrailEntry (int bookID, String entryMessage) throws SQLException {
        String query = "INSERT INTO book_audit_trail (book_id, entry_msg)  VALUES (?, ?)";
        PreparedStatement ps = BookTableGateway.getInstance().getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setInt(1, bookID);
        ps.setString(2, entryMessage);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
    }

    public List<AuditTrailEntry> getAuditTrail(int bookId) {
        List<AuditTrailEntry> auditTrail = new ArrayList<AuditTrailEntry>();
        Statement stmt = null;
        try{
            String query = "SELECT * FROM book_audit_trail ORDER BY date_added ASC ";
            stmt = this.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                if (rs.getInt("book_id") == bookId){
                    AuditTrailEntry atr = new AuditTrailEntry();
                    atr.setId(rs.getInt("id"));
                    atr.setDateAdded(rs.getTimestamp("date_added").toLocalDateTime());
                    atr.setMessage(rs.getString("entry_msg"));
                    auditTrail.add(atr);
                }
            }
        } catch(SQLException err){
            System.out.println(err.getMessage());
        }

        return auditTrail;
    }
}
