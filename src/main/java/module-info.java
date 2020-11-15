module com.unisc {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
	requires ADReNA.API;
	requires javafx.graphics;
    opens com.unisc.moedas to javafx.fxml;
    exports com.unisc.moedas;
}