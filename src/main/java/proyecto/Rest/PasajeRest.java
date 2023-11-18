package proyecto.Rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import proyecto.DTO.AvionDTO;
import proyecto.DTO.PasajeDTO;
import proyecto.Main;

import java.util.ArrayList;
import java.util.List;

@Component
public class PasajeRest {
    private RestTemplate restTemplate;

    @Autowired
    private PasajeRest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<PasajeDTO> addPasaje(PasajeDTO p) {
        return restTemplate.postForEntity(Main.serverURL+"/pasaje/post" , p, PasajeDTO.class);
    }

    public List<PasajeDTO> getPasajesByUserId(int sessionID) {
        ResponseEntity<String> response = restTemplate.getForEntity(Main.serverURL+"/pasaje/getByUserId?id=" + sessionID, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String json = response.getBody();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<PasajeDTO> pasajesUsuario = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, PasajeDTO.class));

                return pasajesUsuario;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            System.err.println("Failed to retrieve Aviones. Status code: " + response.getStatusCode());
        }
        return new ArrayList<>();

    }
}
