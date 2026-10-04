package org.example;

public class CashMachine implements WithdrawalOperations, DepositOperations{
    @Override
    public double deposit(double currentAmount, Double depositAmount) {
        if (depositAmount == null || depositAmount <= 0) {
            return Math.round(currentAmount * 100.0) / 100.0;
        }
        double newBalance = currentAmount + depositAmount;

        return Math.round(newBalance * 100.0) / 100.0;
        }

    @Override
    public double withdraw( double currentBalance,
                     Double withdrawalAmount,
                     BankType bankType) {
        if (withdrawalAmount == null || withdrawalAmount <= 0) {
            return Math.round(currentBalance * 100.0) / 100.0;
        }

        double commission = applyCommission(withdrawalAmount, bankType);

        double totalWithdrawalAmount = withdrawalAmount + commission;

        if (totalWithdrawalAmount > currentBalance) {
            System.out.println("Недостаточно средств на счёте");
            return Math.round(currentBalance * 100.0) / 100.0;
        }
        return Math.round((currentBalance - totalWithdrawalAmount) * 100.0) / 100.0;

    }
}
