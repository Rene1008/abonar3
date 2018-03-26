package com.reneascanta.abonar;

import com.itextpdf.text.Paragraph;

import java.util.ArrayList;

/**
 * Created by Rene on 07/09/2017.
 */

public class Procesos {

    public String valor(String cantidad) {
        String dato = "";
        char aux[] = cantidad.toCharArray();

        boolean estado = true;
        String entero = "", decimal = "";
        for (int i = 0; i < aux.length; i++) {
            if (aux[i] == '.' || estado == false) {
                decimal = decimal + aux[i];
                estado = false;
            } else if (estado) {
                entero = entero + aux[i];
            }
        }

        // String valorIzq[] = aux[0].split("");
        String auxEntero[] = entero.split("");
        // String auxDecimal [] =  decimal.split("");


        int punto = 0;
        for (int i = auxEntero.length - 1; i >= 1; i--) {
            punto++;
            dato = auxEntero[i] + dato;
            if (punto == 3 && aux.length - 2 > 3) {
                dato = "," + dato;
            } else if (punto == 6 && aux.length - 2 > 6) {
                dato = "\'" + dato;
            } else if (punto == 9 && aux.length - 2 > 9) {
                dato = "\'" + dato;
            }
        }


        dato = dato + "" + decimal;

        return dato;
    }

    public double valorDecimal(String numero) {
        char n[] = numero.toCharArray();
        String aux = "";
        for (int i = 0; i < n.length; i++) {
            if (n[i] == '1' || n[i] == '2' || n[i] == '3' || n[i] == '4' || n[i] == '5'
                    || n[i] == '6' || n[i] == '7' || n[i] == '8' || n[i] == '9' || n[i] == '0' || n[i] == '.') {
                aux = aux + n[i];

            }
        }
        Double d = Double.parseDouble(aux);
        return d;

    }

    public String [] areglo(String cadena){
        ArrayList<String> lista =  new ArrayList<>();

        char frase [] =  cadena.toCharArray();
        String fraseIzq="", fraseParentesis="" ;
        int j = 0;
        for(int i = 0; i< frase.length; i++){
            if(frase[i] != '('){
                j=i;
                fraseIzq = fraseIzq+""+frase[i];
            }else {
                break;
            }
        }
        String fraseIzqAux []= fraseIzq.split(" ");
        for(int i = 0; i< fraseIzqAux.length; i++){
            if(!fraseIzqAux[i].equals("")){
                lista.add(fraseIzqAux[i]);
            }
        }


         for(int i = j+2; i<frase.length-1; i++){
            fraseParentesis = fraseParentesis+""+frase[i];
        }
        lista.add(fraseParentesis);
        String [] resp =  new String[5];


        if(lista.size() == 5){
            for(int i = 0 ; i<lista.size(); i++){
                resp[i]=lista.get(i).toString();
            }
            return  resp;
        }else   if(lista.size() > 5){
            String aux="";
            resp[0] = lista.get(0);
            resp[1] = lista.get(1);
            for(int k = 2 ; k < lista.size()-2 ; k++){
             aux = aux+lista.get(k)+" ";
            }
            resp[2] = aux;
            resp[3] = lista.get(lista.size()-2);
            resp[4] = lista.get(lista.size()-1);
        }
        return  resp;

    }



}
