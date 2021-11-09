package com.example.rsys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeleteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeleteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteFragment newInstance(String param1, String param2) {
        DeleteFragment fragment = new DeleteFragment();
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

    private Button buttonDelete,buttonDeleteBack;
    private EditText deleteCursoA, deleteCursoB, deleteLab;

    private CheckBox checkDeleteLab,checkDeleteCurso;

    String URL = "https://clipping-liquors.000webhostapp.com/adicionSpinner.php";

    RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_delete, container, false);

        buttonDelete = root.findViewById(R.id.buttonDelete);
        buttonDeleteBack = root.findViewById(R.id.buttonBackDelete);

        deleteCursoA = root.findViewById(R.id.editTextDeleteCurso);
        deleteCursoB = root.findViewById(R.id.editTextDeleteCursoB);
        deleteLab = root.findViewById(R.id.editTextDeleteLab);

        checkDeleteLab = root.findViewById(R.id.checkBoxDeleteLab);
        checkDeleteCurso = root.findViewById(R.id.checkBoxDeleteCurso);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoCursoA = deleteCursoA.getText().toString().trim().toUpperCase();
                String textoCursoB = deleteCursoB.getText().toString().trim().toUpperCase();
                String textoLaboratorio = deleteLab.getText().toString().trim();

                validarCampos(textoCursoA,textoCursoB,textoLaboratorio);
            }
        });


        buttonDeleteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backDelete();
            }
        });

        return root;
    }

    public void backDelete(){
        Intent intent= new Intent(getContext(), Login.class);
        startActivity(intent);
    }

    private void eliminar(String cursoA, String cursoB, String lab){

        ProgressDialog progressDialog = new ProgressDialog(getContext());

                progressDialog.setMessage("Eliminando...");
                progressDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(),"Eliminacion realizada con exito",Toast.LENGTH_LONG).show();
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

                        String textoCursoGeneral = cursoA + cursoB;

                        if(checkDeleteLab.isChecked()){
                            params.put("opcion","deleteLab");
                            params.put("aula",lab);
                        }
                        else if(checkDeleteCurso.isChecked()){
                            params.put("opcion","deleteCurso");
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

    public void validarCampos(String cursoA, String cursoB, String lab) {

        if (checkDeleteCurso.isChecked() && checkDeleteLab.isChecked()) {
            Toast.makeText(getContext(), "Seleccione solo UNA casilla", Toast.LENGTH_SHORT).show();
        }else{
            if (checkDeleteLab.isChecked()) {

                if (cursoA.length() > 0 || cursoB.length() > 0) {
                    Toast.makeText(getContext(), "Complete los campos correctos", Toast.LENGTH_SHORT).show();
                } else if (lab.length() == 0) {
                    Toast.makeText(getContext(), "Ingrese un laboratorio", Toast.LENGTH_SHORT).show();
                }
            } else {
                eliminar(cursoA, cursoB, lab);
            }

            if (checkDeleteCurso.isChecked()) {

                if (lab.length() > 0) {
                    Toast.makeText(getContext(), "Complete los campos correctos", Toast.LENGTH_SHORT).show();
                } else if (cursoB.length() == 0 && cursoA.length() == 0) {
                    Toast.makeText(getContext(), "Ingrese un curso", Toast.LENGTH_SHORT).show();
                } else if (checkDeleteCurso.isChecked() && checkDeleteLab.isChecked()) {
                    Toast.makeText(getContext(), "Seleccione solo UNA casilla", Toast.LENGTH_SHORT).show();
                } else if (cursoB.length() > 1) {
                    Toast.makeText(getContext(), "Solo se permite letras individuales en categoria", Toast.LENGTH_SHORT).show();
                }

            } else {
                eliminar(cursoA, cursoB, lab);
            }
        }
    }
}

