module com.system.pos.pos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires kernel;
    requires layout;
    requires java.sql;
    requires barcodes;
    requires io;
    requires java.desktop;

    opens com.system.pos.pos to javafx.fxml;
    opens com.system.pos.pos.view to javafx.fxml;
    opens com.system.pos.pos.model to javafx.fxml;
    opens com.system.pos.pos.database to javafx.fxml;
    opens com.system.pos.pos.controller to javafx.fxml;


    exports com.system.pos.pos;
    exports com.system.pos.pos.view;
    exports com.system.pos.pos.database;
    exports com.system.pos.pos.model;
    exports com.system.pos.pos.controller;
    exports com.system.pos.pos.report;
    exports com.system.pos.pos.service;
}