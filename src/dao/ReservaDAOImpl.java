package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import model.Reserva;
import dao.DatabaseManager; // Clase de utilidad para la conexión a la base de datos

public class ReservaDAOImpl implements ReservaDAO {

	@Override
	public void insertReserva(Reserva reserva) {
	    String query = "INSERT INTO Reservas (Id, FechaEntrada, FechaSalida, Valor, FormaPago) VALUES (?, ?, ?, ?, ?)";
	    try (Connection connection = DatabaseManager.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setString(1, reserva.getId());
	        preparedStatement.setDate(2, new java.sql.Date(reserva.getFechaEntrada().getTime()));
	        preparedStatement.setDate(3, new java.sql.Date(reserva.getFechaSalida().getTime()));
	        preparedStatement.setDouble(4, reserva.getValor());
	        preparedStatement.setString(5, reserva.getFormaPago());

	        preparedStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


    // Otros métodos necesarios
    @Override
    public List<Reserva> buscarPorNumeroReserva(String numeroReserva) {
        List<Reserva> reservas = new ArrayList<>();
        String query = "SELECT * FROM reservas WHERE id LIKE ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             preparedStatement.setString(1, "%" + numeroReserva + "%");
             ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Reserva reserva = new Reserva();
                // Llena los atributos de 'reserva' con los valores del resultado
                // del conjunto de resultados (resultSet).
                reserva.setId(resultSet.getString("id"));
                reserva.setFechaEntrada(resultSet.getDate("fechaEntrada"));
                reserva.setFechaSalida(resultSet.getDate("fechaSalida"));
                reserva.setValor(resultSet.getDouble("valor"));
                reserva.setFormaPago(resultSet.getString("formaPago"));
                // ... otros atributos ...
                reservas.add(reserva);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }
    
    public boolean actualizarReserva(Reserva reserva) throws SQLException {
        String sql = "UPDATE reservas SET fechaEntrada=?, fechaSalida=?, valor=?, formaPago=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(reserva.getFechaEntrada().getTime()));
            stmt.setDate(2, new java.sql.Date(reserva.getFechaSalida().getTime()));
            stmt.setDouble(3, reserva.getValor());
            stmt.setString(4, reserva.getFormaPago());
            stmt.setString(5, reserva.getId());
            int filasActualizadas = stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminarReservaPorId(String idReserva) {
        String sql = "DELETE FROM reservas WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idReserva);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
