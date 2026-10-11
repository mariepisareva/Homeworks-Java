package org.example;

public class Warrior extends Hero implements Restable, Castable {
    private final int armor;

    public Warrior(String name, int maxHealth, int baseAttack, int armor) {
        super(name, maxHealth, baseAttack);

        if (armor < 0) {
            throw new IllegalArgumentException("Броня не может быть отрицательной");
        }

        this.armor = armor;
    }

    @Override
    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Урон не может быть отрицательным");
        }

        int finalDamage = damage - armor;

        if (finalDamage < 1) {
            finalDamage = 1;
        }

        super.takeDamage(finalDamage);
    }

    @Override
    public void castSpecialSkill(Hero target) {
        if (target == null) {
            throw new IllegalArgumentException("Цель атаки не может быть null");
        }

        if (canCast()) {
            int damage = getBaseAttack() + (armor * 2);
            target.takeDamage(damage);
        }
        else {
            System.out.println("Воин попытался ударить щитом, но потерял равновесие!");
        }
    }

    @Override
    public boolean canCast() {
        return getHealth() < 0.5 * getMaxHealth();
    }

    @Override
    public void attack(Hero target) {
        if (target == null) {
            throw new IllegalArgumentException("Цель атаки не может быть null");
        }

        target.takeDamage(getBaseAttack());
        System.out.println(getName() + " атакует " + target.getName() + "!");
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
        int addition = armor * 2;
        super.heal(addition);
        System.out.println(getName() + " использует Второе дыхание и восстанавливает " + addition + " здоровья!");
    }

    @Override
    public boolean needsRest() {
        return getHealth() < 0.15 * getMaxHealth();
    }

    public int getArmor() {
        return armor;
    }
}
