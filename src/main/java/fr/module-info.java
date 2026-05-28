
module fr.school.smartenergy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;
    requires com.google.gson;

    exports fr.school.smartenergy;
    exports fr.school.smartenergy.controller;
    exports fr.school.smartenergy.model;
    exports fr.school.smartenergy.service;
    exports fr.school.smartenergy.dao;
    exports fr.school.smartenergy.exception;
    exports fr.school.smartenergy.util;

    opens fr.school.smartenergy.controller to javafx.fxml;
    opens fr.school.smartenergy.model to javafx.base;
}
