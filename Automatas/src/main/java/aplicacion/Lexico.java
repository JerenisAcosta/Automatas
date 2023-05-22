/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package aplicacion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Lexico {

    static final HashMap<String, Integer> palabrasClaves = new HashMap<>();

    //TOKENS
    static {
        //PALABRAS RESERVADAS
        palabrasClaves.put("programa", -1);
        palabrasClaves.put("inicio", -2);
        palabrasClaves.put("fin", -3);
        palabrasClaves.put("leer", -4);
        palabrasClaves.put("escribir", -5);
        palabrasClaves.put("si", -6);
        palabrasClaves.put("sino", -7);
        palabrasClaves.put("mientras", -8);
        palabrasClaves.put("repetir", -9);
        palabrasClaves.put("hasta", -10);
        palabrasClaves.put("limpiar", -11);
        palabrasClaves.put("ejecutar", -12);
        palabrasClaves.put("posxy", -13);
        palabrasClaves.put("proc", -14);
        palabrasClaves.put("var", -15);
        palabrasClaves.put("encaso", -16);
        palabrasClaves.put("valor", -17);

        //OPERADORES ARITMETICOS
        palabrasClaves.put("+", -18);
        palabrasClaves.put("-", -19);
        palabrasClaves.put("*", -20);
        palabrasClaves.put("/", -21);
        palabrasClaves.put("%", -22);
        palabrasClaves.put("=", -23);

        //OPERADORES RELACIONALES
        palabrasClaves.put("<", -24);
        palabrasClaves.put("<=", -25);
        palabrasClaves.put(">", -26);
        palabrasClaves.put(">=", -27);
        palabrasClaves.put("==", -28);
        palabrasClaves.put("!=", -29);

        //OPERADORES LOGICOS
        palabrasClaves.put("!", -30);
        palabrasClaves.put("&&", -31);
        palabrasClaves.put("||", -32);

        //CARACTERES
        palabrasClaves.put(";", -33);
        palabrasClaves.put("[", -34);
        palabrasClaves.put("]", -35);
        palabrasClaves.put(",", -36);
        palabrasClaves.put(":", -37);
        palabrasClaves.put("(", -38);
        palabrasClaves.put(")", -39);
        palabrasClaves.put("{", -40);
        palabrasClaves.put("}", -41);

        //IDENTIFICADORES
        //VALORES REALES -42
        //ENTEROS -43
        //RUTINAS -44
        //CONSTANTES
        //REALES -45
        //ENTEROS -46
        //STRINGS -47
    }

    public static void main(String[] args) {
        String codigo = "src\\main\\java\\archivos\\codigo.txt";
        String tablaTokens = "src\\main\\java\\archivos\\tablaTokens.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(codigo)); BufferedWriter bw = new BufferedWriter(new FileWriter(tablaTokens))) {

            String linea; //LINEA LEIDA
            int numeroLinea = 1;

            while ((linea = br.readLine()) != null) {
                if (!(linea.isBlank() || linea.isEmpty())) {
                    String[] tokens = new String[1];

                    //METODO PARA ANALIZAR LA LINEA DE CODIGO
                    if (!linea.startsWith("//")) {
                        tokens = obtenerTokens(linea, numeroLinea);
                        for (String tabla : tokens) {
                            //ESCRIBIR EN LA TABLA DE TOKENS
                            bw.write(tabla);
                            bw.newLine();
                            System.out.println(tabla);
                        }
                    }
                }
                numeroLinea++;
            }

            System.out.println("Archivo leído y contenido guardado correctamente en el archivo de destino.");
            bw.close();

        } catch (IOException e) {
            System.out.println("Error al leer o guardar el archivo: " + e.getMessage());
        }
    }

    private static String[] obtenerTokens(String linea, int numeroLinea) {
        String[] temporal = linea.split(" ");
        String[] palabras = lineaSeparada(temporal);
        String[] tokens = new String[palabras.length];

        for (int i = 0; i < palabras.length; i++) {
            Integer token = palabraClaveuOperador(palabras[i]);

            if (token != null) {
                tokens[i] = palabras[i] + " " + token.toString() + " -1 " + numeroLinea;
            } else {
                if (identificador(palabras[i]) != null) {
                    tokens[i] = palabras[i] + " " + identificador(palabras[i]).toString() + " -2 " + numeroLinea;
                } else {
                    if (constante(palabras[i]) != null) {
                        tokens[i] = palabras[i] + " " + constante(palabras[i]).toString() + " -1 " + numeroLinea;
                    }
                }
            }
        }

        return tokens;
    }

    public static String[] lineaSeparada(String[] temporal) {
        // Transformar a minúsculas
        for (int i = 0; i < temporal.length; i++) {
            temporal[i] = temporal[i].toLowerCase();
        }

        // Verificar si la última palabra termina con punto y coma
        String ultimaPalabra = temporal[temporal.length - 1];
        boolean ultimaPalabraConPuntoComa = ultimaPalabra.endsWith(";");

        // Calcular el tamaño del nuevo array considerando la última palabra
        int nuevoTamanio = temporal.length;
        if (ultimaPalabraConPuntoComa) {
            nuevoTamanio++;
        }

        // Crear un nuevo array para almacenar el contenido modificado
        String[] nuevoArray = new String[nuevoTamanio];

        // Copiar todos los elementos de "temporal" al nuevo array
        System.arraycopy(temporal, 0, nuevoArray, 0, temporal.length);

        // Guardar la última palabra y el punto y coma en el nuevo array, si es necesario
        if (ultimaPalabraConPuntoComa) {
            String palabraSinPuntoComa = ultimaPalabra.substring(0, ultimaPalabra.length() - 1);
            nuevoArray[temporal.length - 1] = palabraSinPuntoComa;
            nuevoArray[temporal.length] = ";";
        }
        return nuevoArray;
    }

    public static Integer palabraClaveuOperador(String palabra) {
        return palabrasClaves.get(palabra);
    }

    public static Integer identificador(String palabra) {
        if (palabra.endsWith("@")) {
            return -44;
        }

        if (palabra.endsWith("&")) {
            return -43;
        }

        if (palabra.endsWith("%")) {
            return -42;
        }

        return null;
    }

    public static Integer constante(String palabra) {
        if (entero(palabra) != null) {
            return entero(palabra);
        }
        if (real(palabra) != null) {
            return real(palabra);
        }
        if (string(palabra) != null) {
            return string(palabra);
        }
        return null;
    }

    public static Integer entero(String palabra) {

        try {
            short rango = Short.parseShort(palabra.substring(0, palabra.length() - 1));
            return -46;
        } catch (NumberFormatException e) {
            real(palabra);
        }
        return null;
    }

    public static Integer real(String palabra) {
        if (palabra.startsWith(".")) {
            palabra = palabra.substring(1);
            if (palabra.contains(".")) {
                return null;
            }
            return -45;
        }

        try {
            Double.parseDouble(palabra.substring(0, palabra.length() - 1));
            return -45;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer string(String palabra) {
        if (palabra.startsWith("\"") && palabra.endsWith("\"")) {
            return -47;
        }
        return null;
    }

}
