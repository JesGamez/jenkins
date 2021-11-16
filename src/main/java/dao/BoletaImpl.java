package dao;

import modelo.Boleta;
import modelo.BoletaDetalle;
import modelo.Medicamento;
import servicios.UtilToSql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BoletaImpl extends Conexion {

    public BoletaDetalle agregarFila(int idmed) throws SQLException, Exception {

        BoletaDetalle boletaDetalle = null;
        String sql2 = "Select idmed,presmed,commed,precmed,stocmed from MEDICAMENTO\n"
                + "where IDMED = " + idmed;

        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                boletaDetalle = new BoletaDetalle();
                Medicamento medicamento = new Medicamento();

                boletaDetalle.setIDMED(rs.getInt("IDMED"));

                medicamento.setPRESMED(rs.getString("PRESMED"));
                medicamento.setCOMMED(rs.getString("COMMED"));
                medicamento.setPRECMED(rs.getDouble("PRECMED"));
                medicamento.setSTOCMED(rs.getInt("STOCMED"));

                boletaDetalle.setMedicamento(medicamento);
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error en AGREGAR FILA BoletaImpl " + e.getMessage());
            e.printStackTrace();
        } finally {
            this.cerrar();
            return boletaDetalle;
        }
    }

    public void registroMultiple(List<BoletaDetalle> listaVentaDetalle, int idBoleta) throws Exception {
        String sql = "INSERT INTO BOLETA_DETALLE VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            for (BoletaDetalle ventadetalle : listaVentaDetalle) {
                ps.setInt(1, ventadetalle.getCANTBODE());
                ps.setInt(2, idBoleta);
                ps.setInt(3, ventadetalle.getIDMED());
                ps.setDouble(4, ventadetalle.getSUBTOT());
                ps.setString(5, "A");
                ps.executeUpdate();
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.cerrar();
        }
    }

    public int obtenerUltimoId() {
        try {

            PreparedStatement ps1 = this.conectar().prepareStatement("SELECT MAX(v.idbole) as IDBOLE\n"
                    + "FROM BOLETA v");
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                return rs.getInt("IDBOLE");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error en obtenerUltimoId BoletaImpl" + e.getMessage());
        }
        return -1;
    }

    public void registrar(Boleta ven) throws Exception {
        String sql = "INSERT INTO BOLETA VALUES ((select getdate()),?,?,?)";
        String sql2 = "select now()";
        try {
//            Date d = null;
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            PreparedStatement ps1 = this.conectar().prepareStatement(sql2);
            if (ven.getFECHEMBOLE() == null) {
                ven.setFECHEMBOLE(UtilToSql.convert(Date.from(Instant.now())));
            }
            ps.setTimestamp(1, UtilToSql.convert(ven.getFECHEMBOLE()));//servicio covert
            ps.setInt(2, ven.getIDCLI());
            ps.setInt(3, ven.getIDVEN());
            ps.setDouble(4, ven.getIMPBOLE());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.cerrar();
        }
    }

    public List<BoletaDetalle> listar() throws Exception {
        List<BoletaDetalle> listado = new ArrayList<>();
        BoletaDetalle vendet;
        Medicamento med;
        Boleta ven;
        String sql = "SELECT \n"
                + "	BD.CANTBODE,\n"
                + "	bd.SUBTOT,\n"
                + "	M.IDMED,\n"
                + "     M.GENMED,\n"
                + "     M.COMMED,\n"
                + "	B.IDBOLE,\n"
                + "	B.FECHEMBOLE,\n"
                + "	C.NOMCLI \n"
                + "     FROM BOLETA_DETALLE BD \n"
                + "	INNER JOIN MEDICAMENTO AS M ON \n"
                + "	BD.IDMED = M.IDMED\n"
                + "     INNER JOIN BOLETA B ON \n"
                + "	BD.IDBOLE = B.IDBOLE\n"
                + "     INNER JOIN CLIENTE C ON \n"
                + "	B.IDCLI = C.IDCLI\n"
                + "     ORDER BY B.IDBOLE DESC";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                vendet = new BoletaDetalle();
                vendet.setCANTBODE(rs.getInt("CANTBODE"));
                vendet.setSUBTOT(rs.getDouble("SUBTOT"));
                
                med = new Medicamento();
                med.setIDMED(rs.getInt("IDMED"));
                med.setGENMED(rs.getString("GENMED"));
                med.setCOMMED(rs.getString("COMMED"));
                vendet.setMedicamento(med);
                
                ven = new Boleta();
                ven.setIDBOLE(rs.getInt("IDBOLE"));
                ven.setFECHEMBOLE(rs.getDate("FECHEMBOLE"));
                vendet.setBoleta(ven);
                
                vendet.setNOMCLI(rs.getString("NOMCLI"));
                listado.add(vendet);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en el listado BoletaImpl" + e.getMessage());
        } finally {
            this.cerrar();
        }
        return listado;
    }

}