package main.bicycle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

import static main.bicycle.List.editable;
import static main.bicycle.Start.*;

//Контроллер формы изменения товара
public class Product {
    //Объекты элементов на форме
    @FXML
    private TextField name;
    @FXML
    private ComboBox<String> category;
    @FXML
    private HBox categoryBox;
    @FXML
    private TextField categoryField;
    @FXML
    private Button addCategoryButton;
    @FXML
    private ComboBox<String> manufacturer;
    @FXML
    private HBox manufacturerBox;
    @FXML
    private TextField manufacturerField;
    @FXML
    private Button addManufacturerButton;
    @FXML
    private ComboBox<String> supplier;
    @FXML
    private HBox supplierBox;
    @FXML
    private TextField supplierField;
    @FXML
    private Button addSupplierButton;
    @FXML
    private TextArea specifications;
    @FXML
    private TextField article;
    @FXML
    private TextField price;
    @FXML
    private TextField count;
    @FXML
    private Button doActionButton;
    //Результаты запросов на данные по производителям, поставщикам и категориям соответственно
    //Доступны в любых методах этого контроллера
    private ArrayList<ArrayList<String>> queryManufacturer;
    private ArrayList<ArrayList<String>> querySupplier;
    private ArrayList<ArrayList<String>> queryCategory;

    //Метод, выполняющийся после того как все компоненты были загружены на форму
    public void initialize() throws SQLException {
        try {
            //Добавляем в выпадающий список производителей значения из базы данных
            queryManufacturer = database.query("SELECT id, name FROM manufacturer");
            ArrayList<String> resultManufacturer = new ArrayList<>();
            for (ArrayList<String> row : queryManufacturer) {
                resultManufacturer.add(row.get(1));
            }
            manufacturer.getItems().setAll(FXCollections.observableArrayList(resultManufacturer));
            //Добавляем в выпадающий список поставщиков значения из базы данных
            querySupplier = database.query("SELECT id, name FROM supplier");
            ArrayList<String> resultSupplier = new ArrayList<>();
            for (ArrayList<String> row : querySupplier) {
                resultSupplier.add(row.get(1));
            }
            supplier.getItems().setAll(FXCollections.observableArrayList(resultSupplier));
            //Добавляем в выпадающий список поставщиков значения из базы данных
            queryCategory = database.query("SELECT id, name FROM category");
            ArrayList<String> resultCategory = new ArrayList<>();
            for (ArrayList<String> row : queryCategory) {
                resultCategory.add(row.get(1));
            }
            category.getItems().setAll(FXCollections.observableArrayList(resultCategory));
            //Если глобальная строка, которую мы изменяем, указана, то передаём с неё данные на форму
            if (editable != null) {
                name.setText(editable.getName());
                category.getSelectionModel().select(editable.getCategory());
                manufacturer.getSelectionModel().select(editable.getManufacturer());
                supplier.getSelectionModel().select(editable.getSupplier());
                specifications.setText(editable.getSpecifications());
                article.setText(editable.getArticle());
                price.setText(editable.getPrice());
                count.setText(editable.getCount());
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
    //Метод для добавления или изменения товара
    @FXML
    void doAction(ActionEvent event) throws SQLException {
        //Проверяем, не пусты ли строки
        if(!name.getText().isEmpty() && category.getSelectionModel().getSelectedItem() != null && manufacturer.getSelectionModel().getSelectedItem() != null && supplier.getSelectionModel().getSelectedItem() != null && !article.getText().isEmpty() && !price.getText().isEmpty() && !count.getText().isEmpty()) {
            //Проверяем формат ввода цены
            if (price.getText().matches(".*\\d.*\\..{2}")) {
                //Если глобальная строка, которую мы изменяем, указана, то изменяем данные по товару в базе данных
                if (editable != null) {
                    try {
                        database.query("UPDATE product " +
                                "SET id_supplier = " + querySupplier.get(supplier.getSelectionModel().getSelectedIndex()).get(0) + ", " +
                                "id_manufacturer = " + queryManufacturer.get(manufacturer.getSelectionModel().getSelectedIndex()).get(0) + ", " +
                                "id_category = " + queryCategory.get(category.getSelectionModel().getSelectedIndex()).get(0) + ", " +
                                "name = '" + name.getText() + "', " +
                                "specifications = '" + specifications.getText() + "', " +
                                "article = '" + article.getText() + "', " +
                                "price = " + price.getText() + ", " +
                                "count = " + count.getText() + " " +
                                "WHERE id = " + editable.getId()
                        );
                    } catch (Exception e) {
                        //Если на стороне сервера возникла проблема - выводим об этом ошибку
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Ошибка на стороне сервера!");
                        alert.setContentText("Попробуйте позже!");
                        alert.showAndWait();
                    }
                    //Иначе вносим новый товар в базу данных
                } else {
                    try {
                        database.query("INSERT INTO product(id_supplier, id_manufacturer, id_category, name, specifications, article, price, count) " +
                                "VALUE ( " + querySupplier.get(supplier.getSelectionModel().getSelectedIndex()).get(0) + ", " +
                                queryManufacturer.get(manufacturer.getSelectionModel().getSelectedIndex()).get(0) + ", " +
                                queryCategory.get(category.getSelectionModel().getSelectedIndex()).get(0) + ", " +
                                "'" + name.getText() + "', " +
                                "'" + specifications.getText() + "', " +
                                "'" + article.getText() + "', " +
                                price.getText() + ", " +
                                count.getText() + ")"
                        );
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
                //Закрываем окно с формой ввода данных
                ((Stage) doActionButton.getScene().getWindow()).close();
            } else {
                //Если формат ввода не совпал - выдаём окно с ошибкой
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Неверный формат ввода цены!");
                alert.setContentText("Укажите верные данные!");
                alert.showAndWait();
            }
        } else {
            //Если есть пустые строки - выдаём окно с ошибкой
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Пустые строки!");
            alert.setContentText("Заполните их данными!");
            alert.showAndWait();
        }
    }
    //Метод для отображения элементов внесения новой категории
    @FXML
    void newCategory(ActionEvent event) {
        categoryBox.setVisible(true);
    }
    //Метод для отображения элементов внесения нового производителя
    @FXML
    void newManufacturer(ActionEvent event) {
        manufacturerBox.setVisible(true);
    }
    //Метод для отображения элементов внесения нового поставщика
    @FXML
    void newSupplier(ActionEvent event) {
        supplierBox.setVisible(true);
    }
    //Метод для внесения новой категории в базу данных
    @FXML
    void addCategory(ActionEvent event){
        try {
            database.query("INSERT INTO category(name) VALUE ('" + categoryField.getText() + "')");
            categoryBox.setVisible(false);
            //Обновляем содержимое формы
            initialize();
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
    //Метод для внесения нового производителя в базу данных
    @FXML
    void addManufacturer(ActionEvent event){
        try {
            database.query("INSERT INTO manufacturer(name) VALUE ('" + manufacturerField.getText() + "')");
            manufacturerBox.setVisible(false);
            //Обновляем содержимое формы
            initialize();
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
    //Метод для внесения нового поставщика в базу данных
    @FXML
    void addSupplier(ActionEvent event){
        try{
            database.query("INSERT INTO supplier(name) VALUE ('" + supplierField.getText() + "')");
            supplierBox.setVisible(false);
            //Обновляем содержимое формы
            initialize();
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
    //Метод для удаления категории из базы данных
    @FXML
    void deleteCategory(ActionEvent event){
        try{
            database.query("DELETE FROM category WHERE id = " + queryCategory.get(category.getSelectionModel().getSelectedIndex()).get(0));
            //Обновляем содержимое формы
            initialize();
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
    //Метод для удаления производителя из базы данных
    @FXML
    void deleteManufacturer(ActionEvent event){
        try{
            database.query("DELETE FROM manufacturer WHERE id = " + queryManufacturer.get(manufacturer.getSelectionModel().getSelectedIndex()).get(0));
            //Обновляем содержимое формы
            initialize();
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
    //Метод для удаления поставщика из базы данных
    @FXML
    void deleteSupplier(ActionEvent event){
        try{
            database.query("DELETE FROM supplier WHERE id = " + querySupplier.get(supplier.getSelectionModel().getSelectedIndex()).get(0));
            //Обновляем содержимое формы
            initialize();
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