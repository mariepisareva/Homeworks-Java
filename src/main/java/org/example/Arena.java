package org.example;

public class Arena {
    private final Hero fighter1;
    private final Hero fighter2;
    private int roundCounter;

    public Arena(Hero fighter1, Hero fighter2) {
        if (fighter1 == null || fighter2 == null) {
            throw new IllegalArgumentException("Бойцы не могут быть null");
        }

        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
        this.roundCounter = 0;
    }

    public void startTournament() {
        while (fighter1.isAlive() && fighter2.isAlive()) {
            roundCounter++;
            System.out.println("Раунд " + roundCounter);

            float chooseNumber = (float) Math.random();

            Hero attacker;
            Hero defender;

            if (chooseNumber < 0.5) {
                attacker = fighter1;
                defender = fighter2;
            } else {
                attacker = fighter2;
                defender = fighter1;
            }

            ActionType action1 = attacker.makeTurn(defender);
            System.out.println("Игрок " + attacker.getName() + " совершил действие: " + action1.getDescription());

            if (!defender.isAlive()) {
                break;
            }

            ActionType action2 = defender.makeTurn(attacker);
            System.out.println("Игрок " + defender.getName() + " совершил действие: " + action2.getDescription());

            System.out.println();
            System.out.println("Раунд окончен");
            System.out.println();

            System.out.println("Игрок " + attacker.getName() + ": здоровье " + attacker.getHealth() + "/" + attacker.getMaxHealth());
            if (attacker instanceof Mage) {
                Mage mage = (Mage) attacker;

                System.out.println("Мана: " + mage.getMana() + "/" + mage.getMaxMana());

            } else if (attacker instanceof Warrior) {
                Warrior warrior = (Warrior) attacker;

                System.out.println("Броня: " + warrior.getArmor());
            }

            System.out.println("Игрок " + defender.getName() + ": здоровье " + defender.getHealth() + "/" + defender.getMaxHealth());
            if (defender instanceof Mage) {
                Mage mage = (Mage) defender;

                System.out.println("Мана: " + mage.getMana() + "/" + mage.getMaxMana());

            } else if (defender instanceof Warrior) {
                Warrior warrior = (Warrior) defender;

                System.out.println("Броня: " + warrior.getArmor());
            }
        }
        if (fighter1.isAlive()) {
            System.out.println("Игрок " + fighter1.getName() + " победил!!!");
        }
        else {
            System.out.println("Игрок " + fighter2.getName() + " победил!!!");
        }
    }
}
