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

import static main.bicycle.Start.*;

//Контроллер формы регистрации
public class Registration {
    //Объекты элементов на форме
    @FXML
    private TextField fullName;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private TextField phone;
    @FXML
    private TextField mail;
    @FXML
    private TextField code;
    //Метод для регистрации пользователя в базе данных
    @FXML
    void registration(ActionEvent event){
        try {
            //Если были введены все данные
            if (!fullName.getText().isEmpty() && !login.getText().isEmpty() && !password.getText().isEmpty() && !phone.getText().isEmpty() && !mail.getText().isEmpty()) {
                //Если телефон был введён верно
                if (phone.getText().matches("^[0-9]{5,}$") || phone.getText().matches("^\\+[0-9]{5,}$")) {
                    //Если почта была введёна верно
                    if (mail.getText().matches(".*@.*\\..*")) {
                        //Если введёного логина нет в базе данных
                        if (database.query("SELECT * FROM user WHERE login = '" + login.getText().trim() + "'").isEmpty()) {
                            //Если код администратора был введён
                            if (!code.getText().isEmpty()) {
                                //Если этот код есть в базе данных
                                if (!database.query("SELECT * FROM admin_code WHERE code = '" + code.getText() + "'").isEmpty()) {
                                    //Если нет ошибки на сервере
                                    try {
                                        //Выполняем добавление нового сотрудника
                                        database.query(
                                                "INSERT INTO user(id_role, login, password, full_name, phone, mail) " +
                                                        "VALUE (2, '" + login.getText().trim() + "', '" + password.getText().trim() + "', '" + fullName.getText().trim() + "', '" + phone.getText().trim() + "', '" + mail.getText().trim() + "')"
                                        );
                                        //Переходим на окно авторизации для входа, сохраняя размер окна
                                        //(предполагается, что пользователь введёт данные, введёные при регистрации, но он может ввести и другие данные)
                                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                        Scene oldScene = stage.getScene();
                                        Parent newRoot = FXMLLoader.load(getClass().getResource("login-view.fxml"));
                                        oldScene.setRoot(newRoot);
                                        stage.setTitle("Вход");
                                        stage.show();
                                    } catch (Exception e) {
                                        //Если есть ошибка на стороне сервера
                                        e.printStackTrace();
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Ошибка");
                                        alert.setHeaderText("Ошибка на стороне сервера!");
                                        alert.setContentText("Попробуйте ввести другие данные или повторить попытку позже!");
                                        alert.showAndWait();
                                    }
                                } else {
                                    //Если код администратора не совпал - выдаём окно с ошибкой
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Ошибка");
                                    alert.setHeaderText("Неверный код администратора!");
                                    alert.setContentText("Попробуйте другой код!");
                                    alert.showAndWait();
                                }
                            } else {
                                try {
                                    //Иначе выполняем добавление нового сотрудника
                                    database.query(
                                            "INSERT INTO user(id_role, login, password, full_name, phone, mail) " +
                                                    "VALUE (1, '" + login.getText().trim() + "', '" + password.getText().trim() + "', '" + fullName.getText().trim() + "', '" + phone.getText().trim() + "', '" + mail.getText().trim() + "')"
                                    );
                                    //Переходим на окно авторизации для входа, сохраняя размер окна
                                    //(предполагается, что пользователь введёт данные, введёные при регистрации, но он может ввести и другие данные)
                                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    Scene oldScene = stage.getScene();
                                    Parent newRoot = FXMLLoader.load(getClass().getResource("login-view.fxml"));
                                    oldScene.setRoot(newRoot);
                                    stage.setTitle("Вход");
                                    stage.show();
                                } catch (Exception e) {
                                    //Если есть ошибка на стороне сервера
                                    e.printStackTrace();
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Ошибка");
                                    alert.setHeaderText("Ошибка на стороне сервера!");
                                    alert.setContentText("Попробуйте позже!");
                                    alert.showAndWait();
                                }
                            }

                        } else {
                            //Если логин уже существует - выводим ошибку
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ошибка");
                            alert.setHeaderText("Данный логин уже существует!");
                            alert.setContentText("Придумайте другой!");
                            alert.showAndWait();
                        }
                    } else {
                        //Если почта введена в неверном фомате
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Неправильный формат ввода почты!");
                        alert.setContentText("Введите почту в формате example@example.com!");
                        alert.showAndWait();
                    }
                } else {
                    //Если телефон введен в неверном фомате
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Неправильный формат ввода номера телефона!");
                    alert.setContentText("Введите номер в формате +78005553535 или 12345");
                    alert.showAndWait();
                }
            } else {
                //Если не все данные были введены - выводим ошибку
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Данные введены не полностью!");
                alert.setContentText("Введите данные!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            //Если есть ошибка на стороне сервера
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