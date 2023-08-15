package sa.med.imc.myimc.SYSQUO.Chat.chat.messages;


import com.twilio.chat.Message;

public class StatusMessage implements ChatMessage {
  private String author = "";

  public StatusMessage(String author) {
    this.author = author;
  }

  @Override
  public String getAuthor() {
    return author;
  }

  @Override
  public String getDateCreated() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getFileName() {
    return null;
  }

  @Override
  public void setContentTemporaryUrl(String s) {

  }

  @Override
  public String getContentTemporaryUrl() {
    return null;
  }


  @Override
  public Message.Type getMediaType() {
    return null;
  }

  @Override
  public boolean hasMedia() {
    return false;
  }

  @Override
  public String getMessageBody() {
    throw new UnsupportedOperationException();
  }
}
