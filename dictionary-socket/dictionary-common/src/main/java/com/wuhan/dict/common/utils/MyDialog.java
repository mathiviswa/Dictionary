package com.wuhan.dict.common.utils;

import javax.swing.*;
import java.awt.*;

// Utility class for displaying warning dialogs
public class MyDialog {

    // Method to display a warning dialog with a specified message
    public static void warning(Component component, String msg) {
        // Show the message dialog with the specified message, titled "Warning", and using the warning icon
        JOptionPane.showMessageDialog(component, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
