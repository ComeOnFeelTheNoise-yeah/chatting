package application;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class chatListController extends Application {

	@FXML
	private Button addChat, search, bell, yes, no, friendBtn, chatBtn;

//	private ObservableList<chatRoom> chatRooms;

	@FXML
	public void handleBtnClick1(ActionEvent ae) throws IOException {
		switchScene(ae, "friendList.fxml", 570, 900);
	}

	@FXML
	public void handleBtnClick2(ActionEvent ae) throws IOException {
		switchScene(ae, "chatList.fxml", 570, 900);
	}

	private void switchScene(ActionEvent ae, String fxml, int width, int height) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
		Parent root = loader.load();

		Scene scene = new Scene(root, width, height);

		Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();

		stage.setScene(scene);

		stage.setResizable(false);

		stage.show();
	}


	public void handleBell(ActionEvent ae) throws IOException {
		int num = 0; 
		FXMLLoader loader = new FXMLLoader(getClass().getResource("popup.fxml"));
		Parent root = loader.load();

		popupController controller = loader.getController();
		controller.setMessage(num + " ���� ä�ù� �˶��� ���ðڽ��ϱ�?");

		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}

	
	public void chatNameChanges(ActionEvent ae) {}
	
	public void chatFix() {}
	
	public void chatSearch(ActionEvent ae) {

	}

	public void friendChat(ActionEvent ae) {
	}

//	public chatListController() {
//		this.chatRooms = FXCollections.observableArrayList();
//	}

//	public void addNewChat(String roomName, Image profileImage) {
//		this.chatRooms.add(new chatRoom(roomName, profileImage));
//	}

	public void chatSort() {
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	}

}
