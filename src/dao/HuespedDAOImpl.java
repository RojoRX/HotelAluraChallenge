package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Huesped;

public class HuespedDAOImpl implements HuespedDAO {

	@Override
	public void insertHuesped(Huesped huesped) {
	    String query = "INSERT INTO Huespedes (Nombre, Apellido, FechaNacimiento, Nacionalidad, Telefono, IdReserva) "
	                 + "VALUES (?, ?, ?, ?, ?, ?)";
	    try (Connection connection = DatabaseManager.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setString(1, huesped.getNombre());
	        preparedStatement.setString(2, huesped.getApellido());
	        preparedStatement.setDate(3, new java.sql.Date(huesped.getFechaNacimiento().getTime()));
	        preparedStatement.setString(4, huesped.getNacionalidad());
	        preparedStatement.setString(5, huesped.getTelefono());
	        preparedStatement.setString(6, huesped.getIdReserva());

	        preparedStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
    public List<Huesped> buscarPorApellido(String apellido) {
        // Implementa la lógica de búsqueda por apellido utilizando una consulta SQL
        // y devuelve la lista de huéspedes encontrados.
        List<Huesped> huespedes = new ArrayList<>();
        String query = "SELECT * FROM huespedes WHERE apellido LIKE ?";
        
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        	preparedStatement.setString(1, "%" + apellido + "%"); // Agrega "%" para buscar coincidencias parciales
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                Huesped huesped = new Huesped();
                // Llena los atributos de 'huesped' con los valores del resultado
                // del conjunto de resultados (resultSet).
                huesped.setId(resultSet.getInt("id"));
                huesped.setNombre(resultSet.getString("nombre"));
                huesped.setApellido(resultSet.getString("apellido"));
                huesped.setFechaNacimiento(resultSet.getDate("fechaNacimiento"));
                huesped.setNacionalidad(resultSet.getString("nacionalidad"));
                huesped.setTelefono(resultSet.getString("telefono"));
                huesped.setIdReserva(resultSet.getString("idReserva"));
                // ... otros atributos ...
                
                huespedes.add(huesped);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return huespedes;
    }
	@Override
	public void actualizarHuesped(Huesped huesped) {
	    String sql = "UPDATE huespedes SET nombre=?, apellido=?, fechaNacimiento=?, nacionalidad=?, telefono=? WHERE id=?";
	    try (Connection conn = DatabaseManager.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, huesped.getNombre());
	        stmt.setString(2, huesped.getApellido());
	        stmt.setDate(3, new java.sql.Date(huesped.getFechaNacimiento().getTime()));
	        stmt.setString(4, huesped.getNacionalidad());
	        stmt.setString(5, huesped.getTelefono());
	        stmt.setInt(6, huesped.getId());

	        int filasActualizadas = stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public boolean eliminarHuespedPorId(int idHuesped) throws SQLException {
	    String sql = "DELETE FROM huespedes WHERE id = ?";
	    try (Connection conn = DatabaseManager.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, idHuesped);
	        int filasAfectadas = stmt.executeUpdate();
	        return filasAfectadas > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
    // Otros métodos necesarios
}
