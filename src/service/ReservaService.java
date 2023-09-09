package service;
import java.util.Date;
import java.util.List;
import java.util.Random;
import dao.ReservaDAO;
import model.Reserva;

public class ReservaService {
	private ReservaDAO reservaDAO;
	public ReservaService(ReservaDAO reservaDAO) {
		this.reservaDAO = reservaDAO;
	}
	public Reserva crearReserva(String numeroReserva, Date fechaEntrada,
			Date fechaSalida, double valor, String formaPago) {
		// Aquí puedes calcular el valor, generar un número de reserva único,
		// etc.
		Reserva reserva = new Reserva();

		reserva.setId(numeroReserva);
		reserva.setFechaEntrada(fechaEntrada);
		reserva.setFechaSalida(fechaSalida);
		reserva.setValor(valor);
		reserva.setFormaPago(formaPago);

		// Guardar la reserva en la base de datos a través del DAO
		reservaDAO.insertReserva(reserva);
		return reserva;
	}
	public String generarNumeroReservaUnico() {
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int longitud = 5; // Longitud del número de reserva
		StringBuilder numeroReserva = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < longitud; i++) {
			int index = random.nextInt(caracteres.length());
			char caracter = caracteres.charAt(index);
			numeroReserva.append(caracter);
		}

		return numeroReserva.toString();
	}
	public List<Reserva> buscarReservasPorNumeroReserva(String numeroReserva) {
		return reservaDAO.buscarPorNumeroReserva(numeroReserva);
	}

}
