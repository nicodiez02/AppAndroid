package com.example.rsys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText user;
    private EditText password;

    private Button iniciar, back;

    RequestQueue requestQueue;
    private static final String URL = "https://clipping-liquors.000webhostapp.com/updatePC.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        user = (EditText)findViewById(R.id.editTextUser);
        password = (EditText)findViewById(R.id.editTextPassword);

        iniciar = (Button)findViewById(R.id.buttonLogin);
        back = (Button)findViewById(R.id.buttonBackLogin);



        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Usuario = user.getText().toString().trim();
                String Clave = password.getText().toString().trim();
                validarCampos(Usuario, Clave);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backLogin();
            }
        });
    }

    public void backLogin(){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void validarCampos(String Usuario, String Clave){

        ProgressDialog progressDialog = new ProgressDialog(this);


        if(Usuario.isEmpty() || Clave.isEmpty()){
            Toast.makeText(Login.this, "Porfavor ingrese sus datos", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setMessage("Validando...");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String serverResponse) {
                            try{
                                JSONObject jsonObject = new JSONObject(serverResponse);
                                Boolean error = jsonObject.getBoolean("error");

                                if(error == true){
                                    progressDialog.dismiss();
                                    Toast.makeText(Login.this, "Los datos ingresados no son correctos", Toast.LENGTH_SHORT).show();
                                }else{
                                    openWindow();
                                    progressDialog.dismiss();
                                }

                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Login.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<>();

                            params.put("opcion","login");
                            params.put("Usuario",Usuario);
                            params.put("Clave",Clave);

                            return params;
                        }
                    };

            requestQueue = Volley.newRequestQueue(Login.this);
            requestQueue.add(stringRequest);

        }

    }

    public void openWindow(){
        Intent intent= new Intent(this, Update.class);
        startActivity(intent);
    }
}
