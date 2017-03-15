package com.cj.worldbank;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;


public class Application {
    private static final Menu menu = new Menu();
    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public static void main(String[] args) {
        try {
            menu.run();
        } catch (IOException ioe) {
            System.out.println("There was a problem starting running the application's menu.");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session createSession(SessionFactory sessionFactory) {
        return sessionFactory.openSession();
    }
}
