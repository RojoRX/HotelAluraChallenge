package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Reserva;
import dao.DatabaseManager; // Clase de utilidad para la conexión a la base de datos

public class ReservaDAOImpl implements ReservaDAO {

    @Override
    public void insertReserva(Reserva reserva) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseManager.getConnection(); // Obtiene la conexión a la base de datos

            String query = "INSERT INTO Reservas (NumeroReserva, FechaEntrada, FechaSalida, Valor, FormaPago) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reserva.getNumeroReserva()); // Agrega el número de reserva
            preparedStatement.setDate(2, new java.sql.Date(reserva.getFechaEntrada().getTime()));
            preparedStatement.setDate(3, new java.sql.Date(reserva.getFechaSalida().getTime()));
            preparedStatement.setDouble(4, reserva.getValor());
            preparedStatement.setString(5, reserva.getFormaPago());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Otros métodos necesarios
}
