module org.niaktes.netty.chat.client {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.all;
    requires io.netty.codec;

    opens org.niaktes.netty.chat.client to javafx.fxml;
    exports org.niaktes.netty.chat.client;
}