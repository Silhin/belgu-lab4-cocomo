package ru.silhin.cocomo.layer;

import ru.silhin.cocomo.AdvancedScoreType;

import java.util.Arrays;

public class AdvancedLayer extends IntermediateLayer {
    private AdvancedScoreType scoreType;
    private final double[] scaleFactors = new double[5];

    @Override
    public double getComplexity() {
        return this.getRegulatoryFactor() * scoreType.A
                * Math.pow(this.getSize(), 0.91D + 0.01D * getSummScaleFactor());
    }

    @Override
    public double getTime() {
        double sced = this.getFactor(scoreType.n - 1);
        return sced * 3.67F * Math.pow(this.getComplexity() / sced, 0.28D + 0.002D * getSummScaleFactor());
    }

    private double getSummScaleFactor() {
        return Arrays.stream(scaleFactors).sum();
    }

    public void setScaleFactor(int index, double value) {
        this.scaleFactors[index] = value;
    }

    public AdvancedScoreType getScoreType() {
        return scoreType;
    }

    public void setScoreType(AdvancedScoreType scoreType) {
        this.scoreType = scoreType;
        this.setFactors(new double[scoreType.n]);
    }
}
