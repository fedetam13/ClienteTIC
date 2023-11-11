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
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import proyecto.DTO.AeropuertoDTO;
import proyecto.DTO.UsuarioDTO;
import proyecto.DTO.VueloDTO;
import proyecto.Main;
import proyecto.Rest.AeropuertoRest;
import proyecto.Rest.UsuarioRest;
import proyecto.Rest.VueloRest;
import proyecto.SceneController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


@Controller
public class AeropuertoController implements Initializable {



    @Autowired
    private AeropuertoRest aeropuertoRest;
    @Autowired
    private VueloRest vueloRest;

    @FXML
    private TableView<FlightTuple> vuelosSinAutorizar;
    @FXML
    public TableColumn<FlightTuple,String> tipoVueloPendiente;
    @FXML
    public TableColumn<FlightTuple,String> aerolineaPendiente;
    @FXML
    public TableColumn<FlightTuple,String> avionPendiente;
    @FXML
    public TableColumn<FlightTuple,String> puertaPendiente;
    @FXML
    public TableColumn<FlightTuple,String> fechaHoraPendiente;
    @FXML
    public TableColumn<FlightTuple,String> idVueloPendiente;

    @FXML
    public void volverAlMain(ActionEvent actionEvent) {
        Stage actual = (Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow();
        actual.close();
    }

    @FXML
    public void agregarServicio(ActionEvent actionEvent) {
    }

    @FXML
    public void updateService(ActionEvent actionEvent) {
    }

    @FXML
    public void deleteServices(ActionEvent actionEvent) {
    }

    @FXML
    public void agregarEmpleadoEmpresa(ActionEvent actionEvent) {
    }

    @FXML
    public void despedirEmpleado(ActionEvent actionEvent) {
    }

    @FXML
    public void cambiarDeAreaEmpleado(ActionEvent actionEvent) {
    }

    @FXML
    public void deleteBusiness(ActionEvent actionEvent) {
    }

    @FXML
    public void agregarAerolineaPertenece(ActionEvent actionEvent) {
    }

    @FXML
    public void updateCheckIn(ActionEvent actionEvent) {
    }

    @FXML
    public void deleteAerolineaPertenece(ActionEvent actionEvent) {
    }

    @FXML
    public void volverAlLogIn(ActionEvent actionEvent) throws IOException {
        SceneController sc = new SceneController();
        sc.volverAlLogIn(actionEvent);
    }

    public void changePassword(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(Main.getContext()::getBean);
        Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("CambiarContra.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Change Password");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("INITIALIZING");
        List<VueloDTO> vuelosPendientes = vueloRest.getVuelosPendientes(Main.sessionName);


        ObservableList<FlightTuple> data = FXCollections.observableArrayList();

        tipoVueloPendiente.setCellValueFactory(new PropertyValueFactory<>("tipoVuelo"));
        aerolineaPendiente.setCellValueFactory(new PropertyValueFactory<>("aerolinea"));
        avionPendiente.setCellValueFactory(new PropertyValueFactory<>("avion"));
        puertaPendiente.setCellValueFactory(new PropertyValueFactory<>("puerta"));
        fechaHoraPendiente.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        idVueloPendiente.setCellValueFactory(new PropertyValueFactory<>("idVuelo"));


        for(VueloDTO v : vuelosPendientes){
            String tipo;
            String puerta;
            String fechaHora;

            AeropuertoDTO a = aeropuertoRest.getAeropuertoByNombre(Main.sessionName);
            if(a.getIdAeropuerto()==v.getIdAeropuertoArribo()){tipo = "Arribo";}else{ tipo = "Partida";}
            if(Objects.equals(tipo,"Arribo")){puerta= String.valueOf(v.getPuertaDeArribo());}else{puerta= String.valueOf(v.getPueretaDeEmbarque());}
            if(Objects.equals(tipo,"Arribo")){fechaHora= String.valueOf(v.getHoraDeArribo());}else{fechaHora= String.valueOf(v.getHoraDePartida());}


            FlightTuple f = new FlightTuple();
            f.setTipoVuelo(tipo);
            f.setAerolinea(v.getNombreAerolinea());
            f.setPuerta(puerta);
            f.setFechaHora(fechaHora);
            f.setAvion(v.getMatriculaAvion());
            f.setIdVuelo(String.valueOf(v.getIdVuelo()));

            data.add(f);
        }

        vuelosSinAutorizar.setItems(data);
    }

    public static class FlightTuple {
        private final StringProperty tipoVuelo = new SimpleStringProperty();
        private final StringProperty aerolinea = new SimpleStringProperty();
        private final StringProperty avion = new SimpleStringProperty();
        private final StringProperty puerta = new SimpleStringProperty();
        private final StringProperty fechaHora = new SimpleStringProperty();
        private final StringProperty idVuelo = new SimpleStringProperty();

        public FlightTuple(){}

        public StringProperty tipoVueloProperty() {
            return tipoVuelo;
        }

        public String getIdVuelo() {
            return idVuelo.get();
        }

        public StringProperty idVueloProperty() {
            return idVuelo;
        }

        public void setIdVuelo(String idVuelo) {
            this.idVuelo.set(idVuelo);
        }

        public String getTipoVuelo() {
            return tipoVuelo.get();
        }

        public void setTipoVuelo(String tipoVuelo) {
            this.tipoVuelo.set(tipoVuelo);
        }

        public String getAerolinea() {
            return aerolinea.get();
        }

        public StringProperty aerolineaProperty() {
            return aerolinea;
        }

        public void setAerolinea(String aerolinea) {
            this.aerolinea.set(aerolinea);
        }

        public String getAvion() {
            return avion.get();
        }

        public StringProperty avionProperty() {
            return avion;
        }

        public void setAvion(String avion) {
            this.avion.set(avion);
        }

        public String getPuerta() {
            return puerta.get();
        }

        public StringProperty puertaProperty() {
            return puerta;
        }

        public void setPuerta(String puerta) {
            this.puerta.set(puerta);
        }

        public String getFechaHora() {
            return fechaHora.get();
        }

        public StringProperty fechaHoraProperty() {
            return fechaHora;
        }

        public void setFechaHora(String fechaHora) {
            this.fechaHora.set(fechaHora);
        }
    }
}
