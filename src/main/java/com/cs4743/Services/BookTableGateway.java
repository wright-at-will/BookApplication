package com.cs4743.Services;

import com.cs4743.Controller.MenuController;
import com.cs4743.Model.AuditTrailEntry;
import com.cs4743.Services.BookException;
import com.cs4743.Model.Book;
import com.mysql.cj.jdbc.ConnectionGroup;
import com.mysql.cj.jdbc.MysqlDataSource;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BookTableGateway {

    private Logger logger = LogManager.getLogger(MenuController.class);
    private MysqlDataSource ds = null;
    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;
    
    private static BookTableGateway instance = null;

    private int INSERT = 0;
    private int UPDATE = 1;
    private int BOOK = 2;
    private int BOOKLIST = 3;
    private int DELETE = 4;

    static final String url = "jdbc:mysql://easel2.fulgentcorp.com/yby805" +
            "?" +
            "db=yby805" +
            "&useLegacyDatetimeCode=false&useTimezone=true&serverTimezone=GMT ";
    static final String user = "yby805";
    static final String password = "y9X8yYS2ZsFsuK1Xlzgj";

    //TODO Change queires so that they can take the right keys and values
    private static String[] queries =
            {
                    "INSERT INTO `Book`" +
                            "() " +
                            "VALUES ()",
                    "UPDATE `Book` SET" +
                            "`title`=?,`summary`=?,`year_published`=?,`isbn`=?" +
                            "WHERE `id` = ?",
                    "SELECT * FROM Book WHERE id = ? FOR UPDATE",
                    "SELECT id, title FROM Book",
                    "DELETE FROM `Book` WHERE `id` = ?"
            };

    public static BookTableGateway getInstance() {
        if(instance == null) {
            instance = new BookTableGateway();
        }

        return instance;
    }

    private BookTableGateway(){
        bookTableGatewayFactory();
    }

    //Update should expect that a connection already exists
    public int update(Book book) throws BookException{
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
                params.add(book.getSummary());
                query.append(", `summary` = ? ");
                params.add(book.getPubYear());
                query.append(", `year_published` = ? ");
                params.add(book.getIsbn());
                query.append(", `isbn` = ? ");
            query.append("WHERE `id` = ? ");
            params.add(bookID);
            getResultSet(params, query.toString(), Connection.TRANSACTION_REPEATABLE_READ);
            conn.commit();
            closeConnection();
        } catch (Exception e){
            e.printStackTrace();
            bookID = 0;
        }
        return bookID;
    }

    public int create(Book book){
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
            }if(book.getPubYear() != 0) {
                params.add(book.getPubYear());
                filledFields.add("`year_published`");
            }if(book.getIsbn()!=null) {
                params.add(book.getIsbn());
                filledFields.add("`isbn`");
            }
            String query = "INSERT INTO `Book`("+String.join(", ",filledFields)+") "
            		+ "VALUES(" + String.join(", ", Collections.nCopies(params.size(),"?"))+")";
            getConnection(Connection.TRANSACTION_REPEATABLE_READ);
            getResultSet(params,query, Connection.TRANSACTION_REPEATABLE_READ);
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            int id = rs.getInt(1);
            closeConnection();
            return id;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return -1;
    }

    public Book read(int bookID){
        ArrayList<Object> params = new ArrayList<>();
        Book book = null;
        try {
            if(bookID < 1) {
                logger.info("Creating new book object");
                return new Book();
            }
            getConnection(Connection.TRANSACTION_REPEATABLE_READ);

            params.add(bookID);
            conn.setAutoCommit(false);
            getResultSet(params,queries[BOOK], Connection.TRANSACTION_REPEATABLE_READ);
            book = new Book(rs);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        } finally{
            //closeConnection();
        }
        return book;
    }



    public void delete(Book book) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(book.getBookID());
        try{
            getConnection(Connection.TRANSACTION_REPEATABLE_READ);
            getResultSet(params, queries[DELETE], Connection.TRANSACTION_REPEATABLE_READ);
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public ArrayList bookList() {

        ArrayList<Book> books = new ArrayList<>();
        try {
            getConnection(Connection.TRANSACTION_READ_COMMITTED);
            getResultSet(new ArrayList<>(), queries[BOOKLIST], Connection.TRANSACTION_READ_COMMITTED);
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

    private void getResultSet(ArrayList<Object> params, String query, int isolation) throws SQLException {
        bookTableGatewayFactory();
        stmt = conn.prepareStatement(query);
        for(int i=0; i<params.size();i++){
                stmt.setObject(i+1,params.get(i));
        }
        stmt.setQueryTimeout(5);
        try{stmt.execute();}
        catch (SQLException e){
            throw new BookException("Request timed out");
        }
        rs = stmt.getResultSet();
    }

    private void bookTableGatewayFactory() {
        if(ds == null) {
            ds = new MysqlDataSource();
            ds.setURL(url);
            ds.setUser(user);
            ds.setPassword(password);
        }
    }

    public void closeConnection(){
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

    /*
    This method is only called while editing a book entry, thus we have to make sure to preserve the
    `FOR UPDATE` clause in the transaction. This means that we cannot close the connection or commit
    here.
     */
    public void insertAuditTrailEntry (int bookID, String entryMessage) throws SQLException {
        String query = "INSERT INTO book_audit_trail (book_id, entry_msg)  VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
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
            stmt = getConnection(Connection.TRANSACTION_READ_COMMITTED).createStatement();
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
            closeConnection();
        } catch(SQLException err){
            System.out.println(err.getMessage());
        }

        return auditTrail;
    }
    

	
	public void saveBook(Book book) {

			try {
				if(book.getBookID() == 0) {
					int newId = create(book);
					book.setBookID(newId);
				} else {
                    update(book);
                }
			} catch (Exception e) {
				e.printStackTrace();
			}

	}
	
	public Connection getConnection(int isolationLevel) throws SQLException {
        if(conn == null || conn.isClosed())
            conn = ds.getConnection();
        conn.setTransactionIsolation(isolationLevel);
		return conn;
	}

	public void setConnection(Connection connection) {
		this.conn = connection;
	}
}
