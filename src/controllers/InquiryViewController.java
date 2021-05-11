package controllers;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
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

public class InquiryViewController implements Initializable {

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtYarnColor;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtProductId;

    @FXML
    private TextArea txtMessage;

    @FXML
    private TextField txtYarnCount;

    @FXML
    private Button btnSend;


    @FXML
    private Button btnLogin;

    @FXML
    private Button goToProducts;

    @FXML
    private Button signoutButton;

    @FXML
    TableView tblInq;

    /// --
    Connection con;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    @FXML
    public void handleButton(MouseEvent event) {
        System.out.println("Mouse Event Recieved");

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
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
        }
//        fetColumnList();
//        fetRowList();
    }

    // constructor that initialises the db connection
    public InquiryViewController() {
        con = ConnectionUtil.conDB();
    }

    // Table View Inquiry
    private ObservableList<ObservableList> data;
    String SQL = "SELECT * from yarn_inquiry";

    //only fetch columns
    private void fetColumnList() {

        try {
            System.out.println("fetching columns for inq");
            ResultSet rs = con.createStatement().executeQuery(SQL);
            System.out.println("fetching columns for inq 2");
            //SQL FOR SELECTING ALL OF CUSTOMER
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                System.out.println("fetching columns for inq 3");
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                System.out.println("fetching columns for inq 4"+ btnSend);
                tblInq.getColumns().removeAll(col);
                tblInq.getColumns().addAll(col);
                System.out.println("fetching columns for inq 5");
                System.out.println("Column [" + i + "] ");

            }

        } catch (Exception e) {
            System.out.println("Error " + e);

        }
    }

    //fetches rows and data from the list
    private void fetRowList() {
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = con.createStatement().executeQuery(SQL);

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

            tblInq.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }


    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }
}