package com.bt_121shoppe.motorbike.settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.ChangepassModel;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePassword extends AppCompatActivity {

    EditText etOld,etNew,etComfirm;
    Button ChangePass;
    SharedPreferences prefer;
    private String name,pass,Encode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = getEncodedString(name,pass);

        TextView back = findViewById(R.id.tvBack_setting);
        etOld = (EditText)findViewById(R.id.etOldpass);
        etNew = (EditText)findViewById(R.id.etNewpass);
        etComfirm = (EditText)findViewById(R.id.etComfirmpass);
        ChangePass=(Button)findViewById(R.id.btnChangPassword);
        ChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etComfirm.getText().toString().equals(etNew.getText().toString())){
                    Change(Encode);
                }else {
                    Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void Change(String encode) {
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = String.format("%s%s", ConsumeAPI.BASE_URL, "api/v1/changepassword/");
        OkHttpClient client = new OkHttpClient();
        JSONObject post = new JSONObject();
        try{
            post.put("old_password",etOld.getText().toString());
            post.put("new_password",etComfirm.getText().toString());

        }catch (Exception e){
            e.printStackTrace();
        }
        String auth = "Basic "+ encode;
        RequestBody body = RequestBody.create(MEDIA_TYPE, post.toString());
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String st = response.body().string();
                Log.d("Result",st);
//               if (response.isSuccessful()){
//                   SharedPreferences.Editor editor = prefer.edit();
//                   editor.clear();
//                   editor.commit();
//                   startActivity(new Intent(ChangePassword.this, Home.class));
//               }
//               else {
//                   runOnUiThread(new Runnable() {
//                       @Override
//                       public void run() {
//                           Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_LONG).show();
//                       }
//                   });
//
//               }
                Gson gson = new Gson();
                ChangepassModel model = new ChangepassModel();
                try{
                    model=gson.fromJson(st,ChangepassModel.class);
                    if (model!=null){
                        int statusCode = model.getStatus();
                        if (statusCode==201){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this).create();
                                    alertDialog.setTitle(getString(R.string.title_change_password));
                                    alertDialog.setMessage(getString(R.string.success_change_password));
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    SharedPreferences.Editor editor = prefer.edit();
                                                    editor.putString("name",name);
                                                    editor.putString("pass",etComfirm.getText().toString());
                                                    editor.commit();
//                                                    startActivity(new Intent(ChangePassword.this, Home.class));
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            });
                                    alertDialog.show();
                                }
                            });
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),st,Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this).create();
                                alertDialog.setTitle(getString(R.string.title_change_password));
                                alertDialog.setMessage(getString(R.string.fail_change_password));
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });
                    }
                }catch (JsonParseException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),
                Base64.NO_WRAP);
    }
}
