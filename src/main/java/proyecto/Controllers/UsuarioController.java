package proyecto.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import proyecto.DTO.UsuarioDTO;
import proyecto.Main;
import proyecto.Rest.UsuarioRest;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

@Controller
public class UsuarioController {



    @Autowired
    private UsuarioRest usuarioRest;


    public Label errorLabel;
    public Label logInErrorLabel;
    public Label cambiarContraError;

    @FXML
    private TextField textFieldSingInScene_Username;

    @FXML
    private TextField textFieldSingInScene_nombre;

    @FXML
    public PasswordField passwordFieldSingInScenePassword;

    @FXML
    public PasswordField passwordFieldSingInSceneRepeatPassword;

    @FXML
    private TextField textFieldUsernameLogInScene;

    @FXML
    private PasswordField passwordFieldLogInScene;

    @FXML
    public PasswordField passwordFieldChangePasswordRepeat;

    @FXML
    public PasswordField passwordFieldChangePasswordOne;

    @FXML
    public void agregarUsuario(ActionEvent actionEvent) throws Exception {

        if(usuarioRest.getUser(textFieldSingInScene_Username.getText())!=null){
            errorLabel.setText("Email already in use");
            errorLabel.setVisible(true);
        }else if(textFieldSingInScene_Username.getText().isEmpty() ||
                textFieldSingInScene_nombre.getText().isEmpty() ||
                passwordFieldSingInScenePassword.getText().isEmpty() ||
                passwordFieldSingInSceneRepeatPassword.getText().isEmpty() ){

            errorLabel.setText("All fields must be filled");
            errorLabel.setVisible(true);
        }
        else if(!Objects.equals(passwordFieldSingInScenePassword.getText(),passwordFieldSingInSceneRepeatPassword.getText())){
            errorLabel.setText("Passwords dont match");
            errorLabel.setVisible(true);
        }else{
            UsuarioDTO u = new UsuarioDTO();
            u.setNombre(textFieldSingInScene_nombre.getText());
            u.setEmail(textFieldSingInScene_Username.getText());
            u.setPassword(passwordFieldSingInScenePassword.getText());
            u.setTipoUsuario("Pasajero");

            if (usuarioRest.addUsers(u).getStatusCode().is2xxSuccessful()) {
                Stage actual = (Stage) ((Button) actionEvent.getSource()).getParent().getScene().getWindow();
                actual.close();
            }
        }
    }

    @FXML
    public void attemptLogin(ActionEvent actionEvent) throws IOException {
        UsuarioDTO u = usuarioRest.getUser(textFieldUsernameLogInScene.getText().toLowerCase());
        if(u!=null){
            if (Objects.equals(u.getPassword(),passwordFieldLogInScene.getText())){
                Main.sessionID = u.getIdUsuario();
                Main.sessionName = u.getNombre();
                //ADMIN
                if(Objects.equals(u.getTipoUsuario(), "Admin")){
                    adminWindow(actionEvent);
                }
                //ADMIN AEROLINEA
                else if (Objects.equals(u.getTipoUsuario(),"AdminAerolinea")){
                    aerolinaWindow(actionEvent);
                }
                //ADMIN AEROPUERTO
                else if (Objects.equals(u.getTipoUsuario(), "AdminAeropuerto")) {
                    aeropuertoWindow(actionEvent);
                }
                //PASAJERO
                else if(Objects.equals(u.getTipoUsuario(),"Pasajero")){
                    pasajerosWindow(actionEvent);
                }
            }
            else{
                logInErrorLabel.setText("Wrong Email or Password");
                logInErrorLabel.setVisible(true);
            }
        }
        else{
            logInErrorLabel.setText("Wrong Email or Password");
            logInErrorLabel.setVisible(true);
        }
    }

    public void adminWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("AdminGod.fxml"));

        Stage stage = ((Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow());
        stage.setTitle("General Administration");
        stage.setScene(new Scene(root1));
    }

    public void aerolinaWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("AerolineaPaginaPrincipal.fxml"));

        Stage stage = ((Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow());
        stage.setTitle("Airline Manager");
        stage.setScene(new Scene(root1));

    }

    public void aeropuertoWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("AeropuertoPaginaPrincipalFresco.fxml"));

        Stage stage = ((Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow());
        stage.setTitle("Airport Manager");
        stage.setScene(new Scene(root1));

    }

    public void pasajerosWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("PasajerosNew.fxml"));

        Stage stage = ((Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow());
        stage.setTitle("Passenger Services");
        stage.setScene(new Scene(root1));
    }

    public void cerrar(ActionEvent actionEvent) {
        Stage actual = (Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow();
        actual.close();
    }

    @FXML
    public void openSignUp(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("SignUp.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Create a new User");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    @FXML
    public void changePassword(ActionEvent actionEvent) {
        if(passwordFieldChangePasswordOne.getText().isEmpty() ||
                passwordFieldChangePasswordRepeat.getText().isEmpty()){
            cambiarContraError.setText("Both fields must be filled");
            cambiarContraError.setVisible(true);
        }
        else if(Objects.equals(passwordFieldChangePasswordOne.getText(),passwordFieldChangePasswordRepeat.getText())){
            UsuarioDTO u = usuarioRest.getUserById(Main.sessionID);
            u.setPassword(passwordFieldChangePasswordOne.getText());

            if(usuarioRest.changePassword(u).getStatusCode().is2xxSuccessful()){
                cerrar(actionEvent);
            }
        }
        else{
            cambiarContraError.setText("Passwords do not Match");
            cambiarContraError.setVisible(true);
        }
    }

    public void openAbout(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("AboutPage.fxml"));

        Stage stage = new Stage();
        stage.setTitle("About Us");
        stage.setScene(new Scene(root1));
        stage.show();
    }
}