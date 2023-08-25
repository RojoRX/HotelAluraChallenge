package service;

import dao.UserDAO;

public class RegistrationService {
    private UserDAO userDAO;

    public RegistrationService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public RegistrationResult registerUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return RegistrationResult.INVALID_USERNAME;
        }

        if (password == null || password.length() < 8) {
            return RegistrationResult.INVALID_PASSWORD;
        }

        if (userDAO.findByUsername(username) != null) {
            return RegistrationResult.USERNAME_EXISTS;
        }

        // Aplicar hashing y salting a la contraseña antes de registrar

        boolean registrationSuccessful = userDAO.insertUser(username, password);

        if (registrationSuccessful) {
            return RegistrationResult.SUCCESS;
        } else {
            return RegistrationResult.REGISTRATION_FAILED;
        }
    }

    public enum RegistrationResult {
        SUCCESS,
        INVALID_USERNAME,
        INVALID_PASSWORD,
        USERNAME_EXISTS,
        REGISTRATION_FAILED
    }
}
