package org.example;

public class Account {
    public int cardNumber;
    public int pinCode;
    public double balance;
    public BankType bankType;

    public Account(int cardNumber, int pinCode, double balance, BankType bankType) {
        if (cardNumber >= 10000 && cardNumber <= 99999) {
                this.cardNumber = cardNumber;
        }
        else {
            System.out.println("ОШИБКА: номер карты должен содержать 5 цифр");
            this.cardNumber = 0;
        }

        if (pinCode >= 100 && pinCode <= 999) {
            this.pinCode = pinCode;
        }
        else {
            System.out.println("ОШИБКА: пин-код должен содержать 3 цифры");
            this.pinCode = 0;
        }

        if (balance >= 0) {
            this.balance = Math.round(balance * 100.0) / 100.0;
        }
        else {
            System.out.println("ОШИБКА: баланс не может быть отрицательным");
            this.balance = 0.00;
        }

        if (bankType == null) {
            this.bankType = BankType.NEO;
        }
        else {
            this.bankType = bankType;
        }
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public int getPinCode() {
        return pinCode;
    }

    public double getBalance() {
        return balance;
    }

    public BankType getBankType() {
        return bankType;
    }

    @Override
    public String toString() {
        return (bankType.getBankFullName() + " " + "Карта: " + cardNumber + ", Баланс: " + Math.round(balance * 100.0) / 100.0 + " руб.");
    }
}
