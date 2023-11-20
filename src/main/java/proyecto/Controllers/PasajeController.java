package proyecto.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import proyecto.DTO.AeropuertoDTO;
import proyecto.DTO.AvionDTO;
import proyecto.DTO.PasajeDTO;
import proyecto.DTO.VueloDTO;
import proyecto.Main;
import proyecto.Rest.AvionRest;
import proyecto.Rest.PasajeRest;
import proyecto.Rest.VueloRest;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class PasajeController implements Initializable {


    private AeropuertoDTO aOrigen;
    private AeropuertoDTO aDestino;
    private int selectedVuelo;

    @Autowired
    VueloRest vueloRest;

    @Autowired
    AvionRest avionRest;

    @Autowired
    PasajeRest pasajeRest;

    public void setaOrigen(AeropuertoDTO aOrigen) {
        this.aOrigen = aOrigen;
    }

    public void setaDestino(AeropuertoDTO aDestino) {
        this.aDestino = aDestino;
        initialize(null,null);
    }



    public TableView<flightTuple> vuelosSinAutorizar;
    public TableColumn<flightTuple,String> idVuelo;
    public TableColumn<flightTuple,String> aeropuertoOrigen;
    public TableColumn<flightTuple,String> aeropuertoDestino;
    public TableColumn<flightTuple,String> avionVuelo;
    public TableColumn<flightTuple,String> fechaHoraVuelo;
    public TableColumn<flightTuple,String> espacioDeVuelo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(aOrigen!=null){
            List<VueloDTO> vuelosPosibles = vueloRest.getVuelosAceptados(aOrigen.getIdAeropuerto(), aDestino.getIdAeropuerto());

            ObservableList<flightTuple> flights = FXCollections.observableArrayList();

            idVuelo.setCellValueFactory(new PropertyValueFactory<>("idVuelo"));
            aeropuertoOrigen.setCellValueFactory(new PropertyValueFactory<>("aeropuertoOrigen"));
            aeropuertoDestino.setCellValueFactory(new PropertyValueFactory<>("aeropuertoDestino"));
            avionVuelo.setCellValueFactory(new PropertyValueFactory<>("avionVuelo"));
            fechaHoraVuelo.setCellValueFactory(new PropertyValueFactory<>("fechaHoraVuelo"));
            espacioDeVuelo.setCellValueFactory(new PropertyValueFactory<>("espacioDeVuelo"));

            for(VueloDTO v : vuelosPosibles){
                flightTuple f = new flightTuple();

                AvionDTO a = avionRest.getAvion(v.getMatriculaAvion());

                f.setIdVuelo(String.valueOf(v.getIdVuelo()));
                f.setAeropuertoOrigen(aOrigen.getIata());
                f.setAeropuertoDestino(aDestino.getIata());
                f.setAvionVuelo(v.getMatriculaAvion());
                f.setFechaHoraVuelo(String.valueOf(v.getHoraDePartida()));
                f.setEspacioDeVuelo(v.getOcupados() + "/" + a.getCapacidad());

                flights.add(f);
            }

            vuelosSinAutorizar.setItems(flights);
        }

        vuelosSinAutorizar.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedVuelo = Integer.parseInt(newValue.getIdVuelo());
                System.out.println(selectedVuelo);
            }
        });
    }


    public void comprarTicket(ActionEvent actionEvent) {
        PasajeDTO p = new PasajeDTO();
        p.setIdUsuario(Main.sessionID);
        p.setIdVuelo(selectedVuelo);
        vueloRest.addCounter(selectedVuelo);
        if(pasajeRest.addPasaje(p).getStatusCode().is2xxSuccessful()){
            closeWindow(actionEvent);
        }

    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage actual = (Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow();
        actual.close();
    }

    public static class flightTuple{
        private final StringProperty idVuelo = new SimpleStringProperty();
        private final StringProperty aeropuertoOrigen = new SimpleStringProperty();
        private final StringProperty aeropuertoDestino = new SimpleStringProperty();
        private final StringProperty avionVuelo = new SimpleStringProperty();
        private final StringProperty fechaHoraVuelo = new SimpleStringProperty();
        private final StringProperty espacioDeVuelo = new SimpleStringProperty();

        public flightTuple() {
        }

        public String getIdVuelo() {
            return idVuelo.get();
        }

        public StringProperty idVueloProperty() {
            return idVuelo;
        }

        public String getAeropuertoOrigen() {
            return aeropuertoOrigen.get();
        }

        public StringProperty aeropuertoOrigenProperty() {
            return aeropuertoOrigen;
        }

        public String getAeropuertoDestino() {
            return aeropuertoDestino.get();
        }

        public StringProperty aeropuertoDestinoProperty() {
            return aeropuertoDestino;
        }

        public String getAvionVuelo() {
            return avionVuelo.get();
        }

        public StringProperty avionVueloProperty() {
            return avionVuelo;
        }

        public String getFechaHoraVuelo() {
            return fechaHoraVuelo.get();
        }

        public StringProperty fechaHoraVueloProperty() {
            return fechaHoraVuelo;
        }

        public String getEspacioDeVuelo() {
            return espacioDeVuelo.get();
        }

        public StringProperty espacioDeVueloProperty() {
            return espacioDeVuelo;
        }

        public void setIdVuelo(String idVuelo) {
            this.idVuelo.set(idVuelo);
        }

        public void setAeropuertoOrigen(String aeropuertoOrigen) {
            this.aeropuertoOrigen.set(aeropuertoOrigen);
        }

        public void setAeropuertoDestino(String aeropuertoDestino) {
            this.aeropuertoDestino.set(aeropuertoDestino);
        }

        public void setAvionVuelo(String avionVuelo) {
            this.avionVuelo.set(avionVuelo);
        }

        public void setFechaHoraVuelo(String fechaHoraVuelo) {
            this.fechaHoraVuelo.set(fechaHoraVuelo);
        }

        public void setEspacioDeVuelo(String espacioDeVuelo) {
            this.espacioDeVuelo.set(espacioDeVuelo);
        }
    }
}
