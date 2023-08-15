package sa.med.imc.myimc.SYSQUO.Chat.chat.messages;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twilio.chat.Message;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SYSQUO.Chat.chat.AttachmentSelectionInterface;
import sa.med.imc.myimc.SYSQUO.Chat.chat.ImageLoader.ImageLoader;
import sa.med.imc.myimc.SYSQUO.util.Constants;


public class MessageAdapter extends BaseAdapter {
  private final int TYPE_MESSAGE = 0;
  private final int TYPE_STATUS = 1;

  private List<ChatMessage> messages;
  private LayoutInflater layoutInflater;
  private TreeSet<Integer> statusMessageSet = new TreeSet<>();
  Context mContext;
  AttachmentSelectionInterface attachmentSelectionInterface;
  public MessageAdapter(Activity activity, AttachmentSelectionInterface attachmentSelectionInterface) {
    layoutInflater = activity.getLayoutInflater();
    this.mContext = activity;
    this.attachmentSelectionInterface = attachmentSelectionInterface;
    messages = new ArrayList<>();
  }

  public void setMessages(List<Message> messages) {
    this.messages = convertTwilioMessages(messages);
    this.statusMessageSet.clear();
    notifyDataSetChanged();
  }
  public int getMessagesCount() {
    return this.messages.size();
  }

  public void addMessage(Message message) {
    messages.add(new UserMessage(message));
    notifyDataSetChanged();
  }

  public void addStatusMessage(StatusMessage message) {
    messages.add(message);
    statusMessageSet.add(messages.size() - 1);
    notifyDataSetChanged();
  }

  public void removeMessage(Message message) {
    messages.remove(messages.indexOf(message));
    notifyDataSetChanged();
  }

  private List<ChatMessage> convertTwilioMessages(List<Message> messages) {
    List<ChatMessage> chatMessages = new ArrayList<>();
    for (Message message : messages) {
      chatMessages.add(new UserMessage(message));
    }
    return chatMessages;
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public int getItemViewType(int position) {
    return statusMessageSet.contains(position) ? TYPE_STATUS : TYPE_MESSAGE;
  }

  @Override
  public int getCount() {
    return messages.size();
  }

  @Override
  public Object getItem(int i) {
    return messages.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup viewGroup) {

    int type = getItemViewType(position);
    int res;

    SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREF_LANGUAGE, 0);
    String language = sharedPreferences.getString(Constants.KEY_LANGUAGE,"");
    if (SharedPreferencesUtils.getInstance(mContext).getValue(sa.med.imc.myimc.Network.Constants.KEY_LANGUAGE, sa.med.imc.myimc.Network.Constants.ENGLISH).equalsIgnoreCase(sa.med.imc.myimc.Network.Constants.ENGLISH))
    {
      switch (type) {
        case TYPE_MESSAGE:
          ChatMessage message = messages.get(position);
//          MEDIA MESSAGE
          if(message.hasMedia())
          {
            String identity = SharedPreferencesUtils.getInstance(mContext).getValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_PHYSICIAN, null);
            if (!message.getAuthor().equals(identity))
            {
              res = R.layout.sysquo_message_send;
              convertView = layoutInflater.inflate(res, viewGroup, false);
              ImageLoader imgLoader = new ImageLoader(mContext);
              int loader = R.drawable.sysquo_loader;
              LinearLayout LayoutMain             = (LinearLayout) convertView.findViewById(R.id.LayoutMain);
              LinearLayout LayoutMain_Sender             = (LinearLayout) convertView.findViewById(R.id.LayoutMain_Sender);
              LayoutMain_Sender.setBackground(mContext.getDrawable(R.drawable.sysquo_chat_send));

              LinearLayout LinearLayoutMessage    = (LinearLayout) convertView.findViewById(R.id.LinearLayoutMessage);
              TextView TextViewMessage            = (TextView) convertView.findViewById(R.id.TextViewMessage);

              LinearLayout LinearLayoutImage      = (LinearLayout)convertView.findViewById(R.id.LinearLayoutImage);
              ImageView ImageView_Icon_Image      = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Image);
              TextView TextView_Image_Name        = (TextView) convertView.findViewById(R.id.TextView_Image_Name);
              ImageView ImagevIew_Download_Image  = (ImageView) convertView.findViewById(R.id.ImagevIew_Download_Image);

              LinearLayout LinearLayoutPdf        = (LinearLayout)convertView.findViewById(R.id.LinearLayoutPdf);
              ImageView ImageView_Icon_Pdf        = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Pdf);
              TextView TextView_Pdf_Name          = (TextView) convertView.findViewById(R.id.TextView_Pdf_Name);
              ImageView ImageView_Download_PDF    = (ImageView) convertView.findViewById(R.id.ImageView_Download_PDF);

              TextView textViewAuthor             = (TextView) convertView.findViewById(R.id.textViewAuthor);
              TextView textViewDate               = (TextView) convertView.findViewById(R.id.textViewDate);

              if(message.getMediaType().getValue() == 1)
              {
                if(message.getFileName() != null)
                {
                  String imgUrl = message.getContentTemporaryUrl();
                  if (message.getFileName().contains(".pdf") || message.getFileName().contains(".PDF") || message.getFileName().contains(".Pdf"))
                  {
                    LinearLayoutMessage.setVisibility(View.GONE);
                    LinearLayoutImage.setVisibility(View.GONE);
                    LinearLayoutPdf.setVisibility(View.VISIBLE);

                    TextView_Pdf_Name.setText(message.getFileName());

                    LinearLayoutPdf.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    ImageView_Icon_Pdf.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    TextView_Pdf_Name.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    ImageView_Download_PDF.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    LayoutMain.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    textViewDate.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });
                  }
                  else if(message.getFileName().contains(".gif") || message.getFileName().contains(".jpeg") || message.getFileName().contains(".jpg") || message.getFileName().contains(".png"))
                  {
                    LinearLayoutMessage.setVisibility(View.GONE);
                    LinearLayoutImage.setVisibility(View.VISIBLE);
                    LinearLayoutPdf.setVisibility(View.GONE);

                    TextView_Image_Name.setText(message.getFileName());

                    LayoutMain.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    LinearLayoutImage.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    ImageView_Icon_Image.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    TextView_Image_Name.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    ImagevIew_Download_Image.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    textViewDate.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });
                  }
                }
                else
                {
                  LinearLayoutMessage.setVisibility(View.GONE);
                  LinearLayoutImage.setVisibility(View.GONE);
                  LinearLayoutPdf.setVisibility(View.VISIBLE);
                  ImageView_Download_PDF.setVisibility(View.GONE);
                  Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_error_red);
                  ImageView_Icon_Pdf.setImageDrawable(drawable);
                  TextView_Pdf_Name.setText("unknown Media");
                }
              }

              textViewAuthor.setText(message.getAuthor().trim());
              textViewDate.setText(DateFormatter.getFormattedDateFromISOString(message.getDateCreated().trim()));
            }
            else
            {
              res = R.layout.sysquo_message;
              convertView = layoutInflater.inflate(res, viewGroup, false);
              ImageLoader imgLoader = new ImageLoader(mContext);
              int loader = R.drawable.sysquo_loader;
              LinearLayout LayoutMain       = (LinearLayout) convertView.findViewById(R.id.LayoutMain);
              LinearLayout LayoutMain_Receiver             = (LinearLayout) convertView.findViewById(R.id.LayoutMain_Receiver);
              LayoutMain_Receiver.setBackground(mContext.getDrawable(R.drawable.sysquo_chat_received));

              LinearLayout LinearLayoutMessage    = (LinearLayout) convertView.findViewById(R.id.LinearLayoutMessage);
              TextView TextViewMessage            = (TextView) convertView.findViewById(R.id.TextViewMessage);

              LinearLayout LinearLayoutImage      = (LinearLayout)convertView.findViewById(R.id.LinearLayoutImage);
              ImageView ImageView_Icon_Image      = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Image);
              TextView TextView_Image_Name        = (TextView) convertView.findViewById(R.id.TextView_Image_Name);
              ImageView ImagevIew_Download_Image  = (ImageView) convertView.findViewById(R.id.ImagevIew_Download_Image);

              LinearLayout LinearLayoutPdf        = (LinearLayout)convertView.findViewById(R.id.LinearLayoutPdf);
              ImageView ImageView_Icon_Pdf        = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Pdf);
              TextView TextView_Pdf_Name          = (TextView) convertView.findViewById(R.id.TextView_Pdf_Name);
              ImageView ImageView_Download_PDF    = (ImageView) convertView.findViewById(R.id.ImageView_Download_PDF);

              TextView textViewAuthor             = (TextView) convertView.findViewById(R.id.textViewAuthor);
              TextView textViewDate               = (TextView) convertView.findViewById(R.id.textViewDate);
              if(message.getMediaType().getValue() == 1)
              {
                if(message.getFileName() != null)
                {
                  String imgUrl = message.getContentTemporaryUrl();
                  if (message.getFileName().contains(".pdf") || message.getFileName().contains(".Pdf") || message.getFileName().contains(".PDF"))
                  {
                    LinearLayoutMessage.setVisibility(View.GONE);
                    LinearLayoutImage.setVisibility(View.GONE);
                    LinearLayoutPdf.setVisibility(View.VISIBLE);

                    TextView_Pdf_Name.setText(message.getFileName());

                    LinearLayoutPdf.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    ImageView_Icon_Pdf.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    TextView_Pdf_Name.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    ImageView_Download_PDF.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    LayoutMain.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    textViewDate.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });
                  }
                  else if(message.getFileName().contains(".gif") || message.getFileName().contains(".jpeg") || message.getFileName().contains(".jpg") || message.getFileName().contains(".png"))
                  {
                    LinearLayoutMessage.setVisibility(View.GONE);
                    LinearLayoutImage.setVisibility(View.VISIBLE);
                    LinearLayoutPdf.setVisibility(View.GONE);

                    TextView_Image_Name.setText(message.getFileName());

                    LayoutMain.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    LinearLayoutImage.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    ImageView_Icon_Image.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    TextView_Image_Name.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    ImagevIew_Download_Image.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    textViewDate.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });
                  }
                }
                else
                {
                  LinearLayoutMessage.setVisibility(View.GONE);
                  LinearLayoutImage.setVisibility(View.GONE);
                  LinearLayoutPdf.setVisibility(View.VISIBLE);
                  ImageView_Download_PDF.setVisibility(View.GONE);
                  Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_error_red);
                  ImageView_Icon_Pdf.setImageDrawable(drawable);
                  TextView_Pdf_Name.setText("unknown Media");
                }
              }

              textViewAuthor.setText(message.getAuthor().trim());
              textViewDate.setText(DateFormatter.getFormattedDateFromISOString(message.getDateCreated().trim()));
            }
          }
          else {
//          PIYUSH 28-0-2022
            String identity = SharedPreferencesUtils.getInstance(mContext).getValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_PHYSICIAN, null);
            if (!message.getAuthor().equals(identity)) {
              res = R.layout.sysquo_message_send;
              convertView = layoutInflater.inflate(res, viewGroup, false);
              LinearLayout LayoutMain = (LinearLayout) convertView.findViewById(R.id.LayoutMain);
              LinearLayout LayoutMain_Sender             = (LinearLayout) convertView.findViewById(R.id.LayoutMain_Sender);
              LayoutMain_Sender.setBackground(mContext.getDrawable(R.drawable.sysquo_chat_send));

              LinearLayout LinearLayoutMessage    = (LinearLayout) convertView.findViewById(R.id.LinearLayoutMessage);
              TextView TextViewMessage            = (TextView) convertView.findViewById(R.id.TextViewMessage);

              LinearLayout LinearLayoutImage      = (LinearLayout)convertView.findViewById(R.id.LinearLayoutImage);
              ImageView ImageView_Icon_Image      = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Image);
              TextView TextView_Image_Name        = (TextView) convertView.findViewById(R.id.TextView_Image_Name);
              ImageView ImagevIew_Download_Image  = (ImageView) convertView.findViewById(R.id.ImagevIew_Download_Image);

              LinearLayout LinearLayoutPdf        = (LinearLayout)convertView.findViewById(R.id.LinearLayoutPdf);
              ImageView ImageView_Icon_Pdf        = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Pdf);
              TextView TextView_Pdf_Name          = (TextView) convertView.findViewById(R.id.TextView_Pdf_Name);
              ImageView ImageView_Download_PDF    = (ImageView) convertView.findViewById(R.id.ImageView_Download_PDF);

              TextView textViewAuthor             = (TextView) convertView.findViewById(R.id.textViewAuthor);
              TextView textViewDate               = (TextView) convertView.findViewById(R.id.textViewDate);

              LinearLayoutMessage.setVisibility(View.VISIBLE);
              LinearLayoutPdf.setVisibility(View.GONE);
              LinearLayoutImage.setVisibility(View.GONE);

              TextViewMessage.setText(message.getMessageBody().trim());
              textViewAuthor.setText(message.getAuthor());
              textViewDate.setText(DateFormatter.getFormattedDateFromISOString(message.getDateCreated()));
            }
            else {
              res = R.layout.sysquo_message;
              convertView = layoutInflater.inflate(res, viewGroup, false);
              LinearLayout LayoutMain = (LinearLayout) convertView.findViewById(R.id.LayoutMain);

              LinearLayout LinearLayoutMessage    = (LinearLayout) convertView.findViewById(R.id.LinearLayoutMessage);
              TextView TextViewMessage            = (TextView) convertView.findViewById(R.id.TextViewMessage);

              LinearLayout LinearLayoutImage      = (LinearLayout)convertView.findViewById(R.id.LinearLayoutImage);
              ImageView ImageView_Icon_Image      = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Image);
              TextView TextView_Image_Name        = (TextView) convertView.findViewById(R.id.TextView_Image_Name);
              ImageView ImagevIew_Download_Image  = (ImageView) convertView.findViewById(R.id.ImagevIew_Download_Image);

              LinearLayout LinearLayoutPdf        = (LinearLayout)convertView.findViewById(R.id.LinearLayoutPdf);
              ImageView ImageView_Icon_Pdf        = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Pdf);
              TextView TextView_Pdf_Name          = (TextView) convertView.findViewById(R.id.TextView_Pdf_Name);
              ImageView ImageView_Download_PDF    = (ImageView) convertView.findViewById(R.id.ImageView_Download_PDF);

              TextView textViewAuthor             = (TextView) convertView.findViewById(R.id.textViewAuthor);
              TextView textViewDate               = (TextView) convertView.findViewById(R.id.textViewDate);

              LinearLayoutMessage.setVisibility(View.VISIBLE);
              LinearLayoutPdf.setVisibility(View.GONE);
              LinearLayoutImage.setVisibility(View.GONE);

              TextViewMessage.setText(message.getMessageBody().trim());
              textViewAuthor.setText(message.getAuthor());
              textViewDate.setText(DateFormatter.getFormattedDateFromISOString(message.getDateCreated()));
            }
          }
          break;
        case TYPE_STATUS:
          /*res = R.layout.status_message;
          convertView = layoutInflater.inflate(res, viewGroup, false);
          ChatMessage status = messages.get(position);
          TextView textViewStatus = (TextView) convertView.findViewById(R.id.textViewStatusMessage);
          String statusMessage = status.getMessageBody();
          textViewStatus.setText(statusMessage);*/
          break;
      }
    }
//    ARABIC
    else
    {
      switch (type) {
        case TYPE_MESSAGE:
          ChatMessage message = messages.get(position);
//          PIYUSH 28-03-2022
          /*String identity = SharedPreferencesUtils.getInstance(mContext).getValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_PHYSICIAN, null);
          if(!message.getAuthor().equals(identity))
          {
            res = R.layout.sysquo_message_send;
            convertView = layoutInflater.inflate(res, viewGroup, false);
            LinearLayout LayoutMain = (LinearLayout) convertView.findViewById(R.id.LayoutMain);
            LinearLayout LayoutMain_Sender = (LinearLayout) convertView.findViewById(R.id.LayoutMain_Sender);
            LayoutMain_Sender.setBackground(mContext.getDrawable(R.drawable.sysquo_chat_send_ar));
            TextView textViewMessage = (TextView) convertView.findViewById(R.id.textViewMessage);
            TextView textViewAuthor = (TextView) convertView.findViewById(R.id.textViewAuthor);
            TextView textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
            textViewMessage.setText(message.getMessageBody().trim());
            textViewAuthor.setText(message.getAuthor().trim());
            textViewDate.setText(DateFormatter.getFormattedDateFromISOString(message.getDateCreated().trim()));
          }
          else
          {
            res = R.layout.sysquo_message;
            convertView = layoutInflater.inflate(res, viewGroup, false);
            LinearLayout LayoutMain = (LinearLayout) convertView.findViewById(R.id.LayoutMain);
            LinearLayout LayoutMain_Receiver = (LinearLayout) convertView.findViewById(R.id.LayoutMain_Receiver);
            LayoutMain_Receiver.setBackground(mContext.getDrawable(R.drawable.sysquo_chat_received_ar));
            TextView textViewMessage = (TextView) convertView.findViewById(R.id.textViewMessage);
            TextView textViewAuthor = (TextView) convertView.findViewById(R.id.textViewAuthor);
            TextView textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
            textViewMessage.setText(message.getMessageBody().trim());
            textViewAuthor.setText(message.getAuthor().trim());
            textViewDate.setText(DateFormatter.getFormattedDateFromISOString(message.getDateCreated().trim()));
          }*/

          if(message.hasMedia())
          {
            String identity = SharedPreferencesUtils.getInstance(mContext).getValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_PHYSICIAN, null);
            if (!message.getAuthor().equals(identity))
            {
              res = R.layout.sysquo_message_send;
              convertView = layoutInflater.inflate(res, viewGroup, false);
              ImageLoader imgLoader = new ImageLoader(mContext);
              int loader = R.drawable.sysquo_loader;
              LinearLayout LayoutMain             = (LinearLayout) convertView.findViewById(R.id.LayoutMain);
              LinearLayout LayoutMain_Sender             = (LinearLayout) convertView.findViewById(R.id.LayoutMain_Sender);
              LayoutMain_Sender.setBackground(mContext.getDrawable(R.drawable.sysquo_chat_send_ar));

              LinearLayout LinearLayoutMessage    = (LinearLayout) convertView.findViewById(R.id.LinearLayoutMessage);
              TextView TextViewMessage            = (TextView) convertView.findViewById(R.id.TextViewMessage);

              LinearLayout LinearLayoutImage      = (LinearLayout)convertView.findViewById(R.id.LinearLayoutImage);
              ImageView ImageView_Icon_Image      = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Image);
              TextView TextView_Image_Name        = (TextView) convertView.findViewById(R.id.TextView_Image_Name);
              ImageView ImagevIew_Download_Image  = (ImageView) convertView.findViewById(R.id.ImagevIew_Download_Image);

              LinearLayout LinearLayoutPdf        = (LinearLayout)convertView.findViewById(R.id.LinearLayoutPdf);
              ImageView ImageView_Icon_Pdf        = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Pdf);
              TextView TextView_Pdf_Name          = (TextView) convertView.findViewById(R.id.TextView_Pdf_Name);
              ImageView ImageView_Download_PDF    = (ImageView) convertView.findViewById(R.id.ImageView_Download_PDF);

              TextView textViewAuthor             = (TextView) convertView.findViewById(R.id.textViewAuthor);
              TextView textViewDate               = (TextView) convertView.findViewById(R.id.textViewDate);

              if(message.getMediaType().getValue() == 1)
              {
                if(message.getFileName() != null)
                {
                  String imgUrl = message.getContentTemporaryUrl();
                  if (message.getFileName().contains(".pdf") || message.getFileName().contains(".PDF") || message.getFileName().contains(".Pdf"))
                  {
                    LinearLayoutMessage.setVisibility(View.GONE);
                    LinearLayoutImage.setVisibility(View.GONE);
                    LinearLayoutPdf.setVisibility(View.VISIBLE);

                    TextView_Pdf_Name.setText(message.getFileName());

                    LinearLayoutPdf.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    ImageView_Icon_Pdf.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    TextView_Pdf_Name.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    ImageView_Download_PDF.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    LayoutMain.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    textViewDate.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });
                  }
                  else if(message.getFileName().contains(".gif") || message.getFileName().contains(".jpeg") || message.getFileName().contains(".jpg") || message.getFileName().contains(".png"))
                  {
                    LinearLayoutMessage.setVisibility(View.GONE);
                    LinearLayoutImage.setVisibility(View.VISIBLE);
                    LinearLayoutPdf.setVisibility(View.GONE);

                    TextView_Image_Name.setText(message.getFileName());

                    LayoutMain.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    LinearLayoutImage.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    ImageView_Icon_Image.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    TextView_Image_Name.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    ImagevIew_Download_Image.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    textViewDate.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });
                  }
                }
                else
                {
                  LinearLayoutMessage.setVisibility(View.GONE);
                  LinearLayoutImage.setVisibility(View.GONE);
                  LinearLayoutPdf.setVisibility(View.VISIBLE);
                  ImageView_Download_PDF.setVisibility(View.GONE);
                  Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_error_red);
                  ImageView_Icon_Pdf.setImageDrawable(drawable);
                  TextView_Pdf_Name.setText("unknown Media");
                }
              }

              textViewAuthor.setText(message.getAuthor().trim());
              textViewDate.setText(DateFormatter.getFormattedDateFromISOString(message.getDateCreated().trim()));
            }
            else
            {
              res = R.layout.sysquo_message;
              convertView = layoutInflater.inflate(res, viewGroup, false);
              ImageLoader imgLoader = new ImageLoader(mContext);
              int loader = R.drawable.sysquo_loader;
              LinearLayout LayoutMain       = (LinearLayout) convertView.findViewById(R.id.LayoutMain);
              LinearLayout LayoutMain_Receiver             = (LinearLayout) convertView.findViewById(R.id.LayoutMain_Receiver);
              LayoutMain_Receiver.setBackground(mContext.getDrawable(R.drawable.sysquo_chat_received_ar));

              LinearLayout LinearLayoutMessage    = (LinearLayout) convertView.findViewById(R.id.LinearLayoutMessage);
              TextView TextViewMessage            = (TextView) convertView.findViewById(R.id.TextViewMessage);

              LinearLayout LinearLayoutImage      = (LinearLayout)convertView.findViewById(R.id.LinearLayoutImage);
              ImageView ImageView_Icon_Image      = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Image);
              TextView TextView_Image_Name        = (TextView) convertView.findViewById(R.id.TextView_Image_Name);
              ImageView ImagevIew_Download_Image  = (ImageView) convertView.findViewById(R.id.ImagevIew_Download_Image);

              LinearLayout LinearLayoutPdf        = (LinearLayout)convertView.findViewById(R.id.LinearLayoutPdf);
              ImageView ImageView_Icon_Pdf        = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Pdf);
              TextView TextView_Pdf_Name          = (TextView) convertView.findViewById(R.id.TextView_Pdf_Name);
              ImageView ImageView_Download_PDF    = (ImageView) convertView.findViewById(R.id.ImageView_Download_PDF);

              TextView textViewAuthor             = (TextView) convertView.findViewById(R.id.textViewAuthor);
              TextView textViewDate               = (TextView) convertView.findViewById(R.id.textViewDate);
              if(message.getMediaType().getValue() == 1)
              {
                if(message.getFileName() != null)
                {
                  String imgUrl = message.getContentTemporaryUrl();
                  if (message.getFileName().contains(".pdf") || message.getFileName().contains(".Pdf") || message.getFileName().contains(".PDF"))
                  {
                    LinearLayoutMessage.setVisibility(View.GONE);
                    LinearLayoutImage.setVisibility(View.GONE);
                    LinearLayoutPdf.setVisibility(View.VISIBLE);

                    TextView_Pdf_Name.setText(message.getFileName());

                    LinearLayoutPdf.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    ImageView_Icon_Pdf.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    TextView_Pdf_Name.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    ImageView_Download_PDF.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    LayoutMain.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });

                    textViewDate.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "application/pdf", v);
                      }
                    });
                  }
                  else if(message.getFileName().contains(".gif") || message.getFileName().contains(".jpeg") || message.getFileName().contains(".jpg") || message.getFileName().contains(".png"))
                  {
                    LinearLayoutMessage.setVisibility(View.GONE);
                    LinearLayoutImage.setVisibility(View.VISIBLE);
                    LinearLayoutPdf.setVisibility(View.GONE);

                    TextView_Image_Name.setText(message.getFileName());

                    LayoutMain.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    LinearLayoutImage.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    ImageView_Icon_Image.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    TextView_Image_Name.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    ImagevIew_Download_Image.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });

                    textViewDate.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        if (message.getFileName() != null)
                        {
                          attachmentSelectionInterface.downloadPdf(message.getFileName(), message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage(message.getFileName(), message.getContentTemporaryUrl(), v);
                        }
                        else
                        {
                          attachmentSelectionInterface.downloadPdf("unknown.jpg", message.getContentTemporaryUrl(), "image/*", v);
                          attachmentSelectionInterface.enlargeImage("unknown.jpg", message.getContentTemporaryUrl(), v);
                        }
                      }
                    });
                  }
                }
                else
                {
                  LinearLayoutMessage.setVisibility(View.GONE);
                  LinearLayoutImage.setVisibility(View.GONE);
                  LinearLayoutPdf.setVisibility(View.VISIBLE);
                  ImageView_Download_PDF.setVisibility(View.GONE);
                  Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_error_red);
                  ImageView_Icon_Pdf.setImageDrawable(drawable);
                  TextView_Pdf_Name.setText("unknown Media");
                }
              }

              textViewAuthor.setText(message.getAuthor().trim());
              textViewDate.setText(DateFormatter.getFormattedDateFromISOString(message.getDateCreated().trim()));
            }
          }
          else {
//          PIYUSH 28-0-2022
            String identity = SharedPreferencesUtils.getInstance(mContext).getValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_PHYSICIAN, null);
            if (!message.getAuthor().equals(identity)) {
              res = R.layout.sysquo_message_send;
              convertView = layoutInflater.inflate(res, viewGroup, false);
              LinearLayout LayoutMain = (LinearLayout) convertView.findViewById(R.id.LayoutMain);
              LinearLayout LayoutMain_Sender             = (LinearLayout) convertView.findViewById(R.id.LayoutMain_Sender);
              LayoutMain_Sender.setBackground(mContext.getDrawable(R.drawable.sysquo_chat_send_ar));

              LinearLayout LinearLayoutMessage    = (LinearLayout) convertView.findViewById(R.id.LinearLayoutMessage);
              TextView TextViewMessage            = (TextView) convertView.findViewById(R.id.TextViewMessage);

              LinearLayout LinearLayoutImage      = (LinearLayout)convertView.findViewById(R.id.LinearLayoutImage);
              ImageView ImageView_Icon_Image      = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Image);
              TextView TextView_Image_Name        = (TextView) convertView.findViewById(R.id.TextView_Image_Name);
              ImageView ImagevIew_Download_Image  = (ImageView) convertView.findViewById(R.id.ImagevIew_Download_Image);

              LinearLayout LinearLayoutPdf        = (LinearLayout)convertView.findViewById(R.id.LinearLayoutPdf);
              ImageView ImageView_Icon_Pdf        = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Pdf);
              TextView TextView_Pdf_Name          = (TextView) convertView.findViewById(R.id.TextView_Pdf_Name);
              ImageView ImageView_Download_PDF    = (ImageView) convertView.findViewById(R.id.ImageView_Download_PDF);

              TextView textViewAuthor             = (TextView) convertView.findViewById(R.id.textViewAuthor);
              TextView textViewDate               = (TextView) convertView.findViewById(R.id.textViewDate);

              LinearLayoutMessage.setVisibility(View.VISIBLE);
              LinearLayoutPdf.setVisibility(View.GONE);
              LinearLayoutImage.setVisibility(View.GONE);

              TextViewMessage.setText(message.getMessageBody().trim());
              textViewAuthor.setText(message.getAuthor());
              textViewDate.setText(DateFormatter.getFormattedDateFromISOString(message.getDateCreated()));
            }
            else {
              res = R.layout.sysquo_message;
              convertView = layoutInflater.inflate(res, viewGroup, false);
              LinearLayout LayoutMain = (LinearLayout) convertView.findViewById(R.id.LayoutMain);
              LinearLayout LayoutMain_Receiver             = (LinearLayout) convertView.findViewById(R.id.LayoutMain_Receiver);
              LayoutMain_Receiver.setBackground(mContext.getDrawable(R.drawable.sysquo_chat_received_ar));

              LinearLayout LinearLayoutMessage    = (LinearLayout) convertView.findViewById(R.id.LinearLayoutMessage);
              TextView TextViewMessage            = (TextView) convertView.findViewById(R.id.TextViewMessage);

              LinearLayout LinearLayoutImage      = (LinearLayout)convertView.findViewById(R.id.LinearLayoutImage);
              ImageView ImageView_Icon_Image      = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Image);
              TextView TextView_Image_Name        = (TextView) convertView.findViewById(R.id.TextView_Image_Name);
              ImageView ImagevIew_Download_Image  = (ImageView) convertView.findViewById(R.id.ImagevIew_Download_Image);

              LinearLayout LinearLayoutPdf        = (LinearLayout)convertView.findViewById(R.id.LinearLayoutPdf);
              ImageView ImageView_Icon_Pdf        = (ImageView) convertView.findViewById(R.id.ImageView_Icon_Pdf);
              TextView TextView_Pdf_Name          = (TextView) convertView.findViewById(R.id.TextView_Pdf_Name);
              ImageView ImageView_Download_PDF    = (ImageView) convertView.findViewById(R.id.ImageView_Download_PDF);

              TextView textViewAuthor             = (TextView) convertView.findViewById(R.id.textViewAuthor);
              TextView textViewDate               = (TextView) convertView.findViewById(R.id.textViewDate);

              LinearLayoutMessage.setVisibility(View.VISIBLE);
              LinearLayoutPdf.setVisibility(View.GONE);
              LinearLayoutImage.setVisibility(View.GONE);

              TextViewMessage.setText(message.getMessageBody().trim());
              textViewAuthor.setText(message.getAuthor());
              textViewDate.setText(DateFormatter.getFormattedDateFromISOString(message.getDateCreated()));
            }
          }
          break;
        case TYPE_STATUS:
          res = R.layout.sysquo_status_message;
          convertView = layoutInflater.inflate(res, viewGroup, false);
          ChatMessage status = messages.get(position);
          TextView textViewStatus = (TextView) convertView.findViewById(R.id.textViewStatusMessage);
          String statusMessage = status.getMessageBody().trim();
          textViewStatus.setText(statusMessage);
          break;
      }
    }

    return convertView;
  }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
      ImageView bmImage;
      ProgressBar progressBar;
      public DownloadImageTask(ImageView bmImage, ProgressBar progressBar) {
        this.bmImage = bmImage;
        this.progressBar = progressBar;
      }

      protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
          InputStream in = new java.net.URL(urldisplay).openStream();
          mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
          Log.e("Error", e.getMessage());
          e.printStackTrace();
        }
        return mIcon11;
      }

      protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        progressBar.setVisibility(View.GONE);
      }
    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
}
