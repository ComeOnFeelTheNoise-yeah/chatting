package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ChatController extends Application {
	// ObservableList로 메시지 목록을 관리합니다. ListView와 바인딩되어 있어서 이 리스트가 변경되면 자동으로 UI가
	// 업데이트됩니다.
	private ObservableList<String> messages;

	@FXML
	private Pane chat_background;

	@FXML
	private ImageView caht_back_btn;

	@FXML
	private TextArea chat_text_area;

	@FXML
	private ImageView send_btn;

	@FXML
	private Button friendBtn, chatBtn;

	@FXML
	private ListView<String> chat_list_view;

	@FXML
	private Label pinnedMessageLabel1; // FXML 파일에서 이 ID로 Label을 연결

	public ChatController() {
		messages = FXCollections.observableArrayList();
	}

	@FXML
	public void handleBtnClick1(ActionEvent ae) throws IOException {
		switchScene(ae, "profile.fxml", 570, 900);
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

	@FXML
	public void send() {

		String text = chat_text_area.getText(); // 텍스트 영역의 내용을 가져옵니다.

		if (!text.isEmpty()) { // 텍스트 영역이 비어있지 않다면
			messages.add(text); // 내용을 messages 리스트에 추가하고,
			chat_text_area.clear(); // 텍스트 영역은 비웁니다.
		}

//		// chat_list_view에 messages 리스트를 바인딩합니다.
//		chat_list_view.setItems(messages);

	}

	@FXML
	public void initialize() {

		// chat_list_view에 messages 리스트를 바인딩합니다.
		chat_list_view.setItems(messages);

		chat_text_area.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER) {
				if (!keyEvent.isShiftDown()) { // Shift키가 눌리지 않았으면,
					keyEvent.consume(); // 기본 동작(줄 바꿈)은 취소하고,
					send(); // 메시지를 전송합니다.
				} else { // Shift+Enter가 눌렸으면,
					keyEvent.consume(); // 기본 동작(줄 바꿈)은 취소하고,
					chat_text_area.appendText(""); // 직접 줄 바꿈 문자를 추가합니다.
				}
			}
		});

		// 고정메세지
		chat_list_view.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
			if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
				String selectedMessage = chat_list_view.getSelectionModel().getSelectedItem(); // 선택된 메시지를 가져옵니다.
				if (selectedMessage != null) { // 선택된 메시지가 있다면,
					pinnedMessageLabel1.setText(selectedMessage); // 해당 메시지를 상단에 고정합
				}
			}
		});

//		chat_list_view.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
//			if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
//				String selectedMessage = chat_list_view.getSelectionModel().getSelectedItem(); // 선택된 메시지를 가져옵니다.
//				if (selectedMessage != null) { // 선택된 메시지가 있다면,
//					pinnedMessageLabel1.setText(selectedMessage); // 해당 메시지를 상단에 고정합
//				}
//			}
//		});
	}

	// start 메소드에서 JavaFX 애플리케이션의 초기 설정을 합니다.
	@Override
	public void start(Stage primaryStage) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
		Parent root = loader.load();

		ChatController chatController = loader.getController();
		chatController.chat_list_view = new ListView<>();

		Scene scene = new Scene(root);

		primaryStage.setTitle("Chat"); // 윈도우 제목을 설정합니다.
		primaryStage.setScene(scene); // 윈도우에 씬(Scene) 객체를 설정합니다.
		primaryStage.show(); // 윈도우를 보여줍니다.
	}

	// start 메소드에서 JavaFX 애플리케이션의 초기 설정을 합니다.
//	@Override
//	public void start(Stage primaryStage) {
//
//		// 씬(Scene) 객체를 생성하여 레이아웃 컨테이너와 함께 초기화하고,
//		Scene scene = new Scene(chat_background);
//
//		primaryStage.setTitle("Chat App"); // 윈도우 제목을 설정합니다.
//		primaryStage.setScene(scene); // 윈도우에 씬(Scene) 객체를 설정합니다.
//		primaryStage.show(); // 윈도우를 보여줍니다.
//	}

//    @FXML
//    public void showGroupMessages() {
//    	loadGroupMessages();
//    }

	// main 함수에서는 JavaFX 애플리케이션을 시작하는 launch 메소드를 호출합니다.
	public static void main(String[] args) {
		launch(args);
	}
}
