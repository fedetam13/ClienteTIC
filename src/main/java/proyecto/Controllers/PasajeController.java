package proyecto.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import proyecto.Rest.PasajeRest;

@Controller
public class PasajeController {
    @Autowired
    private PasajeRest pasajeRest;
}
