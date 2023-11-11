package proyecto.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import proyecto.DTO.ServicioDTO;
import proyecto.Rest.ServicioRest;

import java.util.List;


@RestController
@RequestMapping(path="api/v1/Servicio")
public class ServicioController {
    public ServicioRest servicioRest;

    @Autowired
    public ServicioController(ServicioRest sr){
        this.servicioRest = sr;
    }


    @PostMapping
    public void registrarServicio(@RequestBody ServicioDTO servicio){
        servicioRest.addServicio(servicio);
    }


}
