package sa.med.imc.myimc.SYSQUO.Chat.chat;

import android.view.View;

public interface AttachmentSelectionInterface {
    void getAttachMediaType(String mediaType);
    void downloadPdf(String fileName, String fileUrl, String mimeType, View view);
    void enlargeImage(String fileName, String fileUrl, View view);
}
