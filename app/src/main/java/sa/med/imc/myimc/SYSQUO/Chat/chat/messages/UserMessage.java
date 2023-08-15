package sa.med.imc.myimc.SYSQUO.Chat.chat.messages;

import com.twilio.chat.CallbackListener;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.Message;

public class UserMessage implements ChatMessage {

  private String author = "";
  private String dateCreated = "";
  private String messageBody = "";
  private boolean hasMedia = false;
  public String url = null;
  public String fileName = null;
  public Message.Type mediaType = null;
  public UserMessage(Message message) {
    this.author = message.getAuthor();
    this.dateCreated = message.getDateCreated();
    this.messageBody = message.getMessageBody();
    this.hasMedia = message.hasMedia();
    if(hasMedia) {
      setURL(message);
      this.mediaType = message.getType();
      this.fileName = message.getMedia().getFileName();
    }

  }

  @Override
  public String getMessageBody() {
    return messageBody;
  }

  @Override
  public String getAuthor() {
    return author;
  }

  @Override
  public String getDateCreated() {
    return dateCreated;
  }

  @Override
  public String getFileName() {
    return fileName;
  }

  @Override
  public void setContentTemporaryUrl(String s) {
    this.url = s;
  }

  @Override
  public String getContentTemporaryUrl() {

    return url;
  }


  @Override
  public Message.Type getMediaType() {
    return mediaType;
  }


  public void setURL(Message messagess){
    try {
      messagess.getMedia().getContentTemporaryUrl(new CallbackListener<String>() {

        @Override
        public void onSuccess(String s) {
          setContentTemporaryUrl(s);
        }

        @Override
        public void onError(ErrorInfo errorInfo) {
          super.onError(errorInfo);
          setContentTemporaryUrl(errorInfo.getMessage());
        }
      });
    }catch (Exception e){
      e.printStackTrace();
    }
  }
  @Override
  public boolean hasMedia() {
    return hasMedia;
  }


}
