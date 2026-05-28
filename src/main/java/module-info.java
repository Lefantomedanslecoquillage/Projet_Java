module fr.school.smartenergy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires com.google.gson;
    requires java.xml;
    requires java.net.http;

    opens fr.school.smartenergy to javafx.fxml;
    opens fr.school.smartenergy.controller to javafx.fxml;
    opens fr.school.smartenergy.model to javafx.base, com.google.gson;

    exports fr.school.smartenergy;
    exports fr.school.smartenergy.controller;
    exports fr.school.smartenergy.model;
    exports fr.school.smartenergy.service;
    exports fr.school.smartenergy.dao;
    exports fr.school.smartenergy.util;
    exports fr.school.smartenergy.exception;
}
