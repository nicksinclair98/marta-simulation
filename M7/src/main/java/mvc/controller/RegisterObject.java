package mvc.controller;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mvc.model.Account;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class RegisterObject {

    /**
     * This method uses the information entered by the user on the WelcomeScreen's toRegister() method to check if the
     * password and verify password boxes match, and, if they do, will upload the information to Google's
     * Firebase server using a personal json file that service accounts will have on their computers. If the passwords
     * do not match, a PasswordMismatch custom exception will be thrown by the checkPasswords() method,
     * displayed on a standard dialogue box, and will prompt the user to reenter their information in the correct
     * format.
     * @param username username entered into TextField by application user on WelcomeScreen in method toRegister()
     * @param password password entered into TextField by application user on WelcomeScreen in method toRegister()
     * @param verifyPassword password verification
     *                       entered into TextField by application user on WelcomeScreen in method toRegister()
     * @param phone phone number entered into TextField by application user on WelcomeScreen in method toRegister()
     * @param newStage the stage of the register popup that will be closed upon successful registration
     */
    public static void register(TextField username, TextField password, TextField verifyPassword,
                                TextField phone, Stage newStage) {
        try {
            try {
                checkPasswords(password, verifyPassword);

                String path = System.getProperty("user.dir");
                InputStream serviceAccount = new FileInputStream(
                        path + "/src/main/java/json"
                                + "/marta-simulation-system-firebase-adminsdk-597h3-c3547f744f.json");
                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(credentials)
                        .build();
                FirebaseApp.initializeApp(options);
                Firestore db = FirestoreClient.getFirestore();
                DocumentReference docRef = db.collection("users").document(username.getCharacters().toString());
                // Add document data  with id "newUser" using a hashmap
                Map<String, Map<String, Object>> data = new HashMap<>();
                Account user = new Account(username.getCharacters().toString(), password.getCharacters().toString(),
                        Long.parseLong(phone.getCharacters().toString()));
                Map<String, Object> map = user.createMap();
                data.put(username.getCharacters().toString(), map);
                ApiFuture<WriteResult> result = docRef.set(data);
                System.out.println("Update time : " + result.get().getUpdateTime());
                newStage.close();
            } catch (PasswordMismatch e) {
                Dialog message = new Dialog();
                message.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                Node closeButton = message.getDialogPane().lookupButton(ButtonType.CLOSE);
                closeButton.managedProperty().bind(closeButton.visibleProperty());
                closeButton.setVisible(false);
                message.setContentText(e.getMessage());
                message.showAndWait();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * @param password the TextField representation of the password the user entered, same from the above method
     * @param verifyPassword the TextField representation of the verify password the user entered,
     *                       same from the above method.
     * @throws PasswordMismatch a custom exception thrown to indicate the user entered passwords that do not match
     */
    public static void checkPasswords(TextField password, TextField verifyPassword) throws PasswordMismatch {
        if (!(password.getCharacters().toString().equals(verifyPassword.getCharacters().toString()))) {
            throw new PasswordMismatch("Passwords entered do not match. Please re-enter passwords correctly");

        }
    }
}

class PasswordMismatch extends Exception {
    public PasswordMismatch(String message) {
        super(message);
    }
}
