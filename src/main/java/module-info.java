module top.pxasen.dewdock {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires atlantafx.base;
    requires io.vproxy.vfx;
    requires org.kordamp.ikonli.feather;

    opens top.pxasen.dewdock to javafx.fxml;
    exports top.pxasen.dewdock;
}