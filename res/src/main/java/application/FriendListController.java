package application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ListCell;

public class FriendListController {

	@FXML
	private ListView<UserInfo> listView;

	@FXML
	private ImageView profile;

	@FXML
	private Label friend;

	@FXML
	private Text name;

	@FXML
	private Button friendBtn, chatBtn;

	private DatabaseReference databaseReference;

	public void initialize() {

		FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
		databaseReference = firebaseDatabase.getReference("UserInfo");
		Platform.runLater(() -> {
			profile.setPreserveRatio(true); // Add this line
			double centerX = profile.getFitWidth() / 2;
			double centerY = profile.getFitHeight() / 2;
			double radius = Math.min(centerX, centerY);
			Circle clip = new Circle(centerX, centerY, radius);
			profile.setClip(clip);
		});

		String bucketName = "blossom-40039.appspot.com";
		String currentUid = User.getInstance().getUid();
		String imageUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/"
				+ URLEncoder.encode("profile_img/" + currentUid, StandardCharsets.UTF_8) + "?alt=media";
		Image image;

		try (InputStream in = new URL(imageUrl).openStream()) {
			image = new Image(in);
			profile.setImage(image);

			System.out.println("Loaded profile image for user: " + currentUid);

		} catch (IOException e) {
			System.out.println("Failed to load profile image: " + e.getMessage());

			String defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/"
					+ URLEncoder.encode("profile_img/default_image.jpg", StandardCharsets.UTF_8) + "?alt=media";

			try (InputStream inDefault = new URL(defaultImageUrl).openStream()) {
				Image defaultImage = new Image(inDefault);
				profile.setImage(defaultImage);

				System.out.println("Loaded default profile image");

			} catch (IOException ex) {
				System.out.println("Failed to load default profile image: " + ex.getMessage());
			}
		}
		databaseReference.child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()) {
					UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
					String Name = userInfo.getName(); // getNickName 메소드가 UserInfo 클래스에 존재해야 합니다.
					name.setText(Name);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				databaseError.toException().printStackTrace();
			}
		});

		listView.setCellFactory(lv -> new ListCell<UserInfo>() {
			private final ImageView profileImageView = new ImageView();
			private final Label nameLabel = new Label();
			private final Button deleteButton = new Button("삭제");
			private final Region spacer = new Region();
			private final HBox content = new HBox(profileImageView, nameLabel, spacer, deleteButton);

			{
				// Initialize the imageView.
				profileImageView.setFitHeight(40); // You can adjust this value.
				profileImageView.setFitWidth(40); // You can adjust this value.
				profileImageView.setPreserveRatio(true);

				double centerX = profileImageView.getFitWidth() / 2;
				double centerY = profileImageView.getFitHeight() / 2;
				double radius = Math.min(centerX, centerY);

				Circle clip = new Circle(centerX, centerY, radius);

				profileImageView.setClip(clip);
				// Adjust the position of nameLabel
				VBox nameContainer = new VBox(nameLabel);
				VBox.setMargin(nameLabel, new Insets(10.0)); // Add a margin to move nameLabel down by 10 pixels

				content.getChildren().add(1, nameContainer); // Insert nameContainer at index 1 (between imageView and
																// button)

				HBox.setHgrow(spacer, Priority.ALWAYS); // Make the spacer take up all remaining space

				content.setAlignment(Pos.CENTER_LEFT);
				content.setSpacing(12);

				deleteButton.setOnAction(event -> {
					UserInfo selectedUser = getItem();
					System.out.println("Selected user: " + selectedUser.getName());

					if (selectedUser != null && selectedUser.getUid() != null) {
						System.out.println("Deleted " + selectedUser.getName());

						DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference("friends")
								.child(User.getInstance().getUid()).child(selectedUser.getUid());

						friendRef.removeValueAsync().addListener(new Runnable() {
							@Override
							public void run() {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										System.out.println("Data deleted successfully.");
										getListView().getItems().remove(getItem());
									}
								});
							}
						}, MoreExecutors.directExecutor());
					}
				});
			}

			@Override
			protected void updateItem(UserInfo item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setGraphic(null);
				} else {
					String bucketName = "blossom-40039.appspot.com";
					String imageUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/"
							+ URLEncoder.encode("profile_img/" + item.getUid(), StandardCharsets.UTF_8) + "?alt=media";

					Image image;

					try (InputStream in = new URL(imageUrl).openStream()) {
						image = new Image(in);

						Platform.runLater(() -> profileImageView.setImage(image));

						System.out.println("Loaded profile image for user: " + item.getUid());

					} catch (IOException e) {

						System.out.println("Failed to load profile image: " + e.getMessage());

						String defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/"
								+ URLEncoder.encode("profile_img/default_image.jpg", StandardCharsets.UTF_8)
								+ "?alt=media";

						try (InputStream inDefault = new URL(defaultImageUrl).openStream()) {

							Image defaultImage = new Image(inDefault);

							Platform.runLater(() -> {
								if (!empty) {
									profileImageView.setImage(defaultImage);
								}
							});

							System.out.println("Loaded default profile image");

						} catch (IOException ex) {

							System.out.println("Failed to load default profile image: " + ex.getMessage());
						}
					}

					nameLabel.setText(item.getName());
					setGraphic(content);
				}
			}
		});

		DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference("friends").child(currentUid);

		friendRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				ObservableList<UserInfo> friends = FXCollections.observableArrayList();

				for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
					String friendUid = friendSnapshot.getKey();
					Boolean isFriend = (Boolean) friendSnapshot.getValue();

					if (isFriend != null && isFriend) {
						DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserInfo")
								.child(friendUid);

						userRef.addListenerForSingleValueEvent(new ValueEventListener() {
							@Override
							public void onDataChange(DataSnapshot dataSnapshot) {
								UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
								friends.add(userInfo);

								Platform.runLater(() -> {
									listView.setItems(friends);
									listView.scrollTo(0); // Add this line to scroll to the top.
								});
							}

							@Override
							public void onCancelled(DatabaseError databaseError) {
							}
						});
					}
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}

	@FXML
	public void changeProfilePicture(MouseEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
		Parent root = loader.load();

		Scene scene = new Scene(root);

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();

	}
	
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
}