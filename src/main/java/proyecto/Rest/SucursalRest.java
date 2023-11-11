package proyecto.Rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import proyecto.DTO.SucursalDTO;

@Component
public class SucursalRest {
    private RestTemplate restTemplate;

    @Autowired
    private SucursalRest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity addSucursal(SucursalDTO client) {
        return restTemplate.postForEntity("http://localhost:8080/sucursal",
                client, SucursalDTO.class);
    }
}
