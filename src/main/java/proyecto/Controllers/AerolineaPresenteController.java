package proyecto.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import proyecto.Rest.AerolineaPerteneceRest;

@Controller
public class AerolineaPresenteController {
    @Autowired
    private AerolineaPerteneceRest aerolineaPerteneceRest;
}
