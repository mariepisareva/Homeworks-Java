package org.example;

public class Calculator<T extends Number> {
    public double sum(T a, T b) {
        if (a != null && b != null) {
            return a.doubleValue() + b.doubleValue();
        }
        return Double.NaN;
    }

    public double subtract(T a, T b) {
        if (a != null && b != null) {
            return a.doubleValue() - b.doubleValue();
        }
        return Double.NaN;
    }

    public double multiply(T a, T b) {
        if (a != null && b != null) {
            return a.doubleValue() * b.doubleValue();
        }
        return Double.NaN;
    }

    public double divide(T a, T b) {
        if (a != null && b != null) {
            if (b.doubleValue() != 0) {
                return a.doubleValue() / b.doubleValue();
            }
        }
        return Double.NaN;
    }

    public static void main(String[] args) {
        final Calculator<Integer> intCalc = new Calculator<>();
        final double result = intCalc.sum(5, 3);

        final Calculator<Double> doubleCalc = new Calculator<>();
        final double div = doubleCalc.divide(10.0, 4.0);
    }
}