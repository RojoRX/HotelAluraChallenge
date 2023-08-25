package service;

import java.util.Date;
import java.util.Random;

import dao.ReservaDAO;
import model.Reserva;

public class ReservaService {
    private ReservaDAO reservaDAO;
    public ReservaService(ReservaDAO reservaDAO) {
        this.reservaDAO = reservaDAO;
    }


    public Reserva crearReserva(Date fechaEntrada, Date fechaSalida, double valor, String formaPago) {
        // Aquí puedes calcular el valor, generar un número de reserva único, etc.
        Reserva reserva = new Reserva();
        reserva.setFechaEntrada(fechaEntrada);
        reserva.setFechaSalida(fechaSalida);
        reserva.setValor(valor);
        reserva.setFormaPago(formaPago);

        // Generar un número de reserva (puedes usar un algoritmo adecuado)
        String numeroReserva = generarNumeroReservaUnico();

        reserva.setNumeroReserva(numeroReserva);

        
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
}
