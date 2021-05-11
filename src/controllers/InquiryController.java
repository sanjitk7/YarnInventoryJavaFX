package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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

public class InquiryController implements Initializable {

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


    /// --
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public void handleSignUpPageButtonAction(MouseEvent event) {

        if (event.getSource() == btnSend) {
                try {
                    String st = "INSERT INTO yarn_inquiry ( yarn_color, yarn_count, email, pid, message) VALUES (?,?,?,?,?)";
                    preparedStatement = (PreparedStatement) con.prepareStatement(st);
                    preparedStatement.setString(1, txtYarnColor.getText());
                    preparedStatement.setString(2,txtYarnCount.getText());
                    preparedStatement.setString(3, txtEmail.getText());
                    preparedStatement.setInt(4, Integer.parseInt(txtProductId.getText()));
                    preparedStatement.setString(5, txtMessage.getText());

                    preparedStatement.executeUpdate();

                    setLblError(Color.GREEN, "Inquiry Sent Successfully..");

                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }

        }
        if(event.getSource() == btnLogin){
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
        // TODO
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
        }
    }

    // constructor that initialises the db connection
    public InquiryController() {
        con = ConnectionUtil.conDB();
    }


    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }
}