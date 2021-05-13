package GU.Lesson02;

import GU.Lesson02.Models.UserModel;

public class Main {

    // Нужно ли все-таки к проекту подключать сетевой чат?
    // Я не понял как в проект включать информацию о подключенной БД - я скачал jar и подключил его через
    // ProjectSettings->Libraries, но это же неправильно??

    public static void main(String[] args) {
        UserModel userModel = new UserModel();

         if (!userModel.getUser("login","password").isUserInitialized())
             userModel.createUser("login","password","nick");

        System.out.println(userModel);

        userModel.changeName("Nick2");
        System.out.println(userModel);

        userModel.changePassword("password","password2");
        System.out.println(userModel);


    }


}
