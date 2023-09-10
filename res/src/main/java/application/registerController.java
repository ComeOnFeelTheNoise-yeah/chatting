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

	// »∏ø¯∞°¿‘ πˆ∆∞ ¥≠∑»¿ª ∂ß ∏ﬁº≠µÂ »£√‚
	private boolean nameCheck(String inputName) {

		if (TextUtils.isEmpty(inputName)) {
			errorLabel.setText("¿Ã∏ß¿ª ¿‘∑¬«ÿ¡÷ººø‰!");
			errorLabel.setStyle("-fx-text-fill: red;");
			name.requestFocus();
			return false;
		}
		Pattern pattern = Pattern.compile("^[∞°-∆R\\s]*$");
		Matcher matcher = pattern.matcher(inputName);
		if (!matcher.find()) {
			errorLabel.setText("¿Ø»ø «œ¡ˆ æ ¿∫ ¿Ã∏ß¿‘¥œ¥Ÿ!"); // ¿Ø»ø«œ¡ˆ æ ¿∏∏È ø°∑Ø ∏ﬁΩ√¡ˆ «•Ω√
			errorLabel.setStyle("-fx-text-fill: red;");
			name.requestFocus();
			return false;
		}

		return true;

	}

	private boolean birthCheck(String inputBirth) {
		if (inputBirth == null || inputBirth.isEmpty()) {
			errorLabel.setText("ª˝≥‚ø˘¿œ¿ª ¿‘∑¬«ÿ¡÷ººø‰!");
			errorLabel.setStyle("-fx-text-fill: red;");
			birth.requestFocus();
			return false;
		}

		LocalDate parsedBirth;
		try {
			parsedBirth = LocalDate.parse(inputBirth);
		} catch (DateTimeParseException e) {
			errorLabel.setText("¿Ø»ø«— ≥Ø¬• «¸Ωƒ¿ª ¿‘∑¬«ÿ¡÷ººø‰! (øπ: 2000-01-21)");
			errorLabel.setStyle("-fx-text-fill: red;");
			birth.requestFocus();
			return false;
		}

		LocalDate today = LocalDate.now();
		Period period = Period.between(parsedBirth, today);

		if (period.getYears() < 12) {
			errorLabel.setText("12ºº πÃ∏∏¿∫ ∞°¿‘«“ ºˆ æ¯Ω¿¥œ¥Ÿ!");
			errorLabel.setStyle("-fx-text-fill: red;");
			birth.requestFocus();
			return false;
		}

		return true;
	}

	private boolean telCheck(String inputTel) {
		if (TextUtils.isEmpty(inputTel)) {
			errorLabel.setText("¿¸»≠π¯»£∏¶ ¿‘∑¬«ÿ¡÷ººø‰!");
			errorLabel.setStyle("-fx-text-fill: red;");
			tel.requestFocus();
			return false;
		}

		Pattern pattern = Pattern.compile("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$");
		Matcher matcher = pattern.matcher(inputTel);

		if (!matcher.find()) {
			errorLabel.setText("¿Ø»ø«œ¡ˆ æ ¿∫ ¿¸»≠π¯»£ «¸Ωƒ¿‘¥œ¥Ÿ! (øπ: 010-1234-5678)");
			errorLabel.setStyle("-fx-text-fill: red;");
			tel.requestFocus();
			return false;
		}

		return true;

	}

	private boolean pwCheck(String inputPw) {
		if (TextUtils.isEmpty(inputPw)) {
			errorLabel.setText("∫Òπ–π¯»£∏¶ ¿‘∑¬«ÿ¡÷ººø‰!");
			errorLabel.setStyle("-fx-text-fill: red;");
			pw.requestFocus();
			return false;
		}
		Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[!?@#$%^&+=])[a-zA-Z0-9!?@#$%^&+=]{6,16}$");
		Matcher matcher = pattern.matcher(inputPw);
		if (!matcher.find()) {
			errorLabel.setText("∫Òπ–π¯»£ «¸Ωƒ¿Ã ¿Ø»ø«œ¡ˆ æ Ω¿¥œ¥Ÿ! \n∫Òπ–π¯»£¥¬ º“πÆ¿⁄ æÀ∆ƒ∫™, º˝¿⁄, ∆ØºˆπÆ¿⁄ !?@#$%^&+= ∏¶ ∆˜«‘«ÿæﬂ«œ∏Á 6~16¿⁄∑Œ ±∏º∫µ«æÓæﬂ«’¥œ¥Ÿ §–"); // ¿Ø»ø«œ¡ˆ
																														// æ ¿∏∏È
																														// ø°∑Ø
																														// ∏ﬁΩ√¡ˆ
																														// «•Ω√
			errorLabel.setStyle("-fx-text-fill: red;");
			pw.requestFocus();
			return false;
		}
		return true;
	}

	private boolean pwCheckCheck(String inputPw, String pwCheckStr) {
		if (TextUtils.isEmpty(pwCheckStr)) {
			errorLabel.setText("∫Òπ–π¯»£ »Æ¿Œ∂ı¿ª ¿‘∑¬«ÿ¡÷ººø‰!");
			errorLabel.setStyle("-fx-text-fill: red;");
			pwCheck.requestFocus();
			return false;
		}
		if (!inputPw.equals(pwCheckStr)) {
			errorLabel.setText("∫Òπ–π¯»£øÕ ∫Òπ–π¯»£ »Æ¿Œ¿Ã ¿œƒ°«œ¡ˆ æ Ω¿¥œ¥Ÿ!");
			errorLabel.setStyle("-fx-text-fill: red;");
			pwCheck.requestFocus();
			return false;
		}
		return true;
	}

	private boolean emailCheck(String inputEmail) {
		if (TextUtils.isEmpty(inputEmail)) {
			errorLabel.setText("¿Ã∏ﬁ¿œ¿ª ¿‘∑¬«ÿ¡÷ººø‰!");
			errorLabel.setStyle("-fx-text-fill: red;");
			email.requestFocus();
			return false;
		}
		Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$");
		Matcher matcher = pattern.matcher(inputEmail);
		if (!matcher.find()) {
			errorLabel.setText("¿Ã∏ﬁ¿œ «¸Ωƒ¿Ã ¿Ø»ø«œ¡ˆ æ Ω¿¥œ¥Ÿ!"); // ¿Ø»ø«œ¡ˆ æ ¿∏∏È ø°∑Ø ∏ﬁΩ√¡ˆ «•Ω√
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

				// ∑Œ±◊¿Œ »≠∏È¿∏∑Œ ≥—æÓ∞°±‚ ±‚¥… ±∏«ˆ
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
				Parent root = loader.load();

				Scene scene = new Scene(root);

				Stage stage = (Stage) signUp.getScene().getWindow();
				stage.setScene(scene);

			} catch (FirebaseAuthException e) {
				System.out.println(e.getMessage()); // print the full error message
				if (e.getMessage().contains("The user with the provided email already exists (EMAIL_EXISTS).")) {
					errorLabel.setText("¿ÃπÃ ¡∏¿Á«œ¥¬ ¿Ã∏ﬁ¿œ¿‘¥œ¥Ÿ!");
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
		// ¿Ã¿¸ »≠∏È¿∏∑Œ µπæ∆∞°¥¬ ±‚¥… ±∏«ˆ « ø‰.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
		Parent root = loader.load();

		Scene scene = new Scene(root);

		Stage stage = (Stage) signUp.getScene().getWindow();
		stage.setScene(scene);

	}

	@FXML
	public void initialize() {
		signUp.setOnMouseClicked(event -> {
			// πˆ∆∞ ≈¨∏Ø Ω√ µø¿€«“ ∑Œ¡˜ ¿€º∫
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