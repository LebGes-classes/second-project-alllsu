package org.example;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Menu menu = new Menu(manager);
        menu.showMenu();
    }
}