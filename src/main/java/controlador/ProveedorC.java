package controlador;

import dao.ProveedorImpl;
import modelo.Proveedor;
import servicios.ReniecS;
import servicios.Reporte;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import servicios.Reportes;

@Named(value = "proveedorC")
@SessionScoped
public class ProveedorC implements Serializable {

    private Proveedor pro; // Jala del modelo
    private ProveedorImpl dao; // Jala del DAO
    private List<Proveedor> listadoPro; //Jala del modelo

    public ProveedorC() {
        pro = new Proveedor();
        dao = new ProveedorImpl();
        listadoPro = new ArrayList<>();
    }

    public void registrar() throws Exception {
        try {
            pro.setUbigeo(dao.obtenerCodigoUbigeo(pro.getUbigeo())); //Obtener codigo de Ubigeo
            dao.registrar(pro);
            //Manda mensaje
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Registrado con éxito"));
            listar();
            limpiar();
        } catch (Exception e) {
            System.out.println("Error en REGISTRAR ProveedorC" + e.getMessage()); // Imprime en GlassFish
        }

    }

    public void modificar() {
        try {

            dao.modificar(pro);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificado con éxito"));
            listar();
            limpiar();
        } catch (Exception e) {
            System.out.println("Error en MODIFICAR ProveedorC" + e.getMessage());
        }
    }

    public void eliminar() {
        try {
            dao.eliminar(pro);
            //Manda mensaje
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Eliminado con éxito"));
            listar();
            limpiar();
        } catch (Exception e) {
            System.out.println("Error en ELIMINAR ProveedorC" + e.getMessage());
        }
    }

    public void buscarRUC() {
        try {
            ReniecS.buscarRuc(pro);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Busqueda", "RUC no encontrado"));
            System.out.println("error en Busqueda RUC" + e.getMessage());
        }
    }

    public void reporteProveedor() throws Exception {
        Reporte report = new Reporte();
        try {
            Map<String, Object> parameters = new HashMap();
            report.exportarPDFGlobal(parameters, "LISTADOV1.jasper", "ListadoProveedor.pdf");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PDF GENERADO", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ERROR AL GENERAR PDF", null));
            throw e;
        }
    }

    //cOMPLETA EL UBIGEO
    public List<String> completeTextUbigeo(String query) throws SQLException, Exception {
        ProveedorImpl daoUbi = new ProveedorImpl();
        return daoUbi.autocompleteUbigeo(query); //Jala del DAO PARA COMPLETAR UBIGEO
    }

    //nOSE USA AUN
    public void limpiar() {
        pro = new Proveedor();

    }

    //LISTA JALANDO DEL DAO
    public void listar() {
        try {
            listadoPro = dao.listarTodos();
        } catch (Exception e) {
            System.out.println("Error en LISTAR ProveedorControlador: " + e.getMessage());
        }

    }

    public Proveedor getPro() {
        return pro;
    }

    public void setPro(Proveedor pro) {
        this.pro = pro;
    }

    public ProveedorImpl getDao() {
        return dao;
    }

    public void setDao(ProveedorImpl dao) {
        this.dao = dao;
    }

    public List<Proveedor> getListadoPro() {
        return listadoPro;
    }

    public void setListadoPro(List<Proveedor> listadoPro) {
        this.listadoPro = listadoPro;
    }

}
