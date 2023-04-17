module com.example.ynovdesktopapp {
    requires javafx.controls;
    requires javafx.fxml;
        requires javafx.web;

        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
            requires net.synedra.validatorfx;
                requires org.kordamp.bootstrapfx.core;
    requires java.sql;
//            requires eu.hansolo.tilesfx;

    opens com.example.ynovdesktopapp to javafx.fxml;
    exports com.example.ynovdesktopapp;
}