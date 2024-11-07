package main.bicycle;

import java.sql.*;
import java.util.ArrayList;

//Класс для работы с базой данных
public class Database {
    //Храню подключение для объекта класса
    public Connection connection;
    //В конструкторе создаю подключение к базе данных
    public Database(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(true);
    }
    //Код для обработки запросов MySQL
    public ArrayList<ArrayList<String>> query(String query) throws SQLException {
        //Создаю новый запрос
        Statement statement = connection.createStatement();
        try {
            //Беру результат запроса
            ResultSet resultSet = statement.executeQuery(query);
            //Создаю список списков (строк) для хранения результата в удобном формате
            ArrayList<ArrayList<String>> result = new ArrayList<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            //Добавляю в список списков (строк) результат запроса
            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getString(i));
                }
                result.add(row);
            }
            //Возвращаю список списков (строк)
            return result;
        } catch (Exception e1){
            //Если запрос не возвращает значений, то просто выполняю данный запрос
            try {
                statement.executeUpdate(query);
                return null;
            } catch(Exception e2) {
                //Если произошла ошибка - вывожу её в консоль
                e1.printStackTrace();
                e2.printStackTrace();
                return null;
            }
        }
    }
}