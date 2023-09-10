package application;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.TextUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private Button member_register, login;

	@FXML
	private TextField idInput;
	@FXML
	private PasswordField pwInput;
	@FXML
	private Label errorLabel;

	User user;

	public LoginController() {
		super();
	}

	private boolean isValidation() {
		if (TextUtils.isEmpty(idInput.getText())) {
			errorLabel.setText("이메일을 입력해주세요!");
			errorLabel.setStyle("-fx-text-fill: red;");
			idInput.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(pwInput.getText())) {
			errorLabel.setText("비밀번호를 입력해주세요!");
			errorLabel.setStyle("-fx-text-fill: red;");
			pwInput.requestFocus();
			return false;
		}
		Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$");
		Matcher matcher = pattern.matcher(idInput.getText());
		if (!matcher.find()) {
			errorLabel.setText("이메일 형식이 유효하지 않습니다!"); // 유효하지 않으면 에러 메시지 표시
			errorLabel.setStyle("-fx-text-fill: red;");
			idInput.requestFocus();
			return false;
		}
		Pattern patternPw = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[!?@#$%^&+=])[a-zA-Z0-9!?@#$%^&+=]{6,16}$");
		Matcher matcherPw = patternPw.matcher(pwInput.getText());
		if (!matcherPw.find()) {
			errorLabel.setText("비밀번호 형식이 유효하지 않습니다! \n비밀번호는 소문자 알파벳, 숫자, 특수문자 !?@#$%^&+= 를 포함해야하며 6~16자로 구성되어야합니다 ㅠ"); // 유효하지
																														// 않으면
																														// 에러
																														// 메시지
																														// 표시
			errorLabel.setStyle("-fx-text-fill: red;");
			pwInput.requestFocus();
			return false;
		}

		return true;
	}

	@FXML
	protected void goLogin() {
		if (isValidation()) {
			DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserInfo");

			ref.orderByChild("email").equalTo(idInput.getText())
					.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
							if (dataSnapshot.exists()) {
								for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
									UserInfo userInfo = userSnapshot.getValue(UserInfo.class);
									if (userInfo.getPw().equals(pwInput.getText())) { // 비밀번호 일치 확인
										System.out.println("로그인 성공");
										String uid = userSnapshot.getKey(); // 로그인한 사용자의 uid 가져오기

										User userInstance = User.getInstance(); // User 객체의 인스턴스 가져오기
										userInstance.setUid(uid); // 가져온 인스턴스에 대해 setUid(String uid) 메소드 호출
										System.out.println("user uid : " + userInstance.getUid());
										System.out.println("로그인 완료");

										Platform.runLater(() -> {
											try {
												FXMLLoader loader = new FXMLLoader(
														getClass().getResource("friendList.fxml"));
												Parent root = loader.load();

												Scene scene = new Scene(root);

												Stage stage = (Stage) login.getScene().getWindow();
												stage.setScene(scene);

											} catch (IOException e) {
												e.printStackTrace();
											}
										});
									}
								}

							} else {

								Platform.runLater(() -> {
									errorLabel.setText("로그인에 실패했습니다!");
									errorLabel.setStyle("-fx-text-fill: red;");
									idInput.requestFocus();
								});
							}
						}

						@Override
						public void onCancelled(DatabaseError databaseError) {

						}
					});
		}
	}

	@FXML
	protected void goRegister() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml")); // 회원가입 페이지의 FXML 파일 경로를 확인하세요.
			Parent root = loader.load();

			Scene scene = new Scene(root);

			Stage stage = (Stage) login.getScene().getWindow(); // 현재 스테이지 가져오기
			stage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {
		member_register.setOnMouseClicked(event -> {
			goRegister();
		});
		login.setOnMouseClicked(event -> {
			goLogin();
		});
	}

	public void authenticateUser(String inputEmail, String inputPw) {
		idInput.setText(inputEmail);
		pwInput.setText(inputPw);
	}

}