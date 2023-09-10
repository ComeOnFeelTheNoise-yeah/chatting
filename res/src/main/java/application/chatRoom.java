package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

// 채팅방 목록을 괸리하는 클래스
public class chatRoom {
	private String roomName;
	private Image profileImage;
	private ObservableList<String> messages;

	public chatRoom(String roomName, Image profileImage) {
		this.roomName = roomName;
		this.profileImage = profileImage;
		this.messages = FXCollections.observableArrayList();
	}

	// getter and setter methods
}