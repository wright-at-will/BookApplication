package com.cs4743.Services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cs4743.Model.Publisher;
import javafx.application.Application;
import javafx.stage.Stage;

public class PublisherTableGateway extends Application{

    private static PublisherTableGateway instance = null;

    public PublisherTableGateway (){}

    public List<Publisher> fetchPublishers() {
        List<Publisher> publisherList = new ArrayList<Publisher>();
        Statement stmt = null;
        try {
            String query = "select * from publisher";
            stmt = BookTableGateway.getInstance().getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                Publisher publisher = new Publisher(rs.getInt("id"), rs.getString("publisherName"), 
                		rs.getTimestamp("date_added"));
                publisherList.add(publisher);
            }
        } catch(SQLException err){
            System.out.println(err.getMessage());
        }
        return publisherList;
    }

    @Override
    public void start(Stage arg0) throws Exception {
        // TODO Auto-generated method stub

    }

    public static PublisherTableGateway getInstance() { if(instance == null) instance = new PublisherTableGateway(); return instance; }
}