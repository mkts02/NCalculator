package com.herrera.cristobal.ncalculator;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class MainActivity extends AppCompatActivity {


    private RelativeLayout layout;
    private TableLayout tablePruebas;
    private TableLayout tableEvas;
    private TextView presentacion;
    private TextView setPromedio;
    private EditText examen;
    private RadioGroup rg;
    private RadioButton r1,r2,r3,r4;
    private Button calPresentacionButton;
    private Button calExamenButton;
    private FloatingActionButton autorb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ocultar titulo de la barra superior
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");


        layout = (RelativeLayout) findViewById(R.id.relativeLayout1);

        presentacion = (TextView) findViewById(R.id.presentacion);
        setPromedio = (TextView) findViewById(R.id.promedio);

        tablePruebas = (TableLayout) findViewById(R.id.tpruebas);
        tableEvas = (TableLayout) findViewById(R.id.tevas);

        calPresentacionButton = (Button) findViewById(R.id.button);
        calExamenButton = (Button) findViewById(R.id.button2);

        examen = (EditText) findViewById(R.id.examen);

        rg = (RadioGroup) findViewById(R.id.radioGroup);

        r1 = (RadioButton) findViewById(R.id.rd1);
        r2 = (RadioButton) findViewById(R.id.rd2);
        r3 = (RadioButton) findViewById(R.id.rd3);
        r4 = (RadioButton) findViewById(R.id.rd4);

        autorb = (FloatingActionButton) findViewById(R.id.autor);



        createPruebas();
        createEvas(5);
        validarCaracteres();
        examen.setEnabled(false);

        autorb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToAutor();
            }
        });


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int botonid = rg.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) findViewById(botonid);
                int selectedB = parseInt(selectedRadioButton.getText().toString());
                int cantidad = 0;

                switch (selectedB) {
                    case 1:
                        cantidad=2;
                        break;
                    case 2:
                        cantidad=3;
                        break;
                    case 3:
                        cantidad=4;
                        break;
                    case 4:
                        cantidad=5;
                        break;

                }

                createEvas(cantidad);

            }
        });
    }

    public void calcularPresentacion(View v){

        presentacion.setText("");
        setPromedio.setText("");

        if(validarVacios()==0){
            double promedio = calSolemnes()+calEvas();
            if(!validarRango()) {
                calEximision(calPresentacion(promedio));
            }else{
                Toast.makeText(this, "Notas invalidas rango(10-70)", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void calcularExamen(View v){

        if(validarVacios()==0){
            if(!validarRango()) {
                calPromedioFinal();
            }else{
                Toast.makeText(this, "Notas invalidas rango(10-70)", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void createEvas(int cantidad) {

        tableEvas.removeAllViews();

        for (int i = 1; i < cantidad; i++) {
            TableRow rows = new TableRow(this);
            TextView tw = new TextView(this);
            tw.setTextSize(18);
            tw.setText("EVA"+i);
            tw.setGravity(Gravity.CENTER);
            tw.setTextSize(getResources().getDimension(R.dimen.textsize));
            rows.addView(tw);
            EditText edt = new EditText(this);
            int maxLength = 2;
            String eva="eva"+i;
            edt.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});// limitar caracteres
            edt.setInputType(InputType.TYPE_CLASS_NUMBER); // solo admitir numeros
            edt.setEms(2);
            edt.setGravity(Gravity.CENTER);
            edt.setTag(eva);
            rows.addView(edt);
            tableEvas.addView(rows);
        }

    }

    public void createPruebas() {

        for (int i = 1; i < 5; i++) {


            TableRow rows = new TableRow(this);
            TextView tw = new TextView(this);
            tw.setTextSize(18);
            tw.setGravity(Gravity.CENTER);
            tw.setTextSize(getResources().getDimension(R.dimen.textsize));
            rows.addView(tw);
            EditText edt = new EditText(this);
            int maxLength = 2;
            edt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});// limitar caracteres
            edt.setInputType(InputType.TYPE_CLASS_NUMBER); // solo admitir numeros
            edt.setEms(2);
            edt.setGravity(Gravity.CENTER);
            //Log.d("CUENTA", ""+i);
            switch (i) {
                case 1:
                    tw.setText("EPR1");
                    edt.setTag("EPR1");
                    break;
                case 2:
                    tw.setText("EPE1");
                    edt.setTag("EPE1");
                    break;
                case 3:
                    tw.setText("EPR2");
                    edt.setTag("EPR2");
                    break;
                case 4:
                    tw.setText("EPE2");
                    edt.setTag("EPE2");
                    break;

            }

            rows.addView(edt);
            tablePruebas.addView(rows);
        }

    }

    public int validarVacios() {

        int vacios = 0;

        for(int j =0; j < layout.getChildCount(); j++) {

            View elementosLayout = layout.getChildAt(j);

            if (elementosLayout instanceof TableLayout) {

                TableLayout tablas = (TableLayout) elementosLayout;

                for (int i = 0; i < tablas.getChildCount(); i++) {
                    View child = tablas.getChildAt(i);

                    if (child instanceof TableRow) {
                        TableRow row = (TableRow) child;

                        for (int x = 0; x < row.getChildCount(); x++) {
                            View edts = row.getChildAt(x);
                            if (edts instanceof EditText && edts.isEnabled()) {
                                EditText edtValue = ((EditText) edts);
                                String edtValue2 = edtValue.getText().toString();

                                if (edtValue.getText().length() < 1) {
                                    edtValue.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorDanger), PorterDuff.Mode.SRC_ATOP);
                                    vacios += 1;

                                }

                            }
                        }
                    }

                }
            }
        }

        if(vacios>0)
        {
            Toast.makeText(this,"Rellenar campos vacios",Toast.LENGTH_SHORT).show();

        }

        return vacios;
    }

    public void calEximision(double promedio){

        int contaRojos=0;

        for(int j =0; j < layout.getChildCount(); j++) {

            View elementosLayout = layout.getChildAt(j);

            if (elementosLayout instanceof TableLayout) {

                TableLayout tablas = (TableLayout) elementosLayout;

                for (int i = 0; i < tablas.getChildCount(); i++) {
                    View child = tablas.getChildAt(i);

                    if (child instanceof TableRow) {
                        TableRow row = (TableRow) child;

                        for (int x = 0; x < row.getChildCount(); x++) {
                            View edts = row.getChildAt(x);
                            if (edts instanceof EditText && edts.isEnabled()) {
                                EditText edtValue = ((EditText) edts);
                                String edtValue2 = edtValue.getText().toString();
                                String tagName = edtValue.getTag().toString();
                                try{
                                    double notas = parseDouble(edtValue2)/10;
                                    //Log.d(tagName," "+notas);
                                    if(tagName.toLowerCase().contains("ep")){
                                        if(notas>=4.0 && promedio>5.5 ){
                                        }else{
                                            contaRojos =+1;
                                        }
                                    }
                                }catch (NumberFormatException e){
                                      // Log.e("FATAL ERROR: ","ESTADO ALUMNO "+e);
                                }

                            }
                        }
                    }

                }
            }
        }
        if(contaRojos==0) {
            Toast.makeText(this, "Felicidades te eximiste", Toast.LENGTH_SHORT).show();
            setPromedio.setText(Double.toString(promedio));
            setPromedio.setTextColor(getResources().getColor(R.color.colorVerde));
            calExamenButton.setEnabled(false);
        }else{
            Toast.makeText(this, "Debes dar examen", Toast.LENGTH_SHORT).show();
            bloquearCasillas();

        }


    }

    public boolean validarRango() {

        boolean error = false;

        for(int j =0; j < layout.getChildCount(); j++) {

            View elementosLayout = layout.getChildAt(j);

            if (elementosLayout instanceof TableLayout) {

                TableLayout tablas = (TableLayout) elementosLayout;

                for (int i = 0; i < tablas.getChildCount(); i++) {
                    View child = tablas.getChildAt(i);

                    if (child instanceof TableRow) {
                        TableRow row = (TableRow) child;

                        for (int x = 0; x < row.getChildCount(); x++) {
                            View edts = row.getChildAt(x);
                            if (edts instanceof EditText && edts.isEnabled()) {
                                EditText edtValue = ((EditText) edts);
                                String edtValue2 = edtValue.getText().toString();
                                if (edtValue.getText().length() > 1) {

                                    edtValue.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

                                }
                                try {
                                    if ((parseInt(edtValue.getText().toString()) < 10 || (parseInt(edtValue.getText().toString()) > 70))) {
                                        edtValue.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorDanger), PorterDuff.Mode.SRC_ATOP);
                                        edtValue.setText("");
                                        error = true;
                                    }
                                }catch (NumberFormatException e){
                                    error = true;
                                }


                            }
                        }
                    }

                }
            }
        }
        return error;

    }

    public void validarCaracteres() {


        layout = (RelativeLayout) findViewById(R.id.relativeLayout1);

        for (int j = 0; j < layout.getChildCount(); j++) {

            View elementosLayout = layout.getChildAt(j);

            if (elementosLayout instanceof TableLayout) {

                TableLayout tablas = (TableLayout) elementosLayout;

                for (int i = 0; i < tablas.getChildCount(); i++) {
                    View child = tablas.getChildAt(i);

                    if (child instanceof TableRow) {
                        TableRow row = (TableRow) child;

                        for (int x = 0; x < row.getChildCount(); x++) {
                            View edts = row.getChildAt(x);
                            if (edts instanceof EditText && edts.isEnabled()) {
                                final EditText edtValue = ((EditText) edts);

                                edtValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View v, boolean hasFocus) {
                                        if (!hasFocus && edtValue.getText().toString().length()==1) {
                                            edtValue.append("0");

                                        }
                                    }
                                });


                            }
                        }
                    }

                }
            }

        }
    }

    public double calSolemnes() {

        double promedio = 0;
        boolean error = false;
        for (int i = 0; i < tablePruebas.getChildCount(); i++) {
            View child = tablePruebas.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;

                for (int x = 0; x < row.getChildCount(); x++) {
                    View edts = row.getChildAt(x);
                    if(edts instanceof EditText){
                        EditText edtValue = ((EditText) edts);
                        String edTexto = edtValue.getText().toString();
                        String tagName = edtValue.getTag().toString();

                        if (edTexto.length() == 1 || edTexto.length() > 1) {

                            edtValue.append("0");
                            String edtexto2 = edtValue.getText().toString();
                            //Log.d("DEBUG NOTAS1: ",""+edtexto2);
                            double notas = parseDouble(edtexto2);

                            if(edtexto2.length()==2)
                            {
                                notas=notas/10;
                                // Log.d("DEBUG NOTAS1: ",""+notas);
                            }

                            switch (tagName) {
                                case "EPR1":
                                    notas = (double) notas * 10 / 100;
                                    //  Log.d("epr1: ", "" + notas);
                                    promedio += notas;
                                    break;
                                case "EPE1":
                                    notas = (double) notas * 15 / 100;
                                    // Log.d("epe1: ", "" + notas);
                                    promedio += notas;
                                    break;
                                case "EPR2":
                                    notas = (double) notas * 20 / 100;
                                    //  Log.d("epr2: ", " " + notas);
                                    promedio += notas;
                                    break;
                                case "EPE2":
                                    notas = (double) notas * 25 / 100;
                                    // Log.d("epe2: ", " " + notas);
                                    promedio += notas;
                                    break;


                            }

                        }else{

                            error = true;
                            //Log.d("ERROR: ","");
                        }


                    }

                }
            }
        }
        if(error==false){

            Log.d("PROMSOLEMNES: "," "+promedio);
        }
        else{
            promedio = 0;
        }

        //Log.d("SOLEMNES: ",""+promedio);
        return promedio;

    }

    public double calEvas() {

        double promEva = 0;
        double notas = 0;
        int cantidad = 0;
        boolean error = false;

        for (int i = 0; i < tableEvas.getChildCount(); i++) {
            View child = tableEvas.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;

                for (int x = 0; x < row.getChildCount(); x++) {
                    View edts = row.getChildAt(x);
                    if (edts instanceof EditText) {
                        EditText edtValue = ((EditText) edts);
                        String tagName = edtValue.getTag().toString();
                        String edTexto = edtValue.getText().toString();
                        if (edTexto.length() == 1 || edTexto.length() > 1) {

                            edtValue.append("0");
                            String edtexto2 = edtValue.getText().toString();
                            //Log.d("DEBUG NOTAS1: ",""+edtexto2);
                            notas = parseDouble(edtexto2);

                            if(edtexto2.length()==2)
                            {
                                notas=notas/10;
                                //Log.d("DEBUG NOTAS1: ",""+notas);
                            }
                            cantidad +=x;
                            //Log.d("CANTIDAD EVAS: ",""+cantidad);
                            promEva += notas;
                        }else{

                            error = true;
                            notas = 0;

                        }
                        //Log.d("DEBUGo", "44"+edtValue.getText());
                    }

                }
            }

        }
        if(!error)
        {
            promEva = ((promEva/cantidad)*30)/100;
            //Log.d("PROMEVA",""+promEva);
        }

        return promEva;
    }

    public void calPromedioFinal(){

        double promedio = Double.parseDouble(presentacion.getText().toString());
        double ex =   Double.parseDouble(examen.getText().toString());


        promedio = promedio*0.7;
        Log.d("presentacion: ",""+promedio);
        ex = (ex/10)*0.3;
        promedio = roundNumber(promedio+ex);

        Log.d("promedio: ",""+promedio);
        Log.d("examen: ",""+ex);


        if(promedio<4.0)
        {
            Toast.makeText(this,"Ramo reprobado",Toast.LENGTH_SHORT).show();
            setPromedio.setTextColor(getResources().getColor(R.color.colorDanger));
            setPromedio.setText(Double.toString(promedio));

        }else{

            Toast.makeText(this,"Enhorabuena, has aprobado :D",Toast.LENGTH_SHORT).show();
            setPromedio.setTextColor(getResources().getColor(R.color.colorAmarillo));
            setPromedio.setText(Double.toString(promedio));

        }




    }

    public double calPresentacion(double promediofinal){

        promediofinal = roundNumber(promediofinal);
        presentacion.setText(Double.toString(promediofinal));
        if(promediofinal>=5.5){

            presentacion.setTextColor(getResources().getColor(R.color.colorVerde));

        }else if(promediofinal<5.5 && promediofinal> 4.0){

            presentacion.setTextColor(getResources().getColor(R.color.colorAmarillo));


        }else{

            presentacion.setTextColor(getResources().getColor(R.color.colorDanger));

        }
        return  promediofinal;

    }

    public double roundNumber(double prom){
        long mult=(long)Math.pow(10,1);
        double promediofinal=(Math.round(prom*mult))/(double)mult;
        return promediofinal;

    }
    public void bloquearCasillas(){

        for(int j =0; j < layout.getChildCount(); j++) {

            View elementosLayout = layout.getChildAt(j);

            if (elementosLayout instanceof TableLayout) {

                TableLayout tablas = (TableLayout) elementosLayout;

                for (int i = 0; i < tablas.getChildCount(); i++) {
                    View child = tablas.getChildAt(i);

                    if (child instanceof TableRow) {
                        TableRow row = (TableRow) child;

                        for (int x = 0; x < row.getChildCount(); x++) {
                            View edts = row.getChildAt(x);
                            if (edts instanceof EditText && edts.isEnabled()) {
                                EditText edtValue = ((EditText) edts);
                                edtValue.setEnabled(false);

                            }
                        }
                    }

                }
            }

        }
        r1.setEnabled(false);
        r2.setEnabled(false);
        r3.setEnabled(false);
        r4.setEnabled(false);

        examen.setEnabled(true);
        calExamenButton.setEnabled(true);
        calPresentacionButton.setEnabled(false);


    }
    public void limpiar(View v){


            for(int j =0; j < layout.getChildCount(); j++) {

                View elementosLayout = layout.getChildAt(j);

                if (elementosLayout instanceof TableLayout) {

                    TableLayout tablas = (TableLayout) elementosLayout;

                    for (int i = 0; i < tablas.getChildCount(); i++) {
                        View child = tablas.getChildAt(i);

                        if (child instanceof TableRow) {
                            TableRow row = (TableRow) child;

                            for (int x = 0; x < row.getChildCount(); x++) {
                                View edts = row.getChildAt(x);
                                if (edts instanceof EditText) {
                                    EditText edtValue = ((EditText) edts);
                                    edtValue.setEnabled(true);
                                    edtValue.setText("");

                                }
                            }
                        }

                    }
                }

            }
            r1.setEnabled(true);
            r2.setEnabled(true);
            r3.setEnabled(true);
            r4.setEnabled(true);
            examen.setEnabled(false);
            calExamenButton.setEnabled(false);
            calPresentacionButton.setEnabled(true);
            presentacion.setText("");
            setPromedio.setText("");

    }

    public void goToAutor(){

        Intent i = new Intent(this, Autor.class);
        startActivity(i);

    }


}
