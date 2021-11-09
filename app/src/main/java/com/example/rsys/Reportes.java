package com.example.rsys;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.rsys.GetterSetter.Laboratorios;
import com.example.rsys.GetterSetter.Curso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class Reportes extends AppCompatActivity {

    private AsyncHttpClient curso;
    private AsyncHttpClient lab;

    private Spinner spinnerProblem;
    private Spinner spinnerLAB;
    private Spinner spinnerPC;
    private Spinner spinnerCurso;

    private EditText nombre;
    private EditText apellido;
    private EditText comentario;
    private EditText auxiliar;

    private Button send,back;

    private static final String URL = "https://clipping-liquors.000webhostapp.com/android.php";
    private static final String URL1 = "https://clipping-liquors.000webhostapp.com/updateSpinner.php";
    private static final String URL2 = "https://clipping-liquors.000webhostapp.com/updateLabs.php";

    RequestQueue requestQueue;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_reportes);

        spinnerCurso = (Spinner)findViewById(R.id.spinnerCurso);
        spinnerLAB = (Spinner)findViewById(R.id.spinnerLab);
        spinnerProblem = (Spinner)findViewById(R.id.spinnerCategoria);
        spinnerPC = (Spinner)findViewById(R.id.spinnerPC);

        nombre = (EditText)findViewById(R.id.editTextName);
        apellido = (EditText) findViewById(R.id.editTextApellido);
        auxiliar = (EditText) findViewById(R.id.multiLineAuxiliar);
        comentario = (EditText) findViewById(R.id.editTextComentario);

        send = (Button) findViewById(R.id.buttonSend);
        back = (Button) findViewById(R.id.buttonBack);

        curso = new AsyncHttpClient();
        lab = new AsyncHttpClient();

        String [] problemas = {"Seleccione opcion de reporte","Internet", "Monitor", "Perifericos","Software","Teams","Windows", "Otro"};

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,R.layout.activity_personalize_spinner, problemas);
        spinnerProblem.setAdapter(arrayAdapter2);

        String seleccion = "106";

        progressDialog.setMessage("Procesando...");
        progressDialog.show();

        loadSpinner();
        loadSpinnerPC(seleccion, progressDialog);

        spinnerLAB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
                String seleccion1 = spinnerLAB.getItemAtPosition(i).toString();
                loadSpinnerPC(seleccion1, progressDialog);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                String name = nombre.getText().toString();
                String surname = apellido.getText().toString();
                String report = spinnerProblem.getSelectedItem().toString();
                String aux = auxiliar.getText().toString();
                String coment = comentario.getText().toString();
                String course = spinnerCurso.getSelectedItem().toString();
                String compu = spinnerPC.getSelectedItem().toString();
                String laboratory = spinnerLAB.getSelectedItem().toString();

                switch (validaciones(0)){
                    case 11:
                        inserccion(name, surname,report,aux,coment,course,compu,laboratory);
                        break;
                    case 1:
                        nombre.setError("Introduzca su nombre");
                        break;
                    case 2:
                        apellido.setError("Introduzca su apellido");
                        break;
                    case 3:
                        Toast.makeText(Reportes.this, "Seleccion un reporte", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        auxiliar.setError("Si desea completar este campo, seleccion Otro");
                        break;
                    case 5:
                        auxiliar.setError("Describa el problema(Max:250 caracteres)");

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

    }

    public void back(){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public int validaciones(int okey1){
        okey1 = 0;

        String nombreMetodo = nombre.getText().toString();
        String apellidoMetodo = apellido.getText().toString();
        String auxiliarMetodo = auxiliar.getText().toString();

        int positionProblem = spinnerProblem.getSelectedItemPosition();

        switch (positionProblem) {
            case 0:
                okey1 = okey1 + 3;
                return okey1;
            case 7:
                if (auxiliarMetodo.length() == 0 || auxiliarMetodo.length() > 250) {
                    okey1 = 5;
                    return okey1;
                }
        }

        if(nombreMetodo.length() == 0){
            okey1++;
            return okey1;
        }else if(apellidoMetodo.length() == 0){
            okey1 = okey1 + 2;
            return okey1;
        }else if(positionProblem != 7){
            if(auxiliarMetodo.length() > 0){
                okey1 = 4;
                return okey1;
            }
        }
        okey1= 11;
        return okey1;
    }

    private void inserccion(final String user, final String surname, final String report, final String other, final String coment, final String course, final String  computer, final String laboratory) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(Reportes.this,"Su reporte ha sido enviado con exito",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Reportes.this,"Error: " + error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre",user);
                params.put("apellido",surname);
                params.put("reporte",report);
                params.put("otro",other);
                params.put("comentario",coment);
                params.put("curso",course);
                params.put("pc",computer);
                params.put("lab",laboratory);

                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(Reportes.this);
        requestQueue.add(stringRequest);
    }

    private void loadSpinnerPC(String seleccion, ProgressDialog progressDialog){
        String urlPC = "https://clipping-liquors.000webhostapp.com/fetch.php?Aula=" + seleccion;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlPC,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String aula;
                        ArrayList<Integer> arrayList = new ArrayList<Integer>();

                        try {
                            aula = response.getString("CantPC");

                            int aulaNum = Integer.parseInt(aula);

                            for(int i = 1; i < aulaNum + 1; i++){
                                arrayList.add(i);
                            }

                            spinnerPC.setAdapter(new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

        progressDialog.dismiss();

    }

    private void loadSpinner(){
        curso.post(URL1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    loadSpinnerCourse(new String (responseBody));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        lab.post(URL2, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    loadSpinnerLabs(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void loadSpinnerCourse(String respuesta){
        ArrayList<Curso> lista = new ArrayList<Curso>();

        try{
            JSONArray jsonArray = new JSONArray(respuesta);
            for(int i = 0; i < jsonArray.length(); i++){
                Curso c = new Curso();
                c.setNombre(jsonArray.getJSONObject(i).getString("Nombre"));
                lista.add(c);
            }
            ArrayAdapter<Curso> a = new ArrayAdapter<Curso>(this, android.R.layout.simple_dropdown_item_1line,lista);
            spinnerCurso.setAdapter(a);
        }catch(Exception e){

        }
    }

    public void loadSpinnerLabs(String answer){
        ArrayList<Laboratorios> lista1 = new ArrayList<Laboratorios>();

        try{
            JSONArray jsonArray = new JSONArray(answer);
            for(int i = 0; i < jsonArray.length(); i++){
                Laboratorios l = new Laboratorios();
                l.setAula(jsonArray.getJSONObject(i).getString("Aula"));
                lista1.add(l);
            }
            ArrayAdapter<Laboratorios> b = new ArrayAdapter<Laboratorios>(this, android.R.layout.simple_dropdown_item_1line,lista1);
            spinnerLAB.setAdapter(b);
        }catch(Exception e){

        }

    }
}