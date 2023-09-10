package application;

public class User {
	   private static User instance;
	   private String uid;

	   private User() {
	   }

	   public static synchronized User getInstance() {
	      if (instance == null) {
	         instance = new User();
	      }
	      return instance;
	   }

	   public String getUid() {
	      return uid;
	   }

	   public void setUid(String uid) {
	      this.uid = uid;
	   }

	   public void clearUserUid() {
	      uid = null;
	   }
	}