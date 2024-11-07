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
import java.util.Objects;

import static main.bicycle.Login.roleID;
import static main.bicycle.Login.userID;
import static main.bicycle.Start.database;

//Контроллер формы заказов
public class Orders {
    //Глобальная переменная идентификатора заказа
    public static String orderID;
    //Объекты элементов на форме
    @FXML
    private TableView<Row> orders;
    @FXML
    private TableColumn<Row, String> idColumn;
    @FXML
    private TableColumn<Row, String> statusColumn;
    @FXML
    private TableColumn<Row, String> priceColumn;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField status;
    @FXML
    private Button statusButton;
    //Класс строки таблицы списка заказов
    public static class Row {
        private String id;
        private String status;
        private String price;
        private String refund;
        private String fullName;
        private String phone;
        private String mail;
        //Конструктор строки для клиента
        public Row(String id, String status, String price) {
            this.id = id;
            this.status = status;
            this.price = price;
        }
        //Конструктор строки для сотрудника
        public Row(String id, String status, String price, String refund, String fullName, String phone, String mail) {
            this.id = id;
            this.status = status;
            this.refund = refund;
            this.price = price;
            this.fullName = fullName;
            this.phone = phone;
            this.mail = mail;
        }
        //Геттеры для получения информации из строки
        public String getId() {
            return id;
        }
        public String getStatus() {
            return status;
        }
        public String getRefund() {
            return refund;
        }
        public String getPrice() {
            return price;
        }
        public String getFullName() {
            return fullName;
        }
        public String getPhone() {
            return phone;
        }
        public String getMail() {
            return mail;
        }
    }
    //Метод, выполняющийся после того как все компоненты были загружены на форму
    public void initialize(){
        //Убираем надпись, когда таблица пустая
        orders.setPlaceholder(new Label(""));
        //Применяем данный класс для ячеек каждого столбца
        idColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
        statusColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
        priceColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
        //Настраиваем ячейки столбцов так, чтобы они брали значения из полей класса строки списка заказов
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        try {
            //Если пользователь - клиент
            if (roleID.equals("1")) {
                //Добавляем данные в таблицу
                String stringQuery = "SELECT id, status, price FROM `order` WHERE id_user = " + userID;
                ArrayList<ArrayList<String>> query = database.query(stringQuery);
                ObservableList<Row> data = FXCollections.observableArrayList();
                for (ArrayList<String> strings : query) {
                    data.add(new Row(strings.get(0), strings.get(1), strings.get(2)));
                }
                orders.setItems(data);
            }
            //Если пользователь - сотрудник
            if (roleID.equals("2")) {
                //Создаём новые столбцы
                TableColumn<Row, String> refundColumn = new TableColumn<>("Возврат");
                TableColumn<Row, String> fullNameColumn = new TableColumn<>("ФИО");
                TableColumn<Row, String> phoneColumn = new TableColumn<>("Телефон");
                TableColumn<Row, String> mailColumn = new TableColumn<>("Почта");
                //Настраиваем ячейки столбцов так, чтобы они брали значения из полей класса строки списка заказов
                refundColumn.setCellValueFactory(new PropertyValueFactory<>("refund"));
                fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
                phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
                mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));
                //Применяем класс для содержимого ячейки для ячеек каждого столбца
                refundColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
                fullNameColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
                phoneColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
                mailColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
                //Добавляем столбцы в таблицу
                orders.getColumns().addAll(refundColumn, fullNameColumn, phoneColumn, mailColumn);
                //Добавляем данные в таблицу
                ArrayList<ArrayList<String>> query = database.query("SELECT `order`.id, status, price, comment, full_name, phone, mail FROM `order` LEFT JOIN refund ON id_order = order.id LEFT JOIN user ON id_user = user.id");
                ObservableList<Row> data = FXCollections.observableArrayList();
                for (ArrayList<String> strings : query) {
                    data.add(new Row(strings.get(0), strings.get(1), strings.get(2), Objects.toString(strings.get(3), ""), strings.get(4), strings.get(5), strings.get(6)));
                }
                orders.setItems(data);
            }
            //Добавляем контекстное меню
            orders.setRowFactory(tv -> {
                //Объект строки
                TableRow<Row> row = new TableRow<>();
                //Создаём контекстное меню и добавляем к нему кнопки
                ContextMenu contextMenu = new ContextMenu();
                //Если пользователь - клиент
                if (roleID.equals("1")) {
                    //Возврат заказа
                    MenuItem refund = new MenuItem("Возврат заказа");
                    refund.setOnAction(event -> {
                        try {
                            //Назначаем идентификатор выбранной строки в глобальную переменную
                            orderID = row.getItem().getId();
                            //Переходим на форму возврата, с сохранением размера окна
                            Stage stage = (Stage) ((Node) orders).getScene().getWindow();
                            Scene oldScene = stage.getScene();
                            Parent newRoot = FXMLLoader.load(getClass().getResource("refund-view.fxml"));
                            oldScene.setRoot(newRoot);
                            stage.setTitle("Оформление возврата");
                            stage.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //Добавляем кнопку возврата заказа
                    contextMenu.getItems().addAll(refund);
                }
                //Если пользователь - сотрудник
                if (roleID.equals("2")) {
                    //Удаление заказа
                    MenuItem delete = new MenuItem("Удалить заказ");
                    delete.setOnAction(event -> {
                        try {
                            //Удаляем заказ из базы данных
                            database.query("DELETE FROM `order` WHERE id = " + row.getItem().getId());
                            //Обновляем информацию в таблице
                            ArrayList<ArrayList<String>> query = database.query("SELECT `order`.id, status, price, comment, full_name, phone, mail FROM `order` LEFT JOIN refund ON id_order = order.id LEFT JOIN user ON id_user = user.id");
                            ObservableList<Row> data = FXCollections.observableArrayList();
                            for (ArrayList<String> strings : query) {
                                data.add(new Row(strings.get(0), strings.get(1), strings.get(2), Objects.toString(strings.get(3), ""), strings.get(4), strings.get(5), strings.get(6)));
                            }
                            orders.setItems(data);
                        } catch (Exception e) {
                            //Если на стороне сервера возникла проблема - выводим об этом ошибку
                            e.printStackTrace();
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ошибка");
                            alert.setHeaderText("Ошибка на стороне сервера!");
                            alert.setContentText("Попробуйте позже!");
                            alert.showAndWait();
                        }
                    });
                    //Изменение статуса заказа
                    MenuItem changeStatus = new MenuItem("Изменить статус");
                    changeStatus.setOnAction(event -> {
                        try {
                            //Назначаем идентификатор выбранной строки в глобальную переменную
                            orderID = row.getItem().getId();
                            //Отображаем дополнительные элементы формы для редактирования
                            statusLabel.setVisible(true);
                            status.setVisible(true);
                            statusButton.setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //Добавляем кнопки удаления и изменения статуса в контекстное меню
                    contextMenu.getItems().addAll(delete, changeStatus);
                }
                //Делаем так, чтобы контекстное меню появлялось только при нажатии на не пустую строку
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu) null)
                                .otherwise(contextMenu)
                );
                //Возвращаем строку
                return row;
            });
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
    //Метод для изменения статуса заказа
    @FXML
    void changeStatus(ActionEvent event) throws SQLException {
        try {
            //Изменяем статус заказа, по идентификатору из глобальной переменной
            database.query("UPDATE `order` SET status = '" + status.getText() + "' WHERE id = " + orderID);
            //Скрываем элементы для редактирования
            statusLabel.setVisible(false);
            status.setVisible(false);
            statusButton.setVisible(false);
            //Обновляем информацию в таблице
            ArrayList<ArrayList<String>> query = database.query("SELECT `order`.id, status, price, comment, full_name, phone, mail FROM `order` LEFT JOIN refund ON id_order = order.id LEFT JOIN user ON id_user = user.id");
            ObservableList<Row> data = FXCollections.observableArrayList();
            for (ArrayList<String> strings : query) {
                data.add(new Row(strings.get(0), strings.get(1), strings.get(2), Objects.toString(strings.get(3), ""), strings.get(4), strings.get(5), strings.get(6)));
            }
            orders.setItems(data);
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
}