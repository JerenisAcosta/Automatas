/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplicacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author acost
 */
public class sintaxis {

    public static void main(String[] args) {
        String nombreArchivo = "src\\main\\java\\archivos\\tablaTokens.txt";
        int cuartoNumeroAnterior = -1;
        List<Integer> numerosEncontrados = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(" ");
                int cuartoNumero = Integer.parseInt(partes[3]);

                if (cuartoNumeroAnterior == -1) {
                    cuartoNumeroAnterior = cuartoNumero;
                }

                if (cuartoNumero == cuartoNumeroAnterior) {
                    int segundoNumero = Integer.parseInt(partes[3]);
                    int primerNumero = Integer.parseInt(partes[1]);
                    numerosEncontrados.add(primerNumero);
                    //numerosEncontrados.add(segundoNumero);
                } else {
                    for (int numero : numerosEncontrados) {
                        System.out.println(numero);
                    }

                    boolean resultado = variables(numerosEncontrados.toArray(new Integer[0]));
                    if (resultado) {
                        cuartoNumeroAnterior = cuartoNumero;
                        numerosEncontrados.clear();
                        int segundoNumero = Integer.parseInt(partes[3]);
                        int primerNumero = Integer.parseInt(partes[1]);
                        numerosEncontrados.add(primerNumero);
                        //numerosEncontrados.add(segundoNumero);
                    } else {
                        break;
                    }
                }
            }

            // Procesar el último grupo de números si hay algún número restante
            if (!numerosEncontrados.isEmpty()) {
                for (int numero : numerosEncontrados) {
                    System.out.println(numero);
                }

                boolean resultado = variables(numerosEncontrados.toArray(new Integer[0]));
                if (!resultado) {
                    System.out.println("El método procesarNumeros devolvió false para el último grupo de números.");
                } else{
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean variables(Integer[] tokens) {
        if (tokens == null || tokens.length == 0) { //IDENTIFICAMOS SI ES UNA LINEA VACIA
            return true;
        }

        int i = 0;

        if (tokens[i] != -15) { //IDENTIFICAMOS SI ES LA PALABRA VAR
            return false;
        }
        System.out.println(tokens[i]);
        i++;
        System.out.println(tokens[i]);
        boolean repetir = true;
        while (repetir) {
            if (!id(tokens[i])) { //IDENTIFICAMOS ID
                return false;
            }

            i++;

            if (tokens[i] == -34) { //SI ES UN [ 
                i++;

                if (!constante(tokens[i])) { //IDENTIFICAMOS CONSTANTE
                    return false;
                }

                i++;

                while (tokens[i] == -36) { //SI ES UNA , COMPROBAMOS QUE LO SIGUIENTE SEA UNA CONSTANTE 
                    i++;
                    if (!constante(tokens[i])) {
                        return false;
                    }
                    i++;
                }

                if (tokens[i] != -35) { //IDENTIFICAMOS ]
                    return false;
                }
            }

            if (tokens[i] != -36) { //SI NO ES UNA ,
                repetir = false;
            }

            //i++;
        }
        System.out.println(tokens[i]);
        return tokens[i] == -33; //COMPROBAMOS QUE EL FINAL DE LA LINEA SEA UN ;
    }

    public static boolean id(int token) {
        return token == -42 || token == -43 || token == -44;
    }

    public static boolean constante(int token) {
        return token == -45 || token == -46 || token == -47;
    }

}
