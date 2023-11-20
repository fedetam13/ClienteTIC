package proyecto.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import proyecto.DTO.AeropuertoDTO;
import proyecto.DTO.PasajeDTO;
import proyecto.DTO.VueloDTO;
import proyecto.Main;
import proyecto.Rest.AeropuertoRest;
import proyecto.Rest.PasajeRest;
import proyecto.Rest.VueloRest;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Controller
public class PasajeroController implements Initializable {



    @Autowired
    AeropuertoRest aeropuertoRest;

    @Autowired
    PasajeRest pasajeRest;

    @Autowired
    VueloRest vueloRest;

    public ChoiceBox choiceBoxDestino;
    public ChoiceBox choiceBoxOrigen;
    public Label buscarVueloError;

    public TableView<registeredFlights> vuelosAutorizados;
    public TableColumn<registeredFlights,String> origenVuelo;
    public TableColumn<registeredFlights,String> destinoVuelo;
    public TableColumn<registeredFlights,String> avionVuelo;
    public TableColumn<registeredFlights,String> puertaVuelo;
    public TableColumn<registeredFlights,String> fechaHoraVuelo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<AeropuertoDTO> aeropuertos = aeropuertoRest.getAllAeropuertos();
        List<PasajeDTO> pasajes = pasajeRest.getPasajesByUserId(Main.sessionID);

        ObservableList<registeredFlights> flights = FXCollections.observableArrayList();
        ObservableList<AeropuertoDTO> aeropuertosList = FXCollections.observableArrayList(aeropuertos);

        choiceBoxOrigen.setItems(aeropuertosList);
        choiceBoxDestino.setItems(aeropuertosList);

        origenVuelo.setCellValueFactory(new PropertyValueFactory<>("origenVuelo"));
        destinoVuelo.setCellValueFactory(new PropertyValueFactory<>("destinoVuelo"));
        avionVuelo.setCellValueFactory(new PropertyValueFactory<>("avionVuelo"));
        puertaVuelo.setCellValueFactory(new PropertyValueFactory<>("puertaVuelo"));
        fechaHoraVuelo.setCellValueFactory(new PropertyValueFactory<>("fechaHoraVuelo"));

        for(PasajeDTO p : pasajes){
            registeredFlights f = new registeredFlights();
            VueloDTO v = vueloRest.getVueloById(p.getIdVuelo());
            AeropuertoDTO origen = aeropuertoRest.getAeropuertoById(v.getIdAeropuertoPartida());
            AeropuertoDTO destino = aeropuertoRest.getAeropuertoById(v.getIdAeropuertoArribo());

            f.setOrigenVuelo(origen.getIata());
            f.setDestinoVuelo(destino.getIata());
            f.setAvionVuelo(v.getMatriculaAvion());
            f.setPuertaVuelo(String.valueOf(v.getPueretaDeEmbarque()));
            f.setFechaHoraVuelo(String.valueOf(v.getHoraDePartida()));

            flights.add(f);
        }

        vuelosAutorizados.setItems(flights);

    }

    public void changePasswordWindwow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("CambiarContra.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Change Password");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    public void retornarVuelosDisponibles(ActionEvent actionEvent) throws IOException {
        if(choiceBoxOrigen.getValue()!=null&&choiceBoxDestino.getValue()!=null){
            if(Objects.equals(choiceBoxOrigen.getValue(),choiceBoxDestino.getValue())){
                buscarVueloError.setText("Departure and Arrival locations must be different");
                buscarVueloError.setVisible(true);
            }
            else{
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setControllerFactory(Main.getContext()::getBean);
                Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("ComprarVuelo.fxml"));

                PasajeController pasajeController = fxmlLoader.getController();

                pasajeController.setaOrigen((AeropuertoDTO) choiceBoxOrigen.getValue());
                pasajeController.setaDestino((AeropuertoDTO) choiceBoxDestino.getValue());


                Stage stage = new Stage();
                stage.setTitle("Buy Flight Ticket");
                stage.setScene(new Scene(root1));
                stage.show();
            }
        }else{
            buscarVueloError.setText("Both boxes must be chosen");
            buscarVueloError.setVisible(true);
        }
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
        stage.setTitle("Ingresar Usuario");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    public void reloadPage(ActionEvent actionEvent) {
        initialize(null,null);
    }

    public static class registeredFlights{
        private final StringProperty origenVuelo = new SimpleStringProperty();
        private final StringProperty destinoVuelo = new SimpleStringProperty();
        private final StringProperty avionVuelo = new SimpleStringProperty();
        private final StringProperty puertaVuelo = new SimpleStringProperty();
        private final StringProperty fechaHoraVuelo = new SimpleStringProperty();

        public String getOrigenVuelo() {
            return origenVuelo.get();
        }

        public StringProperty origenVueloProperty() {
            return origenVuelo;
        }

        public void setOrigenVuelo(String origenVuelo) {
            this.origenVuelo.set(origenVuelo);
        }

        public String getDestinoVuelo() {
            return destinoVuelo.get();
        }

        public StringProperty destinoVueloProperty() {
            return destinoVuelo;
        }

        public void setDestinoVuelo(String destinoVuelo) {
            this.destinoVuelo.set(destinoVuelo);
        }

        public String getAvionVuelo() {
            return avionVuelo.get();
        }

        public StringProperty avionVueloProperty() {
            return avionVuelo;
        }

        public void setAvionVuelo(String avionVuelo) {
            this.avionVuelo.set(avionVuelo);
        }

        public String getPuertaVuelo() {
            return puertaVuelo.get();
        }

        public StringProperty puertaVueloProperty() {
            return puertaVuelo;
        }

        public void setPuertaVuelo(String puertaVuelo) {
            this.puertaVuelo.set(puertaVuelo);
        }

        public String getFechaHoraVuelo() {
            return fechaHoraVuelo.get();
        }

        public StringProperty fechaHoraVueloProperty() {
            return fechaHoraVuelo;
        }

        public void setFechaHoraVuelo(String fechaHoraVuelo) {
            this.fechaHoraVuelo.set(fechaHoraVuelo);
        }
    }
}
