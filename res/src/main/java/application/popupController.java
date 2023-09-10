package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class popupController {

	@FXML
	private Text messageText;

	@FXML
	private Button yesButton, noButton;

	public void setMessage(String message) {
		messageText.setText(message);
	}

	public void handleYes(ActionEvent event) {
		// 알림 끄기 로직 구현

		closeWindow(event);
	}

	public void handleNo(ActionEvent event) {
		closeWindow(event);
	}

	private void closeWindow(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}
}
