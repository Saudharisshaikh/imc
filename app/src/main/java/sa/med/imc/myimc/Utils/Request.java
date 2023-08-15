package sa.med.imc.myimc.Utils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import sa.med.imc.myimc.BuildConfig;

public class Request {
    String url=  BuildConfig.BASE_URL;
    public interface InterfaceRequest{
        public void success(String response);
        public void error(String response);
    }
    Context context;

    private Request.InterfaceRequest interfaceRequest;

    public Request(Context context,Request.InterfaceRequest interfaceRequest){
        this.context = context;
        this.interfaceRequest = interfaceRequest;
    }

    public void call(final String action){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url+action,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        interfaceRequest.success(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                interfaceRequest.error(error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void call(final String action, final JSONObject jsonBody){
        RequestQueue queue = Volley.newRequestQueue(context);
        final String requestBody = jsonBody.toString();

        url = url + action;
        Log.e("url -> ",requestBody);

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        interfaceRequest.success(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                interfaceRequest.error(error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";

            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
}
