package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * Esta es la clase Main
 * @author naomi
 */
public class Main {
    /**
     * Este es el metodo principal main
     * @param args Estos son los argumentos
     * @throws IOException Esto es la excepcion para errores
     */
    public static void main(String[] args) throws IOException {
            String[] clientes = {"Código cliente", "Nombre de la empresa", "Localidad", "Correo electrónico", "Nombre responsable"};


            String[][] data = {
                    {"101", "EcoTech Solutions", "Barcelona", "ecotech@example.com", "Carlos González"},
                    {"102", "InnovaSoft", "Sevilla", "innovasoft@example.com", "Marta López"},
                    {"103", "GlobalTech", "Valencia", "globaltech@example.com", "Andrés Martínez"},
                    {"104", "ElectroMasters", "Madrid", "electromasters@example.com", "Sofía Pérez"},
            };

            String template = "Hola %%5%%.\n" +
                    "\n" +
                    "Desde nuestra empresa sabemos que estáis empezando nuevos proyectos en %%2%% y queremos ayudarte a que tenga éxito en estos tiempos difíciles.\n" +
                    "\n" +
                    "Para ello, vamos a compartir tu correo %%3%% con nuestro departamento técnico en %%3%% para que se pongan en contacto y podamos realizar una propuesta conjunta.\n" +
                    "\n" +
                    "Un saludo.";

            Map<String, String> marcadores = new HashMap<>();
            marcadores.put("%%5%%", "Nombre responsable");
            marcadores.put("%%2%%", "Localidad");
            marcadores.put("%%3%%", "Correo electrónico");

            File outputFolder = new File("salida");
            if (outputFolder.exists()) {
                File[] files = outputFolder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        try{
                            if (file.isFile()) {
                                if (!file.delete()) {
                                    System.err.println("No se pudo eliminar el archivo: " + file.getName());
                                }
                            }
                        } catch (SecurityException e) {
                            System.err.println("Error al eliminar el archivo: " + file.getName());
                        }
                    }
                }
            } else {
                outputFolder.mkdirs();
            }

            FileWriter writer = new FileWriter("data.csv");

            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

            printer.printRecord(clientes);


        for (String[] row : data) {
            if (row.length != clientes.length) {
                System.out.println("Error: Faltan datos en el archivo CSV para la fila con código de cliente " + row[0]);
                continue;
            }

            String output = template;
            for (String marker : marcadores.keySet()) {
                if (output.contains(marker)) {
                    String replacement = row[Arrays.asList(clientes).indexOf(marcadores.get(marker))];
                    if (replacement != null) {
                        output = output.replace(marker, replacement);
                    } else {
                        System.out.println("Dato faltante para marcador: " + marker);
                    }
                } else {
                    System.out.println("Marcador inválido: " + marker);
                }
            }
                String fileName = "template-" + row[0] + ".txt";
                FileWriter writer1 = new FileWriter(new File(outputFolder, fileName));
                writer1.write(output);
                writer1.close();
            }

            for (String[] row : data) {
                printer.printRecord(row);
            }
            printer.close();
    }
}