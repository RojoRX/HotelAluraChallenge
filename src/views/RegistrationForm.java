package views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import dao.UserDAO;
import service.RegistrationService;
import dao.DatabaseManager;
import views.MenuPrincipal;
import views.Login;

public class RegistrationForm extends JFrame {
    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnRegister;
    private Connection yourDatabaseConnection;

    public RegistrationForm() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 50, 100, 20);
        contentPane.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(160, 50, 150, 20);
        contentPane.add(txtUsername);
        txtUsername.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 100, 100, 20);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(160, 100, 150, 20);
        contentPane.add(txtPassword);

        btnRegister = new JButton("Register");
        btnRegister.setBounds(160, 150, 80, 25);
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        contentPane.add(btnRegister);
        try {
            yourDatabaseConnection = DatabaseManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
     // Botón para volver al menú principal
        JButton btnBackToMenu = new JButton("Back to Menu");
        btnBackToMenu.setBounds(60, 200, 120, 30);
        contentPane.add(btnBackToMenu);
        btnBackToMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuPrincipal menuPrincipal = new MenuPrincipal();
                menuPrincipal.setVisible(true);
                dispose();
            }
        });

        // Botón para ir a la ventana de inicio de sesión
        JButton btnGoToLogin = new JButton("Go to Login");
        btnGoToLogin.setBounds(200, 200, 120, 30);
        contentPane.add(btnGoToLogin);
        btnGoToLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Login login = new Login();
                login.setVisible(true);
                dispose();
            }
        });
    }

    private void registerUser() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        RegistrationService registrationService = new RegistrationService(new UserDAO(yourDatabaseConnection));
        RegistrationService.RegistrationResult result = registrationService.registerUser(username, password);

        handleRegistrationResult(result);
    }

    private void handleRegistrationResult(RegistrationService.RegistrationResult result) {
        switch (result) {
            case SUCCESS:
                JOptionPane.showMessageDialog(this, "Registro exitoso. Ahora puedes iniciar sesión.");
                dispose();
                break;
            case INVALID_USERNAME:
                JOptionPane.showMessageDialog(this, "Nombre de usuario inválido. Asegúrate de ingresarlo correctamente.");
                break;
            case INVALID_PASSWORD:
                JOptionPane.showMessageDialog(this, "Contraseña inválida. Debe tener al menos 8 caracteres.");
                break;
            case USERNAME_EXISTS:
                JOptionPane.showMessageDialog(this, "El nombre de usuario ya está en uso. Por favor, elige otro.");
                break;
            case REGISTRATION_FAILED:
                JOptionPane.showMessageDialog(this, "No se pudo completar el registro. Intenta nuevamente.");
                break;
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    RegistrationForm frame = new RegistrationForm();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
