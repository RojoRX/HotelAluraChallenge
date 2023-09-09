package dao;

import java.sql.SQLException;
import java.util.List;

import model.Reserva;

public interface ReservaDAO {
    void insertReserva(Reserva reserva);
    // Otros métodos necesarios
    List<Reserva> buscarPorNumeroReserva(String numeroReserva);
    boolean actualizarReserva(Reserva reserva) throws SQLException;
    boolean eliminarReservaPorId(String idReserva);
}
