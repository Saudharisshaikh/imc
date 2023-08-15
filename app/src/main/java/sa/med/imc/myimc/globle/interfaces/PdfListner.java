package sa.med.imc.myimc.globle.interfaces;

import java.io.InputStream;

public interface PdfListner {

    void onSuccess(String pdf_base64);
    void onFail();

}
