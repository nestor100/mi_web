package com.herprogramacion.pedidos.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.herprogramacion.pedidos.R;
import com.herprogramacion.pedidos.modelo.CabeceraPedido;
import com.herprogramacion.pedidos.modelo.DetallePedido;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos;
import com.herprogramacion.pedidos.sqlite.OperacionesBaseDatos;

import java.util.Calendar;


/**
 * Created by Ravi on 29/07/15.
 */
public class ProductoFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    Context thiscontext;
    View rootView;
    String idcliente;
    String idcabecera;
    Integer putprecio;
    Integer putcantidad = 0;
    Integer puttotal;
    Integer secuencia;
    String fechaActual = Calendar.getInstance().getTime().toString();

    //version 2.0
     EditText cantidad;
     TextView precio ;
    TextView stock;
    TextView total;

    public ProductoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idcliente  = getArguments().getString("idcliente");
        idcabecera  = getArguments().getString("cabecera");
        secuencia  = getArguments().getInt("secuencia");





    }


    @Override
    public void onStart() {
        super.onStart();
        OperacionesBaseDatos datos = new OperacionesBaseDatos();
        datos.getDb().beginTransaction();
        // poblar el spinner
        Spinner prueba = (Spinner) rootView.findViewById(R.id.spinProducto);

        // SimpleCursorAdapter adapter;
        //Creamos el cursor
        Cursor c = datos.obtenerProductos();
        //Creamos el adaptador
        SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, c, new String[]{"nombre"}, new int[]{android.R.id.text1});
        //Añadimos el layout para el menú
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prueba.setAdapter(adapter2);
        String cant = String.valueOf(prueba.getCount());
       // Toast.makeText(getContext(), "Now onStart() calls " + cant, Toast.LENGTH_LONG).show(); //onStart Called

        //seleccionar el spin
        prueba.setOnItemSelectedListener(this);


        precio =  (TextView) rootView.findViewById(R.id.tvPrecio);


        // accion del boton
        Button buttonClick = (Button) rootView.findViewById(R.id.btnProducto1);
        buttonClick.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                total =  (TextView) rootView.findViewById(R.id.tvtotal);
                puttotal = Integer.parseInt(String.valueOf(total.getText()));
                stock = (TextView) rootView.findViewById(R.id.tvStock);

                NumberPicker np = (NumberPicker) rootView.findViewById(R.id.numberPicker1);

               Log.i("total",total.getText().toString());

                if(total.getText().toString().equals("0")){

                    Toast.makeText(v.getContext(), "Ingrese la cantidad", Toast.LENGTH_SHORT).show(); //onStart Called
                }else if(np.getValue() > Integer.parseInt(stock.getText().toString())){
                    Toast.makeText(v.getContext(), "Cantidad supera el stock", Toast.LENGTH_SHORT).show(); //onStart Called
                }else{
                    Fragment fragment = new PedidosFragment();

                    //guardar el cliente seleccionado
                    String idproducto = null;
                    //obtener texto del spinner
                    Spinner spinner =  (Spinner) rootView.findViewById(R.id.spinProducto);
                    TextView textView = (TextView)spinner.getSelectedView();
                    String result = textView.getText().toString();

                    //recorrer la base de datos
                    OperacionesBaseDatos datos = new OperacionesBaseDatos();
                    Cursor c = datos.obtenerProductos();
                    if (c.moveToFirst()) {
                        //Recorremos el cursor hasta que no haya más registros

                        int idColumn = c.getColumnIndex(ContratoPedidos.Productos.ID);
                        int nombreColumn = c.getColumnIndex(ContratoPedidos.Productos.NOMBRE);

                        //Recorremos el cursor
                        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                            String name = c.getString(nombreColumn);
                            String id = c.getString(idColumn);

                            Log.d("IF: ", result+" = " +name);
                            if(result.equals(name)){
                                idproducto = id;
                                //Toast.makeText(getContext(), name + " =  " +  result, Toast.LENGTH_LONG).show(); //onStart Called
                            }
                        }
                    }

                    precio =  (TextView) rootView.findViewById(R.id.tvPrecio);
                   // cantidad  =  (EditText) rootView.findViewById(R.id.ITcantidad);
                    putprecio = Integer.parseInt(String.valueOf(precio.getText()));
                  //  putcantidad = Integer.parseInt(String.valueOf(cantidad.getText()));

                    //crear cabecera pediddo
                    String pedido1 =null;
                    if(idcabecera.equals("0")){
                         pedido1 = datos.insertarCabeceraPedido( new CabeceraPedido(null, fechaActual, idcliente, String.valueOf(total.getText())));
                    }else{
                         pedido1 = idcabecera;
                    }


                    // Inserción Detalles
                    datos.insertarDetallePedido(new DetallePedido(pedido1, secuencia, idproducto,putcantidad, Float.parseFloat(String.valueOf(precio.getText()))));


                    Bundle parametro = new Bundle();
                    parametro.putString("idcliente" , idcliente);
                    parametro.putString("pedido" , pedido1);


                    fragment.setArguments(parametro);

                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.commit();

                        // set the toolbar title
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Pedidos");
                    }

                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_producto, container, false);
        thiscontext = container.getContext();




        // Inflate the layout for this fragment
        return rootView;
    }

    public void calcularTotal(int precio, int cant){
        total =  (TextView) rootView.findViewById(R.id.tvtotal);
        Integer tot = cant * precio;
        total.setText(String.valueOf(tot));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView text = (TextView) view;


        OperacionesBaseDatos datos = new OperacionesBaseDatos();
        Cursor c = datos.obtenerProductos();
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros

            int nombreColumn = c.getColumnIndex(ContratoPedidos.Productos.NOMBRE);
            int precioColumn = c.getColumnIndex(ContratoPedidos.Productos.PRECIO);
            int stockColumn = c.getColumnIndex(ContratoPedidos.Productos.EXISTENCIAS);

            //Recorremos el cursor
            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                String name = c.getString(nombreColumn);
                String precio = c.getString(precioColumn);
                String instock = c.getString(stockColumn);

                Log.d("IF: ", text.getText()+" = " +name);
                if(text.getText().equals(name)){
                  //  Toast.makeText(getContext(), name + " =  " + text.getText(), Toast.LENGTH_LONG).show(); //onStart Called
                    TextView inputprecio =  (TextView) rootView.findViewById(R.id.tvPrecio);
                    TextView stock = (TextView) rootView.findViewById(R.id.tvStock);
                    inputprecio.setText(precio);
                    stock.setText(instock);


                    //picker de cantidad
                    NumberPicker np = (NumberPicker) rootView.findViewById(R.id.numberPicker1);
                    stock = (TextView) rootView.findViewById(R.id.tvStock);
                    Integer maxvalue = Integer.parseInt(stock.getText().toString());
                    np.setMinValue(0);// restricted number to minimum value i.e 1
                    np.setMaxValue(maxvalue);// restricked number to maximum value i.e. 31
                    np.setWrapSelectorWheel(true);

                    np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {

                            TextView  precio =  (TextView) rootView.findViewById(R.id.tvPrecio);
                            putcantidad = newVal;
                            calcularTotal(  Integer.parseInt(String.valueOf(precio.getText())), newVal);


                        }
                    });

                    calcularTotal(  Integer.parseInt(precio), np.getValue());

                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
