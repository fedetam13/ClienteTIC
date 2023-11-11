package proyecto.Rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import proyecto.DTO.PasajeDTO;

@Component
public class PasajeRest {
    private RestTemplate restTemplate;

    @Autowired
    private PasajeRest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity addPasaje(PasajeDTO client) {
        return restTemplate.postForEntity("http://localhost:8080/pasaero",
                client, PasajeDTO.class);
    }
}
