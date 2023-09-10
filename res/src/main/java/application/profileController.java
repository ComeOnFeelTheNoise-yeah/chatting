package application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class profileController {
	@FXML
	private Text profileNick;
	@FXML
	private ImageView goBack,profileImage,coverImage,profileEditBtn;

	public profileController() { }

	
	@FXML
	public void handleBtnClick(MouseEvent ae) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
//        User.getInstance().clearUserUid(); //로그아웃
        stage.setScene(scene);
        stage.show();
    }
	
	@FXML
    public void goToEdit(MouseEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("profileEdit.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
		
    }
	
	@FXML
	public void initialize() {
		Platform.runLater(() -> {
			double centerX = profileImage.getFitWidth() / 2;
			double centerY = profileImage.getFitHeight() / 2;
			double radius = Math.min(centerX, centerY);
			Circle clip = new Circle(centerX, centerY, radius);
			profileImage.setClip(clip);
		});
	    String currentUid = User.getInstance().getUid();
	    System.out.println("로그인 후 : " + currentUid);

	    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserInfo");

	    ref.child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
	        @Override
	        public void onDataChange(DataSnapshot dataSnapshot) {
	            if (dataSnapshot.exists()) {
	                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
	                String nickName = userInfo.getNickName();  // getNickName 메소드가 UserInfo 클래스에 존재해야 합니다.
	                profileNick.setText(nickName);
	            }
	        }

	        @Override
	        public void onCancelled(DatabaseError databaseError) {
	            // 필요한 경우 데이터베이스 오류 처리
	        }
	    });
	    String bucketName = "blossom-40039.appspot.com";
	    String imageUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/" +
	    	      URLEncoder.encode("profile_img/" + currentUid, StandardCharsets.UTF_8) +
	    	      "?alt=media";
	    
	    String coverUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/" +
	    	      URLEncoder.encode("cover_img/" + currentUid, StandardCharsets.UTF_8) +
	    	      "?alt=media";
	    
	    	  Image cImage;
	    	  
	    	  try (InputStream in = new URL(imageUrl).openStream()) {
	    		    Image originalImage = new Image(in);
	    		    PixelReader reader = originalImage.getPixelReader();

	    		    int size = (int) Math.min(originalImage.getWidth(), originalImage.getHeight());

	    		    int x = (int) ((originalImage.getWidth() - size) / 2);
	    		    int y = (int) ((originalImage.getHeight() - size) / 2);

	    			WritableImage squareImage = new WritableImage(size, size);
	    			PixelWriter writer = squareImage.getPixelWriter();

	    			for (int i = 0; i < size; i++) {
	    				for (int j = 0; j < size; j++) {
	    					writer.setColor(i, j, reader.getColor(x + i, y + j));
	    				}
	    			}
	    			profileImage.setImage(squareImage);

	    			System.out.println("프로필 사진 가져옴: " + currentUid);
	    		}catch (IOException e) {
	    		  System.out.println("프로필 사진 불러오기 실패, 기본 프로필 사진 불러오는 중: " + e.getMessage());

	    		    String defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/" +
	    		            URLEncoder.encode("profile_img/default_image.jpg", StandardCharsets.UTF_8) +
	    		            "?alt=media";

	    		    try (InputStream inDefault = new URL(defaultImageUrl).openStream()) {
	    		        Image defaultImage = new Image(inDefault);
	    		        profileImage.setImage(defaultImage);

	    		        System.out.println("기본 프로필 사진 가져옴");
	    		        
	    		    } catch (IOException ex) {
	    		        System.out.println("기본 프로필 사진 가져오기 실패: " + ex.getMessage());
	    		    }
	    	  }
	    	  try(InputStream coverIn = new URL(coverUrl).openStream()){
	    		  cImage = new Image(coverIn, coverImage.getFitWidth(), coverImage.getFitHeight(), true, true);
	    		    coverImage.setImage(cImage);
    	      }catch (IOException e){
    	    	  System.out.println("커버이미지 불러오기 실패,기본 커버 이미지 불러오는 중: " + e.getMessage());
    	    	  String defaultCoverImageUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/" +
	    		            URLEncoder.encode("cover_img/default_image_cover.jpg", StandardCharsets.UTF_8) +
	    		            "?alt=media";
    	    	  try (InputStream inDefault = new URL(defaultCoverImageUrl).openStream()) {
	    		        Image defaultImage = new Image(inDefault);
	    		        coverImage.setImage(defaultImage);

	    		        System.out.println("기본 커버 이미지 불러옴");
	    		        
	    		    } catch (IOException ex) {
	    		        System.out.println("기본 커버 이미지 가져오기 실패: " + ex.getMessage());
	    		    }
    	      }
	}
}
