package service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.HuespedDAO;
import dao.HuespedDAOImpl;
import model.Huesped;

public class HuespedServiceTest {
    private HuespedDAO huespedDAO;
    private HuespedService huespedService;

    @Before
    public void setUp() {
        // Inicializa tus objetos DAO y Service aquí
        huespedDAO = new HuespedDAOImpl(/* Parámetros de conexión si es necesario */);
        huespedService = new HuespedService(huespedDAO);
    }

    @After
    public void tearDown() {
        // Realiza tareas de limpieza si es necesario
    }

    @Test
    public void testBuscarHuespedesPorApellido() {
        // Inserta huéspedes de prueba en la base de datos antes de realizar la búsqueda

        // Realiza la búsqueda por apellido
        String apellido = "qwrw"; // Cambia por el apellido que deseas buscar
        List<Huesped> huespedesEncontrados = huespedService.buscarHuespedesPorApellido(apellido);

        // Asegura que se hayan encontrado al menos un huésped
        assertFalse("No se encontraron huéspedes para el apellido: " + apellido,
                huespedesEncontrados.isEmpty());

        // Imprime los detalles de los huéspedes encontrados
        for (Huesped huesped : huespedesEncontrados) {
            System.out.println("ID: " + huesped.getId());
            System.out.println("Nombre: " + huesped.getNombre());
            System.out.println("Apellido: " + huesped.getApellido());
            System.out.println("Fecha de Nacimiento: " + huesped.getFechaNacimiento());
            System.out.println("Nacionalidad: " + huesped.getNacionalidad());
            System.out.println("Teléfono: " + huesped.getTelefono());
            System.out.println("Número de Reserva: " + huesped.getIdReserva());
            // ... otros atributos ...
            System.out.println("----");
        }
    }
}
