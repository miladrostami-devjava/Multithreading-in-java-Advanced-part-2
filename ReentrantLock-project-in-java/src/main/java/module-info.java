module com.example.reentrantlockprojectinjava {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.reentrantlockprojectinjava to javafx.fxml;
    exports com.example.reentrantlockprojectinjava;
}