package team.isaz.dmbot.domain.dice.model;

public enum DiceTypeEnum {
    COMMON, FUDGE, COIN, D20;

    public static DiceTypeEnum formByValue(String val) {
        if (val.equals("F")) {
            return DiceTypeEnum.FUDGE;
        }
        if (val.equals("C") || val.equals("С")) {
            return DiceTypeEnum.COIN;
        }
        if (val.equals("20")) {
            return DiceTypeEnum.D20;
        }
        return DiceTypeEnum.COMMON;
    }
}
