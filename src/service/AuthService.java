package service;
import dao.UserDAO;
import model.User;

public class AuthService {
    private UserDAO userDAO;
    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public boolean authenticateUser(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null) {
            // Aquí deberías verificar la contraseña de manera segura (hashing y comparación)
            // Por simplicidad, estamos comparando contraseñas en texto plano en este ejemplo.
            return user.getPassword().equals(password);
        }
        return false;
    }
}
