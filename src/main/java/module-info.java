module ru.silhin.cocomo {
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens ru.silhin.cocomo to javafx.fxml;
    exports ru.silhin.cocomo;
    exports ru.silhin.cocomo.layer;
}