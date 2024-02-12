package ru.silhin.cocomo.layer;

import ru.silhin.cocomo.ProjectType;

public class BasicLayer implements Layer {
    private long size;
    private ProjectType type;

    @Override
    public double getComplexity() {
        return type.coeffA * Math.pow(size, type.coeffB);
    }

    @Override
    public double getTime() {
        return type.coeffC * Math.pow(this.getComplexity(), type.coeffD);
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public ProjectType getType() {
        return type;
    }

    @Override
    public void setType(ProjectType type) {
        this.type = type;
    }
}
