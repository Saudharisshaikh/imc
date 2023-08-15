package sa.med.imc.myimc.SYSQUO.Chat.chat.accesstoken;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import sa.med.imc.myimc.Network.ImcApplication;

public class TokenRequest {
  private static TokenRequest mInstance;
  private RequestQueue mRequestQueue;

  private TokenRequest() {
    mRequestQueue = Volley.newRequestQueue(ImcApplication.getInstance().getApplicationContext());
  }

  public static synchronized TokenRequest getInstance() {
    if (mInstance == null) {
      mInstance = new TokenRequest();
    }
    return mInstance;
  }

  public <T> void addToRequestQueue(Request<T> req) {
    mRequestQueue.add(req);
  }
}
