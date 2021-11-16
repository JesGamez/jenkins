
package dao;

import modelo.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class UsuarioD extends Conexion {
    
    public Usuario login(String user, String pass) throws Exception{
        Usuario usuario = null;
        String sql = "SELECT NOMUSU, PWUSU, NIVUSU FROM USUARIO WHERE NOMUSU=? and PWUSU=? ";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1,user);
            ps.setString(2,pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                usuario = new Usuario();
                usuario.setNOMUSU(user);
                usuario.setPWUSU(pass);
                usuario.setNIVUSU(rs.getInt("NIVUSU"));
            }
            ps.close();
            rs.close();
            return usuario;
        } catch (Exception e) {
            System.out.println("Error en usarioD");
            throw e;
        }
    } 
}
