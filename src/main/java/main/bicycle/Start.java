package main.bicycle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//Класс для запуска приложения
public class Start extends Application {
    //Создаю глобальный объект класса для работы с базой данных
    public static Database database;
    //Делаю подключение к локальной базе данных
    static {
        try {
            database = new Database(
                    "jdbc:mysql://localhost:13306/bicycle",
                    "javafxTest",
                    "changeme"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        //При запуске подгружаю на окно список товаров
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("list-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Список товаров");
        stage.setScene(scene);
        stage.show();
    }
    //Запуск самого приложения
    public static void main(String[] args) {
        launch();
    }
}