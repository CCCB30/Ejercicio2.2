package com.example.ejercicio22;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ejercicio22.Interfaces.Interfaz;
import com.example.ejercicio22.Modelos.Usuarios;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    ListView list;
    ArrayList<String> titulos = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    EditText buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        obtenerUsuario();

        list = (ListView) findViewById(R.id.lista);

        buscar = (EditText) findViewById(R.id.txtBuscar);

        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    buscarUsuario(buscar.getText().toString());
                    if (buscar.getText().toString().equals("")){
                        obtenerUsuario();
                    }
                } catch (Exception ex){
                    Toast.makeText(getApplicationContext(),"Valor invalido",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void obtenerUsuario() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Interfaz interfaceusers = retrofit.create(Interfaz.class);

        Call <List<Usuarios>> llamada = interfaceusers.getUsuarios();

        llamada.enqueue(new Callback<List<Usuarios>>() {
            @Override
            public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                titulos.clear();
                for (Usuarios usuarios : response.body()){
                    titulos.add(usuarios.getTitle());

                    Log.i("On Response", usuarios.getTitle());
                }

                arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, titulos);
                list.setAdapter(arrayAdapter);
                //notifica si la data ha cambiado
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Usuarios>> call, Throwable t) {

            }
        });

    }

    private void buscarUsuario(String valor) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Interfaz interfaceusers = retrofit.create(Interfaz.class);


        Call <Usuarios> llamada = interfaceusers.getUsuario(valor);

        llamada.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {


                try {
                    Usuarios usuarios = response.body();

                    titulos.clear();//eliminar todo los registros
                    titulos.add(usuarios.getTitle());
                    arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, titulos);
                    list.setAdapter(arrayAdapter);

                    //Log.i("On Response", usuarios.getTitle());

                    //notifica si la data ha cambiado
                    arrayAdapter.notifyDataSetChanged();
                }catch (Exception ex){
                    titulos.clear();
                    buscar.setText("");
                    obtenerUsuario();
                    Toast.makeText(getApplicationContext(),"Valor no encontrado",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {

            }


        });

    }
}