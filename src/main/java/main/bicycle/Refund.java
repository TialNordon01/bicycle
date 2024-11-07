package main.bicycle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.SQLException;

import static main.bicycle.Orders.orderID;
import static main.bicycle.Start.database;

//Контроллер формы возврата
public class Refund {
    //Объекты элементов на форме
    @FXML
    private TextArea comment;
    //Метод для выполнения возврата заказа
    @FXML
    void doRefund(ActionEvent event) {
        try{
            //Проверяем, не пустой ли комментарий к возврату
            if(!comment.getText().isEmpty()) {
                //Добавляем комментарий, оставленный пользователем в базу данных
                database.query("INSERT INTO refund(id_order, comment) VALUE (" + orderID + ", '" + comment.getText() + "')");
                //Возвращаемся на форму заказов
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene oldScene = stage.getScene();
                Parent newRoot = FXMLLoader.load(getClass().getResource("orders-view.fxml"));
                oldScene.setRoot(newRoot);
                stage.setTitle("Заказы");
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Не был введён комметарий к возврату!");
                alert.setContentText("Введите комментарий!");
                alert.showAndWait();
            }
        } catch (Exception e){
            //Если на стороне сервера возникла проблема - выводим об этом ошибку
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка на стороне сервера!");
            alert.setContentText("Попробуйте позже!");
            alert.showAndWait();
        }
    }

}