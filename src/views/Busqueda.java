package views;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import dao.HuespedDAO;
import dao.HuespedDAOImpl;
import dao.ReservaDAO;
import dao.ReservaDAOImpl;
import model.Huesped;
import model.Reserva;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import java.awt.Toolkit;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Date;

import service.HuespedService;
import service.ReservaService;

@SuppressWarnings("serial")
public class Busqueda extends JFrame {

	private JPanel contentPane;
	private JTextField txtBuscar;
	private JTable tbHuespedes;
	private JTable tbReservas;
	private DefaultTableModel modelo;
	private DefaultTableModel modeloHuesped;
	private JLabel labelAtras;
	private JLabel labelExit;
	int xMouse, yMouse;

	private ReservaService reservaService;
	private HuespedService huespedService;
	private Reserva reservaSeleccionada;
	private List<Reserva> reservasEncontradas = new ArrayList<>();
	private Huesped huespedSeleccionado;
	private int pestañaSeleccionada; // Variable para realizar un seguimiento de
										// la pestaña actual
	protected int selectedRowG;
	protected int selectedRowR;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Busqueda frame = new Busqueda();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Busqueda() {
		ReservaDAO reservaDAO = new ReservaDAOImpl();
		// Creación de ReservaService y pasando la instancia de ReservaDAO
		ReservaService reservaService = new ReservaService(reservaDAO);
		HuespedDAO huespedDAO = new HuespedDAOImpl();
		HuespedService huespedService = new HuespedService(huespedDAO);

		setIconImage(Toolkit.getDefaultToolkit().getImage(Busqueda.class.getResource("/imagenes/lupa2.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 571);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setUndecorated(true);

		txtBuscar = new JTextField();
		txtBuscar.setBounds(536, 127, 193, 31);
		txtBuscar.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		contentPane.add(txtBuscar);
		txtBuscar.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("SISTEMA DE BÚSQUEDA");
		lblNewLabel_4.setForeground(new Color(12, 138, 199));
		lblNewLabel_4.setFont(new Font("Roboto Black", Font.BOLD, 24));
		lblNewLabel_4.setBounds(331, 62, 280, 42);
		contentPane.add(lblNewLabel_4);

		JTabbedPane panel = new JTabbedPane(JTabbedPane.TOP);
		panel.setBackground(new Color(12, 138, 199));
		panel.setFont(new Font("Roboto", Font.PLAIN, 16));
		panel.setBounds(20, 169, 865, 328);
		contentPane.add(panel);
		panel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int tabIndex = panel.getSelectedIndex();

				if (tabIndex == 0) { // Si está en la pestaña de Reservas
					pestañaSeleccionada = 0; // Actualiza la variable con el
												// índice de la pestaña
												// System.out.println("Estas en
												// Reservas");
				} else if (tabIndex == 1) { // Si está en la pestaña de
											// Huéspedes
					pestañaSeleccionada = 1; // Actualiza la variable con el
												// índice de la pestaña
												// System.out.println("Estas en
												// Huespedes");
				}
			}
		});
		tbReservas = new JTable();
		tbReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbReservas.setFont(new Font("Roboto", Font.PLAIN, 16));
		modelo = (DefaultTableModel) tbReservas.getModel();
		modelo.addColumn("Numero de Reserva");
		modelo.addColumn("Fecha Check In");
		modelo.addColumn("Fecha Check Out");
		modelo.addColumn("Valor");
		modelo.addColumn("Forma de Pago");
		JScrollPane scroll_table = new JScrollPane(tbReservas);
		panel.addTab("Reservas", new ImageIcon(Busqueda.class.getResource("/imagenes/reservado.png")), scroll_table,
				null);
		scroll_table.setVisible(true);
		tbReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Esto
																			// //
																			// fila
		tbReservas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedRow = tbReservas.getSelectedRow();
				selectedRowR = tbReservas.getSelectedRow();
				if (selectedRow >= 0) {
					// Obtén la reserva seleccionada desde tu modelo de tabla
					reservaSeleccionada = obtenerReservaDesdeTabla(selectedRow);
				}
			}

			private Reserva obtenerReservaDesdeTabla(int rowIndex) {
				DefaultTableModel modelo = (DefaultTableModel) tbReservas.getModel();
				if (rowIndex >= 0 && rowIndex < modelo.getRowCount()) {
					// Obtiene los datos de la fila seleccionada
					String id = (String) modelo.getValueAt(rowIndex, 0);
					Date fechaEntrada = (Date) modelo.getValueAt(rowIndex, 1); 
					Date fechaSalida = (Date) modelo.getValueAt(rowIndex, 2); 
					double valor = (double) modelo.getValueAt(rowIndex, 3); 
					String formaPago = (String) modelo.getValueAt(rowIndex, 4); 


					// Crea un objeto Reserva con los datos
					Reserva reserva = new Reserva();
					reserva.setId(id);
					reserva.setFechaEntrada(fechaEntrada);
					reserva.setFechaSalida(fechaSalida);
					reserva.setValor(valor);
					reserva.setFormaPago(formaPago);

					return reserva;
				}

				// Si rowIndex no es válido o hubo un error, retorna null
				return null;
			}
		});

		tbHuespedes = new JTable();
		tbHuespedes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbHuespedes.setFont(new Font("Roboto", Font.PLAIN, 16));
		modeloHuesped = (DefaultTableModel) tbHuespedes.getModel();
		modeloHuesped.addColumn("Número de Huesped");
		modeloHuesped.addColumn("Nombre");
		modeloHuesped.addColumn("Apellido");
		modeloHuesped.addColumn("Fecha de Nacimiento");
		modeloHuesped.addColumn("Nacionalidad");
		modeloHuesped.addColumn("Telefono");
		modeloHuesped.addColumn("Número de Reserva");

		tbHuespedes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbHuespedes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedRow = tbHuespedes.getSelectedRow();
				selectedRowG = tbHuespedes.getSelectedRow();
				if (selectedRow >= 0) {
					// Obtén el huésped seleccionado desde tu modelo de tabla
					huespedSeleccionado = obtenerHuespedDesdeTabla(selectedRow);
					// Ahora tienes el huésped seleccionado para editar
					// Puedes pasarlo a tu ventana de edición de huéspedes o
					// hacer lo que necesites con él
				}
			}

			private Huesped obtenerHuespedDesdeTabla(int rowIndex) {
				DefaultTableModel modelo = (DefaultTableModel) tbHuespedes.getModel();
				if (rowIndex >= 0 && rowIndex < modelo.getRowCount()) {
					// Obtiene los datos de la fila seleccionada
					int id = (int) modelo.getValueAt(rowIndex, 0); 
					String nombre = (String) modelo.getValueAt(rowIndex, 1);
					String apellido = (String) modelo.getValueAt(rowIndex, 2); 
					Date fechaNacimiento = (Date) modelo.getValueAt(rowIndex, 3); 
					String nacionalidad = (String) modelo.getValueAt(rowIndex, 4);
					String telefono = (String) modelo.getValueAt(rowIndex, 5); 
					String idReserva = (String) modelo.getValueAt(rowIndex, 6); 
	
					// Crea un objeto Huesped con los datos
					Huesped huesped = new Huesped();
					huesped.setId(id);
					huesped.setNombre(nombre);
					huesped.setApellido(apellido);
					huesped.setFechaNacimiento(fechaNacimiento);
					huesped.setNacionalidad(nacionalidad);
					huesped.setTelefono(telefono);
					huesped.setIdReserva(idReserva);
					return huesped;
				}
				// Si rowIndex no es válido o hubo un error, retorna null
				return null;
			}
		});

		JScrollPane scroll_tableHuespedes = new JScrollPane(tbHuespedes);
		panel.addTab("Huéspedes", new ImageIcon(Busqueda.class.getResource("/imagenes/pessoas.png")),
				scroll_tableHuespedes, null);
		scroll_tableHuespedes.setVisible(true);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(Busqueda.class.getResource("/imagenes/Ha-100px.png")));
		lblNewLabel_2.setBounds(56, 51, 104, 107);
		contentPane.add(lblNewLabel_2);

		JPanel header = new JPanel();
		header.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				headerMouseDragged(e);

			}
		});
		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				headerMousePressed(e);
			}
		});
		header.setLayout(null);
		header.setBackground(Color.WHITE);
		header.setBounds(0, 0, 910, 36);
		contentPane.add(header);

		JPanel btnAtras = new JPanel();
		btnAtras.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MenuUsuario usuario = new MenuUsuario();
				usuario.setVisible(true);
				dispose();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				btnAtras.setBackground(new Color(12, 138, 199));
				labelAtras.setForeground(Color.white);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnAtras.setBackground(Color.white);
				labelAtras.setForeground(Color.black);
			}
		});
		btnAtras.setLayout(null);
		btnAtras.setBackground(Color.WHITE);
		btnAtras.setBounds(0, 0, 53, 36);
		header.add(btnAtras);

		labelAtras = new JLabel("<");
		labelAtras.setHorizontalAlignment(SwingConstants.CENTER);
		labelAtras.setFont(new Font("Roboto", Font.PLAIN, 23));
		labelAtras.setBounds(0, 0, 53, 36);
		btnAtras.add(labelAtras);

		JPanel btnexit = new JPanel();
		btnexit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MenuUsuario usuario = new MenuUsuario();
				usuario.setVisible(true);
				dispose();
			}

			@Override
			public void mouseEntered(MouseEvent e) { // Al usuario pasar el
														// mouse por el botón
														// este cambiará de
														// color
				btnexit.setBackground(Color.red);
				labelExit.setForeground(Color.white);
			}

			@Override
			public void mouseExited(MouseEvent e) { // Al usuario quitar el
													// mouse por el botón este
													// volverá al estado
													// original
				btnexit.setBackground(Color.white);
				labelExit.setForeground(Color.black);
			}
		});
		btnexit.setLayout(null);
		btnexit.setBackground(Color.WHITE);
		btnexit.setBounds(857, 0, 53, 36);
		header.add(btnexit);

		labelExit = new JLabel("X");
		labelExit.setHorizontalAlignment(SwingConstants.CENTER);
		labelExit.setForeground(Color.BLACK);
		labelExit.setFont(new Font("Roboto", Font.PLAIN, 18));
		labelExit.setBounds(0, 0, 53, 36);
		btnexit.add(labelExit);

		JSeparator separator_1_2 = new JSeparator();
		separator_1_2.setForeground(new Color(12, 138, 199));
		separator_1_2.setBackground(new Color(12, 138, 199));
		separator_1_2.setBounds(539, 159, 193, 2);
		contentPane.add(separator_1_2);

		JPanel btnbuscar = new JPanel();
		btnbuscar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String valorBusqueda = txtBuscar.getText().trim();
				buscarResultados(valorBusqueda);
			}

			private void buscarResultados(String valorBusqueda) {
				int tabIndex = panel.getSelectedIndex();

				if (tabIndex == 0) { // Pestaña de Reservas
					buscarReservasPorNumeroReserva(valorBusqueda);
				} else if (tabIndex == 1) { // Pestaña de Huéspedes
					buscarHuespedesPorApellido(valorBusqueda);
				}
			}

			private void buscarHuespedesPorApellido(String apellido) {
				List<Huesped> huespedesEncontrados = huespedService.buscarHuespedesPorApellido(apellido);

				modeloHuesped.setRowCount(0); // Limpia los datos actuales de la
												// tabla
				if(huespedesEncontrados.isEmpty()) {
					JOptionPane.showMessageDialog(separator_1_2, "No se encontraron Huespedes que coincidan");
				}
				else {
					for (Huesped huesped : huespedesEncontrados) {
						Object[] rowData = { huesped.getId(), huesped.getNombre(), huesped.getApellido(),
								huesped.getFechaNacimiento(), huesped.getNacionalidad(), huesped.getTelefono(),
								huesped.getIdReserva() };
						modeloHuesped.addRow(rowData); // Agrega los datos a la
														// tabla de huéspedes
					}
				}

			}
			private void buscarReservasPorNumeroReserva(String numeroReserva) {
				List<Reserva> reservasEncontradas = reservaService.buscarReservasPorNumeroReserva(numeroReserva);

				modelo.setRowCount(0);

				if (reservasEncontradas.isEmpty()) {
					// Mostrar un mensaje en la tabla en caso de no encontrar
					// resultados
					JOptionPane.showMessageDialog(separator_1_2, "No se encontraron reservas que coincidan");
				} else {
					for (Reserva reserva : reservasEncontradas) {
						Object[] rowData = { reserva.getId(), reserva.getFechaEntrada(), reserva.getFechaSalida(),
								reserva.getValor(), reserva.getFormaPago() };
						modelo.addRow(rowData);
					}
				}
			}

		});

		btnbuscar.setLayout(null);
		btnbuscar.setBackground(new Color(12, 138, 199));
		btnbuscar.setBounds(748, 125, 122, 35);
		btnbuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		contentPane.add(btnbuscar);

		JLabel lblBuscar = new JLabel("BUSCAR");
		lblBuscar.setBounds(0, 0, 122, 35);
		btnbuscar.add(lblBuscar);
		lblBuscar.setHorizontalAlignment(SwingConstants.CENTER);
		lblBuscar.setForeground(Color.WHITE);
		lblBuscar.setFont(new Font("Roboto", Font.PLAIN, 18));

		JButton btnEditar = new JButton("EDITAR"); // Crea un botón con el texto
													// "EDITAR"
		btnEditar.setBounds(635, 508, 122, 35);
		btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		contentPane.add(btnEditar);

		btnEditar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pestañaSeleccionada == 0) { // Reservas
					editarReservaSeleccionada();
				} else if (pestañaSeleccionada == 1) { // Huéspedes
					editarHuespedSeleccionado();
				}
			}

			private void editarReservaSeleccionada() {
				if (reservaSeleccionada != null) {
					try {
						System.out.println("Botón Editar presionado para Reservas");
						EdicionReserva edicionReserva = new EdicionReserva(reservaSeleccionada);
						edicionReserva.setVisible(true);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					System.out.println("Reserva seleccionada es nula");
				}
			}

			private void editarHuespedSeleccionado() {
				if (huespedSeleccionado != null) {
					try {
						EdicionHuesped edicionHuesped = new EdicionHuesped(huespedSeleccionado);
						edicionHuesped.setVisible(true);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					System.out.println("Huésped seleccionado es nulo");
				}
			}
		});

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setLayout(null);
		btnEliminar.setBackground(new Color(12, 138, 199));
		btnEliminar.setBounds(767, 508, 122, 35);
		btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		contentPane.add(btnEliminar);

		JLabel lblEliminar = new JLabel("ELIMINAR");
		lblEliminar.setHorizontalAlignment(SwingConstants.CENTER);
		lblEliminar.setForeground(Color.WHITE);
		lblEliminar.setFont(new Font("Roboto", Font.PLAIN, 18));
		lblEliminar.setBounds(0, 0, 122, 35);
		btnEliminar.add(lblEliminar);
		setResizable(false);

		btnEliminar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pestañaSeleccionada == 0) { // Reservas
					eliminarReservaSeleccionada();
				} else if (pestañaSeleccionada == 1) { // Huéspedes
					eliminarHuespedSeleccionado();
				}
			}

			private void eliminarReservaSeleccionada() {
				if (reservaSeleccionada != null) {
					try {
						String idReservaSeleccionada = reservaSeleccionada.getId();
						int opcion = JOptionPane.showConfirmDialog(null,
								"¿Estás seguro de que deseas eliminar esta Reserva?", "Confirmar eliminación",
								JOptionPane.YES_NO_OPTION);
						if (opcion == JOptionPane.YES_OPTION) {
							reservaDAO.eliminarReservaPorId(idReservaSeleccionada);
							JOptionPane.showMessageDialog(null, "La reserva fue borrada con exito!!");
							modelo.removeRow(selectedRowG);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "No se pudo borrar la reserva");
					}
				} else {
					System.out.println("Reserva seleccionada es nula");
				}
			}

			private void eliminarHuespedSeleccionado() {
				if (huespedSeleccionado != null) {
					try {
						int idHuespedSeleccionado = huespedSeleccionado.getId();
						int opcion = JOptionPane.showConfirmDialog(null,
								"¿Estás seguro de que deseas eliminar este Huesped?", "Confirmar eliminación",
								JOptionPane.YES_NO_OPTION);
						if (opcion == JOptionPane.YES_OPTION) {
							boolean eliminacionExitosa = huespedDAO.eliminarHuespedPorId(idHuespedSeleccionado);
							if (eliminacionExitosa) {
								JOptionPane.showMessageDialog(null, "Huésped eliminado exitosamente.");
								modeloHuesped.removeRow(selectedRowG);
							} else {
								JOptionPane.showMessageDialog(null, "Error al eliminar el huésped.");
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					System.out.println("Huésped seleccionado es nulo");
				}
			}

		});

	}

	// Código que permite mover la ventana por la pantalla según la posición de
	// "x" y "y"
	private void headerMousePressed(java.awt.event.MouseEvent evt) {
		xMouse = evt.getX();
		yMouse = evt.getY();
	}

	private void headerMouseDragged(java.awt.event.MouseEvent evt) {
		int x = evt.getXOnScreen();
		int y = evt.getYOnScreen();
		this.setLocation(x - xMouse, y - yMouse);
	}
}
