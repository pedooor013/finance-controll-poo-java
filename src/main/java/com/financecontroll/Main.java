package com.financecontroll;

import com.financecontroll.model.dao.DatabaseConnection;

class Main{
    public static void main(String[] args){
        DatabaseConnection.getInstance().getConnection();
        System.out.println("Connected to database");
    }
}