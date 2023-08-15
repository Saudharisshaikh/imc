package sa.med.imc.myimc.SYSQUO.Chat.chat.accesstoken;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.SYSQUO.Chat.application.SessionManager;
import sa.med.imc.myimc.SYSQUO.Chat.chat.listeners.TaskCompletionListener;

public class AccessTokenFetcher {

  private Context context;

  public AccessTokenFetcher(Context context) {
    this.context = context;
  }

  public void fetch(final TaskCompletionListener<String, String> listener) {

    try {
      RequestQueue requestQueue = Volley.newRequestQueue(ImcApplication.getInstance());
      String URL = BuildConfig.BASE_URL_VIDEO + "api/twilio/chat/get-access-token";

      String mrnNumber = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_MRN, null);
      String jwtToken = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_ACCESS_TOKEN, null);

      StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
//          Log.i("VOLLEY", response);
//          Toast.makeText(context,response,Toast.LENGTH_LONG).show();
          JSONObject foo = null;
          String token = null;
          try {
            foo = new JSONObject(response);
            String status = foo.getString("status").toString();
            String message = foo.getString("message").toString();
            token = foo.getString("accessToken").toString();
          } catch (JSONException e) {
            e.printStackTrace();
          }

          listener.onSuccess(token);
        }
      }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          Log.e("VOLLEY", error.toString());
        }
      }) {
        @Override
        public String getBodyContentType() {
          return "application/json; charset=utf-8";
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
          try {
            // request body goes here
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("identity", mrnNumber);
            String requestBody = jsonBody.toString();
            return requestBody.getBytes("utf-8");
          } catch (UnsupportedEncodingException | JSONException uee) {
//            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody., "utf-8");
            return null;
          }
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
          Map<String, String>  params = new HashMap<String, String>();
          params.put("Content-Type", "application/json");
          params.put("Authorization", jwtToken);
          return params;
        }

        /*@Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
          String responseString = "";
          if (response != null) {
            responseString = String.valueOf(response.data.);
            // can get more details such as response.headers
          }
          return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
        }*/
      };
      Log.d("string", stringRequest.toString());
      requestQueue.add(stringRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }


    /*try {
      String url = BuildConfig.BASE_URL_VIDEO + "api/twilio/chat/get-access-token";

      String mrnNumber = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(sa.med.imc.myimc.Network.Constants.KEY_REMEMBER_MRN, null);
      String jwtToken = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(sa.med.imc.myimc.Network.Constants.KEY_ACCESS_TOKEN, null);
      StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
              new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                  JSONObject foo = null;
                  String token = null;
                  try {
                    foo = new JSONObject(response);
                    String status = foo.getString("status").toString();
                    String message = foo.getString("message").toString();
                    String data = foo.getString("data").toString();
                    JSONObject RData = new JSONObject(data);
                    token = RData.getString("token");
                  } catch (JSONException e) {
                    e.printStackTrace();
                  }

                  listener.onSuccess(token);
                }
              },
              new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                  Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                  listener.onError("Failed to fetch token");
                }
              }) {

        @Override
        public String getBodyContentType() {
          return "application/json; charset=utf-8";
        }

        //This is for Headers If You Needed
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
          Map<String, String> params = new HashMap<String, String>();
          params.put("Authorization", jwtToken);
          return params;
        }

        @Override
        protected Map<String, String> getParams() {
          Map<String, String> params = new HashMap<String, String>();
          params.put("identity", mrnNumber);
          return params;
        }

      };
//    Volley.newRequestQueue(context).add(jsonRequest);
//    jsonRequest.setShouldCache(false);

      stringRequest.setShouldCache(false);
//      VollySupport.getmInstance(RegisterActivity.this).addToRequestque(stringRequest);
      TokenRequest.getInstance().addToRequestQueue(stringRequest);
    }catch (Exception e){
      e.printStackTrace();
    }*/


   /* try {
      ChatAccessTokenViewModel viewModel = ViewModelProviders.of(ImcApplication.getInstance().getMainChatActivity()).get(ChatAccessTokenViewModel.class);
      viewModel.init();
//        String roomID = SharedPreferencesUtils.getInstance(VideoActivity.this).getValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_ROOM_ID, null);
      ChatAccessTokenRequetModel chatAccessTokenRequetModel = new ChatAccessTokenRequetModel();
      String mrnNumber = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(sa.med.imc.myimc.Network.Constants.KEY_REMEMBER_MRN, null);
      chatAccessTokenRequetModel.setIdentity(mrnNumber);
      String jwtToken = SharedPreferencesUtils.getInstance(context).getValue(sa.med.imc.myimc.Network.Constants.KEY_ACCESS_TOKEN, null);
      viewModel.ChatAccessToken(jwtToken, chatAccessTokenRequetModel);
      viewModel.getVolumesResponseLiveData().observe(ImcApplication.getInstance().getMainChatActivity(), response -> {
        if (response != null) {
          if (response.getStatus()) {
            listener.onSuccess(response.getAccessToken());
          } else {
            listener.onError("Failed to fetch token");
          }
        } else {
          listener.onError("Failed to fetch token");
        }
      });
    }catch (Exception e){
      e.printStackTrace();
    }*/
  }

  /*public void ChatAccessToken(){
    ChatAccessTokenViewModel viewModel = ViewModelProviders.of((FragmentActivity) context).get(ChatAccessTokenViewModel.class);
    viewModel.init();
//        String roomID = SharedPreferencesUtils.getInstance(VideoActivity.this).getValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_ROOM_ID, null);
    ChatAccessTokenRequetModel chatAccessTokenRequetModel = new ChatAccessTokenRequetModel();
    String mrnNumber = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(sa.med.imc.myimc.Network.Constants.KEY_REMEMBER_MRN, null);
    chatAccessTokenRequetModel.setIdentity(mrnNumber);
    String jwtToken = SharedPreferencesUtils.getInstance(context).getValue(sa.med.imc.myimc.Network.Constants.KEY_ACCESS_TOKEN, null);
    viewModel.ChatAccessToken(jwtToken, chatAccessTokenRequetModel);
    viewModel.getVolumesResponseLiveData().observe(context, response -> {
      if(response!=null){
        if(response.getStatus()){
          listener.onSuccess(token);
        }
      }
      else
      {
        Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }*/


  private Map<String, String> getTokenRequestParams(Context context) {
    Map<String, String> params = new HashMap<>();
    params.put("identity", SessionManager.getInstance().getUsername());
    return params;
  }

  private String getStringResource(int id) {
    Resources resources = ImcApplication.getInstance().getResources();
    return resources.getString(id);
  }

}
