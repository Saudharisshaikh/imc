package sa.med.imc.myimc.SYSQUO.Chat.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mindorks.editdrawabletext.DrawablePosition;
import com.mindorks.editdrawabletext.EditDrawableText;
import com.mindorks.editdrawabletext.onDrawableClickListener;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Channel;
import com.twilio.chat.ChannelListener;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.Member;
import com.twilio.chat.Message;
import com.twilio.chat.Messages;
import com.twilio.chat.StatusListener;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Objects;

import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SYSQUO.Chat.chat.messages.JoinedStatusMessage;
import sa.med.imc.myimc.SYSQUO.Chat.chat.messages.LeftStatusMessage;
import sa.med.imc.myimc.SYSQUO.Chat.chat.messages.MessageAdapter;
import sa.med.imc.myimc.SYSQUO.Chat.chat.messages.StatusMessage;
import sa.med.imc.myimc.SYSQUO.util.Progress;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.FileUtils;

public class MainChatFragment extends Fragment implements ChannelListener, AttachmentSelectionInterface {
  Context context;
  Activity mainActivity;
  Button sendButton;
  ListView messagesListView;
  EditDrawableText messageTextEdit;
  MessageAdapter messageAdapter;
  Channel currentChannel;
  Messages messagesObject;
  AttachmentSelectionInterface attachmentSelectionInterface;

  private static final int REQUEST_CODE = 6384; // onActivityResult request
  Progress progress;
  public MainChatFragment() {
  }

  public static MainChatFragment newInstance() {
    MainChatFragment fragment = new MainChatFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = this.getActivity();
    mainActivity = this.getActivity();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.sysquo_fragment_main_chat, container, false);

    progress = new Progress(getActivity());
    (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    progress.setCanceledOnTouchOutside(false);
    progress.setCancelable(false);

    sendButton = (Button) view.findViewById(R.id.buttonSend);
    messagesListView = (ListView) view.findViewById(R.id.listViewMessages);
    messageTextEdit = (EditDrawableText) view.findViewById(R.id.editTextMessage);

    messageAdapter = new MessageAdapter(mainActivity, attachmentSelectionInterface);
    messagesListView.setAdapter(messageAdapter);
    setUpListeners();
    setMessageInputEnabled(false);
    messageTextEdit.setDrawableClickListener(new onDrawableClickListener() {
      @Override
      public void onClick(@NotNull DrawablePosition drawablePosition) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, REQUEST_CODE);
      }
    });
    return view;
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  @Override
  public void onDetach() {
    super.onDetach();
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  public Channel getCurrentChannel() {
    return currentChannel;
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  public void setCurrentChannel(Channel currentChannel, final StatusListener handler) {
    showActivityIndicator("");
    if (currentChannel == null) {
      this.currentChannel = null;
//      Common.makeToast(getActivity(), "Retrun");
      return;
    }
    if (!currentChannel.equals(this.currentChannel)) {
      setMessageInputEnabled(false);
      this.currentChannel = currentChannel;
      this.currentChannel.addListener(this);
      messagesObject = currentChannel.getMessages();
      if (this.currentChannel.getStatus() == Channel.ChannelStatus.JOINED) {
//        Common.makeToast(getActivity(), "Joined "+currentChannel.getFriendlyName());
        final Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
          @Override
          public void run() {
            loadMessages(handler);
            stopActivityIndicator();
          }
        }, 2000);


      }
      else
      {
        stopActivityIndicator();
        this.currentChannel.join(new StatusListener() {
          @Override
          public void onSuccess() {
//            Common.makeToast(getActivity(), "Join Success "+currentChannel.getFriendlyName());
            loadMessages(handler);
          }

          @Override
          public void onError(ErrorInfo errorInfo) {
//            Common.makeToast(getActivity(), "Joining "+errorInfo.getMessage());
          }
        });
      }
    }
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  private void loadMessages(final StatusListener handler) {
    messagesObject = currentChannel.getMessages();

    if (messagesObject != null) {
      messagesObject.getLastMessages(100, new CallbackListener<List<Message>>() {
        @Override
        public void onSuccess(List<Message> messageList) {
          messageAdapter.setMessages(messageList);
          setMessageInputEnabled(true);
          messageTextEdit.requestFocus();
          handler.onSuccess();
        }
      });
    }
    else {
//      Common.makeToast(getActivity(), "Message Object is null ");
    }
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  private void setUpListeners() {
    sendButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sendMessage();
      }
    });
    messageTextEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
      }
    });
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  private void sendMessage() {
    String messageText = getTextInput();
    if (messageText.length() == 0) {
      return;
    }
    Message.Options messageOptions = Message.options().withBody(messageText);
    this.messagesObject.sendMessage(messageOptions, null);
    clearTextInput();
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  private void setMessageInputEnabled(final boolean enabled) {
    mainActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        MainChatFragment.this.sendButton.setEnabled(enabled);
        MainChatFragment.this.messageTextEdit.setEnabled(enabled);
      }
    });
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  private String getTextInput() {
    return messageTextEdit.getText().toString();
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  private void clearTextInput() {
    messageTextEdit.setText("");
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  @Override
  public void onMessageAdded(Message message) {
    messageAdapter.addMessage(message);
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  @Override
  public void onMemberAdded(Member member) {
    StatusMessage statusMessage = new JoinedStatusMessage(member.getIdentity());
    this.messageAdapter.addStatusMessage(statusMessage);
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  @Override
  public void onMemberDeleted(Member member) {
    StatusMessage statusMessage = new LeftStatusMessage(member.getIdentity());
    this.messageAdapter.addStatusMessage(statusMessage);
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  @Override
  public void onMessageUpdated(Message message, Message.UpdateReason updateReason) {
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  @Override
  public void onMessageDeleted(Message message) {
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  @Override
  public void onMemberUpdated(Member member, Member.UpdateReason updateReason) {
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  @Override
  public void onTypingStarted(Channel channel, Member member) {
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  @Override
  public void onTypingEnded(Channel channel, Member member) {
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  @Override
  public void onSynchronizationChanged(Channel channel) {
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  public void stopActivityIndicator() {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
                /*if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
        if(progress.isShowing()){
          progress.dismiss();
        }
      }
    });
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  private void showActivityIndicator(final String message) {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
                /*progressDialog = new ProgressDialog(MainChatActivity_New.this.mainActivity);
                progressDialog.setMessage(message);
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);*/
        progress.show();
      }
    });
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (resultCode == getActivity().RESULT_OK) {
      if (data != null) {
        // Get the URI of the selected file
        final Uri uri = data.getData();
        Log.i(ImcApplication.TAG, "Uri = " + uri.toString());
        try {
          // Get the file path from the URI
          final String path = FileUtils.getPath(getContext(), uri);
          if(path.contains(".jpeg") || path.contains(".jpg") || path.contains(".png") || path.contains(".gif") || path.contains(".txt") || path.contains(".pdf")) {
            readFileData(path);
          }else {
            Common.makeToast(getActivity(), getResources().getString(R.string.fileSelectionError));
//                getActivity().finish();
          }
        } catch (Exception e) {
          Log.e("jjjjjjjjj", "File select error", e);
        }
      }
    }
  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
  public void readFileData(String path) throws FileNotFoundException
  {

    String[] data;
    File file = new File(path);
    if (file.exists())
    {
      BufferedReader br = new BufferedReader(new FileReader(file));
      Common.makeToast(getActivity(), file.getName());
    }
    else
    {
      Common.makeToast(getActivity(), getResources().getString(R.string.fileNotExists));
    }
  }

  @Override
  public void getAttachMediaType(String mediaType) {

  }

  @Override
  public void downloadPdf(String fileName, String fileUrl, String mimeType, View view) {

  }



  @Override
  public void enlargeImage(String fileName, String fileUrl, View view) {

  }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
}