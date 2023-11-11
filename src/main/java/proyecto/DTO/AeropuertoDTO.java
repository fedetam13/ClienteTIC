package proyecto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AeropuertoDTO {
    private int idAeropuerto;
    private String nombre;
    private String ubicacion;
    private String iata;
    private int cantidadDePuertas;
    private int cantidadDePistas;
    private int cantidadDeCheckin;

    @Override
    public String toString() {
        String ret = nombre + " (" + iata + ")";
        return ret;
    }
}
