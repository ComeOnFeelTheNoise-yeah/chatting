package application;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

class UserInfo {
	private String name;
	private String birthDate;
	private String tel;
	private String uid;
	private String email;
	private String pw;
	private String nickName;

	public UserInfo() {
		super();
	}

	public UserInfo(String name, String birthDate, String tel, String uid, String email, String pw, String nickName) {
		super();
		this.name = name;
		this.birthDate = birthDate;
		this.tel = tel;
		this.uid = uid;
		this.email = email;
		this.pw = pw;
		this.nickName = nickName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public static DatabaseReference getMessages() {
		// TODO Auto-generated method stub
		return null;
	}

}

public class Main extends Application {
	@Override
	public void init() {
		try {
			FileInputStream serviceAccount = new FileInputStream(
					"C:\\\\Program Files\\\\Java\\\\blossom-40039-firebase-adminsdk-heiuo-f52f3c2390.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://blossom-40039-default-rtdb.firebaseio.com/").build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Pane root = (Pane) FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene scene = new Scene(root, 500, 960);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
