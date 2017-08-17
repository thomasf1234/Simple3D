package experiments.directorytree.threads;

import com.sun.nio.file.SensitivityWatchEventModifier;
import experiments.directorytree.singletons.SLogger;
import experiments.directorytree.factories.FileSystemTreeViewFactory;
import experiments.directorytree.tree_views.FileSystemTreeView;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by tfisher on 10/08/2017.
 */

//https://docs.oracle.com/javase/7/docs/api/java/nio/file/WatchKey.html
//http://howtodoinjava.com/java-8/java-8-watchservice-api-tutorial/
public class FileSystemWatcher implements Runnable {
    public static int POLL_TIMEOUT_SECONDS = 1;

    private enum State { WAITING, WATCHING, FINISHED }

    private Thread thread;
    private final String threadName;
    //must not be cached by threads
    private volatile State state;
    private FileSystemTreeView fileSystemTreeView;
    private volatile Path currentRootPath;
    private WatchService watchService;
    private WatchKey watchKey;

    public FileSystemWatcher(String name, FileSystemTreeView fileSystemTreeView)  {
        this.threadName = name;
        this.fileSystemTreeView = fileSystemTreeView;
        this.state = State.WAITING;
    }

    public State getState() {
        return state;
    }

    public void finish() {
        setState(State.FINISHED);
    }

    //cannot be called in parallel by threads
    private synchronized void setState(State state) {
        this.state = state;
    }

    public boolean isFinished() {
        return state == State.FINISHED;
    }

    @Override
    public void run() {
        try {
            while(!isFinished()) {
                switch (state) {
                    case WAITING:
                        onWaiting();
                        break;
                    case WATCHING:
                        onWatching();
                        break;
                    case FINISHED:
                        onFinished();
                        break;
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            cancelWatchService();
        }
    }

    //State WAITING
    private void onWaiting() {
        if (fileSystemTreeView.getRoot() == null) {
            try {
                SLogger.getInstance().log("fileSystemTreeView is null. Sleeping for 1s");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                SLogger.getInstance().log("New fileSystemTreeView root detected. State transfer to WATCHING");
                updateCurrentRootPath();
                updateWatchService();
                setState(State.WATCHING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //State WATCHING
    private void onWatching() {
        TreeItem<File> fsRootTreeItem = fileSystemTreeView.getRoot();
        if (fsRootTreeItem != null && Objects.equals(fsRootTreeItem.getValue().toString(), currentRootPath.toString())) {
            SLogger.getInstance().log("Triggered watchService.poll() 1s");
            //poll for 5 seconds and then exit
            try {
                watchKey = watchService.poll(POLL_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                setState(State.FINISHED);
            }

            //thread may have been set to State.FINISHED state during polling
            if (!isFinished()) {
                if (watchKey == null) {
                    SLogger.getInstance().log("No events on queue");
                } else {
                    SLogger.getInstance().log("Triggered watchService.pollEvents()");
                    List<WatchEvent<?>> events = watchKey.pollEvents();

                    //efficient to update once, regardless of event count
                    SLogger.getInstance().log("queuing an update to the filesystem update");
                    queueAddFileSystemTreeViewUpdate();

                    //set a new watchService because the current one is now out of sync
                    SLogger.getInstance().log("Re-creating watchService");
                    try {
                        updateWatchService();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            SLogger.getInstance().log("fileSystemTreeView root has been removed while WATCHING. State transfer to WAITING");
            setState(State.WAITING);
        }
    }

    //State FINISHED
    private void onFinished() {
        SLogger.getInstance().log("FileSystemWatcher finishing.");
        cancelWatchService();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void updateCurrentRootPath() {
        TreeItem<File> rootTreeItem = fileSystemTreeView.getRoot();
        File rootFile = rootTreeItem.getValue();

        this.currentRootPath = rootFile.toPath();
    }

    //used to ensure watchService with the filesystem directories registered for changes
    private void updateWatchService() throws IOException {
        SLogger.getInstance().log("Updating watchService");
        //cancel current watching
        cancelWatchService();

        WatchService newWatchService;
        TreeItem<File> rootTreeItem = fileSystemTreeView.getRoot();

        File rootDir = rootTreeItem.getValue();
        List<Path> dirPaths = new ArrayList<Path>();
        appendSubDirectories(dirPaths, rootDir);
        newWatchService = rootDir.toPath().getFileSystem().newWatchService();

        //register all directories for change
        for (Path dirPath : dirPaths) {
            WatchEvent.Kind<?>[] events = { StandardWatchEventKinds.ENTRY_CREATE,  StandardWatchEventKinds.ENTRY_MODIFY,  StandardWatchEventKinds.ENTRY_DELETE};
            //https://www.reddit.com/r/java/comments/3vtv8i/beware_javaniofilewatchservice_is_subtly_broken/
            dirPath.register(newWatchService, events, SensitivityWatchEventModifier.HIGH);
        }

        this.watchService = newWatchService;
    }

    private void cancelWatchService() {
        //ensure watchKey is cancelled
        if (watchKey != null) {
            watchKey.cancel();
        }

        //ensure watchService is closed
        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void queueAddFileSystemTreeViewUpdate() {
        Platform.runLater(() -> {
            FileSystemTreeViewFactory.refresh(fileSystemTreeView);
        });
    }

    private void appendSubDirectories(List<Path> subdirPaths, File directory) {
        if (directory.isDirectory()) {

            subdirPaths.add(directory.toPath());
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    File subdir = file;
                    appendSubDirectories(subdirPaths, subdir);
                }
            }
        }
    }

    public void start() {
        if (thread == null) {
            this.thread = new Thread(this, threadName);
            //prevent this thread from blocking the jvm exiting when gui thread ends
            //thread.setDaemon(true);
            thread.start();
        }
    }

    public Thread getThread() {
        return thread;
    }

}