package com.nkumbo.workshopjavafxjbdc.gui;

import com.nkumbo.workshopjavafxjbdc.database.DbException;
import com.nkumbo.workshopjavafxjbdc.gui.listeners.DataChangeListener;
import com.nkumbo.workshopjavafxjbdc.gui.util.Alerts;
import com.nkumbo.workshopjavafxjbdc.gui.util.Constraints;
import com.nkumbo.workshopjavafxjbdc.gui.util.Utils;
import com.nkumbo.workshopjavafxjbdc.model.entities.Department;
import com.nkumbo.workshopjavafxjbdc.model.entities.Seller;
import com.nkumbo.workshopjavafxjbdc.model.exceptions.ValidationException;
import com.nkumbo.workshopjavafxjbdc.model.services.DepartmentService;
import com.nkumbo.workshopjavafxjbdc.model.services.SellerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller entity;
    private SellerService service;
    private DepartmentService departmentService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    private Boolean editable;

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private TextField txtBaseSalary;
    @FXML
    private ComboBox<Department> comboBoxDepartment;


    @FXML
    private Label labelErrorId;
    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorEmail;
    @FXML
    private Label labelErrorBirthDate;
    @FXML
    private Label labelErrorBaseSalary;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    private ObservableList<Department> obsList;

    public void setSeller(Seller entity) {
        this.entity = entity;
    }

    public void setServices(SellerService service, DepartmentService departmentService) {
        this.service = service;
        this.departmentService = departmentService;
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

    private void notifyDataChangeListeners() {
        for(DataChangeListener listener: dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Seller getFormData() {

        Seller obj = new Seller();

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
        if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")){
            exception.addError("email", "Field can't be empty");
        }
        if(txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")){
            exception.addError("baseSalary", "Field can't be empty");
        }
        if(dpBirthDate.getValue() == null){
            exception.addError("birthDate", "Field can't be empty");
        }
        if(exception.getErrors().size() > 0){
            throw exception;
        }

        obj.setId(id);
        obj.setName(txtName.getText());
        obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
        obj.setEmail(txtEmail.getText());
        obj.setDepartment(comboBoxDepartment.getValue());
        Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
        obj.setBirthDate(Date.from(instant));

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
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
        initializeComboBoxDepartment();
    }


    public void updateFormData(){
        if(entity == null){
            throw new IllegalStateException("Entity is null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText((entity.getEmail()));
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
        if(entity.getBirthDate() != null)
            dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
        if(entity.getDepartment() == null){
            comboBoxDepartment.getSelectionModel().selectFirst();
        } else {
            comboBoxDepartment.setValue(entity.getDepartment());
        }
    }

    public void loadAssociatedObjects(){
        if(departmentService == null){
            throw new IllegalStateException("DepartmentService was null");
        }
        List<Department> list = departmentService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(obsList);
    }

    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        labelErrorId.setText(fields.contains("id")? errors.get("id"):"");
        labelErrorName.setText(fields.contains("name")? errors.get("name"):"");
        labelErrorEmail.setText(fields.contains("email")? errors.get("email"):"");
        labelErrorBaseSalary.setText(fields.contains("baseSalary")? errors.get("baseSalary"):"");
        labelErrorBirthDate.setText(fields.contains("birthDate")? errors.get("birthDate"):"");
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }
}
