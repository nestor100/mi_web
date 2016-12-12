package com.herprogramacion.pedidos.modelo;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.herprogramacion.pedidos.R;

import java.util.ArrayList;

/**
 * Created by jaime on 28/10/16.
 */
public class Tabla {
    private TableLayout tabla; // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas; // Array de las filas de la tabla
    private Activity actividad;
    private Resources rs;
    private int FILAS, COLUMNAS; // Filas y columnas de nuestra tabla


    public Tabla(Activity actividad, TableLayout tabla)
    {
        this.actividad = actividad;
        this.tabla = tabla;
        rs = this.actividad.getResources();
        FILAS = COLUMNAS = 0;
        filas = new ArrayList<TableRow>();
    }


    public void agregarCabecera(int recursocabecera)
    {
        TableRow.LayoutParams layoutCelda;


        TableRow fila = new TableRow(actividad);
        //format
        fila.setPadding(0, 0, 0, 2); //Border between rows
        fila.setBackgroundColor(Color.LTGRAY);




        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        fila.setLayoutParams(layoutFila);

        //format
        layoutFila.setMargins(0, 0, 2, 0);//2px right-margi

        String[] arraycabecera = rs.getStringArray(recursocabecera);
        COLUMNAS = arraycabecera.length;

        for(int i = 0; i < arraycabecera.length; i++)
        {
            TextView texto = new TextView(actividad);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(arraycabecera[i]), TableRow.LayoutParams.MATCH_PARENT);
            texto.setText(arraycabecera[i]);
            texto.setGravity(Gravity.CENTER_HORIZONTAL);

            //format
            texto.setPadding(0, 0, 4, 3);
            texto.setTextSize(18);
            texto.setTextColor(Color.BLACK);

            texto.setLayoutParams(layoutCelda);

            fila.addView(texto);
        }

        tabla.addView(fila);
        filas.add(fila);

        FILAS++;
    }

    /**
     * Agrega una fila a la tabla
     * @param elementos Elementos de la fila
     */
    public void agregarFilaTabla(ArrayList<String> elementos)
    {
        TableRow.LayoutParams layoutCelda;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);

        for(int i = 0; i< elementos.size(); i++)
        {
            TextView texto = new TextView(actividad);
            texto.setText(String.valueOf(elementos.get(i)));
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            texto.setTextSize(18);

            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(texto.getText().toString()), TableRow.LayoutParams.WRAP_CONTENT);
            texto.setLayoutParams(layoutCelda);

            fila.addView(texto);
        }

        tabla.addView(fila);
        filas.add(fila);

        FILAS++;
    }

    private int obtenerAnchoPixelesTexto(String texto)
    {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(45);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }

}
