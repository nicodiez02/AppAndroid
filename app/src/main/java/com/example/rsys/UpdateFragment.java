package com.example.rsys;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance(String param1, String param2) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private Button buttonUpdate, backBackUpdate;
    private EditText cursoA, cursoB, lab, pc, cantpc;

    private CheckBox checkPC,checkLab,checkCurso;

    String URL = "https://clipping-liquors.000webhostapp.com/adicionSpinner.php";

    RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_update, container, false);

        buttonUpdate = root.findViewById(R.id.buttonUpdate);
        backBackUpdate = root.findViewById(R.id.buttonBackUpdate);

        cursoA = root.findViewById(R.id.editTextUpdateCurso);
        cursoB = root.findViewById(R.id.editTextUpdateCursoB);
        lab = root.findViewById(R.id.editTextUpdateLab);
        cantpc = root.findViewById(R.id.editTextUpdateLABPC);

        checkCurso = root.findViewById(R.id.checkBoxCurso);
        checkLab = root.findViewById(R.id.checkBoxLab);


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoCursoA = cursoA.getText().toString().trim().toUpperCase();
                String textoCursoB = cursoB.getText().toString().trim().toUpperCase();
                String textoLab = lab.getText().toString().trim();
                String CantPC = cantpc.getText().toString().trim();

                validarCampos(textoCursoA,textoCursoB, textoLab, CantPC);
            }
        });

        backBackUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backUpdate();
            }
        });

        return root;
    }

    public void backUpdate(){
        Intent intent= new Intent(getContext(), Login.class);
        startActivity(intent);
    }

    private void update(String textoCursoA, String textoCursoB, String textoLab, String CantPC){

        ProgressDialog progressDialog = new ProgressDialog(getContext());

            progressDialog.setMessage("Actualizando...");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),"Actualizacion realizada con exito",Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),"Error: " + error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
            )
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    String textoCursoGeneral = textoCursoA + " " + textoCursoB;

                    if(checkLab.isChecked()){
                        params.put("opcion","labs");
                        params.put("aula",textoLab);
                        params.put("CantPC",CantPC);
                    }else if(checkCurso.isChecked()){
                        params.put("opcion","cursos");
                        params.put("Nombre",textoCursoGeneral);
                    }else{
                        progressDialog.dismiss();
                        Looper.prepare();
                        Toast.makeText(getContext(), "Tilde alguna opcion (Cursos, Laboratorios)", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    return params;
                }
            };

            requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);


        }

    public void validarCampos(String cursoA, String cursoB, String lab, String cantidad) {

            if (checkCurso.isChecked() && checkLab.isChecked()) {
                Toast.makeText(getContext(), "Seleccione solo UNA casilla", Toast.LENGTH_SHORT).show();
            } else {

                if (checkLab.isChecked()) {

                    if (cursoA.length() > 0 || cursoB.length() > 0) {
                        Toast.makeText(getContext(), "Complete los campos correctos", Toast.LENGTH_SHORT).show();
                    } else if (lab.isEmpty() || cantidad.isEmpty()) {
                        Toast.makeText(getContext(), "Ingrese un laboratorio", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    update(cursoA, cursoB, lab, cantidad);
                }

                if (checkCurso.isChecked()) {

                    if (lab.length() > 0) {
                        Toast.makeText(getContext(), "Complete los campos correctos", Toast.LENGTH_SHORT).show();
                    } else if (cursoB.length() == 0 && cursoA.length() == 0) {
                        Toast.makeText(getContext(), "Ingrese un curso", Toast.LENGTH_SHORT).show();
                    } else if (cursoB.length() > 1) {
                        Toast.makeText(getContext(), "Solo se permite letras individuales en categoria", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    update(cursoA, cursoB, lab, cantidad);
                }
            }
        }
    }