package views;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import model.Reserva;
import service.ReservaService;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import com.toedter.calendar.JDateChooser;
import dao.ReservaDAO;
import dao.ReservaDAOImpl;
import com.toedter.calendar.DateUtil;

public class EdicionReserva extends JFrame {
    private JDateChooser dateChooserFechaEntrada;
    private JDateChooser dateChooserFechaSalida;
    private JTextField txtValor;
    private Reserva reserva;
    
    JComboBox<String> cmbFormaPago;
	private void calcularValorReserva() {
	    if (dateChooserFechaEntrada.getDate() != null && dateChooserFechaSalida.getDate() != null) {
	        long diferenciaEnMilisegundos = dateChooserFechaSalida.getDate().getTime() - dateChooserFechaEntrada.getDate().getTime();
	        int cantidadDeDias = (int) TimeUnit.DAYS.convert(diferenciaEnMilisegundos, TimeUnit.MILLISECONDS) + 1;
	        System.out.println("Cantidad de días: " + cantidadDeDias); // Agrega esta línea para imprimir el valor
	        double valorDiaria = 20.0; // Reemplaza con tu valor real
	        double valorReserva = cantidadDeDias * valorDiaria;
	        System.out.println("Valor de la reserva: " + valorReserva); // Agrega esta línea para imprimir el valor
	        txtValor.setText(String.format("%.2f", valorReserva));
	    }
	}

    public EdicionReserva(Reserva reserva) {
    	ReservaDAO reservaDAO = new ReservaDAOImpl();
    	ReservaService reservaService = new ReservaService(reservaDAO);
        // Configura la ventana de edición
        setTitle("Editar Reserva");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.reserva = reserva;
        dateChooserFechaEntrada = new JDateChooser();
        dateChooserFechaSalida = new JDateChooser();
        txtValor = new JTextField();
        cmbFormaPago = new JComboBox<>(new String[] {"Tarjeta de Crédito", "Tarjeta de Débito", "Dinero en efectivo"});
        cmbFormaPago.setBounds(68, 417, 289, 38);
        cmbFormaPago.setBackground(SystemColor.text);
        cmbFormaPago.setBorder(new LineBorder(new Color(255, 255, 255), 1, true));
        cmbFormaPago.setFont(new Font("Roboto", Font.PLAIN, 16));
        // Agrega los componentes a un panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        panel.add(new JLabel("Fecha de Entrada:"));
        panel.add(dateChooserFechaEntrada);
        panel.add(new JLabel("Fecha de Salida:"));
        panel.add(dateChooserFechaSalida);
        panel.add(new JLabel("Valor:"));
        panel.add(txtValor);
        panel.add(new JLabel("Forma de Pago:"));
        panel.add(cmbFormaPago);
        // Pobla los campos de texto con los datos actuales de la reserva
        dateChooserFechaEntrada.setDate(reserva.getFechaEntrada());
        dateChooserFechaSalida.setDate(reserva.getFechaSalida());
        ((JTextComponent) dateChooserFechaEntrada.getDateEditor()).setDisabledTextColor(Color.BLACK);
        ((JTextComponent) dateChooserFechaSalida.getDateEditor()).setDisabledTextColor(Color.BLACK);
        dateChooserFechaEntrada.getDateEditor().setEnabled(false);
        dateChooserFechaSalida.getDateEditor().setEnabled(false);
        dateChooserFechaEntrada.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    Date fechaEntrada = dateChooserFechaEntrada.getDate();
                    Date fechaSalida = dateChooserFechaSalida.getDate();
                    if (fechaEntrada != null && fechaSalida != null && fechaEntrada.after(fechaSalida)) {
                        dateChooserFechaEntrada.setDate(fechaSalida);
                    }
                    calcularValorReserva();
                }
            }
        });

        dateChooserFechaSalida.addPropertyChangeListener(new PropertyChangeListener() {
		    public void propertyChange(PropertyChangeEvent evt) {
		        if ("date".equals(evt.getPropertyName())) {
		            Date fechaEntrada = dateChooserFechaEntrada.getDate();
		            Date fechaSalida = dateChooserFechaSalida.getDate();
		            if (fechaEntrada != null && fechaSalida != null && fechaSalida.before(fechaEntrada)) {
		                dateChooserFechaSalida.setDate(fechaEntrada);
		            }
		            calcularValorReserva();
		        }
		    }
		});
        txtValor.setText(String.valueOf(reserva.getValor()));
        // Agrega un botón de guardar cambios
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Obtiene las fechas seleccionadas
                    Date fechaEntrada = dateChooserFechaEntrada.getDate();
                    Date fechaSalida = dateChooserFechaSalida.getDate();
                    if (fechaSalida != null && fechaSalida.after(fechaEntrada)) {
                        // Verifica que la fecha de salida no sea menor que la fecha de entrada
                        if (fechaSalida.after(fechaEntrada)) {
                            // Obtiene la cadena de texto del JTextField txtValor
                        	String valorTexto = txtValor.getText().replace(",", ".");
                        	double valor = Double.parseDouble(valorTexto);
                            // Obtiene la cadena de texto del JTextField txtFormaPago
                            String formaPago = cmbFormaPago.getSelectedItem().toString();
                            // Actualiza la reserva con los nuevos valores
                            reserva.setFechaEntrada(fechaEntrada);
                            reserva.setFechaSalida(fechaSalida);
                            reserva.setValor(valor);
                            reserva.setFormaPago(formaPago);
                            boolean actualizado = reservaDAO.actualizarReserva(reserva);
                            JOptionPane.showMessageDialog(null, "Los datos se actualizaron correctamente.", "Actualización Exitosa", JOptionPane.INFORMATION_MESSAGE);
                            // Cierra la ventana de edición
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "La fecha de salida debe ser posterior a la fecha de entrada.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor, seleccione fechas válidas.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException | SQLException ex) {
                    // Maneja una excepción si el valor no es un número válido
                    ex.printStackTrace(); // Puedes manejar el error de otra manera si lo deseasç
                    JOptionPane.showMessageDialog(null, "Error al actualizar los datos: " + ex.getMessage(), "Error en la Actualización", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Agrega los componentes al contenido de la ventana
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnGuardar, BorderLayout.SOUTH);
    }
}
