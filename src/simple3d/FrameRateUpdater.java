package simple3d;

import javafx.animation.AnimationTimer;
import javafx.scene.text.Text;

/**
 * Created by tfisher on 06/03/2017.
 */
public class FrameRateUpdater extends AnimationTimer {
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0 ;
    private boolean arrayFilled = false ;
    private double fps=0;
    private Text text;
    private static final double ONE_SECOND_IN_NANO_SECONDS = 1_000_000_000.0;

    public FrameRateUpdater(Text text) {
        this.text = text;
    }

    @Override
    public void handle(long now) {
        long oldFrameTime = frameTimes[frameTimeIndex] ;
        frameTimes[frameTimeIndex] = now ;
        frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length ;
        if (frameTimeIndex == 0) {
            arrayFilled = true ;
        }
        if (arrayFilled) {
            long elapsedNanos = now - oldFrameTime ;
            long elapsedNanosPerFrame = elapsedNanos / frameTimes.length ;
            fps= ONE_SECOND_IN_NANO_SECONDS / elapsedNanosPerFrame ;
            text.setText(String.format("Current frame rate: %.3f", fps));
        }
    }

    public double getFps() {
        return fps;
    }
}
