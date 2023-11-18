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
import proyecto.DTO.VueloDTO;
import proyecto.Main;
import proyecto.Rest.AeropuertoRest;
import proyecto.Rest.VueloRest;
import proyecto.SceneController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;


@Controller
public class AeropuertoController implements Initializable {


    int selectedId;
    String tipoDeVuelo;


    @Autowired
    private AeropuertoRest aeropuertoRest;
    @Autowired
    private VueloRest vueloRest;

    //PENDIENTE
    @FXML
    private TableView<PendentFlightTuple> vuelosSinAutorizar;
    @FXML
    public TableColumn<PendentFlightTuple,String> tipoVueloPendiente;
    @FXML
    public TableColumn<PendentFlightTuple,String> aerolineaPendiente;
    @FXML
    public TableColumn<PendentFlightTuple,String> avionPendiente;
    @FXML
    public TableColumn<PendentFlightTuple,String> fechaHoraPendiente;
    @FXML
    public TableColumn<PendentFlightTuple,String> idVueloPendiente;


    //NO PENDIENTE
    public TableView<NonPendentFlightTuple> vuelosAutorizados;
    public TableColumn<NonPendentFlightTuple,String> idVueloNP;
    public TableColumn<NonPendentFlightTuple,String> aerolineaNP;
    public TableColumn<NonPendentFlightTuple,String> avionNP;
    public TableColumn<NonPendentFlightTuple,String> puertaNP;
    public TableColumn<NonPendentFlightTuple,String> fechaHoraNP;
    public TableColumn<NonPendentFlightTuple,String> statusNP;
    public TableColumn<NonPendentFlightTuple,String> tipoVueloNP;


    //

    @FXML
    public void volverAlMain(ActionEvent actionEvent) {
        Stage actual = (Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow();
        actual.close();
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
        selectedId = -1;
        tipoDeVuelo = "-";
        List<VueloDTO> vuelosPendientes = vueloRest.getVuelosPendientes(Main.sessionName);
        List<VueloDTO> vuelosNoPendientes = vueloRest.getVuelosNoPendientes(Main.sessionName);


        ObservableList<PendentFlightTuple> pendentFlights = FXCollections.observableArrayList();
        ObservableList<NonPendentFlightTuple> nonPendentFlights = FXCollections.observableArrayList();


        tipoVueloPendiente.setCellValueFactory(new PropertyValueFactory<>("tipoVuelo"));
        aerolineaPendiente.setCellValueFactory(new PropertyValueFactory<>("aerolinea"));
        avionPendiente.setCellValueFactory(new PropertyValueFactory<>("avion"));
        fechaHoraPendiente.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        idVueloPendiente.setCellValueFactory(new PropertyValueFactory<>("idVuelo"));

        idVueloNP.setCellValueFactory(new PropertyValueFactory<>("idVueloNP"));
        aerolineaNP.setCellValueFactory(new PropertyValueFactory<>("aerolineaNP"));
        avionNP.setCellValueFactory(new PropertyValueFactory<>("avionNP"));
        puertaNP.setCellValueFactory(new PropertyValueFactory<>("puertaNP"));
        fechaHoraNP.setCellValueFactory(new PropertyValueFactory<>("fechaHoraNP"));
        statusNP.setCellValueFactory(new PropertyValueFactory<>("statusNP"));
        tipoVueloNP.setCellValueFactory(new PropertyValueFactory<>("tipoVueloNP"));


        String tipo;
        String fechaHora;

        for(VueloDTO v : vuelosPendientes){

            AeropuertoDTO a = aeropuertoRest.getAeropuertoByNombre(Main.sessionName);
            if(a.getIdAeropuerto()==v.getIdAeropuertoArribo()){tipo = "Arribo";}else{ tipo = "Partida";}
            if(Objects.equals(tipo,"Arribo")){fechaHora= String.valueOf(v.getHoraDeArribo());}else{fechaHora= String.valueOf(v.getHoraDePartida());}


            PendentFlightTuple f = new PendentFlightTuple();
            f.setTipoVuelo(tipo);
            f.setAerolinea(v.getNombreAerolinea());
            f.setFechaHora(fechaHora);
            f.setAvion(v.getMatriculaAvion());
            f.setIdVuelo(String.valueOf(v.getIdVuelo()));

            pendentFlights.add(f);
        }


        for(VueloDTO v : vuelosNoPendientes){
            String puerta;
            String status;

            AeropuertoDTO a = aeropuertoRest.getAeropuertoByNombre(Main.sessionName);
            if(a.getIdAeropuerto()==v.getIdAeropuertoArribo()){
                tipo = "Arribo";
                fechaHora = String.valueOf(v.getHoraDeArribo());
                puerta = String.valueOf(v.getPuertaDeArribo());
                if(Objects.equals(v.getAprovacionArribo(),"A")){status="Aprovado";}else{status="Rechazado";}
            }
            else{
                tipo = "Partida";
                fechaHora = String.valueOf(v.getHoraDePartida());
                puerta = String.valueOf(v.getPueretaDeEmbarque());
                if(Objects.equals(v.getAprovacionPartida(),"A")){status="Aprovado";}else{status="Rechazado";}
            }


            NonPendentFlightTuple f = new NonPendentFlightTuple();
            f.setIdVueloNP(String.valueOf(v.getIdVuelo()));
            f.setAerolineaNP(v.getNombreAerolinea());
            f.setAvionNP(v.getMatriculaAvion());
            f.setPuertaNP(puerta);
            f.setFechaHoraNP(fechaHora);
            f.setStatusNP(status);
            f.setTipoVueloNP(tipo);

            nonPendentFlights.add(f);
        }

        vuelosSinAutorizar.setItems(pendentFlights);
        vuelosAutorizados.setItems(nonPendentFlights);


        vuelosSinAutorizar.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedId = Integer.parseInt(newValue.getIdVuelo());
                System.out.println(selectedId);
                tipoDeVuelo = newValue.getTipoVuelo();
            }
        });
         //NI IDEA PERO FUNCIONA
    }

    public void rejectFlight(ActionEvent actionEvent) {
        if(selectedId==-1){
            System.out.println("SELECCIONA ALGO BOLUDO");
        }else{
            VueloDTO v = vueloRest.getVueloById(selectedId);
            if(tipoDeVuelo=="Arribo"){ v.setAprovacionArribo("R"); } else { v.setAprovacionPartida("R"); }
            vueloRest.addVuelo(v);
            reloadFlights();
        }
    }

    public void acceptFlight(ActionEvent actionEvent) throws IOException {
        if(selectedId==-1){
            System.out.println("SELECCIONA ALGO BOLUDO");
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(Main.getContext()::getBean);
            Parent root1 = fxmlLoader.load(Main.class.getResourceAsStream("AsignarPuerta.fxml"));

            AceptarVueloController aceptarVueloController = fxmlLoader.getController();

            aceptarVueloController.setTipoVuelo(tipoDeVuelo);
            aceptarVueloController.setSelectedID(selectedId);


            Stage stage = new Stage();
            stage.setTitle("Asignar Puerta al Vuelo");
            stage.setScene(new Scene(root1));
            stage.show();

        }
    }

    public void reloadFlights() {
        initialize(null,null);
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



    //CLASES PARA HACER TUPLAS
    public static class PendentFlightTuple {
        private final StringProperty tipoVuelo = new SimpleStringProperty();
        private final StringProperty aerolinea = new SimpleStringProperty();
        private final StringProperty avion = new SimpleStringProperty();
        private final StringProperty fechaHora = new SimpleStringProperty();
        private final StringProperty idVuelo = new SimpleStringProperty();

        public PendentFlightTuple(){}

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

    public static class NonPendentFlightTuple{
        private final StringProperty idVueloNP = new SimpleStringProperty();
        private final StringProperty aerolineaNP = new SimpleStringProperty();
        private final StringProperty avionNP = new SimpleStringProperty();
        private final StringProperty puertaNP = new SimpleStringProperty();
        private final StringProperty fechaHoraNP = new SimpleStringProperty();
        private final StringProperty statusNP = new SimpleStringProperty();
        private final StringProperty tipoVueloNP = new SimpleStringProperty();

        public NonPendentFlightTuple(){}

        public void setIdVueloNP(String idVueloNP) {
            this.idVueloNP.set(idVueloNP);
        }

        public void setAerolineaNP(String aerolineaNP) {
            this.aerolineaNP.set(aerolineaNP);
        }

        public void setAvionNP(String avionNP) {
            this.avionNP.set(avionNP);
        }

        public void setPuertaNP(String puertaNP) {
            this.puertaNP.set(puertaNP);
        }

        public void setFechaHoraNP(String fechaHoraNP) {
            this.fechaHoraNP.set(fechaHoraNP);
        }

        public void setStatusNP(String statusNP) {
            this.statusNP.set(statusNP);
        }

        public void setTipoVueloNP(String tipoVueloNP) {
            this.tipoVueloNP.set(tipoVueloNP);
        }

        public String getIdVueloNP() {
            return idVueloNP.get();
        }

        public StringProperty idVueloNPProperty() {
            return idVueloNP;
        }

        public String getAerolineaNP() {
            return aerolineaNP.get();
        }

        public StringProperty aerolineaNPProperty() {
            return aerolineaNP;
        }

        public String getAvionNP() {
            return avionNP.get();
        }

        public StringProperty avionNPProperty() {
            return avionNP;
        }

        public String getPuertaNP() {
            return puertaNP.get();
        }

        public StringProperty puertaNPProperty() {
            return puertaNP;
        }

        public String getFechaHoraNP() {
            return fechaHoraNP.get();
        }

        public StringProperty fechaHoraNPProperty() {
            return fechaHoraNP;
        }

        public String getStatusNP() {
            return statusNP.get();
        }

        public StringProperty statusNPProperty() {
            return statusNP;
        }

        public String getTipoVueloNP() {
            return tipoVueloNP.get();
        }

        public StringProperty tipoVueloNPProperty() {
            return tipoVueloNP;
        }
    }
}
