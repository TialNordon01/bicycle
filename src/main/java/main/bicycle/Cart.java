package main.bicycle;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static main.bicycle.List.cart;
import static main.bicycle.Start.*;

//Контроллер формы корзины товаров
public class Cart {
    //Объекты элементов на форме
    @FXML
    private TableView<List.Row> products;
    @FXML
    private TableColumn<List.Row, String> categoryColumn;
    @FXML
    private TableColumn<List.Row, String> nameColumn;
    @FXML
    private TableColumn<List.Row, String> specificationsColumn;
    @FXML
    private TableColumn<List.Row, String> manufacturerColumn;
    @FXML
    private TableColumn<List.Row, String> supplierColumn;
    @FXML
    private TableColumn<List.Row, String> articleColumn;
    @FXML
    private TableColumn<List.Row, String> priceColumn;
    @FXML
    private ComboBox<String> pickupPoints;
    @FXML
    private TextField login;
    @FXML
    private Label price;
    //Список идентификаторов пунктов выдачи
    //Доступен для любого метода данного котроллера
    private ArrayList<String> pickupPointsIDS = new ArrayList<>();
    //Метод, выполняющийся после того как все компоненты были загружены на форму
    public void initialize() throws SQLException {
        //Очищаем выпадающий список пунктов выдачи
        pickupPoints.getItems().clear();
        //Убираем надпись, когда таблица пустая
        products.setPlaceholder(new Label(""));
        //Применяем данный класс для ячеек каждого столбца
        categoryColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
        nameColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
        specificationsColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
        manufacturerColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
        supplierColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
        articleColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
        priceColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
        //Настраиваем ячейки столбцов так, чтобы они брали значения из полей класса строки списка товаров
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        specificationsColumn.setCellValueFactory(new PropertyValueFactory<>("specifications"));
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        articleColumn.setCellValueFactory(new PropertyValueFactory<>("article"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        try {
            //Устанавливаем содержимое таблицы
            ObservableList<List.Row> data = FXCollections.observableArrayList();
            data.addAll(cart);
            products.setItems(data);
            //Добавляем контекстное меню
            products.setRowFactory(tv -> {
                //Объект строки
                TableRow<List.Row> row = new TableRow<>();
                //Создаём контекстное меню и добавляем к нему кнопки
                ContextMenu contextMenu = new ContextMenu();
                //Удаление товара из корзины
                MenuItem delete = new MenuItem("Удалить");
                delete.setOnAction(event -> {
                    try {
                        //Берём выбранную строку
                        List.Row selectedRow = row.getItem();
                        //Убираем её из таблицы
                        products.getItems().remove(selectedRow);
                        //И из глобальной переменной корзины
                        if (selectedRow != null) {
                            cart.remove(selectedRow);
                        }
                        //Обновляем данные на форме
                        initialize();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                //Добавляем кнопку удаления на контекстное меню
                contextMenu.getItems().addAll(delete);
                //Делаем так, чтобы контекстное меню появлялось только при нажатии на не пустую строку
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu) null)
                                .otherwise(contextMenu)
                );
                //Возвращаем строку
                return row;
            });
            //Добавляем данные
            ArrayList<ArrayList<String>> pickupPointData = database.query("SELECT id, address FROM pickup_point");
            for (ArrayList<String> row : pickupPointData) {
                //В выпадающий список
                pickupPoints.getItems().add(row.get(1));
                //Индентификаторы пунктов выдачи в список для всех методов
                pickupPointsIDS.add(row.get(0));
            }
            //Добавляем сумму заказа
            String sumPrice = "0";
            for (List.Row row : cart) {
                sumPrice = Float.toString(Float.parseFloat(sumPrice) + Float.parseFloat(row.getPrice()));
            }
            price.setText(sumPrice + " рублей");
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
    //Метод для оформления заказа
    @FXML
    void order(ActionEvent event) {
        //Проверяем, пустая ли у нас корзина
        if(!cart.isEmpty()) {
            //Проверяем, есть ли товары с количеством, меньшим или равным 0
            boolean checkZeroCount = true;
            for (List.Row row : cart) {
                if (Integer.parseInt(row.getCount()) <= 0) {
                    checkZeroCount = false;
                }
            }
            if (checkZeroCount) {
                //Проверяем, указан ли пункт выдачи
                try {
                    //Проверяем, указан ли логин
                    if(!login.getText().isEmpty()) {
                        //Берём из базы данных идентификатор по выбранному из выпадающего списка
                        String pickupPointID = database.query("SELECT id FROM pickup_point WHERE id = " + pickupPointsIDS.get(pickupPoints.getSelectionModel().getSelectedIndex())).get(0).get(0);
                        //Проверяем, указан ли логин заказчика
                        try {
                            //Берём идентификатор заказчика
                            String userID = database.query("SELECT id FROM user WHERE login = '" + login.getText() + "'").get(0).get(0);
                            //Проверяем на другие ошибки
                            try {
                                //Делаем транзакцию
                                database.connection.setAutoCommit(false);
                                //Вставляем в таблицу заказов базы данных новый заказ с полученными идентификаторами
                                database.query("INSERT INTO `order`(id_user, id_pickup_point, status) VALUE (" + userID + ", " + pickupPointID + ", 'Новый')");
                                //Берём идентификатор текущего заказа
                                String orderID = database.query("SELECT id FROM `order` ORDER BY id DESC LIMIT 1").get(0).get(0);
                                //Каждый товар в корзине добавляем в связующую таблицу базы данных заказов и продуктов
                                for (List.Row row : cart) {
                                    database.query("INSERT INTO order_product(id_order, id_product) VALUE (" + orderID + ", " + row.getId() + ")");
                                }
                                database.connection.commit();
                                database.connection.setAutoCommit(true);
                                //Очищаем корзину
                                cart.clear();
                                //Используем метод для возвращения на форму списка товаров
                                back(event);
                            } catch (Exception e) {
                                //Если на стороне сервера возникла проблема - выводим об этом ошибку и делаем возврат к предыдущим значениям в базе данных
                                database.connection.rollback();
                                e.printStackTrace();
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Ошибка");
                                alert.setHeaderText("Ошибка на стороне сервера!");
                                alert.setContentText("Попробуйте очистить корзину от товаров!");
                                alert.showAndWait();
                            }
                        } catch (Exception e) {
                            //Если не указал логин заказчика - выдаём окно с ошибкой
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ошибка");
                            alert.setHeaderText("Логин заказчика указан неверно!");
                            alert.setContentText("Укажите логин заказчика!");
                            alert.showAndWait();
                        }
                    } else {
                        //Если не указал логин заказчика - выдаём окно с ошибкой
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Не указан логин заказчика!");
                        alert.setContentText("Укажите логин заказчика!");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    //Если не выбран пункт выдачи - выдаём окно с ошибкой
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Не выбран пункт выдачи!");
                    alert.setContentText("Выберите пункт выдачи!");
                    alert.showAndWait();
                }
            } else {
                //Если оказались товары с количеством, меньшим или равным 0 - выдаём окно с ошибкой
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Недостаточно товара на складе!");
                alert.setContentText("Выберите другие товары!");
                alert.showAndWait();
            }
        } else {
            //Если корзина оказалась пустой - выдаём окно с ошибкой
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Пустая корзина!");
            alert.setContentText("Выберите товар для покупки!");
            alert.showAndWait();
        }
    }
}