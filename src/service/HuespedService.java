package service;

import java.util.Date;
import java.util.List;
import model.Huesped;
import model.Reserva;
import dao.HuespedDAO;

public class HuespedService {
    private HuespedDAO huespedDAO;
    // Constructor
    public HuespedService(HuespedDAO huespedDAO) {
        this.huespedDAO = huespedDAO;
    }
    public Huesped registrarHuesped(String nombre, String apellido, Date fechaNacimiento,
                                 String nacionalidad, String telefono, String idReserva) {
        // Crear el objeto Huesped
    	Huesped huesped = new Huesped();
    	huesped.setNombre(nombre);
    	huesped.setApellido(apellido);
    	huesped.setFechaNacimiento(fechaNacimiento);
    	huesped.setNacionalidad(nacionalidad);
    	huesped.setTelefono(telefono);
    	huesped.setIdReserva(idReserva);
        // Registrar el huésped en la base de datos
        huespedDAO.insertHuesped(huesped);
        return huesped;
    }
    public List<Huesped> buscarHuespedesPorApellido(String apellido) {
        return huespedDAO.buscarPorApellido(apellido);
    }
}