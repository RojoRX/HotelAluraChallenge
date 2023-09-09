package dao;

import java.sql.SQLException;
import java.util.List;
import model.Huesped;

public interface HuespedDAO {
    void insertHuesped(Huesped huesped);
    List<Huesped> buscarPorApellido(String apellido);
    // Agrega aquí otros métodos necesarios para la gestión de huéspedes
	void actualizarHuesped(Huesped huesped);
	boolean eliminarHuespedPorId(int idHuesped) throws SQLException;
}
