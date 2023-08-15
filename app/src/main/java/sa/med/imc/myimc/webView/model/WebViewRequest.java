package sa.med.imc.myimc.webView.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class WebViewRequest implements Parcelable {
    public static final String KEY = WebViewRequest.class.getSimpleName();
    private boolean showDialog;
    private String title;
    private String message;
    private String webLink;
    private String paymentRef;
    private Bundle bundle;

    public WebViewRequest() {
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    protected WebViewRequest(Parcel in) {
        showDialog = in.readByte() != 0;
        title = in.readString();
        message = in.readString();
        webLink = in.readString();
        paymentRef = in.readString();
        bundle = in.readBundle();
    }

    public static final Creator<WebViewRequest> CREATOR = new Creator<WebViewRequest>() {
        @Override
        public WebViewRequest createFromParcel(Parcel in) {
            return new WebViewRequest(in);
        }

        @Override
        public WebViewRequest[] newArray(int size) {
            return new WebViewRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (showDialog ? 1 : 0));
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(webLink);
        dest.writeString(paymentRef);
        dest.writeBundle(bundle);
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }
}

