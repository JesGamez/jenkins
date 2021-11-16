
package modelo;

import dao.BoletaImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Compra {
    
    
    private int IDCOM,IDPROV,IDMED,CANTCOM;
    private Date FECHCOM;
    private Double IMPCOM;
    
    private Proveedor proveedor;
    private Medicamento medicamento;

    public Compra() {
    }

    public Compra(int IDCOM, int IDPROV, int IDMED, int CANTCOM, Date FECHCOM, Double IMPCOM, Proveedor proveedor, Medicamento medicamento) {
        this.IDCOM = IDCOM;
        this.IDPROV = IDPROV;
        this.IDMED = IDMED;
        this.CANTCOM = CANTCOM;
        this.FECHCOM = FECHCOM;
        this.IMPCOM = IMPCOM;
        this.proveedor = proveedor;
        this.medicamento = medicamento;
    }

    public int getIDCOM() {
        return IDCOM;
    }

    public void setIDCOM(int IDCOM) {
        this.IDCOM = IDCOM;
    }

    public int getIDPROV() {
        return IDPROV;
    }

    public void setIDPROV(int IDPROV) {
        this.IDPROV = IDPROV;
    }

    public int getIDMED() {
        return IDMED;
    }

    public void setIDMED(int IDMED) {
        this.IDMED = IDMED;
    }

    public int getCANTCOM() {
        return CANTCOM;
    }

    public void setCANTCOM(int CANTCOM) {
        this.CANTCOM = CANTCOM;
    }

    public Date getFECHCOM() {
        return FECHCOM;
    }

    public void setFECHCOM(Date FECHCOM) {
        this.FECHCOM = FECHCOM;
    }

    public Double getIMPCOM() {
        return IMPCOM;
    }

    public void setIMPCOM(Double IMPCOM) {
        this.IMPCOM = IMPCOM;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    @Override
    public String toString() {
        return "Compra{" + "IDCOM=" + IDCOM + ", IDPROV=" + IDPROV + ", IDMED=" + IDMED + ", CANTCOM=" + CANTCOM + ", FECHCOM=" + FECHCOM + ", IMPCOM=" + IMPCOM + ", proveedor=" + proveedor + ", medicamento=" + medicamento + '}';
    }


    @SessionScoped
    public static class BoletaC implements Serializable {

        private List<BoletaDetalle> listaBoletaDetalle;
        private List<BoletaDetalle> listaBoletaDetalleFinal;
        private BoletaImpl dao;
        private BoletaDetalle boletaDetalle;
        private Boleta boleta;

        public BoletaC() {
            listaBoletaDetalle = new ArrayList<>();
            listaBoletaDetalleFinal = new ArrayList<>();
            dao = new BoletaImpl();
            boletaDetalle = new BoletaDetalle();
            boleta = new Boleta();
        }

        public void agregarFila() {
            try {
                BoletaDetalle boletadet = dao.agregarFila(boletaDetalle.getMedicamento().getIDMED());
                boletadet.setIDMED(this.boletaDetalle.getMedicamento().getIDMED());
                boletadet.setCANTBODE(this.boletaDetalle.getCANTBODE());
    //            boletadet.setLOTE(this.boletaDetalle.getMedicamento().getLOTMED());
                boletadet.setSUBTOT((boletadet.getMedicamento().getPRECMED() + 0.50) * this.boletaDetalle.getCANTBODE());

                this.listaBoletaDetalle.add(boletadet);
                boletaDetalle = new BoletaDetalle();
                for (BoletaDetalle boletadetalle : listaBoletaDetalle) {
                    System.out.println(boletadetalle);
                }
                sumar();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void sumar() {
            double precioTotal = 0;
            for (BoletaDetalle boletaDetalle : listaBoletaDetalle) {
                precioTotal += boletaDetalle.getSUBTOT();
            }
            System.out.println(precioTotal);
            boleta.setIMPBOLE(precioTotal);
        }

        public void eliminarFila(BoletaDetalle v) {
            try {
                listaBoletaDetalle.remove(v);
                sumar();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void registrarVenta() {
            try {
                dao.registrar(boleta);
                int idven = dao.obtenerUltimoId();
                dao.registroMultiple(listaBoletaDetalle, idven);
                listaBoletaDetalle.clear();
                listar();
                boleta = new Boleta();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    //    public void limpiarCampos() throws Exception {
    //        cadenaMed = "";
    //        CANTBODE = 1;
    //    }

    //    public void anularTmp() throws Exception {
    //        limpiarCampos();
    //        productos.clear();
    //    }

        public void listar() {
            try {
                listaBoletaDetalleFinal = dao.listar();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @PostConstruct
        public void start() {
            listar();
        }

        public List<BoletaDetalle> getListaBoletaDetalle() {
            return listaBoletaDetalle;
        }

        public void setListaBoletaDetalle(List<BoletaDetalle> listaBoletaDetalle) {
            this.listaBoletaDetalle = listaBoletaDetalle;
        }

        public List<BoletaDetalle> getListaBoletaDetalleFinal() {
            return listaBoletaDetalleFinal;
        }

        public void setListaBoletaDetalleFinal(List<BoletaDetalle> listaBoletaDetalleFinal) {
            this.listaBoletaDetalleFinal = listaBoletaDetalleFinal;
        }

        public BoletaImpl getDao() {
            return dao;
        }

        public void setDao(BoletaImpl dao) {
            this.dao = dao;
        }

        public BoletaDetalle getBoletaDetalle() {
            return boletaDetalle;
        }

        public void setBoletaDetalle(BoletaDetalle boletaDetalle) {
            this.boletaDetalle = boletaDetalle;
        }

        public Boleta getBoleta() {
            return boleta;
        }

        public void setBoleta(Boleta boleta) {
            this.boleta = boleta;
        }

    }
}
