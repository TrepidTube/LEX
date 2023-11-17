package controller;

import database.DbConnection;
import helper.AlertHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class EventController implements Initializable {

    private final Connection con;
    ZonedDateTime dateFocus;

    @FXML
    private TextField CasoName;

    @FXML
    private TextArea CasoDesc;

    @FXML
    private ChoiceBox<String> CasoType;

    @FXML
    private ChoiceBox<String> CasoState;

    @FXML
    private TextField password;

    @FXML
    private Button Doc;
    private String rutaTemporal;

    @FXML
    private Button agendButton;

    @FXML
    private DatePicker CasoDate;
    
    
    

    Window window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CasoType.getItems().add("Elige una opcion");
        CasoType.getItems().add("Laboral");
        CasoType.getItems().add("Penal");
        CasoType.setValue("Elige una opcion");

        CasoState.getItems().add("Elige una opcion");
        CasoState.getItems().add("Abierto");
        CasoState.getItems().add("En Proceso");
        CasoState.getItems().add("Cerrado");
        CasoState.setValue("Elige una opcion");

        dateFocus = ZonedDateTime.now();
    }

    public EventController() {
        DbConnection dbc = DbConnection.getDatabaseConnection();
        con = dbc.getConnection();
    }

    public static String CasoID() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmss");
        String formattedDateTime = now.format(formatter);
        String CasoID = formattedDateTime;
        return CasoID;
    }

    @FXML
    private void agend() {
        window = agendButton.getScene().getWindow();
        if (this.isValidated()) {
            Statement stmt;
            LocalDate lD = CasoDate.getValue();
            java.sql.Date date = java.sql.Date.valueOf(lD);
            try {
                PreparedStatement ps;
                stmt = con.createStatement();
                String query = "insert into CASOS (CASOID,CASONAME,DESCRIPT,CASODATE,CASOTYPE,ESTADO)values (?,?,?,?,?,?)";
                ps = con.prepareStatement(query);
                ps.setString(1, CasoID());
                ps.setString(2, CasoName.getText());
                ps.setString(3, CasoDesc.getText());
                ps.setDate(4, date);
                ps.setString(5, CasoType.getValue());
                ps.setString(6, CasoState.getValue());
                
                if (ps.executeUpdate() > 0) {
                    
                    AlertHelper.showAlert(Alert.AlertType.INFORMATION, window, "Informacion",
                            "Te has registrado correctamente.");
                            String Idstring = CasoID();
                            int id = Integer.parseInt(Idstring);
                            String name = CasoName.getText();
                            LocalTime now = LocalTime.now();

                    ZonedDateTime time = ZonedDateTime.of(CasoDate.getValue(), now, dateFocus.getZone());
                    JournalController.addCalendarActivity(time, name, id);
                    this.clearForm();
                    
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

    @FXML
    private void AddDoc() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo");
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            rutaTemporal = selectedFile.getAbsolutePath();
        }

        Statement stmt;
            try {
                PreparedStatement ps;
                stmt = con.createStatement();
                String query = "insert into CASODOC (RUTA)values (?)";
                ps = con.prepareStatement(query);
                ps.setString(1, rutaTemporal);
                if (ps.executeUpdate() > 0) {
                    this.clearForm();
                    AlertHelper.showAlert(Alert.AlertType.INFORMATION, window, "Confirmacion",
                            "Te has registrado correctamente.");
                    
                } else {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                            "Ocurrio un error.");
                }
            }  catch (SQLException ex) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                        "Ocurrio un error.");
            }
    }
    

    private boolean isAlreadyRegistered() {
        PreparedStatement ps;
        ResultSet rs;
        boolean CasoExist = false;

        String query = "select * from CASOS WHERE CASONAME = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, CasoName.getText());
            rs = ps.executeQuery();
            if (rs.next()) {
                CasoExist = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return CasoExist;
    }

    private boolean isValidated() {

        window = agendButton.getScene().getWindow();
        if (CasoName.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "El nombre no puede estar en blanco.");
            CasoName.requestFocus();
        } else if (CasoName.getText().length() < 1 || CasoName.getText().length() > 80) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "El nombre no puede ser menor a 1 caracteres ni mayor a 80.");
            CasoName.requestFocus();
        } else if ("Elige una opcion".equals(CasoType.getValue())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Es necesario elegir el tipo del caso para agendar.");
            CasoType.requestFocus();
        } else if ("Elige una opcion".equals(CasoState.getValue())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "Es necesario elegir el estado del caso para agendar.");
            CasoType.requestFocus();
        } else if (isAlreadyRegistered()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Error",
                    "El nombre del caso ya esta en uso.");
            CasoName.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    private boolean clearForm() {
        CasoName.clear();
        CasoDesc.clear();
        CasoState.getItems().clear();
        CasoType.getItems().clear();
        rutaTemporal = "";
        return true;
    }
}

