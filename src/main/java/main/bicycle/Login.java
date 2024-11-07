package main.bicycle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static main.bicycle.Start.*;

//Контроллер формы авторизации
public class Login {
    //Глобальные переменные данных пользователя - идентификатор его роли и его собственный идентификатор
    public static String userID;
    public static String roleID;
    //Объекты элементов на форме
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    //Метод для совершения авторизации
    @FXML
    void enter(ActionEvent event){
        try {
            //Берём данные пользователя по введёному им логину и паролю
            ArrayList<ArrayList<String>> userdata = database.query(
                    "SELECT * FROM user " +
                            "WHERE login = '" + login.getText().trim() + "' " +
                            "AND password = '" + password.getText().trim() + "' "
            );
            //Если данные введены верно
            if (!userdata.isEmpty()) {
                //Присваиваем глобальным переменным данные пользователя
                userID = userdata.get(0).get(0);
                roleID = userdata.get(0).get(1);
                //Переходим на форму списка товаров, сохраняя размер окна
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene oldScene = stage.getScene();
                Parent newRoot = FXMLLoader.load(getClass().getResource("list-view.fxml"));
                oldScene.setRoot(newRoot);
                stage.setTitle("Список товаров");
                stage.show();
            } else {
                //Если данные не совпали - выдаём окно с ошибкой
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка!");
                alert.setHeaderText("Данные введены неверно!");
                alert.setContentText("Укажите верные данные!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            //Если на стороне сервера возникла проблема - выводим об этом ошибку
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка на стороне сервера!");
            alert.setContentText("Попробуйте позже!");
            alert.showAndWait();
        }
    }
    //Метод для возвращения назад, на форму списка товаров
    @FXML
    void back(ActionEvent event) throws IOException {
        //Размер окна при переходе сохраняется
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Parent newRoot = FXMLLoader.load(getClass().getResource("list-view.fxml"));
        oldScene.setRoot(newRoot);
        stage.setTitle("Список товаров");
        stage.show();
    }

}