package proyecto.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.DTO.*;
import proyecto.Main;
import proyecto.Rest.*;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

@Service
public class VueloController implements Initializable {



    @Autowired
    private VueloRest vueloRest;
    @Autowired
    private UsuarioRest usuarioRest;
    @Autowired
    private AvionRest avionRest;
    @Autowired
    private AeropuertoRest aeropuertoRest;
    @Autowired
    private AerolineaRest aerolineaRest;



    @FXML
    public ChoiceBox checkBoxRegistrarVuelo_AeropuertoPartida;
    @FXML
    public ChoiceBox checkBoxRegistrarVuelo_AeropuertoDestino;

    @FXML
    public DatePicker datePickerRegistrarVueloPartida;

    @FXML
    public ChoiceBox choiceboxRegistrarVueloHoursDuracion;
    @FXML
    public ChoiceBox choiceboxRegistrarVueloMinutesDuracion;
    @FXML
    public ChoiceBox choiceboxRegistrarVueloHoursPartida;
    @FXML
    public ChoiceBox choiceboxRegistrarVueloMinutesPartida;

    @FXML
    public ChoiceBox checkBoxRegistrarVuelo_Avion;


    @Override
    public void initialize(URL location, ResourceBundle resourceBundle){
        //AEROPUERTOS
        ObservableList<AeropuertoDTO> aeropuertos = FXCollections.observableArrayList();
        List<AeropuertoDTO> aeropuertoDTOList = aeropuertoRest.getAllAeropuertos();

        for(AeropuertoDTO ap : aeropuertoDTOList){
            aeropuertos.add(ap);
        }
        checkBoxRegistrarVuelo_AeropuertoPartida.setItems(aeropuertos);
        checkBoxRegistrarVuelo_AeropuertoDestino.setItems(aeropuertos);

        //HORAS
        ObservableList<Integer> horas = FXCollections.observableArrayList();
        for(int i = 0;i<24;i++){
            horas.add(i);
        }
        choiceboxRegistrarVueloHoursDuracion.setItems(horas);
        choiceboxRegistrarVueloHoursPartida.setItems(horas);

        //MINUTOS
        ObservableList<Integer> minutos = FXCollections.observableArrayList(00,15,30,45);
        choiceboxRegistrarVueloMinutesDuracion.setItems(minutos);
        choiceboxRegistrarVueloMinutesPartida.setItems(minutos);

        //AVIONES
        UsuarioDTO u = usuarioRest.getUserById(Main.sessionID);

        ObservableList<AvionDTO> aviones = FXCollections.observableArrayList();
        List<AvionDTO> avionDTOList = avionRest.getAvionesByAirlineName(u.getNombre());

        for(AvionDTO av : avionDTOList){
            aviones.add(av);
        }
        checkBoxRegistrarVuelo_Avion.setItems(aviones);

    }

    
    @FXML
    public void agregarVuelo(ActionEvent actionEvent){
        VueloDTO v = new VueloDTO();

        UsuarioDTO u = usuarioRest.getUserById(Main.sessionID);
        v.setNombreAerolinea(u.getNombre());

        AvionDTO av = (AvionDTO) checkBoxRegistrarVuelo_Avion.getValue();
        v.setMatriculaAvion(av.getMatricula());

        AeropuertoDTO aPartida = (AeropuertoDTO)checkBoxRegistrarVuelo_AeropuertoPartida.getValue();
        v.setIdAeropuertoPartida(aPartida.getIdAeropuerto());

        AeropuertoDTO aDestino = (AeropuertoDTO)checkBoxRegistrarVuelo_AeropuertoDestino.getValue();
        v.setIdAeropuertoArribo(aDestino.getIdAeropuerto());

        v.setAprovacionDeVuelo("Pendiente");
        v.setAprovacionArribo(false);
        v.setAprovacionPartida(false);
        v.setEstaAtrasado(false);

        LocalDateTime localDateTimePartida = LocalDateTime.of(datePickerRegistrarVueloPartida.getValue(), LocalTime.of((int)choiceboxRegistrarVueloHoursPartida.getValue(),(int)choiceboxRegistrarVueloMinutesPartida.getValue()));
        v.setHoraDePartida(localDateTimePartida);
        v.setHoraDePartidaEstimada(localDateTimePartida);

        Duration duration = Duration.ofHours((int)choiceboxRegistrarVueloHoursDuracion.getValue()).plusMinutes((int)choiceboxRegistrarVueloMinutesDuracion.getValue());
        LocalDateTime localDateTimeArribo = localDateTimePartida.plus(duration);

        v.setHoraDeArribo(localDateTimeArribo);
        v.setHoraDeArriboEstimada(localDateTimeArribo);

        //ASIGNAR PUERTAS
        Random r = new Random();
        v.setPueretaDeEmbarque(r.nextInt(aPartida.getCantidadDePuertas()));
        v.setPuertaDeArribo(r.nextInt(aDestino.getCantidadDePuertas()));

        if(vueloRest.addVuelo(v).getStatusCode().is2xxSuccessful()){
            cerrar(actionEvent);
        }
    }

    public void cerrar(ActionEvent actionEvent) {
        Stage actual = (Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow();
        actual.close();
    }
}
