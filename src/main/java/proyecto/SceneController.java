package proyecto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import proyecto.DTO.AeropuertoDTO;
import proyecto.DTO.UsuarioDTO;
import proyecto.Rest.AeropuertoRest;
import proyecto.Rest.UsuarioRest;

import java.io.IOException;

@Controller
public class SceneController {


    @Autowired
    private AeropuertoRest aeropuertoRest;
    @Autowired
    private UsuarioRest usuarioRest;

    public Label registrarAeropuertoErrorA;
    public Label registrarAeropuertoErrorCuenta;

    @FXML
    public void volverAlMain(ActionEvent actionEvent) {
        Stage actual = (Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow();
        actual.close();
    }

    //ADMIN GOD
    @FXML
    public void changePasswordWinwow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("CambiarContra.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Change Password");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    public void updateAirport(ActionEvent actionEvent) {
    }

    @FXML
    public void creatAirline(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("RegistarAerolinea.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Create New Airline");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    @FXML
    public void createAirport(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("RegistarAeropuerto.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Create New Airport");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    public void createBusiness(ActionEvent actionEvent) {
    }


    @FXML
    public void volverAlLogIn(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("LogIn.fxml"));

        Stage estage = (Stage) ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow();
        estage.close();
        Main.sessionID = -1;
        Main.sessionName = null;

        Stage stage = new Stage();
        stage.setTitle("Log In");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    @FXML
    public void retornarVuelosDisponibles(ActionEvent actionEvent) {
    }


    @FXML
    private TextField textFieldRegistrarAeropuertoEmail;
    @FXML
    private PasswordField passwordFieldRegistrarAeropuertoPassword;
    @FXML
    private TextField textFieldRegistarAeropuerto_Nombre;
    @FXML
    private TextField textFieldRegistarAeropuerto_Ciudad;
    @FXML
    private TextField textFieldRegistarAeropuerto_Iata;
    @FXML
    private TextField textFieldRegistarAeropuerto_CPuerta;
    @FXML
    private TextField textFieldRegistarAeropuerto_CCheckIn;
    @FXML
    private TextField textFieldRegistarAeropuerto_CPistas;

    @FXML
    public void agregarAeropuerto(){
        if(textFieldRegistarAeropuerto_Iata.getText().isEmpty() ||
                textFieldRegistarAeropuerto_Nombre.getText().isEmpty() ||
                textFieldRegistarAeropuerto_CCheckIn.getText().isEmpty() ||
                textFieldRegistarAeropuerto_Ciudad.getText().isEmpty() ||
                textFieldRegistarAeropuerto_CPuerta.getText().isEmpty() ||
                textFieldRegistarAeropuerto_CPistas.getText().isEmpty()){

            registrarAeropuertoErrorA.setText("All fields must be filled");
            registrarAeropuertoErrorA.setVisible(true);
        }

        else if (aeropuertoRest.getAeropuerto(textFieldRegistarAeropuerto_Iata.getText())!=null){

            registrarAeropuertoErrorA.setText("IATA Code already in use");
            registrarAeropuertoErrorA.setVisible(true);
        }
        else if(textFieldRegistrarAeropuertoEmail.getText().isEmpty() ||
            passwordFieldRegistrarAeropuertoPassword.getText().isEmpty()){

            registrarAeropuertoErrorCuenta.setText("All fields must be filled");
            registrarAeropuertoErrorCuenta.setVisible(true);
        }
        else if(usuarioRest.getUser(textFieldRegistrarAeropuertoEmail.getText())!=null){

            registrarAeropuertoErrorCuenta.setText("Email already in use");
            registrarAeropuertoErrorCuenta.setVisible(true);
        }
        else{
            UsuarioDTO u = new UsuarioDTO();
            u.setEmail(textFieldRegistrarAeropuertoEmail.getText());
            u.setPassword(passwordFieldRegistrarAeropuertoPassword.getText());
            u.setNombre(textFieldRegistarAeropuerto_Nombre.getText());
            u.setTipoUsuario("AdminAeropuerto");

            AeropuertoDTO a = new AeropuertoDTO();
            a.setIata(textFieldRegistarAeropuerto_Iata.getText());
            a.setNombre(textFieldRegistarAeropuerto_Nombre.getText());
            a.setCantidadDeCheckin(Integer.parseInt(textFieldRegistarAeropuerto_CCheckIn.getText()));
            a.setUbicacion(textFieldRegistarAeropuerto_Ciudad.getText());
            a.setCantidadDePuertas(Integer.parseInt(textFieldRegistarAeropuerto_CPuerta.getText()));
            a.setCantidadDePistas(Integer.parseInt(textFieldRegistarAeropuerto_CPistas.getText()));

            if(usuarioRest.addUsers(u).getStatusCode().is2xxSuccessful()){
                if (aeropuertoRest.addAeropuerto(a).getStatusCode().is2xxSuccessful()){
                    Stage actual = (Stage)textFieldRegistarAeropuerto_CPistas.getScene().getWindow();
                    actual.close();
                }
            }
        }

    }
}
