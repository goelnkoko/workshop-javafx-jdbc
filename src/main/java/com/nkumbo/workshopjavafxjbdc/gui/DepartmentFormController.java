package com.nkumbo.workshopjavafxjbdc.gui;

import com.nkumbo.workshopjavafxjbdc.database.DbException;
import com.nkumbo.workshopjavafxjbdc.gui.listeners.DataChangeListener;
import com.nkumbo.workshopjavafxjbdc.gui.util.Alerts;
import com.nkumbo.workshopjavafxjbdc.gui.util.Constraints;
import com.nkumbo.workshopjavafxjbdc.gui.util.Utils;
import com.nkumbo.workshopjavafxjbdc.model.entities.Department;
import com.nkumbo.workshopjavafxjbdc.model.exceptions.ValidationException;
import com.nkumbo.workshopjavafxjbdc.model.services.DepartmentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {

    private Department entity;
    private DepartmentService service;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    private Boolean editable;

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;

    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorId;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    public void setDepartment(Department entity) {
        this.entity = entity;
    }

    public void setDepartmentService(DepartmentService service) {
        this.service = service;
    }

    public void setTxtIdEditable(Boolean editable) {
        this.editable = editable;
        txtId.setEditable(editable);
    }

    public Boolean getTxtIdEditable(){
        return editable;
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event){
        if(entity == null){
            throw new IllegalStateException("Entity was null");
        }
        if(service == null){
            throw new IllegalStateException("Service was null");
        }

        try{
            labelsErrorClean();
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }
        catch (DbException e){
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
        catch (ValidationException e){
            setErrorMessages(e.getErrors());
        }
    }

    private void labelsErrorClean() {
        labelErrorId.setText("");
        labelErrorName.setText("");
    }

    private void notifyDataChangeListeners() {
        for(DataChangeListener listener: dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Department getFormData() {

        Department obj = new Department();

        ValidationException exception = new ValidationException("Validation error");

        Integer id = Utils.tryParseToInt(txtId.getText());

        if(getTxtIdEditable()){
            obj = service.findById(id);
            if(obj == null){
                exception.addError("id", "Unknown department");
            }
        }

        if(txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("name", "Field can't be empty");
        }

        if(exception.getErrors().size() > 0){
            throw exception;
        }

        obj.setId(id);
        obj.setName(txtName.getText());


        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event){
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }


    public void updateFormData(){
        if(entity == null){
            throw new IllegalStateException("Entity is null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }

    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        if(fields.contains("id")){
            labelErrorId.setText(errors.get("id"));
        }
        if(fields.contains("name")){
            labelErrorName.setText(errors.get("name"));
        }
    }
}
