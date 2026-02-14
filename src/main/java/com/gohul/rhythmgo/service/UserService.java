package com.gohul.rhythmgo.service;

import com.gohul.rhythmgo.dao.RoleDAO;
import com.gohul.rhythmgo.dao.UserDAO;
import com.gohul.rhythmgo.dto.request.LoginRequest;
import com.gohul.rhythmgo.dto.request.UserCreateRequest;
import com.gohul.rhythmgo.dto.response.UserResponse;
import com.gohul.rhythmgo.exception.AuthenticationException;
import com.gohul.rhythmgo.exception.ResourceAlreadyExistException;
import com.gohul.rhythmgo.exception.ResourceNotFoundException;
import com.gohul.rhythmgo.exception.ValidationException;
import com.gohul.rhythmgo.model.User;
import com.gohul.rhythmgo.util.JWTUtil;
import com.gohul.rhythmgo.util.PasswordUtil;
import com.gohul.rhythmgo.util.PropertiesReader;
import com.gohul.rhythmgo.util.ValidatorUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;

public class UserService {

    private static final UserDAO dao = new UserDAO();
    private static final Validator validator = ValidatorUtil.getvalidator();
    private static final RoleDAO roleDAO = new RoleDAO();

    private static final int DEFAULT_ROLE_ID = Integer.parseInt(
            PropertiesReader.get("default.user_role")
    );

    public int createUser(UserCreateRequest req){
        try{
            Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(req);
            if (!violations.isEmpty()) {
                String message = violations.iterator().next().getMessage();
                throw new ValidationException(message);
            }

            User existingUser = dao.findByEmail(req.getEmail());
            if (existingUser != null) {
                throw new ResourceAlreadyExistException("User", "Email", req.getEmail());
            }
            String salt = PasswordUtil.generateSalt();
            String hash = PasswordUtil.hashPassword(req.getPassword(), salt);
//            User newUser = new User(req.getName(), req.getEmail(), hash, salt);
            User newUser = User.builder()
                    .name(req.getName())
                    .email(req.getEmail())
                    .password(hash)
                    .salt(salt)
                    .build();

            int id = dao.saveUser(newUser);
            if (id == -1) {
                throw new Exception("Failed to save user");
            }

            roleDAO.addRoleToUser(id, DEFAULT_ROLE_ID);

            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String login(String email, String password) throws Exception {

        Set<ConstraintViolation<LoginRequest>> violations =
                validator.validate(new LoginRequest(email, password));
        if (!violations.isEmpty()) {
            String message = violations.iterator().next().getMessage();
            throw new ValidationException(message);
        }

        User u = dao.findByEmail(email);

        if (u == null) {
            throw new AuthenticationException("Invalid user");
        }

        if (!PasswordUtil.verifyPassword(password, u.getPassword(), u.getSalt())) {
            throw new AuthenticationException("Invalid password");
        }

        List<String> roles = roleDAO.getRolesByUserId(u.getId());
        u.setRoles(roles);

        return JWTUtil.generateToken(u);
    }

    public UserResponse getByEmail(String email) throws Exception {
        User user =  dao.findByEmail(email);
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public List<UserResponse> getAllUsers(int page, int size) throws Exception {

        List<User> users = dao.findAll(page, size);

        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()))
                .toList();
    }

    public UserResponse getById(int id) throws Exception {
        User user = dao.findById(id);
        if (user == null) throw new ResourceNotFoundException("User", "ID", String.valueOf(id));
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public void deleteUser(int userId) throws Exception {

        User user = dao.findById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", "ID", String.valueOf(userId));
        }

        boolean deleted = dao.deleteUser(userId);

        if (!deleted) {
            throw new Exception("Failed to delete user");
        }
    }



}
