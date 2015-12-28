package me.alfredobejarano.apicallexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    /**
     *  ------------------------------------
     * |************************************|
     * |*****[VOLLEY API CALL EXAMPLE] *****|
     * |***** Juan Alfredo Corona Bejarano *|
     * |*****    MercadoLibre México   *****|
     * |************************************|
     *  ------------------------------------
     *
     *  Volley es una libreria desarrollada por Google Inc. y presentada
     *  en la Goolge IO 2013.
     *
     *  Funciona creando una cola de peticiones (RequestQueue), a esta se le añaden peticiones
     *  (Request) :v, esta cola lleva un tiempo de espera y tamaño de caché .
     *
     */
    private TextView textView;
    private Cache cache;
    private Network network;
    private RequestQueue queue;
    private static final String url = "http://api.tvmaze.com/shows";
    private JsonArrayRequest jsonArrayRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Llamamos a nuestro TextView
        textView = (TextView) findViewById(R.id.helloWorld);

        /** Iniciamos el cache y la red para nuestra cola de peticiones
         * Para el cache, le definimos la ruta donde va a alacenarse y el tamaño máximo. (en bytes)
         * Para la red, definimos una pila.
         */
        cache = new DiskBasedCache(getCacheDir(), 1024*1024); // 1MB de caché
        network = new BasicNetwork(new HurlStack());

        // Ahora iniciamos nuestra cola
        queue = new RequestQueue(cache, network);
        queue.start();

        // Creamos nuestra peticion, en este caso, esperamos una String.
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                textView.setText(String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText(String.valueOf(error));
            }
        });

        // Le ponemos una etiqueta a nuestra petición
        jsonArrayRequest.setTag("API_CALL");

        // Ahora, añadimos nuestra peticion a la cola
        queue.add(jsonArrayRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(queue != null) {
            queue.cancelAll("API_CALL");
        }
    }
}