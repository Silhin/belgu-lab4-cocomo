package ru.silhin.cocomo.layer;

import ru.silhin.cocomo.ProjectType;

public interface Layer {
    double getComplexity();

    double getTime();

    long getSize();

    void setSize(long size);

    ProjectType getType();

    void setType(ProjectType type);
}
