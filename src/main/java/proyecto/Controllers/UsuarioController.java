package proyecto.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    @FXML
    public Text incorrectData;

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
        if(!Objects.equals(passwordFieldSingInScenePassword.getText(),passwordFieldSingInSceneRepeatPassword.getText())){
            throw new Exception("Not matching");
        }
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
        }
        else{
            incorrectData.setOpacity(100);
        }
    }

    public void adminWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("AdminGod.fxml"));

        Stage stage = ((Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow());
        stage.setTitle("Administracion general del Sistema");
        stage.setScene(new Scene(root1));
    }

    public void aerolinaWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("AerolineaPaginaPrincipal.fxml"));

        Stage stage = ((Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow());
        stage.setTitle("Pagina Principal Aerollinea");
        stage.setScene(new Scene(root1));

    }

    public void aeropuertoWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("AeropuertoPaginaPrincipalFresco.fxml"));

        Stage stage = ((Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow());
        stage.setTitle("Pagina Principal Aeropuerto");
        stage.setScene(new Scene(root1));

    }

    public void pasajerosWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("PasajerosNew.fxml"));

        Stage stage = ((Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow());
        stage.setTitle("Pasajeros");
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
        if(Objects.equals(passwordFieldChangePasswordOne.getText(),passwordFieldChangePasswordRepeat.getText())){
            UsuarioDTO u = usuarioRest.getUserById(Main.sessionID);
            u.setPassword(passwordFieldChangePasswordOne.getText());

            if(usuarioRest.changePassword(u).getStatusCode().is2xxSuccessful()){
                cerrar(actionEvent);
            }
        }
        else{
            System.out.println("Passwords not matching");
        }
    }
}