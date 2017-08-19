package experiments.directorytree.singletons;

import java.io.IOException;

public abstract class SLoader {
    private enum State { NOT_LOADED, LOADED }

    protected volatile State state;

    public SLoader() {
        this.state = State.NOT_LOADED;
    }

    public State getState() {
        return state;
    }

    public boolean isLoaded() {
        return state == State.LOADED;
    }

    public void ensureLoaded() throws IOException {
        if (!isLoaded()) {
            load();
            this.state = State.LOADED;
        }
    }

    protected abstract void load() throws IOException;
}
