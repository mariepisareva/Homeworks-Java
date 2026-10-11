package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Создайте первого бойца: ");
        Hero fighter1 = createHero(scanner);

        System.out.println();

        System.out.println("Создайте второго бойца: ");
        Hero fighter2 = createHero(scanner);

        Arena arena = new Arena(fighter1, fighter2);

        System.out.println();
        System.out.println("Начинается турнир!");
        arena.startTournament();

    }

    private static Hero createHero(Scanner scanner) {
        int type;

        while (true) {
            System.out.println("Выберите тип персонажа: 1 - Воин или 2 - Маг");

            if (!scanner.hasNextInt()) {
                System.out.println("ОШИБКА: введите число 1 или 2.");
                scanner.nextLine();
                continue;
            }

            type = scanner.nextInt();
            scanner.nextLine();

            if (type == 1 || type == 2) {
                break;
            }

            System.out.println("ОШИБКА: нужно ввести 1 или 2.");
        }

        String name;

        while (true) {
            System.out.print("Введите имя: ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                break;
            }

            System.out.println("ОШИБКА: Имя не может быть пустым. Попробуйте ещё раз.");
        }

        int maxHealth = readPositiveInt(scanner, "Введите максимальное здоровье:");

        int baseAttack = readPositiveInt(scanner, "Введите базовую атаку:");

        if (type == 1) {
            int armor = readNonNegativeInt(scanner, "Введите броню:");
            return new Warrior(name, maxHealth, baseAttack, armor);
        } else {
            int maxMana = readNonNegativeInt(scanner, "Введите максимальную ману:");
            return new Mage(name, maxHealth, baseAttack, maxMana);
        }
    }

    private static int readPositiveInt(Scanner scanner, String message) {
        while (true) {
            System.out.println(message);

            if (!scanner.hasNextInt()) {
                System.out.println("ОШИБКА: введите целое число.");
                scanner.nextLine();
                continue;
            }

            int value = scanner.nextInt();
            scanner.nextLine();

            if (value >= 1) {
                return value;
            }

            System.out.println("ОШИБКА: Значение должно быть не меньше 1.");
        }
    }

    private static int readNonNegativeInt(Scanner scanner, String message) {
        while (true) {
            System.out.println(message);

            if (!scanner.hasNextInt()) {
                System.out.println("ОШИБКА: введите целое число.");
                scanner.nextLine();
                continue;
            }

            int value = scanner.nextInt();
            scanner.nextLine();

            if (value >= 0) {
                return value;
            }

            System.out.println("Значение не может быть отрицательным.");
        }
    }
}