package controller;

import database.DbConnection;
import helper.AlertHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.print.attribute.standard.MediaSize.NA;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

public class RegisterController implements Initializable {

    private final Connection con;

    @FXML
    private TextField Names;

    @FXML
    private TextField ApellidoA;

    @FXML
    private TextField ApellidoB;

    @FXML
    private TextField username;

    @FXML
    private TextField email;

    @FXML
    private TextField Tel;

    @FXML
    private TextField password;

    @FXML
    private TextField confirmPassword;

    @FXML
    private Button registerButton;

    Window window;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public RegisterController() {
        DbConnection dbc = DbConnection.getDatabaseConnection();
        con = dbc.getConnection();
    }
     public static String AbogaID() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = now.format(formatter);
        String AbogaID = formattedDateTime;
        return AbogaID;
    }

    @FXML
    private void register() {
        window = registerButton.getScene().getWindow();
        if (this.isValidated()) {
            Statement stmt;
            try {
                PreparedStatement ps;
                stmt = con.createStatement();
                String query = "insert into ABOGADOS (ABOGADID,NOMBRES,APELLIDOA,APELLIDOB,TELEFONO,EMAIL,USERNAME,PASSWORD)values (?,?,?,?,?,?,?,?)";
                ps = con.prepareStatement(query);
                ps.setString(1, AbogaID());
                ps.setString(2, Names.getText());
                ps.setString(3, ApellidoA.getText());
                ps.setString(4, ApellidoB.getText());
                ps.setString(5, Tel.getText());
                ps.setString(6, email.getText());
                ps.setString(7, username.getText());
                ps.setString(8, password.getText());

                if (ps.executeUpdate() > 0) {
                    this.clearForm();
                    AlertHelper.showAlert(Alert.AlertType.INFORMATION, window, "Information",
                            "Te has registrado correctamente.");
                } else {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                            "Ocurrio un error.");
                }

            } catch (SQLException ex) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                        "Ocurrio un error.");
            }
        }
    }

    private boolean isAlreadyRegistered() {
        PreparedStatement ps;
        ResultSet rs;
        boolean usernameExist = false;

        String query = "select * from ABOGADOS WHERE USERNAME = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, username.getText());
            rs = ps.executeQuery();
            if (rs.next()) {
                usernameExist = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return usernameExist;
    }

    private boolean isValidated() {

        window = registerButton.getScene().getWindow();
        if (Names.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Nombres no puede estar en blanco.");
            Names.requestFocus();
        } else if (Names.getText().length() < 2 || Names.getText().length() > 80) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Los nombres no pueden ser menor a 2 caracteres ni mayor a 80.");
            Names.requestFocus();
        } else if (ApellidoA.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Primer apellido no puede estar en blanco.");
            ApellidoA.requestFocus();
        } else if (ApellidoA.getText().length() < 2 || ApellidoA.getText().length() > 30) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "El Primer apellido no puede ser menor a 2 caracteres, ni mayor a 30.");
            ApellidoA.requestFocus();
        }  else if (ApellidoB.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Segundo apellido no puede estar en blanco.");
            ApellidoB.requestFocus();
        } else if (ApellidoB.getText().length() < 2 || ApellidoB.getText().length() > 30) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "El Segundo apellido no puede ser menor a 2 caracteres, ni mayor a 30.");
            ApellidoB.requestFocus();
        } else if (Tel.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Telefono no puede estar en blanco.");
            Tel.requestFocus();
        } else if (Tel.getText().length() < 5 || Tel.getText().length() > 10) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "El Telefono no puede ser menor a 5 caracteres, ni mayor a 45.");
            Tel.requestFocus();
        } else if (email.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Email no puede estar en blanco.");
            email.requestFocus();
        } else if (email.getText().length() < 5 || email.getText().length() > 80) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "El email no puede ser menor a 5 caracteres, ni mayor a 80.");
            email.requestFocus();
        } else if (username.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Nombre de usuario no puede estar en blanco.");
            username.requestFocus();
        } else if (username.getText().length() < 5 || username.getText().length() > 25) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "El nombre de usuario no puede ser menor a 5 caracteres, ni mayor a 25.");
            username.requestFocus();
        } else if (password.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Contraseña no puede estar en blanco.");
            password.requestFocus();
        } else if (password.getText().length() < 5 || password.getText().length() > 25) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "La contraseña no puede ser menor a 5 caracteres, ni mayor a 25.");
            password.requestFocus();
        } else if (confirmPassword.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Confirmar contraseña no puede estar en blanco.");
            confirmPassword.requestFocus();
        } else if (confirmPassword.getText().length() < 5 || password.getText().length() > 25) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "El confirmar contraseña no puede ser menor a 5 caracteres ni mayor a 25.");
            confirmPassword.requestFocus();
        } else if (!password.getText().equals(confirmPassword.getText())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "La contraseña y el confirmar contraseña no son iguales.");
            password.requestFocus();
        } else if (isAlreadyRegistered()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "El nombre de usuario ya esta en uso.");
            username.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    private boolean clearForm() {
        Names.clear();
        ApellidoA.clear();
        ApellidoB.clear();
        Tel.clear();
        email.clear();
        username.clear();
        password.clear();
        confirmPassword.clear();
        return true;
    }

    @FXML
    private void showLoginStage() throws IOException {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Login");
        stage.getIcons().add(new Image("/asset/icon.png"));
        stage.show();
    }
}
