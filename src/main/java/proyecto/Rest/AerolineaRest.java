package proyecto.Rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import proyecto.DTO.AerolineaDTO;
import proyecto.Main;

@Component
public class AerolineaRest {
    private RestTemplate restTemplate;

    @Autowired
    private AerolineaRest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity addAerolinea(AerolineaDTO client) {
        return restTemplate.postForEntity(Main.serverURL+"/aerolinea/post", client, AerolineaDTO.class);
    }

    public AerolineaDTO getAerolinea(String nombre){
        ResponseEntity<String> response = restTemplate.getForEntity(Main.serverURL + "/aerolinea/getByNombre?nombre=" + nombre, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                AerolineaDTO aerolinea = objectMapper.readValue(response.getBody(), AerolineaDTO.class);
                return aerolinea;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public AerolineaDTO getAerolineaById(int sessionID) {
        ResponseEntity<String> response = restTemplate.getForEntity(Main.serverURL + "/aerolinea/getAerolineaById?idAerolinea=" + sessionID, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                AerolineaDTO aerolinea = objectMapper.readValue(response.getBody(), AerolineaDTO.class);
                return aerolinea;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
