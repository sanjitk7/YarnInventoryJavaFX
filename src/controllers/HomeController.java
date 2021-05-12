/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

//import com.mysql.jdbc.Connection;
//import com.mysql.cj.jdbc.PreparedStatement;
import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.ConnectionUtil;

public class HomeController implements Initializable {

    @FXML
    private TextField txtYarnColor;
    @FXML
    private TextField txtYarnCount;
    @FXML
    private TextField txtQuantity;
    @FXML
    private DatePicker txtDateStocked;
    @FXML
    private Button btnSave;
    @FXML
    private ComboBox<String> txtAvailability;
    @FXML
    Label lblStatus;
    @FXML
    Label lblTotal;
    @FXML
    private Button rowDelete;
    @FXML
    TableView tblData;

    @FXML
    private Button signoutButton;

    @FXML
    private Button goToInquiry;

    /**
     * Initializes the controller class.
     */
    PreparedStatement preparedStatement;
    Connection connection;

    public HomeController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtAvailability.getItems().addAll("Yes", "No");
        txtAvailability.getSelectionModel().select("Yes");
        fetColumnList();
        fetRowList();
        fetchTotal();

    }

    @FXML
    private void HandleEvents(MouseEvent event) {
        //check if not empty
        if (txtQuantity.getText().isEmpty() || txtYarnColor.getText().isEmpty() || txtYarnCount.getText().isEmpty() || txtDateStocked.getValue().equals(null)) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Enter all details");
        } else {
            saveData();
        }

    }

    private void clearFields() {
        txtYarnColor.clear();
        txtYarnCount.clear();
        txtQuantity.clear();
    }

    private String saveData() {

        try {
            String st = "INSERT INTO yarn_java ( yarn_color, yarn_count, quantity, avail, date_stocked) VALUES (?,?,?,?,?)";
            preparedStatement = (PreparedStatement) connection.prepareStatement(st);
            preparedStatement.setString(1, txtYarnColor.getText());
            preparedStatement.setInt(2, Integer.parseInt(txtYarnCount.getText()));
            preparedStatement.setInt(3, Integer.parseInt(txtQuantity.getText()));
            preparedStatement.setString(4, txtAvailability.getValue().toString());
            preparedStatement.setString(5, txtDateStocked.getValue().toString());

            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Added Successfully");

            fetRowList();
            fetchTotal();
            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    private ObservableList<ObservableList> data;
    String SQL = "SELECT * from yarn_java";

    //only fetch columns
    private void fetColumnList() {

        try {
            System.out.println("statement executes");
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            //SQL FOR SELECTING ALL OF CUSTOMER
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tblData.getColumns().removeAll(col);
                tblData.getColumns().addAll(col);

                System.out.println("Column [" + i + "] ");

            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
    }

    //fetches rows and data from the list
    private void fetRowList() {
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = connection.createStatement().executeQuery(SQL);

            while (rs.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            tblData.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void fetchTotal(){
        int total = 0;
//        Object item = tblData.getItems().get(0);
//        System.out.println("tblData.getItems(): "+tblData.getItems().get(0));
//        TableColumn c = (TableColumn) tblData.getColumns().get(4);
//        System.out.println("C : "+ c);
//        System.out.println("cellval : "+ c.getText());
        for (int i=0;i<tblData.getItems().size();i++){
            int currCell = Integer.parseInt(tblData.getItems().get(i).toString().split(",")[3].trim());
            total+=currCell;
            System.out.println(currCell);
        }
        System.out.println("Total : "+ total);
        lblTotal.setText("Total Bags in Stock: "+ total);
    }


    String deleteQuery = "DELETE FROM yarn_java WHERE id=?";
    @FXML
    private void handleRowDelete(ActionEvent event){
//        System.out.println("Handle Row Event Handler is called");
//        System.out.println("Selected Item ID:");
        int d_id = Integer.parseInt(tblData.getSelectionModel().getSelectedItem().toString().split(",")[0].substring(1));
        System.out.println(d_id);
        try {

            preparedStatement = (PreparedStatement) connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, d_id);
            preparedStatement.executeUpdate();

            tblData.getItems().removeAll(tblData.getSelectionModel().getSelectedItem());
            fetchTotal();
        } catch (Exception e){
            System.out.println("An Exception has occurred: "+e);
        }
    }
    @FXML
    public void handleButton(MouseEvent event) {
    System.out.print("This func called");
        if (event.getSource() == signoutButton) {
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
        if (event.getSource() == goToInquiry) {
            //logout here

            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/InquiriesView.fxml")));
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

        }

    }

}
