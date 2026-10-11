package org.example;

public abstract class Hero {
    private static final int MIN_STAT_VALUE = 1;

    private final String name;
    private final int maxHealth;
    private final int baseAttack;
    private int health;

    public Hero(String name, int maxHealth, int baseAttack) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }

        if (maxHealth < MIN_STAT_VALUE || baseAttack < MIN_STAT_VALUE) {
            throw new IllegalArgumentException("Характеристики не могут быть меньше 1");
        }

        this.name = name;
        this.maxHealth = maxHealth;
        this.baseAttack = baseAttack;
        this.health = maxHealth;
    }

    public String getName() { return this.name; }
    public int getMaxHealth() { return this.maxHealth; }
    public int getBaseAttack() { return this.baseAttack; }
    public int getHealth() { return this.health; }

    public abstract void attack(Hero target);
    public abstract ActionType makeTurn(Hero target);
    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Урон не может быть отрицательным");
        }

        health -= damage;
        if (health < 0) {
            health = 0;
        }

        if (health == 0) {
            System.out.println(name + " пал в бою!");
        }
    }

    public void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Характеристика не может быть отрицательной");
        }

        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public boolean isAlive() {
        return health > 0;
    }
}
