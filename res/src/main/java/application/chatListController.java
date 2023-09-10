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

	private ObservableList<chatRoom> chatRooms; // 채팅방 목록

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

	// 한 번에 여러개의 채팅방 알람 끄기
	public void handleBell(ActionEvent ae) throws IOException {
		int num = 0; // 사용자가 선택한 채팅방 갯수로 변경
		FXMLLoader loader = new FXMLLoader(getClass().getResource("popup.fxml"));
		Parent root = loader.load();

		popupController controller = loader.getController();
		controller.setMessage(num + " 개의 채팅방 알람을 끄시겠습니까?");

		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}

	
	// 채팅방 이름 변경
	public void chatNameChanges(ActionEvent ae) {}
	
	// 채팅방 상단 고정
	public void chatFix() {}
	
	// 채팅방 이름으로 채팅방 검색
	public void chatSearch(ActionEvent ae) {

	}

	// 친구 선택 후 채팅방 생성
	public void friendChat(ActionEvent ae) {
	}

	// 대화 시작 시 새로운 채팅방 추가
	public chatListController() {
		this.chatRooms = FXCollections.observableArrayList();
	}

	public void addNewChat(String roomName, Image profileImage) {
		this.chatRooms.add(new chatRoom(roomName, profileImage));
	}

	// 채팅방 정렬 (가장 마지막에 대화한 순서대로)
	public void chatSort() {
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	}

}
