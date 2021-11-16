package dao;


import modelo.Proveedor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProveedorImpl extends Conexion implements ICRUD<Proveedor> {

    @Override
    public void registrar(Proveedor pro) throws Exception {
        String sql = "EXEC spInsertProveedor ?,?,?,?,?,?,?,?,?,?";
                
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, pro.getNombre());
            ps.setString(2, pro.getRuc());
            ps.setString(3, pro.getTelefono());
            ps.setString(4, pro.getEmail());
            ps.setString(5, pro.getTipo());
            ps.setString(6, pro.getNcomercial());
            ps.setString(7, pro.getAbreviatura());
            ps.setString(8, pro.getDireccion());
            ps.setString(9, pro.getUbigeo());
            ps.setString(10, "A");
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al INGRESAR ProveedorImpl" + e.getMessage());
        } finally {
            this.cerrar();
        }
    }

//    "insert into medicamento (Presentacion, Generico, Comercial, Precio, Vencimiento, Formula, Lote, Concentracion, Via)"
    @Override
    public void modificar(Proveedor pro) throws Exception {
        String sql = "EXEC spUpdateProveedor ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, pro.getNombre());
            ps.setString(2, pro.getRuc());
            ps.setString(3, pro.getTelefono());
            ps.setString(4, pro.getEmail());
            ps.setString(5, pro.getTipo());
            ps.setString(6, pro.getNcomercial());
            ps.setString(7, pro.getAbreviatura());
            ps.setString(8, pro.getDireccion());
            ps.setString(9, pro.getUbigeo());
            ps.setInt(10, pro.getIde());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en MODIFICAR ProveedorImpl " + e.getMessage());
        }
    }
    @Override
    public void eliminar(Proveedor pro) throws Exception {
//        this.conectar();
        String sql = "UPDATE PROVEEDOR SET ESTPROV=? WHERE IDPROV=? ";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, "I");
            ps.setInt(2, pro.getIde());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en ELIMINAR ProveedorImpl" + e.getMessage());
        }
    }
    
    
//     public void eliminar(Facturador fac) throws Exception {
//        String sql = "UPDATE FACTURADOR set ESTFAC = 'I' where RUCFAC=?";
//        try {
//            PreparedStatement ps = this.conectar().prepareStatement(sql);
//            ps.setString(1, fac.getRUCFAC());
//            ps.executeUpdate();
//            ps.close();
//            System.out.println("Estoy en eliminar Dao " + fac.getRUCFAC());
//        } catch (Exception e) {
//            System.out.println("Error en eliminarD" + e.getMessage());
            

    
    
    //LISTAR
    
    public List<Proveedor> listarTodos() throws Exception {
        List<Proveedor> listado = null;
        Proveedor prov;
        String sql = "SELECT * FROM vListar";
        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                prov = new Proveedor();
                prov.setIde(rs.getInt("IDPROV"));
                prov.setNombre(rs.getString("NOMPROV"));
                prov.setRuc(rs.getString("RUCPROV"));
                prov.setTelefono(rs.getString("TELPROV"));
                prov.setEmail(rs.getString("EMAPROV"));
                prov.setTipo(rs.getString("TIPPROV"));
                prov.setNcomercial(rs.getString("NCOMPROV"));
                prov.setAbreviatura(rs.getString("ABREPROV"));
                prov.setDireccion(rs.getString("DIRPROV"));
                prov.setUbigeo(rs.getString("DISUBI"));
                listado.add(prov);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en listarTodos ProveedorImpl: " + e.getMessage());
        } finally {
            this.cerrar();
        }
        return listado;
    }
    
    public List<String> autocompleteUbigeo(String consulta) throws SQLException, Exception {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT TOP 5 CONCAT(DISUBI, ', ', PROUBI, ', ',DEPUBI) AS UBIGEODESC FROM UBIGEO WHERE DISUBI LIKE ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, "%" + consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("UBIGEODESC"));
            }
        } catch (Exception e) {
            System.out.println("Error en autocompleteUbigeoDao" + e.getMessage());
        }
        return lista;
    }
    
    public String obtenerCodigoUbigeo(String cadenaUbi) throws SQLException, Exception {
        String sql = "SELECT IDUBI FROM UBIGEO WHERE CONCAT(DISUBI, ', ', PROUBI, ', ',DEPUBI) = ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, cadenaUbi);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("IDUBI");
            }
            return rs.getString("IDUBI");
        } catch (Exception e) {
            System.out.println("Error en obtenerCodigoUbigeo " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Proveedor> listar() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
