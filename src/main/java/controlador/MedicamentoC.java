package controlador;

import dao.MedicamentoImpl;
import lombok.Data;
import modelo.Medicamento;
import servicios.ReporteS;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Named(value = "medicamentoC")
@ConversationScoped
public class MedicamentoC implements Serializable {

    Medicamento med;
    MedicamentoImpl dao;
    List<Medicamento> listadomed;
    int Listado = 1;

    public MedicamentoC() {
        med = new Medicamento();
        dao = new MedicamentoImpl();
        listadomed = new ArrayList<>();
    }

    public void registrar() throws Exception {
        try {
            if (!dao.existePresentacion(med, listadomed) && !dao.existeLote(med, listadomed)) {
                med.setIDPROV(dao.obtenerCodigoProveedor(med.getIDPROV()));
                dao.registrar(med);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "GENIAL", "Registro exitoso"));
                listar();
                limpiar();
            } else {
                if (dao.existePresentacion(med, listadomed)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "ADVENTENCIA", "Presentación existente"));
                }
                if (dao.existeLote(med, listadomed)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "ADVENTENCIA", "Lote existente"));
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR", "Registro fallido"));
            System.out.println("Error en REGISTRAR MedicamentoC" + e.getMessage());
        }
        med = new Medicamento();
        listar();
    }

    public void modificar() throws Exception {
        try {
            dao.modificar(med);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "GENIAL", "Modificado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR", "Modificación fallida"));
            System.out.println("Error en MODIFICAR MedicamentoC" + e.getMessage());
        }
    }

    public void eliminar() throws Exception {
        try {
            dao.eliminar(med);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "GENIAL", "Eliminado con éxito"));
            listar();
            limpiar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR", "Eliminación fallida"));
            System.out.println("Error en ELIMINAR MedicamentoC" + e.getMessage());
        }
    }

    public void Desactivar() throws Exception {
        try {
            dao.desactivar(med);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Desactivado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR", "Desactivación fallida"));
            System.out.println("Error en DESACTIVAR MedicamentoC " + e.getMessage());
        }
    }

    public List<String> completeTextProveedor(String query) throws SQLException, Exception {
        MedicamentoImpl daoPro = new MedicamentoImpl();
        return daoPro.autocompleteProveedor(query);
    }

    public void limpiar() {
        med = new Medicamento();

    }

    public void listar() {
        try {
            listadomed = dao.listar(Listado);
        } catch (Exception e) {
            System.out.println("Error en LISTAR MedicamentoC: " + e.getMessage());
        }
    }

    public void verReportePDFMEDI() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        ReporteS reporteMedi = new ReporteS();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath("reporte/ReporteMedicamento.jasper");
        String numeroinformesocial = null;
        System.out.println("El medicamento es: " + numeroinformesocial);
        reporteMedi.getReportePdf(root, numeroinformesocial);
        FacesContext.getCurrentInstance().responseComplete();
    }

    @PostConstruct
    public void construir() {
        listar();
    }

}
