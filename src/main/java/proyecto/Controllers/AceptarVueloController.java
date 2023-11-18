package proyecto.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import proyecto.DTO.AeropuertoDTO;
import proyecto.DTO.VueloDTO;
import proyecto.Main;
import proyecto.Rest.AeropuertoRest;
import proyecto.Rest.VueloRest;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Controller
public class AceptarVueloController implements Initializable {

    private int selectedId;
    private String tipoDeVuelo;

    @Autowired
    AeropuertoController aeropuertoController;

    @Autowired
    AeropuertoRest aeropuertoRest;

    @Autowired
    VueloRest vueloRest;

    public AceptarVueloController(){}

    public AceptarVueloController(int selectedId, String tipoDeVuelo) {
        this.selectedId = selectedId;
        this.tipoDeVuelo = tipoDeVuelo;
    }

    @FXML
    public ChoiceBox choiceBoxPuertasDisponibles;



    public void setSelectedID(int selectedId) {
        this.selectedId = selectedId;
        initialize(null,null);
    }

    public void setTipoVuelo(String tipoDeVuelo) {
        this.tipoDeVuelo = tipoDeVuelo;
    }



    public void closeAsignarPuerta(ActionEvent actionEvent) {
        Stage actual = (Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow();
        actual.close();
    }

    public void asignarPuerta(ActionEvent actionEvent) {
        VueloDTO v = vueloRest.getVueloById(selectedId);
        if(tipoDeVuelo=="Arribo"){
            v.setAprovacionArribo("A");
            v.setPuertaDeArribo((Integer) choiceBoxPuertasDisponibles.getValue());
        } else {
            v.setAprovacionPartida("A");
            v.setPueretaDeEmbarque((Integer) choiceBoxPuertasDisponibles.getValue());
        }
        vueloRest.updateVuelo(selectedId,v);
        aeropuertoController.reloadFlights();
        closeAsignarPuerta(actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(selectedId!=0){

            List<Integer> puertasOcupadas = new ArrayList<>();

            if(Objects.equals(tipoDeVuelo,"Arribo")){
                puertasOcupadas = vueloRest.getPuertasOcupadasArribo(selectedId);
            }else{
                puertasOcupadas = vueloRest.getPuertasOcupadasPartida(selectedId);
            }

            AeropuertoDTO a = aeropuertoRest.getAeropuertoByNombre(Main.sessionName);
            List<Integer> puertasD = new ArrayList<>();
            for(int i = 1;i<=a.getCantidadDePuertas();i++){
                puertasD.add(i);
            }

            for(Integer i : puertasOcupadas){

                puertasD.remove(i);
            }

            ObservableList<Integer> puertasDisponibles = FXCollections.observableArrayList(puertasD);

            choiceBoxPuertasDisponibles.setItems(puertasDisponibles);
        }


    }
}
