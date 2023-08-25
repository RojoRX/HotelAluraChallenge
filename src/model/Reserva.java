package model;

import java.util.Date;

public class Reserva {
    private int id;
    private Date fechaEntrada;
    private Date fechaSalida;
    private double valor;
    private String formaPago;
    private String numeroReserva;
    
	public Date getFechaEntrada() {
		return fechaEntrada;
	}
	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}
	public Date getFechaSalida() {
		return fechaSalida;
	}
	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public int getId() {
		return id;
	}
	public String getNumeroReserva() {
		return numeroReserva;
	}
	public void setNumeroReserva(String numeroReserva) {
		this.numeroReserva = numeroReserva;
	}

    
}
