package com.herprogramacion.pedidos.ui;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.herprogramacion.pedidos.R;
import com.herprogramacion.pedidos.modelo.CabeceraPedido;
import com.herprogramacion.pedidos.modelo.Cliente;
import com.herprogramacion.pedidos.modelo.Producto;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos;
import com.herprogramacion.pedidos.sqlite.OperacionesBaseDatos;

import java.util.Calendar;


public class ClienteFragment extends Fragment  implements AdapterView.OnItemSelectedListener{

    String selection;
    OperacionesBaseDatos datos;
    Context thiscontext;
    View rootView;
    String idcliente = "0";

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView text = (TextView) view;

        OperacionesBaseDatos datos = new OperacionesBaseDatos();
        Cursor c = datos.obtenerClientes();
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros

            int nombreColumn = c.getColumnIndex(ContratoPedidos.Clientes.NOMBRES);
            int direccionColumn = c.getColumnIndex(ContratoPedidos.Clientes.DIRECCION);
            int telefonoColumn = c.getColumnIndex(ContratoPedidos.Clientes.TELEFONO);

            //Recorremos el cursor
            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                String name = c.getString(nombreColumn);
                String direccion = c.getString(direccionColumn);
                String telefono = c.getString(telefonoColumn);

                Log.d("IF: ", text.getText()+" = " +name);
                if(text.getText().toString().equals(name)){
                    TextView dir =  (TextView) rootView.findViewById(R.id.tvDireccion);
                    TextView tel = (TextView) rootView.findViewById(R.id.tvTelefono);
                    dir.setText(direccion);
                    tel.setText(telefono);

                }
            }
        }
        //manejo de errores
        if(text.getText().toString().equals("")){
            Toast.makeText(getContext(), "Error.. Refresque la aplicacion", Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class TareaPruebaDatos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            // [INSERCIONES]
            String fechaActual = Calendar.getInstance().getTime().toString();

            try {

                datos.getDb().beginTransaction();

                // Inserción Clientes
                String cliente1 = datos.insertarCliente(new Cliente(null, "Jose Lopez", "Del Topo", "4552000","Dr. sosa c/ Zavala Cue "));
                String cliente2 = datos.insertarCliente(new Cliente(null, "Carlos Gonzalez", "Villagran", "4440000","Av. Mcal Lopez Nro. 1025"));
                String cliente3 = datos.insertarCliente(new Cliente(null, "Maria Medina", "Villagran", "4440000","Calle Siempre Viva nro 123"));



                // Inserción Productos
                String producto1 = datos.insertarProducto(new Producto(null, "Manzana", 10000, 10));
                String producto2 = datos.insertarProducto(new Producto(null, "Pera", 20000, 20));
                String producto3 = datos.insertarProducto(new Producto(null, "naranja", 30000, 30));
                String producto4 = datos.insertarProducto(new Producto(null, "Maní", 40000, 40));



                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }

            // [QUERIES]
            Log.d("Clientes","Clientes");
            DatabaseUtils.dumpCursor(datos.obtenerClientes());
            Log.d("Productos", "Productos");
            DatabaseUtils.dumpCursor(datos.obtenerProductos());
            Log.d("Cabeceras de pedido", "Cabeceras de pedido");
            DatabaseUtils.dumpCursor(datos.obtenerCabecerasPedidos());
            Log.d("Detalles de pedido", "Detalles de pedido");
            DatabaseUtils.dumpCursor(datos.obtenerDetallesPedido());

            return null;
        }
    }

    public ClienteFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






    }

    @Override
    public void onStart(){
        super.onStart();




        // accion del boton
        Button buttonClick = (Button) rootView.findViewById(R.id.btnCliente);
        buttonClick.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Fragment fragment = new ProductoFragment();

                //guardar el cliente seleccionado

                //obtener texto del spinner
                Spinner spinner =  (Spinner) rootView.findViewById(R.id.spinCliente);
                TextView textView = (TextView)spinner.getSelectedView();
                String result = textView.getText().toString();

                //recorrer la base de datos
                Cursor c = datos.obtenerClientes();
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros

                    int idColumn = c.getColumnIndex(ContratoPedidos.Clientes.ID);
                    int nombreColumn = c.getColumnIndex(ContratoPedidos.Clientes.NOMBRES);

                    //Recorremos el cursor
                    for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                        String name = c.getString(nombreColumn);
                        String id = c.getString(idColumn);

                        Log.d("IF: ", result+" = " +name);
                        if(result.equals(name)){
                            idcliente = id;

                        }
                    }
                }

                Bundle parametro = new Bundle();
                 parametro.putString("idcliente" , idcliente);
                parametro.putString("cabecera" , "0");
                parametro.putInt("secuencia" , 1);
                fragment.setArguments(parametro);

                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle("Productos");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_cliente, container, false);
        thiscontext = container.getContext();

        // creacion base de datos
       // getContext().deleteDatabase("pedidos.db");
        datos = OperacionesBaseDatos.obtenerInstancia(getContext());

        new TareaPruebaDatos().execute();

        // poblar el spinner
        Spinner prueba = (Spinner) rootView.findViewById(R.id.spinCliente);
        // SimpleCursorAdapter adapter;
        //Creamos el cursor
        Cursor c = datos.obtenerClientes();
        //Creamos el adaptador
        SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(thiscontext,android.R.layout.simple_spinner_item,c,new String[] {"nombres"},new int[] {android.R.id.text1});
        //Añadimos el layout para el menú
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prueba.setAdapter(adapter2);
        prueba.setOnItemSelectedListener(this);






        // Inflate the layout for this fragment
        return rootView;
    }






}
