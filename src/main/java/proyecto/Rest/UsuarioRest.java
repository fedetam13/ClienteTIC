package proyecto.Rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import proyecto.DTO.UsuarioDTO;
import proyecto.Main;


@Component
public class UsuarioRest {

    private RestTemplate restTemplate;


    @Autowired
    private UsuarioRest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<UsuarioDTO> addUsers(UsuarioDTO client) {
        client.setEmail(client.getEmail().toLowerCase());
        ResponseEntity<UsuarioDTO> response = restTemplate.postForEntity(Main.serverURL+"/usuario/post", client, UsuarioDTO.class);

        return response;
    }

    public UsuarioDTO getUser(String email) {
        ResponseEntity<String> response = restTemplate.getForEntity(Main.serverURL+"/usuario/getByEmail?email=" + email.toLowerCase(), String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                UsuarioDTO usuario = objectMapper.readValue(response.getBody(), UsuarioDTO.class);
                return usuario;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public UsuarioDTO getUserById(int id){
        ResponseEntity<String> response = restTemplate.getForEntity(Main.serverURL+"/usuario/getById?idUsuario="+id, String.class);

        if(response.getStatusCode().is2xxSuccessful()){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                UsuarioDTO usuario = objectMapper.readValue(response.getBody(), UsuarioDTO.class);
                return usuario;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public ResponseEntity changePassword(UsuarioDTO u){
        ResponseEntity<UsuarioDTO> response = restTemplate.postForEntity(Main.serverURL+"/usuario/changePassword", u, UsuarioDTO.class);
        return response;
    }

}
