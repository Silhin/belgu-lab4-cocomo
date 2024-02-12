package ru.silhin.cocomo.layer;

import ru.silhin.cocomo.ProjectType;

import java.util.Arrays;

public class IntermediateLayer extends BasicLayer {
    private double[] factors = new double[15];

    public double getRegulatoryFactor() {
        return Arrays.stream(factors).reduce((a, b) -> a*b).orElse(1F);
    }

    @Override
    public double getComplexity() {
        return this.getRegulatoryFactor() * getIntermediateCoeffA(this.getType()) *
                Math.pow(this.getSize(), getIntermediateCoeffB(this.getType()));
    }

    public void setFactors(double[] factors) {
        this.factors = factors;
    }

    public void setFactor(int index, double value) {
        if(index < 0 || index > factors.length) throw new RuntimeException("Failed to set factor value! IndexOut");
        this.factors[index] = value;
    }

    public double getFactor(int index) {
        if(index < 0 || index > factors.length) throw new RuntimeException("Failed to set factor value! IndexOut");
        return factors[index];
    }

    public static float getIntermediateCoeffA(ProjectType type) {
        return switch (type) {
            case WIDESPREAD -> 3.2F;
            case SEMI_INDEPENDENT -> 3.0F;
            case BUILT_IN -> 2.8F;
        };
    }

    public static float getIntermediateCoeffB(ProjectType type) {
        return switch (type) {
            case WIDESPREAD -> 1.05F;
            case SEMI_INDEPENDENT -> 1.12F;
            case BUILT_IN -> 1.20F;
        };
    }
}
