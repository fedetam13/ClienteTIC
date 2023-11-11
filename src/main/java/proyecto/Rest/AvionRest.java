package proyecto.Rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import proyecto.DTO.AeropuertoDTO;
import proyecto.DTO.AvionDTO;
import proyecto.DTO.UsuarioDTO;
import proyecto.Main;

import java.util.ArrayList;
import java.util.List;

@Component
public class AvionRest {
    private RestTemplate restTemplate;

    @Autowired
    private AvionRest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity addAvion(AvionDTO client) {
        return restTemplate.postForEntity(Main.serverURL+"/avion/post", client, AvionDTO.class);
    }

    public AvionDTO getAvion(String matricula){
        ResponseEntity<String> response = restTemplate.getForEntity(Main.serverURL+ "/avion/getByMatricula?matricula=" + matricula, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                AvionDTO avion = objectMapper.readValue(response.getBody(), AvionDTO.class);
                return avion;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }


    public List<AvionDTO> getAvionesByAirlineName(String nombreAerolinea) {
        ResponseEntity<String> response = restTemplate.getForEntity(Main.serverURL+"/avion/getByAirlineName?nombreAerolinea="+nombreAerolinea,String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String json = response.getBody();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<AvionDTO> avionDTOS = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, AvionDTO.class));

                return avionDTOS;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            System.err.println("Failed to retrieve Aviones. Status code: " + response.getStatusCode());
        }
        return new ArrayList<>();
    }
}
