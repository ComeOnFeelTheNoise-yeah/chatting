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
	// ObservableList�� �޽��� ����� �����մϴ�. ListView�� ���ε��Ǿ� �־ �� ����Ʈ�� ����Ǹ� �ڵ����� UI��
	// ������Ʈ�˴ϴ�.
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
	private Label pinnedMessageLabel1; // FXML ���Ͽ��� �� ID�� Label�� ����

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

		String text = chat_text_area.getText(); // �ؽ�Ʈ ������ ������ �����ɴϴ�.

		if (!text.isEmpty()) { // �ؽ�Ʈ ������ ������� �ʴٸ�
			messages.add(text); // ������ messages ����Ʈ�� �߰��ϰ�,
			chat_text_area.clear(); // �ؽ�Ʈ ������ ���ϴ�.
		}

//		// chat_list_view�� messages ����Ʈ�� ���ε��մϴ�.
//		chat_list_view.setItems(messages);

	}

	@FXML
	public void initialize() {

		// chat_list_view�� messages ����Ʈ�� ���ε��մϴ�.
		chat_list_view.setItems(messages);

		chat_text_area.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER) {
				if (!keyEvent.isShiftDown()) { // ShiftŰ�� ������ �ʾ�����,
					keyEvent.consume(); // �⺻ ����(�� �ٲ�)�� ����ϰ�,
					send(); // �޽����� �����մϴ�.
				} else { // Shift+Enter�� ��������,
					keyEvent.consume(); // �⺻ ����(�� �ٲ�)�� ����ϰ�,
					chat_text_area.appendText(""); // ���� �� �ٲ� ���ڸ� �߰��մϴ�.
				}
			}
		});

		// �����޼���
		chat_list_view.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
			if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
				String selectedMessage = chat_list_view.getSelectionModel().getSelectedItem(); // ���õ� �޽����� �����ɴϴ�.
				if (selectedMessage != null) { // ���õ� �޽����� �ִٸ�,
					pinnedMessageLabel1.setText(selectedMessage); // �ش� �޽����� ��ܿ� ������
				}
			}
		});

//		chat_list_view.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
//			if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
//				String selectedMessage = chat_list_view.getSelectionModel().getSelectedItem(); // ���õ� �޽����� �����ɴϴ�.
//				if (selectedMessage != null) { // ���õ� �޽����� �ִٸ�,
//					pinnedMessageLabel1.setText(selectedMessage); // �ش� �޽����� ��ܿ� ������
//				}
//			}
//		});
	}

	// start �޼ҵ忡�� JavaFX ���ø����̼��� �ʱ� ������ �մϴ�.
	@Override
	public void start(Stage primaryStage) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
		Parent root = loader.load();

		ChatController chatController = loader.getController();
		chatController.chat_list_view = new ListView<>();

		Scene scene = new Scene(root);

		primaryStage.setTitle("Chat"); // ������ ������ �����մϴ�.
		primaryStage.setScene(scene); // �����쿡 ��(Scene) ��ü�� �����մϴ�.
		primaryStage.show(); // �����츦 �����ݴϴ�.
	}

	// start �޼ҵ忡�� JavaFX ���ø����̼��� �ʱ� ������ �մϴ�.
//	@Override
//	public void start(Stage primaryStage) {
//
//		// ��(Scene) ��ü�� �����Ͽ� ���̾ƿ� �����̳ʿ� �Բ� �ʱ�ȭ�ϰ�,
//		Scene scene = new Scene(chat_background);
//
//		primaryStage.setTitle("Chat App"); // ������ ������ �����մϴ�.
//		primaryStage.setScene(scene); // �����쿡 ��(Scene) ��ü�� �����մϴ�.
//		primaryStage.show(); // �����츦 �����ݴϴ�.
//	}

//    @FXML
//    public void showGroupMessages() {
//    	loadGroupMessages();
//    }

	// main �Լ������� JavaFX ���ø����̼��� �����ϴ� launch �޼ҵ带 ȣ���մϴ�.
	public static void main(String[] args) {
		launch(args);
	}
}
