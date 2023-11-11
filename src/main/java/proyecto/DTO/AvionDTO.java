package proyecto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AvionDTO {
    private String matricula;
    private int capacidad;
    private String nombreAerolinea;

    @Override
    public String toString() {
        return (matricula + "  (Capacidad: " + capacidad+ " pasajeros)");
    }
}
