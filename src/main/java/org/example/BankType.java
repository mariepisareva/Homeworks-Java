package org.example;

public enum BankType {

    NEO ("НеоКредит Банк", 0.01),
    AUM ("Арум Финтех", 0.02),
    VTA ("Вектор Альянс Банк", 0.00);

    public String bankFullName;
    public double commission;

    BankType(String bankFullName, double commission) {
        this.bankFullName = bankFullName;
        this.commission = commission;
    }

    public String getBankFullName() {
        return bankFullName;
    }

    public double getCommission() {
        return commission;
    }
}
