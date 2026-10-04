package org.example;

public interface WithdrawalOperations {

    double withdraw( double currentBalance,
                     Double withdrawalAmount,
                     BankType bankType);

    default double applyCommission(Double withdrawalAmount, BankType bankType) {
        if (withdrawalAmount == null || bankType == null) {
            return 0.00;
        }

        double commission = withdrawalAmount * bankType.getCommission();

        return Math.round (commission * 100.0) / 100.0;

    }
}
