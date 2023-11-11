package proyecto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AerolineaPresenteDTO {
    private int id;
    private String nombreAerolinea;
    private int idAeropuerto;
}
