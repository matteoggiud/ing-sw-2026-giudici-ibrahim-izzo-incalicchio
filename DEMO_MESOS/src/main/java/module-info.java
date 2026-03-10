module org.example.demo_mesos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.demo_mesos to javafx.fxml;
    exports org.example.demo_mesos;
}