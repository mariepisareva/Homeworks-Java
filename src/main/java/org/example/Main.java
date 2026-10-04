package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Account account = new Account(12345, 999, 10000.00, BankType.AUM);

        System.out.println("Добро пожаловать!");

        System.out.println("Введите номер карты: ");

        if (!scanner.hasNextLine()) {
            System.out.println("Ошибка ввода");
            return;
        }

        String cardInput = scanner.nextLine();

        if (!isCardNumber(cardInput)) {
            System.out.println("ОШИБКА: номер карты должен содержать 5 цифр");
            return;
        }

        int cardNumber = Integer.parseInt(cardInput);

        System.out.println("Введите пин-код: ");

        if (!scanner.hasNextLine()) {
            System.out.println("Ошибка ввода");
            return;
        }

        String pinCodeInput = scanner.nextLine();

        if (!isPinCode(pinCodeInput)) {
            System.out.println("ОШИБКА: пин-код должен содержать 3 цифры");
            return;
        }

        int pinCode = Integer.parseInt(pinCodeInput);

        if (cardNumber != account.getCardNumber()
                || pinCode != account.getPinCode()) {

            System.out.println(
                    "Ошибка доступа: неверный номер карты или PIN-код."
            );
            return;
        }

        System.out.println(account);

        CashMachine cashMachine = new CashMachine();

        System.out.println("Введите сумму, которую хотите внести: ");

        if (!scanner.hasNextLine()) {
            System.out.println("Ошибка ввода.");
            return;
        }

        String depositInput = scanner.nextLine();

        if (!isMoney(depositInput)) {
            System.out.println(
                    "ОШИБКА: введите корректную положительную сумму"
            );
            return;
        }

        double deposit = Double.parseDouble(depositInput.replace(',','.'));

        account.balance = cashMachine.deposit(account.getBalance(),deposit);
        System.out.println("Текущий баланс: " + account.getBalance() + " руб.");

        System.out.println("Введите сумму, которую хотите снять: ");

        if (!scanner.hasNextLine()) {
            System.out.println("Ошибка ввода");
            return;
        }

        String withdrawalInput = scanner.nextLine();

        if (!isMoney(withdrawalInput)) {
            System.out.println(
                    "ОШИБКА: введите корректную положительную сумму"
            );
            return;
        }

        double withdrawal = Double.parseDouble(withdrawalInput.replace(',','.'));

        account.balance = cashMachine.withdraw(account.getBalance(),withdrawal, account.getBankType());

        System.out.println("Текущий баланс: " + account.getBalance() + " руб.");

        System.out.println("Завершение работы. До свидания!");

    }

    public static boolean isCardNumber(String value) {

        if (value == null || value.length() != 5) {
            return false;
        }

        for (int i = 0; i < value.length(); i++) {

            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isPinCode(String value) {

        if (value == null || value.length() != 3) {
            return false;
        }

        for (int i = 0; i < value.length(); i++) {

            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isMoney(String value) {

        if (value == null || value.isEmpty()) {
            return false;
        }

        String normal=
                value.replace(',', '.');

        int dotCount = 0;
        int digitCount = 0;

        for (int i = 0; i < normal.length(); i++) {

            char character =
                    normal.charAt(i);

            if (character == '.') {

                dotCount++;

                if (dotCount > 1) {
                    return false;
                }

            } else if (Character.isDigit(character)) {

                digitCount++;

            } else {

                return false;
            }
        }

        if (digitCount == 0) {
            return false;
        }

        double amount =
                Double.parseDouble(normal);

        if (!Double.isFinite(amount)) {
            return false;
        }

        return amount > 0;
    }
}

