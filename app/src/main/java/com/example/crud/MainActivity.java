package com.example.crud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText Todo;
    Button AddButton;
    ListView TodoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Todo = (EditText)findViewById(R.id.todo);
        AddButton = (Button)findViewById(R.id.addBtn);
        TodoList = (ListView)findViewById(R.id.todoList);


        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="http://192.168.10.4:5000/add_todo";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Toast.makeText(MainActivity.this, "Added Successfully",Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something went wrong"+error, Toast.LENGTH_LONG).show();
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramVariable = new HashMap<>();
                        paramVariable.put("TodoName",((TextView)Todo).getText().toString());
                        return paramVariable;
                    }
                };

                // Add the request to the RequestQueue.
                queue.add(stringRequest);

                //Refreshing Data

                RequestQueue requestQueue;
                requestQueue = Volley.newRequestQueue(MainActivity.this);

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                        "http://192.168.10.4:5000/get_todos", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            ArrayList arrayList = new ArrayList<>();
                            for(int i = 0; i < response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);
                                arrayList.add(obj.getString("TodoName"));

                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                            TodoList.setAdapter(arrayAdapter);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("myApp", "Something went wrong");

                    }
                });

                requestQueue.add(jsonArrayRequest);
            }
        });

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                "http://192.168.10.4:5000/get_todos", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    ArrayList arrayList = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++){
                        JSONObject obj = response.getJSONObject(i);
                        arrayList.add(obj.getString("TodoName"));

                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                    TodoList.setAdapter(arrayAdapter);


                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("myApp", "Something went wrong");

            }
        });

        requestQueue.add(jsonArrayRequest);

        TodoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MainActivity.this, "Item Clicked", Toast.LENGTH_LONG).show();
            }
        });


    }
}