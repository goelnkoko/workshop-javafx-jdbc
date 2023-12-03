module com.nkumbo.workshopjavafxjbdc {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    exports com.nkumbo.workshopjavafxjbdc.gui;
    opens com.nkumbo.workshopjavafxjbdc.gui to javafx.fxml;
    exports com.nkumbo.workshopjavafxjbdc.application;
    opens com.nkumbo.workshopjavafxjbdc.application to javafx.fxml;
}