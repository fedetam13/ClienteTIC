package proyecto.Rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import proyecto.DTO.ServicioDTO;

@Component
public class ServicioRest {
    private RestTemplate restTemplate;

    @Autowired
    private ServicioRest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity addServicio(ServicioDTO client) {
        return restTemplate.postForEntity("http://localhost:8080/servico",
                client, ServicioDTO.class);
    }
}
