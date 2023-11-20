package proyecto.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import proyecto.DTO.AvionDTO;
import proyecto.DTO.UsuarioDTO;
import proyecto.Main;
import proyecto.Rest.AvionRest;
import proyecto.Rest.UsuarioRest;

@Controller
public class AvionController {


    @Autowired
    private AvionRest avionRest;
    @Autowired
    private UsuarioRest usuarioRest;

    public Label registrarAvionError;

    @FXML
    public TextField textfieldRegistrarAvion_Matricula;
    @FXML
    public TextField textfieldRegistrarAvion_Capacidad;
    @FXML
    public TextField textfieldRegistrarAvion_Nombre;

    @FXML
    public void cerrar(ActionEvent actionEvent) {
        Stage actual = (Stage)((Button)actionEvent.getSource()).getParent().getScene().getWindow();
        actual.close();
    }

    @FXML
    public void registrarAvion(ActionEvent actionEvent) {
        if(textfieldRegistrarAvion_Capacidad.getText().isEmpty() ||
                textfieldRegistrarAvion_Matricula.getText().isEmpty() ||
            textfieldRegistrarAvion_Nombre.getText().isEmpty()){

            registrarAvionError.setText("All fields must be filled");
            registrarAvionError.setVisible(true);
        }
        else if(avionRest.getAvion(textfieldRegistrarAvion_Matricula.getText())!=null){
            registrarAvionError.setText("Plate already in use");
            registrarAvionError.setVisible(true);
        }
        else{

            AvionDTO a = new AvionDTO();
            a.setCapacidad(Integer.parseInt(textfieldRegistrarAvion_Capacidad.getText()));
            a.setMatricula(textfieldRegistrarAvion_Matricula.getText());

            UsuarioDTO u = usuarioRest.getUserById(Main.sessionID);
            a.setNombreAerolinea(u.getNombre());

            if(avionRest.addAvion(a).getStatusCode().is2xxSuccessful()) {
                Stage actual = (Stage) textfieldRegistrarAvion_Nombre.getScene().getWindow();
                actual.close();
            }
        }

    }
}
