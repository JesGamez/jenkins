/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.UsuarioD;
import lombok.Data;
import modelo.Usuario;
import org.primefaces.PrimeFaces;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@Data
@Named(value = "usuarioC")
@SessionScoped
public class UsuarioC implements Serializable {

    Usuario usuario;
    String user;
    String pass;
    int intentos = 0;

    public UsuarioC() {
        usuario = new Usuario();
    }

    public void login() {
        UsuarioD dao;
        try {
            dao = new UsuarioD();
            usuario = dao.login(user, pass);
            if (usuario != null) {
                System.out.println("ya estas en Farmavic");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("objetoUsuario", usuario);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/FARMAVIC-MAVEN/faces/vistas/Plantilla.xhtml");
            } else {
                System.out.println("no puedes entrar");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Acceso al Sistema", "Usuario y/o contraseña incorrecta"));
                if (intentos == 3) {
                    bloquearLogin();
                    intentos = 0;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL, "AVISO", "Intentos fallidos, vuelve a intentarlo"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "AVISO", "Le queda " + (3 - intentos) + " intentos."));
                    ++intentos;
                }
            }

        } catch (Exception e) {
            System.out.println("Error en el loginC" + e.getMessage());
        }

    }

    public void bloquearLogin() throws Exception {
        PrimeFaces.current().executeScript("PF('body').show();setTimeout(function() {  console.log('Formulario desactivado');PF('body').hide();},5000)");
    }
}
