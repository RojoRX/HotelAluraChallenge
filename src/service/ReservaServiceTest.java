package service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.ReservaDAO;
import dao.ReservaDAOImpl;
import model.Reserva;
import service.ReservaService;

public class ReservaServiceTest {
    private ReservaDAO reservaDAO;
    private ReservaService reservaService;

    @Before
    public void setUp() {
        // Inicializa tus objetos DAO y Service aquí
        reservaDAO = new ReservaDAOImpl(/* Parámetros de conexión si es necesario */);
        reservaService = new ReservaService(reservaDAO);
    }

    @After
    public void tearDown() {
        // Realiza tareas de limpieza si es necesario
    }

    @Test
    public void testBuscarReservasPorNumeroReserva() {
        // Inserta una reserva de prueba en la base de datos antes de realizar la búsqueda

        // Realiza la búsqueda por número de reserva (ID)
        String numeroReserva = "2AX7C"; // Cambia por el número de reserva que deseas buscar
        List<Reserva> reservasEncontradas = reservaService.buscarReservasPorNumeroReserva(numeroReserva);

        // Asegura que se haya encontrado al menos una reserva
        assertFalse("No se encontraron reservas para el número de reserva: " + numeroReserva,
                reservasEncontradas.isEmpty());

        // Imprime los detalles de las reservas encontradas
        for (Reserva reserva : reservasEncontradas) {
            System.out.println("ID: " + reserva.getId());
            System.out.println("Fecha de Entrada: " + reserva.getFechaEntrada());
            System.out.println("Fecha de Salida: " + reserva.getFechaSalida());
            System.out.println("Valor: " + reserva.getValor());
            System.out.println("Forma de Pago: " + reserva.getFormaPago());
            // ... otros atributos ...
            System.out.println("----");
        }
    }
}
