package ru.geekbrains.july_chat.chat_server.auth;

import ru.geekbrains.july_chat.chat_server.error.UserNotFoundException;
import ru.geekbrains.july_chat.chat_server.error.WrongCredentialsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InMemoryAuthService implements AuthService {

    @Override
    public void start() {
        System.out.println("Auth service started!");
    }

    @Override
    public void stop() {
        System.out.println("Auth service stopped");
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        User user = new User().getUser(login, password);
        if (user.isUserInitialized()) return user.getNick();

        throw new UserNotFoundException("User not found");
    }

    @Override
    public String changeNickname(String oldNick, String newNick) {
        return null;
    }

    @Override
    public void changePassword(String nickname, String oldPassword, String newPassword) {

    }

    @Override
    public void createNewUser(String login, String password, String nickname) {

    }

    @Override
    public void deleteUser(String nickname) {

    }
}
