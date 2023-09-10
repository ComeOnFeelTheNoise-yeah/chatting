package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.TextUtils;

import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class registerController {

	@FXML
	private TextField name, birth, tel, agency, email;
	@FXML
	private PasswordField pw, pwCheck;
	@FXML
	private Label errorLabel;

	@FXML
	private ImageView signUp, cancelBtn;

	public registerController() {
		super();
	}

	// ȸ������ ��ư ������ �� �޼��� ȣ��
	private boolean nameCheck(String inputName) {

		if (TextUtils.isEmpty(inputName)) {
			errorLabel.setText("�̸��� �Է����ּ���!");
			errorLabel.setStyle("-fx-text-fill: red;");
			name.requestFocus();
			return false;
		}
		Pattern pattern = Pattern.compile("^[��-�R\\s]*$");
		Matcher matcher = pattern.matcher(inputName);
		if (!matcher.find()) {
			errorLabel.setText("��ȿ ���� ���� �̸��Դϴ�!"); // ��ȿ���� ������ ���� �޽��� ǥ��
			errorLabel.setStyle("-fx-text-fill: red;");
			name.requestFocus();
			return false;
		}

		return true;

	}

	private boolean birthCheck(String inputBirth) {
		if (inputBirth == null || inputBirth.isEmpty()) {
			errorLabel.setText("��������� �Է����ּ���!");
			errorLabel.setStyle("-fx-text-fill: red;");
			birth.requestFocus();
			return false;
		}

		LocalDate parsedBirth;
		try {
			parsedBirth = LocalDate.parse(inputBirth);
		} catch (DateTimeParseException e) {
			errorLabel.setText("��ȿ�� ��¥ ������ �Է����ּ���! (��: 2000-01-21)");
			errorLabel.setStyle("-fx-text-fill: red;");
			birth.requestFocus();
			return false;
		}

		LocalDate today = LocalDate.now();
		Period period = Period.between(parsedBirth, today);

		if (period.getYears() < 12) {
			errorLabel.setText("12�� �̸��� ������ �� �����ϴ�!");
			errorLabel.setStyle("-fx-text-fill: red;");
			birth.requestFocus();
			return false;
		}

		return true;
	}

	private boolean telCheck(String inputTel) {
		if (TextUtils.isEmpty(inputTel)) {
			errorLabel.setText("��ȭ��ȣ�� �Է����ּ���!");
			errorLabel.setStyle("-fx-text-fill: red;");
			tel.requestFocus();
			return false;
		}

		Pattern pattern = Pattern.compile("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$");
		Matcher matcher = pattern.matcher(inputTel);

		if (!matcher.find()) {
			errorLabel.setText("��ȿ���� ���� ��ȭ��ȣ �����Դϴ�! (��: 010-1234-5678)");
			errorLabel.setStyle("-fx-text-fill: red;");
			tel.requestFocus();
			return false;
		}

		return true;

	}

	private boolean pwCheck(String inputPw) {
		if (TextUtils.isEmpty(inputPw)) {
			errorLabel.setText("��й�ȣ�� �Է����ּ���!");
			errorLabel.setStyle("-fx-text-fill: red;");
			pw.requestFocus();
			return false;
		}
		Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[!?@#$%^&+=])[a-zA-Z0-9!?@#$%^&+=]{6,16}$");
		Matcher matcher = pattern.matcher(inputPw);
		if (!matcher.find()) {
			errorLabel.setText("��й�ȣ ������ ��ȿ���� �ʽ��ϴ�! \n��й�ȣ�� �ҹ��� ���ĺ�, ����, Ư������ !?@#$%^&+= �� �����ؾ��ϸ� 6~16�ڷ� �����Ǿ���մϴ� ��"); // ��ȿ����
																														// ������
																														// ����
																														// �޽���
																														// ǥ��
			errorLabel.setStyle("-fx-text-fill: red;");
			pw.requestFocus();
			return false;
		}
		return true;
	}

	private boolean pwCheckCheck(String inputPw, String pwCheckStr) {
		if (TextUtils.isEmpty(pwCheckStr)) {
			errorLabel.setText("��й�ȣ Ȯ�ζ��� �Է����ּ���!");
			errorLabel.setStyle("-fx-text-fill: red;");
			pwCheck.requestFocus();
			return false;
		}
		if (!inputPw.equals(pwCheckStr)) {
			errorLabel.setText("��й�ȣ�� ��й�ȣ Ȯ���� ��ġ���� �ʽ��ϴ�!");
			errorLabel.setStyle("-fx-text-fill: red;");
			pwCheck.requestFocus();
			return false;
		}
		return true;
	}

	private boolean emailCheck(String inputEmail) {
		if (TextUtils.isEmpty(inputEmail)) {
			errorLabel.setText("�̸����� �Է����ּ���!");
			errorLabel.setStyle("-fx-text-fill: red;");
			email.requestFocus();
			return false;
		}
		Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$");
		Matcher matcher = pattern.matcher(inputEmail);
		if (!matcher.find()) {
			errorLabel.setText("�̸��� ������ ��ȿ���� �ʽ��ϴ�!"); // ��ȿ���� ������ ���� �޽��� ǥ��
			errorLabel.setStyle("-fx-text-fill: red;");
			email.requestFocus();
			return false;
		}

		return true;
	}

	@FXML
	protected void registerCheck() {
		String inputEmail = email.getText();
		String inputPw = pw.getText();
		String inputBirth = birth.getText();
		String inputTel = tel.getText();
		String inputName = name.getText();

		if (nameCheck(inputName) && birthCheck(inputBirth) && telCheck(inputTel) && emailCheck(inputEmail)
				&& pwCheck(inputPw) && pwCheckCheck(inputPw, pwCheck.getText())) {

			FirebaseDatabase database = FirebaseDatabase.getInstance();
			FirebaseAuth mFireAuth = FirebaseAuth.getInstance();
			CreateRequest request = new CreateRequest().setEmail(inputEmail).setPassword(inputPw);

			try {
				UserRecord userRecord = mFireAuth.createUser(request);
				System.out.println("Successfully created new user: " + userRecord.getUid());
				String uid = userRecord.getUid();
				DatabaseReference ref = database.getReference("UserInfo").child(uid);
				UserInfo userInfo = new UserInfo();
				userInfo.setEmail(inputEmail);
				userInfo.setName(inputName);
				userInfo.setBirthDate(inputBirth);
				userInfo.setUid(uid);
				userInfo.setTel(inputTel);
				userInfo.setNickName(inputName);
				userInfo.setPw(inputPw);

				Map<String, String> userInfoMap = new HashMap<>();
				userInfoMap.put("name", userInfo.getName());
				userInfoMap.put("email", userInfo.getEmail());
				userInfoMap.put("tel", userInfo.getTel());
				userInfoMap.put("uid", userInfo.getUid());
				userInfoMap.put("birthDate", userInfo.getBirthDate());
				userInfoMap.put("nickName", userInfo.getNickName());
				userInfoMap.put("pw", userInfo.getPw());
				ApiFuture<Void> future = ref.setValueAsync(userInfoMap);

				// �α��� ȭ������ �Ѿ�� ��� ����
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
				Parent root = loader.load();

				Scene scene = new Scene(root);

				Stage stage = (Stage) signUp.getScene().getWindow();
				stage.setScene(scene);

			} catch (FirebaseAuthException e) {
				System.out.println(e.getMessage()); // print the full error message
				if (e.getMessage().contains("The user with the provided email already exists (EMAIL_EXISTS).")) {
					errorLabel.setText("�̹� �����ϴ� �̸����Դϴ�!");
					errorLabel.setStyle("-fx-text-fill: red;");
					email.requestFocus();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	private void backTo() throws IOException {
		// ���� ȭ������ ���ư��� ��� ���� �ʿ�.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
		Parent root = loader.load();

		Scene scene = new Scene(root);

		Stage stage = (Stage) signUp.getScene().getWindow();
		stage.setScene(scene);

	}

	@FXML
	public void initialize() {
		signUp.setOnMouseClicked(event -> {
			// ��ư Ŭ�� �� ������ ���� �ۼ�
			registerCheck();
		});
		cancelBtn.setOnMouseClicked(event -> {
			try {
				backTo();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}