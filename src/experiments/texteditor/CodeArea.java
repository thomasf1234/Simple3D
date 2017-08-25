package experiments.texteditor;

import com.sun.deploy.util.StringUtils;
import experiments.directorytree.utils.FileUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CodeArea extends HBox {

    public static final String fxmlPath = "/experiments/texteditor/custom_control/code_area/root.fxml";

    @FXML public UnfocusableTextArea lineNumbersTextArea;
    @FXML public TextArea codeTextArea;

    protected volatile int lineCount;
    protected List<String> lineNumbersList;
    protected File openFile;

    public CodeArea() {
        loadFXML();
        init();
        updateLineNumbersTextArea();
    }

    public int getLineCount() {
        return lineCount;
    }

    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set(value);
    }

    public boolean hasOpenFile() { return openFile != null; }

    public File getOpenFile() {
        return openFile;
    }

    public synchronized void openFile(File file) throws IOException {
        codeTextArea.clear();
        String fileContents = FileUtils.read(file);
        setText(fileContents);
        this.openFile = file;
    }

    public StringProperty textProperty() {
        return codeTextArea.textProperty();
    }

    public Font getFont() {
        return fontProperty().get();
    }

    public void setFont(Font font) {
        fontProperty().set(font);
    }

    public ObjectProperty<Font> fontProperty() {
        return codeTextArea.fontProperty();
    }

    public boolean isUndoable() {
        return codeTextArea.isUndoable();
    }

    public void undo() {
        if (isUndoable()) {
            codeTextArea.undo();
        }
    }

    protected void init() {
        this.lineCount = 1;
        this.lineNumbersList = new ArrayList<String>();
        lineNumbersList.add("1");

        //scrolling codeTextArea will update lineNumbersTextArea
        codeTextArea.scrollTopProperty().bindBidirectional(lineNumbersTextArea.scrollTopProperty());
        codeTextArea.fontProperty().bindBidirectional(lineNumbersTextArea.fontProperty());

        codeTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                int oldLineCount = lineCount;
                int newLineCount = (newValue + " ").split(String.format("%n")).length;

                if (oldLineCount < newLineCount) {
                    for (int i=oldLineCount; i<newLineCount; ++i) {
                        lineNumbersList.add(String.format("%s" , i+1));
                    }
                } else if (oldLineCount > newLineCount){
                    for (int i=newLineCount; i<oldLineCount; ++i) {
                        int lastIndex = lineNumbersList.size() - 1;
                        lineNumbersList.remove(lastIndex);
                    }
                } else {
                    //same size
                }

                updateLineNumbersTextArea();
                setLineCount(newLineCount);
            }
        });
    }

    protected void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        // Path to the FXML File
        URL fxmlDocPath = getClass().getResource(fxmlPath);
        fxmlLoader.setLocation(fxmlDocPath);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    protected String getLineNumbersText() {
        return StringUtils.join(lineNumbersList, FileUtils.lineSeparator);
    }

    protected void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    protected void updateLineNumbersTextArea() {
        double pos = lineNumbersTextArea.getScrollTop();
        int anchor = lineNumbersTextArea.getAnchor();
        int caret = lineNumbersTextArea.getCaretPosition();
        lineNumbersTextArea.setText(getLineNumbersText());
        lineNumbersTextArea.setScrollTop(pos);
        lineNumbersTextArea.selectRange(anchor, caret);
    }
}
