package experiments.directorytree.utils;

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
public class FileUtils {
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
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            String alertMessage = String.format("Successfully created project at '%s'.", file.getAbsolutePath());
//            alert.setHeaderText("Success");
//            alert.setContentText(alertMessage);
//
//            alert.showAndWait();
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

    /**
     * Checks, whether the child directory is a subdirectory of the base
     * directory.
     *
     * @param base the base directory.
     * @param child the suspected child directory.
     * @return true, if the child is a subdirectory of the base directory.
     * @throws IOException if an IOError occured during the test.
     */
    public static boolean isSubDirectory(File base, File child)
            throws IOException {
        base = base.getCanonicalFile();
        child = child.getCanonicalFile();

        File parentFile = child;
        while (parentFile != null) {
            if (base.equals(parentFile)) {
                return true;
            }
            parentFile = parentFile.getParentFile();
        }
        return false;
    }

    public static boolean isValidFileMove(File sourceFile, File destDir) throws IOException {
        boolean isValid = false;

        if (sourceFile != null &&
                destDir != null &&
                sourceFile.exists() &&
                destDir.exists()) {

            File sourceParentFile = sourceFile.getParentFile();
            if (destDir.isDirectory() &&
                    !sourceFile.equals(destDir) &&
                    sourceParentFile != null &&
                    !destDir.equals(sourceParentFile)
                    ) {
                if (sourceFile.isDirectory()) {
                    if (!FileUtils.isSubDirectory(sourceFile, destDir)) {
                        isValid = true;
                    }
                } else {
                    isValid = true;
                }
            }
        } else {
            throw new IllegalArgumentException("source and destDir must exist.");
        }

        return isValid;
    }
}
