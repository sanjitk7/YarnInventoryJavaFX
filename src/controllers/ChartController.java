package controllers;


import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;
public class ChartController implements Initializable{
    @FXML
    private Button btnSignout;

    @FXML
    private Button goToProducts;

    @FXML
    PieChart pieChart;

    PreparedStatement preparedStatement;
    Connection con;

    public ChartController(){
            con = ConnectionUtil.conDB();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Preparing ObservbleList object
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Iphone 5S", 13),
                new PieChart.Data("Samsung Grand", 25),
                new PieChart.Data("MOTO G", 10),
                new PieChart.Data("Nokia Lumia", 22));
//        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setData(pieChartData);
        pieChart.setTitle("Mobile Sales");
        fetchPieChartData();

    }



    String query = "SELECT yarn_color,quantity FROM yarn_java";


    public void fetchPieChartData(){
            try{
                ResultSet rs = con.createStatement().executeQuery(query);
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
                while (rs.next()) {
                    // Read values using column name
                    String color = rs.getString("yarn_color");
                    int quantity = rs.getInt("quantity");

                    System.out.println("\n YARN COLOR: "+color+"\n   YARN QTY: "+quantity);
                    pieChartData.add(new PieChart.Data(color, quantity));

                }

                // adding data to each pie
//                pieChartData.forEach(data ->
//                        data.nameProperty().bind(
//                                Bindings.concat(
//                                        data.getName(), " ", data.pieValueProperty(), " Bags"
//                                )
//                        )
//                );
                pieChart.setData(pieChartData);
                pieChart.setTitle("Yarn Stock in Bags (90kgs each)");
            } catch (Exception e){
                System.out.println("Error: "+e);
            }
    }


    public void handleButton(MouseEvent event) {
        System.out.println("Mouse Event Recieved");

        if (event.getSource() == btnSignout) {
            //logout here
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/Login.fxml")));
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

        }
        if (event.getSource() == goToProducts) {
            //logout here
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/OnBoard.fxml")));
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

        }
    }
}
