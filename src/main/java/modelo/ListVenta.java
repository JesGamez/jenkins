
package modelo;

import lombok.Data;

import java.util.Date;

@Data
public class ListVenta {
    String codDoc, nroDoc, obs, nombre;
    double monto;
    Date fecha;
}
