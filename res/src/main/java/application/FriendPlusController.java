package application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
import javafx.util.Callback;

public class FriendPlusController {

   @FXML
   private ListView<UserInfo> listView;

   private DatabaseReference databaseReference;
   @FXML
   private ImageView SFRID,profileImage,Flist;
   @FXML
   private TextField FriendPlus;
   @FXML
   private Label nameLabel;
   @FXML
   private Text name;

   @FXML
   private void searchBtn(MouseEvent event) {
      String searchText = FriendPlus.getText().trim().toLowerCase(); // 입력된 검색어를 소문자로 변환

      ObservableList<UserInfo> filteredList = FXCollections.observableArrayList();

      for (UserInfo userInfo : listView.getItems()) {
         if (userInfo.getName().toLowerCase().contains(searchText)) {
            filteredList.add(userInfo);
         }
      }

      if (searchText.isEmpty()) {
         // 검색어가 비어 있으면 모든 데이터를 표시
         loadOriginalData();
      } else {
         listView.setItems(filteredList);
      }
   }

   @FXML
   private void listBtn(MouseEvent event) {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("FriendListController.fxml"));
         Parent root = loader.load();
         Scene scene = new Scene(root);

         Stage stage = new Stage(); // 새로운 Stage 생성
         stage.setScene(scene);
         stage.show();

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @FXML
   private void initialize() {
      // 데이터베이스 참조 설정
      FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
      databaseReference = firebaseDatabase.getReference("UserInfo");
      FriendPlus.setVisible(false);
      SFRID.setOnMouseClicked(event -> {
           // 이미지가 클릭되면 검색창을 보여줍니다.
           FriendPlus.setVisible(!FriendPlus.isVisible());
       });
      Platform.runLater(() -> {
          profileImage.setPreserveRatio(true); // Add this line
          double centerX = profileImage.getFitWidth() / 2;
          double centerY = profileImage.getFitHeight() / 2;
          double radius = Math.min(centerX, centerY);
          Circle clip = new Circle(centerX, centerY, radius);
          profileImage.setClip(clip);
      });

      String bucketName = "blossom-40039.appspot.com";
      String currentUid = User.getInstance().getUid();
      String imageUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/"
            + URLEncoder.encode("profile_img/" + currentUid, StandardCharsets.UTF_8) + "?alt=media";
      Image image;

      try (InputStream in = new URL(imageUrl).openStream()) {
         image = new Image(in);
         profileImage.setImage(image);

         System.out.println("Loaded profile image for user: " + currentUid);

      } catch (IOException e) {
         System.out.println("Failed to load profile image: " + e.getMessage());

         String defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/"
               + URLEncoder.encode("profile_img/default_image.jpg", StandardCharsets.UTF_8) + "?alt=media";

         try (InputStream inDefault = new URL(defaultImageUrl).openStream()) {
            Image defaultImage = new Image(inDefault);
            profileImage.setImage(defaultImage);

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

      listView.setCellFactory(new Callback<ListView<UserInfo>, ListCell<UserInfo>>() {
             @Override
             public ListCell<UserInfo> call(ListView<UserInfo> param) {
                 return new ListCell<UserInfo>() {
                    private final ImageView profileImageView = new ImageView();
                     private final Label nameLabel = new Label();
                     private final Button addButton = new Button("추가");
                     private final Region spacer = new Region();

                     //private final HBox content = new HBox(profileImageView,nameLabel, spacer, addButton);
                     
                     private final HBox content = new HBox(profileImageView, nameLabel, spacer,addButton);
                     { 
                     // Initialize the imageView.
                         profileImageView.setFitHeight(40);  // You can adjust this value.
                         profileImageView.setFitWidth(40);   // You can adjust this value.
                         profileImageView.setPreserveRatio(true);
                         
                         double centerX = profileImageView.getFitWidth() / 2;
                         double centerY = profileImageView.getFitHeight() / 2;
                         double radius = Math.min(centerX, centerY);
                         
                         Circle clip = new Circle(centerX, centerY, radius);
                         
                         profileImageView.setClip(clip);
                         // Adjust the position of nameLabel
                           VBox nameContainer = new VBox(nameLabel); 
                           VBox.setMargin(nameLabel, new Insets(10.0)); // Add a margin to move nameLabel down by 10 pixels
                           
                           content.getChildren().add(1,nameContainer); // Insert nameContainer at index 1 (between imageView and button)
   
                           HBox.setHgrow(spacer, Priority.ALWAYS); // Make the spacer take up all remaining space
   
                           content.setAlignment(Pos.CENTER_LEFT);
                           content.setSpacing(12);
                           
                           addButton.setOnAction(event -> {
                               UserInfo selectedUser = getItem();
                               System.out.println("Selected user: " + selectedUser.getName());

                               if (selectedUser != null && selectedUser.getUid() != null) {

                                   System.out.println("Added " + selectedUser.getName());

                                   DatabaseReference friendRef =
                                       FirebaseDatabase.getInstance().getReference("friends")
                                           .child(User.getInstance().getUid())
                                           .child(selectedUser.getUid()); 

                                   ApiFuture<Void> future =
                                       friendRef.setValueAsync(true);

                                   future.addListener(new Runnable() {

                                       @Override
                                       public void run() {

                                           try {

                                               future.get();

                                               Platform.runLater(new Runnable() {

                                                   @Override
                                                   public void run() {
                                                       System.out.println("Data saved successfully.");
                                                       // 친구를 추가한 후 현재 보여지고 있는 리스트에서 제거
                                                       listView.getItems().remove(selectedUser);
                                                   }
                                               });
                                           } catch (InterruptedException | ExecutionException e) {

                                               Platform.runLater(new Runnable() {

                                                   @Override
                                                   public void run() {
                                                       System.out.println(
                                                           "Data could not be saved: "
                                                               + e.getMessage());
                                                   }
                                               });
                                           }
                                       }
                                   }, MoreExecutors.directExecutor());
                               }

                               DatabaseReference friendsRef =
                                   FirebaseDatabase.getInstance()
                                       .getReference("friends")
                                       .child(User.getInstance().getUid());

                               friendsRef.addValueEventListener(new ValueEventListener() {
                                   
                                   @Override
                                   public void onDataChange(DataSnapshot dataSnapshot) {
                                       
                                       loadOriginalData();  
                                   }

                                   @Override
                                   public void onCancelled(DatabaseError databaseError) {}
                               });

                           }); 
                               FriendPlus.textProperty().addListener(
                                    (observable, oldValue, newValue) -> { 
                                        handleTextFieldChange(newValue);  
                                    });
                             loadOriginalData();
                     }

                     @Override
                    protected void updateItem(UserInfo item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                       String bucketName = "blossom-40039.appspot.com";
                       String imageUrl =
                           "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/"
                               + URLEncoder.encode("profile_img/" + item.getUid(), StandardCharsets.UTF_8)
                               + "?alt=media";

                       Image image;

                       try (InputStream in = new URL(imageUrl).openStream()) {
                           image = new Image(in);
                           
                           Platform.runLater(() -> profileImageView.setImage(image));
                           
                           System.out.println("Loaded profile image for user: " + item.getUid());

                       } catch (IOException e) {

                           System.out.println("Failed to load profile image: " + e.getMessage());

                      String defaultImageUrl =
                          "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/"
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
                     
                     
                 };
             }
         });

           loadOriginalData();
       }

   private ObservableList<UserInfo> originalData; // 초기 친구 목록을 저장할 변수

   private void loadOriginalData() {
       DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference("friends").child(User.getInstance().getUid());
       friendsRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot friendSnapshot) {
               // 현재 사용자의 친구 목록을 가져옵니다.
               List<String> friendList = new ArrayList<>();
               for (DataSnapshot snapshot : friendSnapshot.getChildren()) {
                   friendList.add(snapshot.getKey());
               }

               databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       ObservableList<UserInfo> items = FXCollections.observableArrayList();

                       for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                           UserInfo userInfo = userSnapshot.getValue(UserInfo.class);
                           
                           if (!userInfo.getUid().equals(User.getInstance().getUid()) && !friendList.contains(userInfo.getUid())) { 
                               items.add(userInfo);
                           }
                       }

                       listView.setItems(items); // Set the items of the ListView

                       if (originalData == null) {  
                           originalData = FXCollections.observableArrayList();
                       }

                       originalData.clear();
                       originalData.addAll(items);

                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {}
              });
          }

          @Override
          public void onCancelled(DatabaseError error) {}
      });
   }

   // 이전 handleTextFieldChange 메소드를 수정하여 새로운 메소드로 만듭니다.
   private void handleTextFieldChange(String searchText) {
      searchText = searchText.trim().toLowerCase();
      ObservableList<UserInfo> filteredList = FXCollections.observableArrayList();

      for (UserInfo userInfo : originalData) {
         // 이름을 소문자로 변환한 뒤 검색어가 포함되어 있는지 확인
         if (userInfo.getName().toLowerCase().contains(searchText)) {
            filteredList.add(userInfo);
         }
      }

      if (searchText.isEmpty()) {
         // 텍스트 필드가 비어 있으면 originalData를 다시 설정하여 초기 친구 목록을 표시
         listView.setItems(originalData);
      } else {
         listView.setItems(filteredList);
      }
   }

}