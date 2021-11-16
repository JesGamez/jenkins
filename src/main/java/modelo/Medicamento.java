package modelo;

import lombok.Data;

import java.util.Date;

@Data
public class Medicamento {

    int IDMED,STOCMED;
    String PRESMED, GENMED, COMMED,LOTMED,ESTMED,IDPROV;
    Double PRECMED;
    Date FVMED;
    
    Proveedor proveedor = new Proveedor();
    
}
