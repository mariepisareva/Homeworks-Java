package org.example;

public class Mage extends Hero implements Restable, Castable {
    private final int maxMana;
    private int mana;

    public Mage(String name, int maxHealth, int baseAttack, int maxMana) {
        super(name, maxHealth, baseAttack);

        if (maxMana < 0) {
            throw new IllegalArgumentException("Мана не может быть отрицательной");
        }

        this.mana = maxMana;
        this.maxMana = maxMana;
    }


    @Override
    public void castSpecialSkill(Hero target) {
        if (target == null) {
            throw new IllegalArgumentException("Цель атаки не может быть null");
        }

        if (mana >= 25) {
            target.takeDamage(getBaseAttack() * 3);
            mana -= 25;
        }
        else {
            System.out.println("Ошибка заклинания: недостаточно маны");
        }
    }

    @Override
    public boolean canCast() {
        return mana >= 25;
    }

    @Override
    public void attack(Hero target) {
        if (target == null) {
            throw new IllegalArgumentException("Цель атаки не может быть null");
        }

        if (mana >= 10) {
            mana -= 10;
            target.takeDamage(getBaseAttack() * 2);
            System.out.println(getName() + " использует магический выстрел!");
        }
        else {
            target.takeDamage(getBaseAttack() / 2);
            mana += 5;
            if (mana > maxMana) {
                mana = maxMana;
            }
            System.out.println(getName() + " ударяет посохом и восстанавливает 5 маны");
        }
    }

    @Override
    public ActionType makeTurn(Hero target) {
        if (canCast()) {
            castSpecialSkill(target);
            return ActionType.SPECIAL_SKILL;
        }

        else if (needsRest()) {
            rest();
            return ActionType.REST;
        }

        else {
            attack(target);
            return ActionType.BASE_ATTACK;
        }
    }

    @Override
    public void rest() {
        mana = maxMana;
        heal(getBaseAttack());
        System.out.println("Здоровье и мана восстановлены");

    }

    @Override
    public boolean needsRest() {
        return (mana == 0 || getHealth() < 0.3 * getMaxHealth());
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }
}
