package GUI;

import GUI.util.Alerts;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.service.DepartmentService;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Function;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemSeller;
    @FXML
    private MenuItem menuItemDepartment;
    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemSellerAction(){
        System.out.println("onMenuItemSellerAction");
    }

    @FXML
    public void onMenuItemDepartmentAction(){
        loadView("/GUI/DepartmentList.fxml", (DepartmentListController controller) ->{
            controller.setDepartmentSerivce(new DepartmentService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemAboutAction(){
        loadView("/GUI/About.fxml", x -> {});
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction ){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            T controller = loader.getController();
            initializingAction.accept(controller);


        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

}
