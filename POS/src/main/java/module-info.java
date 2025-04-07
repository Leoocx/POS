module com.system.pos.pos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires kernel;
    requires layout;
    requires java.sql;

    opens com.system.pos.pos to javafx.fxml;
    exports com.system.pos.pos;
}