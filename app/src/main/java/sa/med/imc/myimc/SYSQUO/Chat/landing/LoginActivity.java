package sa.med.imc.myimc.SYSQUO.Chat.landing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SYSQUO.Chat.application.AlertDialogHandler;
import sa.med.imc.myimc.SYSQUO.Chat.application.SessionManager;
import sa.med.imc.myimc.SYSQUO.Chat.chat.ChatClientManager;
import sa.med.imc.myimc.SYSQUO.Chat.chat.MainChatActivity_New;
import sa.med.imc.myimc.SYSQUO.Chat.chat.listeners.TaskCompletionListener;
import sa.med.imc.myimc.SYSQUO.util.Progress;


public class LoginActivity extends AppCompatActivity {
  final Context context = this;
  private final String USERNAME_FORM_FIELD = "username";
  private ProgressDialog progressDialog;
  private Button loginButton;
  private EditText usernameEditText;
  Progress progress;
  private ChatClientManager clientManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sysquo_activity_login);

    progress = new Progress(this);
    (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    progress.setCanceledOnTouchOutside(false);
    progress.setCancelable(false);

    setUIComponents();

    clientManager = ImcApplication.getInstance().getChatClientManager();
  }

  private void setUIComponents() {
    loginButton = (Button) findViewById(R.id.buttonLogin);
    usernameEditText = (EditText) findViewById(R.id.editTextUsername);

    setUpListeners();
  }

  private void setUpListeners() {
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        login();
      }
    });
    TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        int viewId = v.getImeActionId();
        if (viewId == 100) {
          login();
          return true;
        }
        return false;
      }
    };
    usernameEditText.setOnEditorActionListener(actionListener);
  }

  private void login() {
    Map<String, String> formInput = getFormInput();
    if (formInput.size() < 1) {
      displayAllFieldsRequiredMessage();
      return;
    }

    startStatusDialogWithMessage(getStringResource(R.string.login_user_progress_message));
    SessionManager.getInstance().createLoginSession(formInput.get(USERNAME_FORM_FIELD));
    initializeChatClient();
  }

  private Map<String, String> getFormInput() {
    String username = usernameEditText.getText().toString();

    Map<String, String> formInput = new HashMap<>();

    if (username.length() > 0) {
      formInput.put(USERNAME_FORM_FIELD, username);
    }
    return formInput;
  }

  private void initializeChatClient() {
    clientManager.connectClient(new TaskCompletionListener<Void, String>() {
      @Override
      public void onSuccess(Void aVoid) {
        showMainChatActivity();
      }

      @Override
      public void onError(String errorMessage) {
        stopStatusDialog();
        showAlertWithMessage(errorMessage);
      }
    });
  }

  private void displayAllFieldsRequiredMessage() {
    String message = getStringResource(R.string.login_all_fields_required);
    showAlertWithMessage(message);
  }

  private void startStatusDialogWithMessage(String message) {
    setFormEnabled(false);
    showActivityIndicator(message);
  }

  private void stopStatusDialog() {
    /*if (progressDialog.isShowing()) {
      progressDialog.dismiss();
    }*/
    progress.dismiss();
    setFormEnabled(true);
  }

  private void setFormEnabled(Boolean enabled) {
    usernameEditText.setEnabled(enabled);
  }

  private void showActivityIndicator(String message) {
    /*progressDialog = new ProgressDialog(this);
    progressDialog.setMessage(message);
    progressDialog.show();
    progressDialog.setCanceledOnTouchOutside(false);
    progressDialog.setCancelable(false);*/
    progress.show();
  }

  private void showMainChatActivity() {
    Intent launchIntent = new Intent();
    launchIntent.setClass(getApplicationContext(), MainChatActivity_New.class);
    startActivity(launchIntent);

    finish();
  }

  private String getStringResource(int id) {
    Resources resources = getResources();
    return resources.getString(id);
  }

  private void showAlertWithMessage(String message) {
    AlertDialogHandler.displayAlertWithMessage(message, context);
  }
}
