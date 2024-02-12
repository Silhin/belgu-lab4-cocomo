package ru.silhin.cocomo;

public enum AdvancedScoreType {
    PRELIMINARY(2.94F, 7),
    DETAILED(2.45F, 17);

    public final float A;
    public final int n;
    AdvancedScoreType(float A, int n) {
        this.A = A;
        this.n = n;
    }
}
