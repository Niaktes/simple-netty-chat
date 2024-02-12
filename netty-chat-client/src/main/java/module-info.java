module org.niaktes.netty.chat.client {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.niaktes.netty.chat.client to javafx.fxml;
    exports org.niaktes.netty.chat.client;
}