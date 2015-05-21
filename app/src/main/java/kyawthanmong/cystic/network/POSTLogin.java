package kyawthanmong.cystic.network;

import android.app.ProgressDialog;
import android.content.Context;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.apache.http.Header;

import kyawthanmong.cystic.adapter.Constant;
import kyawthanmong.cystic.adapter.RestClientAdapter;
import kyawthanmong.cystic.adapter.Settings;
import kyawthanmong.cystic.adapter.model.LoginResponseModel;
import kyawthanmong.cystic.delegate.InterfaceLogin;

/**
 * Created by kyawthan on 4/15/15.
 */
public class POSTLogin {

    private InterfaceLogin delegate ;
    private Settings settings;
    private String TAG = POSTLogin.class.getName();
    private String user_id ;
    private ProgressDialog progressDialog;
    private Context mContext;


    public POSTLogin(Context context,String id, InterfaceLogin interfaceLogin)
    {
        this.delegate = interfaceLogin;
        this.user_id = id;
        this.mContext= context;
        this.settings = new Settings(context);
        settings.setUserId(id);
        shouldRequestionLogin(id);


    }

    private void shouldRequestionLogin(String id) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        shouldContactServer(params);
    }

    private void shouldContactServer(RequestParams params) {


        RestClientAdapter.post(Constant.LOGIN_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, new String(responseBody));
                LoginResponseModel model = new LoginResponseModel();
                Gson gson = new Gson();
                model = gson.fromJson(new String(responseBody), LoginResponseModel.class);

                if (model.loginCode == 1) {
                    settings.setUserLoginStatus(true);
                    delegate.didLoginSucceess();
                } else {
                    delegate.didLoginFail();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                delegate.didLoginFail();


            }
        });
    }


}
