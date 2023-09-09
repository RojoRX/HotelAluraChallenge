package views;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import com.mysql.cj.exceptions.DataConversionException;
import com.toedter.calendar.JDateChooser;
import dao.HuespedDAO;
import dao.HuespedDAOImpl;
import model.Huesped;
import service.HuespedService;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import java.awt.event.*;
import java.util.Date;

public class EdicionHuesped extends JFrame {
    protected static final int MIN_LONGITUD_NOMBRE = 4;
	protected static final int MIN_LONGITUD_APELLIDO = 4;
	private JTextField txtNombre;
    private JTextField txtApellido;
    private JDateChooser dateChooserFechaNacimiento;
    private JComboBox<String> txtNacionalidad; // Nuevo campo p
    private JTextField txtTelefono;
    private Huesped huesped;

    public EdicionHuesped(Huesped huesped) {
    	HuespedDAO huespedDAO = new HuespedDAOImpl();
		HuespedService huespedService = new HuespedService(huespedDAO);
        // Configura la ventana de edición
        setTitle("Editar Huésped");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.huesped = huesped;
        txtNombre = new JTextField(huesped.getNombre());
        txtApellido = new JTextField(huesped.getApellido());
        dateChooserFechaNacimiento = new JDateChooser();
        dateChooserFechaNacimiento.setDate(huesped.getFechaNacimiento());
        dateChooserFechaNacimiento.setDateFormatString("dd/MM/yyyy");
        ((JTextComponent) dateChooserFechaNacimiento.getDateEditor()).setDisabledTextColor(Color.BLACK);
        dateChooserFechaNacimiento.getDateEditor().setEnabled(false);
        String nacionalidadHuesped = huesped.getNacionalidad();
        txtNacionalidad = new JComboBox<>(new String[] {
                "afgano-afgana", "alemán-", "alemana", "árabe-árabe", "argentino-argentina", // Agrega todas las nacionalidades aquí
            });
        txtTelefono = new JTextField(huesped.getTelefono());
        // Deshabilita la edición del campo de ID
        txtNacionalidad.setSelectedItem(nacionalidadHuesped);
        
        // Agrega los componentes a un panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(txtApellido);
        panel.add(new JLabel("Fecha de Nacimiento (dd/MM/yyyy):"));
        panel.add(dateChooserFechaNacimiento);
        panel.add(new JLabel("Nacionalidad:"));
        panel.add(txtNacionalidad);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        // Configura el ComboBox de nacionalidad
        // Agrega un botón de guardar cambios
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = txtNombre.getText();
                    String apellido = txtApellido.getText();
                    Date fechaNacimiento = dateChooserFechaNacimiento.getDate();
                    String nacionalidad = (String) txtNacionalidad.getSelectedItem();
                    String telefono = txtTelefono.getText();
                    huesped.setNombre(nombre);
                    huesped.setApellido(apellido);
                    huesped.setFechaNacimiento(fechaNacimiento);
                    huesped.setNacionalidad(nacionalidad);
                    huesped.setTelefono(telefono);
		            // Validaciones
		            // Verificar la longitud mínima de caracteres para nombre y apellido
		            if (nombre.length() < MIN_LONGITUD_NOMBRE || apellido.length() < MIN_LONGITUD_APELLIDO) {
		                JOptionPane.showMessageDialog(null, "El nombre y el apellido deben tener al menos " +
		                        MIN_LONGITUD_NOMBRE + " y " + MIN_LONGITUD_APELLIDO + " caracteres respectivamente.",
		                        "Error de validación", JOptionPane.ERROR_MESSAGE);
		                return; // Detener el proceso de registro
		            }
		            // Validación para evitar inyección SQL y caracteres especiales
		            if (!isValidInput(txtNombre.getText()) || !isValidInput(txtApellido.getText())) {
		                JOptionPane.showMessageDialog(null, "Los campos de nombre y apellido no pueden contener caracteres especiales.");
		                return;
		            }
                    
                    huespedDAO.actualizarHuesped(huesped);
                    JOptionPane.showMessageDialog(null, "Los datos se actualizaron correctamente.", "Actualización Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (Exception ex) {
                    // Maneja una excepción si la cadena de texto no se puede analizar como fecha
                    ex.printStackTrace(); // Puedes manejar el error de otra manera si lo deseas
                    JOptionPane.showMessageDialog(null, "Error al actualizar los datos: " + ex.getMessage(), "Error en la Actualización", JOptionPane.ERROR_MESSAGE);
                }
            }

			private boolean isValidInput(String input) {
			    String pattern = "^[a-zA-Z0-9 ]*$";
			    return input.matches(pattern);
			}
        });

        // Agrega los componentes al contenido de la ventana
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnGuardar, BorderLayout.SOUTH);
    }
}
