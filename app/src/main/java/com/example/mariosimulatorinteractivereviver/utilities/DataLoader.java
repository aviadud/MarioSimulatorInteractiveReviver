package com.example.mariosimulatorinteractivereviver.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class DataLoader {
    private static final int BUFFER_SIZE = 1024;

    public static String JsonFilePathToString(InputStream inputStream) throws IOException{
        Writer writer = new StringWriter();
        char[] buffer = new char[BUFFER_SIZE];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            inputStream.close();
        }
        return writer.toString();
    }




}
