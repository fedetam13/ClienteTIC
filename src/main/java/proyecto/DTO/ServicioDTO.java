package proyecto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServicioDTO {
    private int idServicio;
    private String nombre;
    private int costo;
    private int capacidad;
}
