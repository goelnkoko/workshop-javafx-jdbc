package com.nkumbo.workshopjavafxjbdc.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemSeller;

    @FXML
    private MenuItem menuItemDepartment;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    private void onMenuItemSellerAction(){
        System.out.println("Seller");
    }

    @FXML
    private void onMenuItemDepartmentAction(){
        System.out.println("Department");
    }

    @FXML
    private void onMenuItemAboutAction(){
        System.out.println("About");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}