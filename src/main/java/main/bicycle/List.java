package main.bicycle;

import javafx.application.Platform;
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

import static main.bicycle.Login.roleID;
import static main.bicycle.Login.userID;
import static main.bicycle.Start.*;

//Контроллер формы списка товаров
public class List{
    //Глобальный список строк для корзины
    public static ArrayList<Row> cart = new ArrayList<>();
    //Глобальная строка, которую изменяем
    public static Row editable;
    //Объекты элементов на форме
    @FXML
    private Button toCartButton;
    @FXML
    private Button toOrdersButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button registrationButton;
    @FXML
    private Button logoutButton;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Row> products;
    @FXML
    private TableColumn<Row, String> categoryColumn;
    @FXML
    private TableColumn<Row, String> nameColumn;
    @FXML
    private TableColumn<Row, String> specificationsColumn;
    @FXML
    private TableColumn<Row, String> manufacturerColumn;
    @FXML
    private TableColumn<Row, String> supplierColumn;
    @FXML
    private TableColumn<Row, String> articleColumn;
    @FXML
    private TableColumn<Row, String> priceColumn;
    @FXML
    private ComboBox<String> categories;
    //Строка запроса для вывода товаров
    //Доступна для любого метода данного котроллера
    private String stringQuery;
    //Класс строки таблицы списка товаров
    public static class Row {
        private String id;
        private String category;
        private String name;
        private String specifications;
        private String manufacturer;
        private String supplier;
        private String article;
        private String price;
        private String count;
        //Конструктор строки для клиента
        public Row(String id, String category, String name, String specifications, String manufacturer, String supplier, String article, String price) {
            this.id = id;
            this.category = category;
            this.name = name;
            this.specifications = specifications;
            this.manufacturer = manufacturer;
            this.supplier = supplier;
            this.article = article;
            this.price = price;
        }
        //Конструктор строки для сотрудника
        public Row(String id, String category, String name, String specifications, String manufacturer, String supplier, String article, String price, String count) {
            this.id = id;
            this.category = category;
            this.name = name;
            this.specifications = specifications;
            this.manufacturer = manufacturer;
            this.supplier = supplier;
            this.article = article;
            this.price = price;
            this.count = count;
        }
        //Геттеры для получения информации из строки
        public String getId() {return id;}
        public String getCategory() {return category;}
        public String getName() {return name;}
        public String getSpecifications() {return specifications;}
        public String getManufacturer() {return manufacturer;}
        public String getSupplier() {return supplier;}
        public String getArticle() {return article;}
        public String getPrice() {return price;}
        public String getCount() {return count;}
        //Сеттеры для изменения информации в строке
        public void setId(String id) {this.id = id;}
        public void setCategory(String category) {this.category = category;}
        public void setName(String name) {this.name = name;}
        public void setSpecifications(String specifications) {this.specifications = specifications;}
        public void setManufacturer(String manufacturer) {this.manufacturer = manufacturer;}
        public void setSupplier(String supplier) {this.supplier = supplier;}
        public void setArticle(String article) {this.article = article;}
        public void setPrice(String price) {this.price = price;}
        public void setCount(String count) {this.count = count;}
    }

    //Метод, выполняющийся после того как все компоненты были загружены на форму
    public void initialize() throws SQLException {
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
        //Создаём столбец количества товаров только для сотрудника
        TableColumn<Row, String> countColumn = new TableColumn<>("Количество");
        countColumn.setCellFactory(p -> new WrappingTextFieldTableCell<>());
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        if ((roleID != null) && (roleID.equals("2"))) {
            products.getColumns().add(countColumn);
        }
        if ((roleID == null) || (roleID.equals("1"))) {
            products.getColumns().remove(countColumn);
        }
        //Задаём запрос для получения строк товаров из базы данных
        stringQuery = "SELECT product.id, category.name, product.name, specifications, manufacturer.name, supplier.name, article, price, count " +
                "FROM product " +
                "JOIN supplier ON id_supplier = supplier.id " +
                "JOIN manufacturer ON id_manufacturer = manufacturer.id " +
                "JOIN category ON id_category = category.id ";
        try {
            ArrayList<ArrayList<String>> query = database.query(stringQuery);
            ObservableList<Row> data = FXCollections.observableArrayList();
            //Добавляем информацию на таблицу в зависимости от того, кто её просматривает - клиент или сотрудник
            if ((roleID == null) || (roleID.equals("1"))) {
                for (ArrayList<String> strings : query) {
                    data.add(new Row(strings.get(0), strings.get(1), strings.get(2), strings.get(3), strings.get(4), strings.get(5), strings.get(6), strings.get(7)));
                }
            }
            if ((roleID != null) && (roleID.equals("2"))) {
                for (ArrayList<String> strings : query) {
                    data.add(new Row(strings.get(0), strings.get(1), strings.get(2), strings.get(3), strings.get(4), strings.get(5), strings.get(6), strings.get(7), strings.get(8)));
                }
            }
            products.setItems(data);
            //Добавляем к каждой строке контекстное меню, если это сотрудник
            if ((roleID != null) && (roleID.equals("2"))) {
                products.setRowFactory(tv -> {
                    //Объект строки
                    TableRow<Row> row = new TableRow<>();
                    //Создаём контекстное меню и добавляем к нему кнопки
                    ContextMenu contextMenu = new ContextMenu();
                    //Добавление в корзину товара
                    MenuItem toCart = new MenuItem("Добавить в корзину");
                    toCart.setOnAction(event -> {
                        //Берём выбранную строку
                        Row selectedRow = row.getItem();
                        //Добавляем в корзину, если выбранная строка не пустая
                        if (selectedRow != null && Integer.parseInt(selectedRow.getCount()) != 0) {
                            cart.add(selectedRow);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ошибка");
                            alert.setHeaderText("Товара нет на складе!");
                            alert.setContentText("Выберите другой товар!");
                            alert.showAndWait();
                        }
                    });
                    //Добавление товара в базу данных
                    MenuItem add = new MenuItem("Добавить товар");
                    add.setOnAction(event -> {
                        try {
                            //Убираем глобальную строку для редактирования
                            editable = null;
                            //Создаём новое окно с формой для добавления и редактирования товара
                            Stage stage = new Stage();
                            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("product-view.fxml")));
                            stage.setTitle("Добавление товара");
                            stage.setScene(scene);
                            stage.show();
                            //Ограничиваем её возможный размер
                            stage.setMinWidth(stage.getWidth());
                            stage.setMinHeight(stage.getHeight());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //Удаления товара из базы данных
                    MenuItem delete = new MenuItem("Удалить товар");
                    delete.setOnAction(event -> {
                        try {
                            //Берём выбранную строку
                            Row selectedRow = row.getItem();
                            //Удаляем строку с таблицы и из базы данных, если она не пустая
                            if (selectedRow != null) {
                                products.getItems().remove(selectedRow);
                                database.query("DELETE FROM product WHERE id = " + selectedRow.getId());
                                cart.remove(selectedRow);
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
                    });
                    //Редактирование товара в базе данных
                    MenuItem edit = new MenuItem("Редактировать товар");
                    edit.setOnAction(event -> {
                        try {
                            //Назначаем глобальную строку для редактирования
                            editable = row.getItem();
                            //Создаём новое окно с формой для добавления и редактирования товара
                            Stage stage = new Stage();
                            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("product-view.fxml")));
                            stage.setTitle("Редактирование товара");
                            stage.setScene(scene);
                            stage.show();
                            //Ограничиваем её возможный размер
                            stage.setMinWidth(stage.getWidth());
                            stage.setMinHeight(stage.getHeight());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //Добавляем все кнопки на контекстное меню
                    contextMenu.getItems().addAll(toCart, add, delete, edit);
                    //Делаем так, чтобы контекстное меню появлялось только при нажатии на не пустую строку
                    row.contextMenuProperty().bind(
                            Bindings.when(row.emptyProperty())
                                    .then((ContextMenu) null)
                                    .otherwise(contextMenu)
                    );
                    //Возвращаем строку
                    return row;
                });
            }
            //Делаем запрос для получения всех категорий
            ArrayList<ArrayList<String>> queryCategory = database.query("SELECT id, name FROM category");
            //Добавляем категории в выпадащий список, выбираем значение по умолчанию для всех категорий и выполняем метод выбора категории
            ArrayList<String> resultCategory = new ArrayList<>();
            resultCategory.add("Все категории");
            for (ArrayList<String> row : queryCategory) {
                resultCategory.add(row.get(1));
            }
            categories.getItems().setAll(FXCollections.observableArrayList(resultCategory));
            categories.getSelectionModel().select(0);
            setCategory();
            //Если пользователь в гостевом режиме, ему доступны вход и регистрация
            if (roleID == null) {
                toCartButton.setDisable(true);
                toOrdersButton.setDisable(true);
                loginButton.setDisable(false);
                registrationButton.setDisable(false);
                logoutButton.setDisable(true);
            }
            //Если пользователь - клиент, ему доступны выход, и список заказов
            if (roleID != null && roleID.equals("1")) {
                toCartButton.setDisable(true);
                toOrdersButton.setDisable(false);
                loginButton.setDisable(true);
                registrationButton.setDisable(true);
                logoutButton.setDisable(false);
            }
            //Если пользователь - сотрудник, ему доступны выход, список заказов и корзина
            if (roleID != null && roleID.equals("2")) {
                toCartButton.setDisable(false);
                toOrdersButton.setDisable(false);
                loginButton.setDisable(true);
                registrationButton.setDisable(true);
                logoutButton.setDisable(false);
            }
        } catch (Exception e) {
            //Если на стороне сервера возникла проблема - выводим об этом ошибку
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка на стороне сервера!");
            alert.setContentText("Попробуйте позже!");
            alert.showAndWait();
            //И выключаем весь функционал
            toCartButton.setDisable(true);
            toOrdersButton.setDisable(true);
            loginButton.setDisable(true);
            registrationButton.setDisable(true);
            logoutButton.setDisable(true);
        }
    }
    //Метод для перехода в корзину, сохраняя размер окна
    @FXML
    void toCart(ActionEvent event) throws IOException {
        //Размер окна при переходе сохраняется
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Parent newRoot = FXMLLoader.load(getClass().getResource("cart-view.fxml"));
        oldScene.setRoot(newRoot);
        stage.setTitle("Корзина");
        stage.show();
    }
    //Метод для перехода к заказам
    @FXML
    void toOrders(ActionEvent event) throws IOException {
        //Размер окна при переходе сохраняется
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Parent newRoot = FXMLLoader.load(getClass().getResource("orders-view.fxml"));
        oldScene.setRoot(newRoot);
        stage.setTitle("Заказы");
        stage.show();
    }
    //Метод для перехода к форме входа
    @FXML
    void login(ActionEvent event) throws IOException {
        //Размер окна при переходе сохраняется
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Parent newRoot = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        oldScene.setRoot(newRoot);
        stage.setTitle("Вход");
        stage.show();
    }
    //Метод для перехода к форме регистрации
    @FXML
    void registration(ActionEvent event) throws IOException {
        //Размер окна при переходе сохраняется
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Parent newRoot = FXMLLoader.load(getClass().getResource("registration-view.fxml"));
        oldScene.setRoot(newRoot);
        stage.setTitle("Регистрация");
        stage.show();
    }
    //Метод для выхода из аккаунта
    @FXML
    void logout(ActionEvent event) throws IOException {
        //Убираем данные о пользователе из глобальных переменных
        userID = null;
        roleID = null;
        //Размер окна при переходе сохраняется
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Parent newRoot = FXMLLoader.load(getClass().getResource("list-view.fxml"));
        oldScene.setRoot(newRoot);
        stage.setTitle("Список товаров");
        stage.show();
    }
    //Метод для сортировки по категории
    @FXML
    void setCategory() throws SQLException {
        //Если выбрано значение какой-то категории
        if(!categories.getSelectionModel().getSelectedItem().equals("Все категории")){
            //Выполняем изменённый запрос для выбора из категории
            ArrayList<ArrayList<String>> query = database.query(
                    stringQuery = stringQuery.substring(0, stringQuery.contains("WHERE") ? stringQuery.indexOf("WHERE") : stringQuery.length())
                            + "WHERE category.name LIKE '%" + categories.getSelectionModel().getSelectedItem() + "%' "
            );
            //Изменяем информацию в таблице в зависимости от того, кто её просматривает - клиент или сотрудник
            ObservableList<Row> data = FXCollections.observableArrayList();
            if ((roleID == null) || (roleID.equals("1"))) {
                for (ArrayList<String> strings : query) {
                    data.add(new Row(strings.get(0), strings.get(1), strings.get(2), strings.get(3), strings.get(4), strings.get(5), strings.get(6), strings.get(7)));
                }
            }
            if ((roleID != null) && (roleID.equals("2"))) {
                for (ArrayList<String> strings : query) {
                    data.add(new Row(strings.get(0), strings.get(1), strings.get(2), strings.get(3), strings.get(4), strings.get(5), strings.get(6), strings.get(7), strings.get(8)));
                }
            }
            products.setItems(data);
        //Если выбрано значение по умолчанию для всех категорий
        } else {
            //Выполняем изменённый запрос без категории
            ArrayList<ArrayList<String>> query = database.query(
                    stringQuery = stringQuery.substring(0, stringQuery.contains("WHERE") ? stringQuery.indexOf("WHERE") : stringQuery.length())
            );
            //Изменяем информацию в таблице в зависимости от того, кто её просматривает - клиент или сотрудник
            ObservableList<Row> data = FXCollections.observableArrayList();
            if ((roleID == null) || (roleID.equals("1"))) {
                for (ArrayList<String> strings : query) {
                    data.add(new Row(strings.get(0), strings.get(1), strings.get(2), strings.get(3), strings.get(4), strings.get(5), strings.get(6), strings.get(7)));
                }
            }
            if ((roleID != null) && (roleID.equals("2"))) {
                for (ArrayList<String> strings : query) {
                    data.add(new Row(strings.get(0), strings.get(1), strings.get(2), strings.get(3), strings.get(4), strings.get(5), strings.get(6), strings.get(7), strings.get(8)));
                }
            }
            products.setItems(data);
        }
    }
    //Метод для поиска по названию
    @FXML
    void search() throws SQLException {
        try {
            //Выполняем изменённый запрос для поиска по названию
            ArrayList<ArrayList<String>> query = database.query(
                    stringQuery = stringQuery + "AND product.name LIKE '%" + searchField.getText() + "%' "
            );
            //Изменяем информацию в таблице в зависимости от того, кто её просматривает - клиент или сотрудник
            ObservableList<Row> data = FXCollections.observableArrayList();
            if ((roleID == null) || (roleID.equals("1"))) {
                for (ArrayList<String> strings : query) {
                    data.add(new Row(strings.get(0), strings.get(1), strings.get(2), strings.get(3), strings.get(4), strings.get(5), strings.get(6), strings.get(7)));
                }
            }
            if ((roleID != null) && (roleID.equals("2"))) {
                for (ArrayList<String> strings : query) {
                    data.add(new Row(strings.get(0), strings.get(1), strings.get(2), strings.get(3), strings.get(4), strings.get(5), strings.get(6), strings.get(7), strings.get(8)));
                }
            }
            products.setItems(data);
            //Изменяем запрос для последующего изменения
            stringQuery = stringQuery.substring(0, stringQuery.contains("AND") ? stringQuery.indexOf("AND") : stringQuery.length());
        } catch (Exception e){
            //Если есть ошибка на стороне сервера
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка на стороне сервера!");
            alert.setContentText("Попробуйте позже!");
            alert.showAndWait();
        }
    }
    @FXML
    void refresh() throws SQLException {
        try {
            //Выполняем запрос
            ArrayList<ArrayList<String>> query = database.query(stringQuery);
            //Изменяем информацию в таблице в зависимости от того, кто её просматривает - клиент или сотрудник
            ObservableList<Row> data = FXCollections.observableArrayList();
            if ((roleID == null) || (roleID.equals("1"))) {
                for (ArrayList<String> strings : query) {
                    data.add(new Row(strings.get(0), strings.get(1), strings.get(2), strings.get(3), strings.get(4), strings.get(5), strings.get(6), strings.get(7)));
                }
            }
            if ((roleID != null) && (roleID.equals("2"))) {
                for (ArrayList<String> strings : query) {
                    data.add(new Row(strings.get(0), strings.get(1), strings.get(2), strings.get(3), strings.get(4), strings.get(5), strings.get(6), strings.get(7), strings.get(8)));
                }
            }
            products.setItems(data);
        } catch (Exception e){
            //Если есть ошибка на стороне сервера
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка на стороне сервера!");
            alert.setContentText("Попробуйте позже!");
            alert.showAndWait();
        }
    }
}