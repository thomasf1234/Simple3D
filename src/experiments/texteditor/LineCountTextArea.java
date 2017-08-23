package experiments.texteditor;

import com.sun.deploy.util.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class LineCountTextArea extends TextArea {
    public static final String lineSeparator = String.format("%n");
    protected volatile int lineCount;
    protected List<String> lineNumbersList;
    protected TextArea lineCountDestTextArea;

    public LineCountTextArea() {
        super();
        this.lineCount = 1;
        this.lineNumbersList = new ArrayList<String>();
        lineNumbersList.add("1");
//        JavaCompiler

        textProperty().addListener(new ChangeListener<String>() {
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

                updateLineCountDestTextArea();
                setLineCount(newLineCount);
            }
        });
    }

    public int getLineCount() {
        return lineCount;
    }

    public String getLineNumbersText() {
        return StringUtils.join(lineNumbersList, lineSeparator);
    }

    public void setLineCountDestTextArea(TextArea lineCountDestTextArea) {
        this.lineCountDestTextArea = lineCountDestTextArea;
        updateLineCountDestTextArea();
    }

    protected void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    protected void updateLineCountDestTextArea() {
        double pos = lineCountDestTextArea.getScrollTop();
        int anchor = lineCountDestTextArea.getAnchor();
        int caret = lineCountDestTextArea.getCaretPosition();
        lineCountDestTextArea.setText(getLineNumbersText());
        lineCountDestTextArea.setScrollTop(pos);
        lineCountDestTextArea.selectRange(anchor, caret);
    }
}
