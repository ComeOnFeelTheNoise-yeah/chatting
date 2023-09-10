package application;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.http.util.TextUtils;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;


public class ProfileEditController {
	@FXML
	private Label errorLabel; 
	@FXML
	private ImageView goBack,profileImage,coverImage,changeCoverBtn,changeEveryBtn;
	@FXML
	private TextField nameChange,phoneChange;
	private File selectedProfileFile;
	private File selectedCoverFile;
	
	
	@FXML
	private void changeEveryThing(MouseEvent event) throws IOException {
		String inputName = nameChange.getText();
		String inputTel = phoneChange.getText();
		if(telCheck(inputTel) && nameCheck(inputName) ) {
			String bucketName = "blossom-40039.appspot.com";  
			String uid = User.getInstance().getUid();  
			GoogleCredentials credentials;
			try{
				credentials = GoogleCredentials.fromStream(new FileInputStream("C:\\\\Program Files\\\\Java\\\\blossom-40039-firebase-adminsdk-heiuo-f52f3c2390.json"));
			}catch(IOException e){
				System.out.println("Failed to get credentials: "+e.getMessage());
				return;
			}
			
			if(selectedProfileFile != null){
			    uploadToFirebase(bucketName, uid, "profile_img/", selectedProfileFile, credentials);
			}else{
			    System.out.println("No profile image to upload.");
			}
			
			if(selectedCoverFile != null){
			    uploadToFirebase(bucketName, uid, "cover_img/", selectedCoverFile, credentials);
			}else{
			    System.out.println("No cover image to upload.");
			}
			
			FirebaseDatabase database = FirebaseDatabase.getInstance();
			DatabaseReference ref = database.getReference("UserInfo").child(uid);

			Map<String, Object> updates = new HashMap<>();
			updates.put("nickName", inputName);
			updates.put("tel", inputTel);

			ApiFuture<Void> future = ref.updateChildrenAsync(updates);
	        goBack();
		}
		
	}

	private void uploadToFirebase(String bucketName, String uid, String pathPrefix,
	                              File fileToUpload, GoogleCredentials credentials){
		try{
		    String blobName = pathPrefix + uid;  

		    byte[] content = Files.readAllBytes(fileToUpload.toPath());

	        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
	        BlobId blobId = BlobId.of(bucketName, blobName);
	        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();

	        storage.create(blobInfo, content);

	        System.out.printf("Uploaded %s image for user: %s\n", pathPrefix.replace("/", ""), uid);
			
	    }catch(IOException e){
	    	System.out.printf("Failed to upload %s image: %s\n", pathPrefix.replace("/", ""), e.getMessage());
	    }
		
	}
	
	@FXML
	private void changeCover(MouseEvent event) {
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.getExtensionFilters()
	            .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
	    selectedCoverFile = fileChooser.showOpenDialog(null);

	    if (selectedCoverFile != null) {
	        try {
		        Image img=new Image(selectedCoverFile.toURI().toString());
		        
		        coverImage.setImage(img);  // Set the image to the coverImageView
	            
	        } catch (Exception e) {
	        	System.out.println("Failed to load cover image: " + e.getMessage());
	        }
	        
	    } else {
	    	System.out.println("No file selected");
	    }
	}
	
	@FXML
	private void changeProfile(MouseEvent event) {
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.getExtensionFilters()
	            .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
	    selectedProfileFile = fileChooser.showOpenDialog(null);

	    if (selectedProfileFile != null) {
	        try {
	            BufferedImage originalImage = ImageIO.read(selectedProfileFile);
	            
	            int size = Math.min(originalImage.getWidth(), originalImage.getHeight());
	            
	            int x = (originalImage.getWidth() - size) / 2;
	            int y = (originalImage.getHeight() - size) / 2;

				BufferedImage squareImage = originalImage.getSubimage(x, y, size, size);

				File tempFile=File.createTempFile("tempImg",".png");
				ImageIO.write(squareImage,"png",tempFile);

		        Image img=new Image(tempFile.toURI().toString());
		        
		        profileImage.setImage(img);  // Set the image to the profileImageView
	            
	        } catch (IOException e) {
	        	System.out.println("Failed to load profile image: " + e.getMessage());
	        }
	        
	    } else {
	    	System.out.println("No file selected");
	    }
	}
	
	private boolean telCheck(String inputTel) {
	      if(TextUtils.isEmpty(inputTel)) {
	         errorLabel.setText("전화번호를 입력해주세요!");
	         errorLabel.setStyle("-fx-text-fill: red;");
	         phoneChange.requestFocus();
	         return false;
	      }
	      
	      Pattern pattern = Pattern.compile("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$");
	       Matcher matcher = pattern.matcher(inputTel);

	       if (!matcher.find()) {
	           errorLabel.setText("유효하지 않은 전화번호 형식입니다! (예: 010-1234-5678)");
	           errorLabel.setStyle("-fx-text-fill: red;");
	           phoneChange.requestFocus();
	           return false;
	      }
	       
	       return true;
	      
	   }
	private boolean nameCheck(String inputName) {
	      
	      if(TextUtils.isEmpty(inputName)) {
	         errorLabel.setText("닉네임을 입력해주세요!");
	         errorLabel.setStyle("-fx-text-fill: red;");
	         nameChange.requestFocus();
	         return false;
	      }   
	      Pattern pattern = Pattern.compile("^[A-Za-z가-힣0-9]{3,16}$");
	      Matcher matcher = pattern.matcher(inputName);
	      if (!matcher.find()) {
	         errorLabel.setText("유효 하지 않은 닉네임입니다!");  // 유효하지 않으면 에러 메시지 표시
	         errorLabel.setStyle("-fx-text-fill: red;");
	         nameChange.requestFocus();
	         return false;
	      } 
	      
	      return true;
	      

	   }
	
	
	
	//뒤로가기 버튼
	private void goBack() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        Stage stage = (Stage) goBack.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
	}
	
	
	@FXML
    public void initialize() {
		
		goBack.setOnMouseClicked(event -> {
	          try {
				goBack();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        });
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
	                String cTel = userInfo.getTel();
	                nameChange.setText(nickName);
	                phoneChange.setText(cTel);
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
