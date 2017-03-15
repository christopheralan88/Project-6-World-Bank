package com.cj.worldbank;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Menu {
    private BufferedReader reader;
    private Map<String, String> choices;

    public Menu() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        this.choices = new HashMap<>();
        choices.put("ADD", "add a country");
        choices.put("ALL", "view all countries' data");
        choices.put("DEL", "delete a country");
        choices.put("EDIT", "edit a county's statistics");
        choices.put("STAT", "view statistics for all indicators");
        choices.put("QUIT", "quit the application");
    }

    public void run() throws IOException { //
        String choice;
        do {
            printMenuChoices();
            choice = reader.readLine();
            if (choice.equals("ADD")) {
                try {
                    addCountry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            } else if (choice.equals("ALL")) {
                try {
                    getAllCountries();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            } else if (choice.equals("DEL")) {
                try {
                    deleteCountry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            } else if (choice.equals("EDIT")) {
                try {
                    editCountry();
                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage());
                }
            } else if (choice.equals("STAT")) {
                try {
                    getStats();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } while (! choice.equals("QUIT"));
        System.out.println("Have a good day!");
        System.exit(0);
    }

    public void printMenuChoices() {
        System.out.println("What would you like to do?");
        for (Map.Entry<String, String> entry : choices.entrySet()) {
            System.out.printf("Type %s to %s %n", entry.getKey(), entry.getValue());
        }
    }

    public void editCountry() throws IOException {
        System.out.println("What is the code of the country you want to edit?");
        String code = reader.readLine();

        System.out.println("What do you want to update the name of the country to?");
        String name = reader.readLine();

        System.out.println("What do you want to update the internet usage to?");
        Double internetUsers = Double.parseDouble(reader.readLine());

        System.out.println("What do you want to update the adult literacy rate to?");
        Double adultLiteracy = Double.parseDouble(reader.readLine());

        Session session = Application.createSession(Application.getSessionFactory());
        Query query = session.createQuery(String.format("UPDATE Country SET " +
                                                        "NAME = '%s', " +
                                                        "INTERNETUSERS = '%s', " +
                                                        "ADULTLITERACYRATE = '%s' " +
                                                        "WHERE CODE = '%s'", name, internetUsers, adultLiteracy, code));
        Transaction transaction = session.beginTransaction();
        int result = query.executeUpdate();
        if (result == 0) {
            transaction.rollback();
            System.out.println("Sorry, there was an issue editing the country in the database");
        } else {
            transaction.commit();
            System.out.println("Country edited successfully!");
        }
    }

    public void deleteCountry() throws IOException {
        System.out.println("What is the code of the country you want to delete?");
        String code = reader.readLine();
        Session session = Application.createSession(Application.getSessionFactory());
        Query query = session.createQuery(String.format("DELETE FROM Country WHERE CODE = '%s'", code));
        Transaction transaction = session.beginTransaction();
        int result = query.executeUpdate();
        transaction.commit();

        if (result == 0) {
            System.out.println("Sorry, there was an issue deleting the country from the database");
        } else {
            System.out.println("Country deleted successfully!");
        }
    }

    public void addCountry() throws IOException {
        System.out.println("What is the country's code?");
        String code = reader.readLine();

        System.out.println("What is the country's name?");
        String name = reader.readLine();

        System.out.println("What is the country's internet usage rate?");
        Double internetUsage = Double.parseDouble(reader.readLine());

        System.out.println("What is the country's adult literacy rate?");
        Double adultLiteracyRate = Double.parseDouble(reader.readLine());

        Country newCountry = new Country().setCode(code)
                                          .setName(name)
                                          .setInternetUsers(internetUsage)
                                          .setAdultLiteracyRate(adultLiteracyRate);

        Session session = Application.createSession(Application.getSessionFactory());
        Transaction transaction = session.beginTransaction();
        try {
            session.save(newCountry);
            transaction.commit();
            System.out.println("Country added successfully!");
        } catch (HibernateException he) {
            transaction.rollback();
            System.out.println(he.getMessage());
        }
    }

    public void getAllCountries() {
        Session session = Application.createSession(Application.getSessionFactory());
        String hql = "FROM Country ORDER BY CODE";
        Query query = session.createQuery(hql);
        List<?> data = query.getResultList();

        System.out.println("==================================ALL COUNTRIES==================================");
        System.out.printf("%-5s%-50s%-15s%-20s %n", "Code", "Name", "Internet Users", "Adult Literacy Rate");
        String pattern = "####.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        decimalFormat.setMinimumFractionDigits(2);
        for (Object item : data) {
            Country country = (Country) item;
            String internetUsers = (country.getInternetUsers() == 0.0) ? "--" : decimalFormat.format(country.getInternetUsers());
            String adultLiteracy = (country.getAdultLiteracyRate() == 0.0) ? "--" : decimalFormat.format(country.getAdultLiteracyRate());
            System.out.printf("%-5s%-50s%-15s%-20s %n", country.getCode(),
                                                        country.getName(),
                                                        internetUsers,
                                                        adultLiteracy);
        }
    }

    @SuppressWarnings("unchecked")
    public void getStats() {
        Session session = Application.createSession(Application.getSessionFactory());

        String hql = "FROM Country";
        Query query = session.createQuery(hql);
        List<Country> countries = query.getResultList();

        Country bottomInternetUsage = countries.stream()
                                                   .filter(a -> a.getInternetUsers() != 0.0)
                                                   .sorted((a, b) -> a.getInternetUsers().compareTo(b.getInternetUsers()))
                                                   .findFirst()
                                                   .orElse(null);
        Country topInternetUsage = countries.stream()
                                                .filter(a -> a.getInternetUsers() != 0.0)
                                                .sorted((a, b) -> - a.getInternetUsers().compareTo(b.getInternetUsers()))
                                                .findFirst()
                                                .orElse(null);

        Country topAdultLiteracy = countries.stream()
                                                .filter(a -> a.getAdultLiteracyRate() != 0.0)
                                                .sorted((a, b) -> - a.getAdultLiteracyRate().compareTo(b.getAdultLiteracyRate()))
                                                .findFirst()
                                                .orElse(null);

        Country bottomAdultLiteracy = countries.stream()
                                                   .filter(a -> a.getAdultLiteracyRate() != 0.0)
                                                   .sorted((a, b) -> a.getAdultLiteracyRate().compareTo(b.getAdultLiteracyRate()))
                                                   .findFirst()
                                                   .orElse(null);

        System.out.println("==================================STATISTICS==================================");
        System.out.printf("%-10s%-20s%-20s %n", "Statistic", "Internet Usage", "Adult Literacy Rate");

        String pattern = "####.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        System.out.printf("%-10s%-20s%-20s %n", "MAX",
                                                topInternetUsage.getName() + " - " + decimalFormat.format(topInternetUsage.getInternetUsers()),
                                                topAdultLiteracy.getName() + " - " + decimalFormat.format(topAdultLiteracy.getAdultLiteracyRate()));
        System.out.printf("%-10s%-20s%-20s %n", "MIN",
                                                bottomInternetUsage.getName() + " - " + decimalFormat.format(bottomInternetUsage.getInternetUsers()),
                                                bottomAdultLiteracy.getName() + " - " + decimalFormat.format(bottomAdultLiteracy.getAdultLiteracyRate()));
        System.out.println("------------------------------------------------------------------------------");
        System.out.printf("Correlation Coefficient:  %s %n", correlationCoefficient(countries));
    }

    private Double correlationCoefficient(List<Country> countries) {
        List<Country> countriesFiltered = countries.stream()
                                                   .filter(a -> a.getAdultLiteracyRate() != 0.0)
                                                   .filter(a -> a.getInternetUsers() != 0.0)
                                                   .collect(Collectors.toList());
        Double[][] scores = new Double[countriesFiltered.size()][countriesFiltered.size()];
        for (int i=0; i < countriesFiltered.size(); i++) {
            scores[i][0] = countriesFiltered.get(i).getInternetUsers();
            scores[i][1] = countriesFiltered.get(i).getAdultLiteracyRate();
        }
        CoefficientCorrelation cc = new CoefficientCorrelation(scores);
        return cc.calculate();
    }
}
