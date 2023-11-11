package proyecto.Rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import proyecto.DTO.AerolineaPresenteDTO;

@Component
public class AerolineaPerteneceRest {
    private RestTemplate restTemplate;

    @Autowired
    private AerolineaPerteneceRest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity addAerolinaPertenece(AerolineaPresenteDTO client) {
        return restTemplate.postForEntity("http://localhost:8080/aerolineaPertenece",
                client, AerolineaPresenteDTO.class);
    }
}
