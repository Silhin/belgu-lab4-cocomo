package ru.silhin.cocomo;

public enum ProjectType {
    WIDESPREAD(2.4F, 1.05F, 2.5F, 0.38F),
    SEMI_INDEPENDENT(3.0F, 1.12F, 2.5F, 0.35F),
    BUILT_IN(3.6F, 1.20F, 2.5F, 0.32F);

    public final float coeffA;
    public final float coeffB;
    public final float coeffC;
    public final float coeffD;
    ProjectType(float a, float b, float c, float d) {
        this.coeffA = a;
        this.coeffB = b;
        this.coeffC = c;
        this.coeffD = d;
    }

    @Override
    public String toString() {
        return switch (this) {
            case WIDESPREAD -> "Распространенный";
            case SEMI_INDEPENDENT -> "Полунезависимый";
            case BUILT_IN -> "Встроенный";
        };
    }
}
