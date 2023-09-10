package application;

import java.util.HashMap;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ChatController extends Application {
    private ObservableList<String> messages;
   
    @FXML
    private Pane chat_background;
    @FXML
    private ImageView caht_back_btn;
    @FXML
    private TextArea chat_text_area;
    @FXML
    private ImageView send_btn; 
    
    @FXML
    private ListView<String> chat_list_view;
    
    public ChatController() {
    	messages = FXCollections.observableArrayList();
	}
    
    @FXML
    public void send() {
    	//messages = FXCollections.observableArrayList(); 
       
            String text = chat_text_area.getText();  // 텍스트 영역의 내용을 가져옵니다.
            
            if (!text.isEmpty()) {  // 텍스트 영역이 비어있지 않다면
                Message message = new Message(User.getInstance().getUid(), "receiver-uid", text, System.currentTimeMillis());

                // Get a reference to the database
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

             
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("senderId", message.getSenderId());
                messageMap.put("receiverId", message.getRecieverId());
                messageMap.put("message", message.getMessage());
                messageMap.put("timestamp", message.getTimestamp());

                 ApiFuture<Void> future = dbRef.child("Messages").child("chat-room-uid").push().setValueAsync(messageMap);
                 chat_text_area.clear();   // 텍스트 영역은 비웁니다.
                
            }
            messages.add(text);  // 내용을 messages 리스트에 추가하고,  //text를 firebase메서드 통해서 저장하기 
            // chat_list_view에 messages 리스트를 바인딩합니다.
            chat_list_view.setItems(messages);
    }
    
    @FXML
    public void initialize() {
        chat_text_area.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
           if (keyEvent.getCode() == KeyCode.ENTER) { 
               if (!keyEvent.isShiftDown()) {  // Shift키가 눌리지 않았으면,
                   keyEvent.consume();         // 기본 동작(줄 바꿈)은 취소하고,
                   send();                     // 메시지를 전송합니다.
               } else {                        // Shift+Enter가 눌렸으면,
                   keyEvent.consume();         // 기본 동작(줄 바꿈)은 취소하고,
                   chat_text_area.appendText("\n");  // 직접 줄 바꿈 문자를 추가합니다.
               }
           }
        });
    }
    
    // start 메소드에서 JavaFX 애플리케이션의 초기 설정을 합니다.
    @Override
    public void start(Stage primaryStage) {

      	// 씬(Scene) 객체를 생성하여 레이아웃 컨테이너와 함께 초기화하고,
      	Scene scene = new Scene(chat_background);

	    primaryStage.setTitle("Chat App");  	// 윈도우 제목을 설정합니다.
	    primaryStage.setScene(scene);  		// 윈도우에 씬(Scene) 객체를 설정합니다.
	    primaryStage.show();  				// 윈도우를 보여줍니다.
    }

  	// main 함수에서는 JavaFX 애플리케이션을 시작하는 launch 메소드를 호출합니다.
	public static void main(String[] args) {
	   launch(args);
	}
}
