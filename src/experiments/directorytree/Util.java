package experiments.directorytree;

import experiments.directorytree.exceptions.FileNotCreated;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tfisher on 07/08/2017.
 */
public class Util {
    public static void createFile(File file) throws IOException {
        createFile(file, false);
    }

    public static void createDirectory(File directory) throws IOException {
        createFile(directory, true);
    }

    private static void createFile(File file, boolean isDirectory) throws IOException {
        boolean successful;

        if (isDirectory == true) {
            successful = file.mkdir();
        } else {
            successful = file.createNewFile();
        }

        if (successful && file.exists()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            String alertMessage = String.format("Successfully created project at '%s'.", file.getAbsolutePath());
            alert.setHeaderText("Success");
            alert.setContentText(alertMessage);

            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            String errorMessage = String.format("Failed trying to create directory '%s'.", file.getAbsolutePath());
            alert.setHeaderText("Error");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            throw new FileNotCreated(file.getAbsolutePath());
        }

    }

    public static String getFileExtension(File file) {
        //default return null
        String extension = null;
        String fileName = file.getName();
        Pattern fileExtensionPattern = Pattern.compile("\\.[^\\.]+$");
        Matcher matcher = fileExtensionPattern.matcher(fileName);

        if (matcher.find()) {
            extension = matcher.group();
        }

        return extension;
    }

    public static String unixToPlatIndepPath(String path) {
        return path.replaceAll("/", File.separator);
    }
}
