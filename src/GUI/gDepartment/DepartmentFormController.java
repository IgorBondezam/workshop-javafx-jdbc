package GUI.gDepartment;

import GUI.listeners.DataChangeListener;
import GUI.util.Alerts;
import GUI.util.Constraints;
import GUI.util.Utils;
import db.DbException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.service.DepartmentService;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    private Department entity;

    private DepartmentService service;

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private Label labelErrorName;
    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    @FXML
    public void onBtSaveAction(ActionEvent event){
        if(entity ==null){
            throw new IllegalStateException("Entity was null");
        }

        if(service ==null){
            throw new IllegalStateException("Service was null");
        }

        try{
            entity = getFormData();
            service.saveOrUpdate(entity);
            Alerts.showAlert("Object Saved", null, "Success to save a new Department", Alert.AlertType.INFORMATION);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }catch (DbException e){
            Alerts.showAlert("Error Saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }catch (ValidationException e){
            setErrorMessages(e.getErrors());
        }



    }

    private Department getFormData() {
        Department obj = new Department();

        ValidationException exception = new ValidationException("Validation error");

        obj.setId(Utils.tryParseToInt(txtId.getText()));
        if(txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("name", "Field can't be empty");
        }
        obj.setName(txtName.getText());
        if (exception.getErrors().size() > 0){
            throw exception;
        }

        return obj;
    }

    @FXML
    private void onBtCancelAction(ActionEvent event){
        Utils.currentStage(event).close();
    }

    public void setEntity(Department entity) {
        this.entity = entity;
    }

    public void setService(DepartmentService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener: dataChangeListeners) {
            listener.onDataChanged();
        }
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
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }

    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        if(fields.contains("name")){
            labelErrorName.setText(errors.get("name"));
        }
    }
}
