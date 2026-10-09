package com.wuhan.dict.server;

import com.wuhan.dict.common.bean.DicBean;

import java.io.*;
import java.util.Map;

// Utility class for saving and loading maps to/from files
public class DicTools {

    // Method to save a map to a file
    public static void saveMapToFile(Map<String, DicBean> map, String fileName) throws IOException {
        // Use try-with-resources to automatically close the output stream
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(map); // Write the map object to the output stream
        }
    }

    // Method to load a map from a file
    public static Map<String, DicBean> loadMapFromFile(String fileName) throws IOException, ClassNotFoundException {
        // Use try-with-resources to automatically close the input stream
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Map<String, DicBean>) in.readObject(); // Read the map object from the input stream and cast it to the appropriate type
        }
    }
}
