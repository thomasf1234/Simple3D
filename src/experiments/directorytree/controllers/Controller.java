package experiments.directorytree.controllers;

import experiments.directorytree.singletons.SLogger;

public abstract class Controller {
    public void initialize() {
        String logMessage = String.format("%s initializing", getClassName());
        SLogger.getInstance().log(logMessage);
    }

    protected String getClassName() {
        return this.getClass().getName();
    }
}
