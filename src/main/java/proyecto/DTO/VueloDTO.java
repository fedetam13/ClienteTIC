package proyecto.DTO;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VueloDTO {
    private int idVuelo;
    private LocalDateTime horaDePartida;
    private LocalDateTime horaDePartidaEstimada;
    private LocalDateTime horaDeArribo;
    private LocalDateTime horaDeArriboEstimada;
    private boolean estaAtrasado;
    private int pueretaDeEmbarque;
    private int puertaDeArribo;

    private String aprovacionDeVuelo; //LA IDEA QUE SEA, Pendiente, Aprovado, Rechazado
    private boolean aprovacionPartida;
    private boolean aprovacionArribo;


    private int idAeropuertoPartida;

    private int idAeropuertoArribo;

    private String nombreAerolinea;

    private String matriculaAvion;


}
