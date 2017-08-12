package experiments.directorytree.threads;

import experiments.directorytree.factories.FileSystemTreeViewFactory;
import experiments.directorytree.tree_views.FileSystemTreeView;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by tfisher on 10/08/2017.
 */

//https://docs.oracle.com/javase/7/docs/api/java/nio/file/WatchKey.html
//http://howtodoinjava.com/java-8/java-8-watchservice-api-tutorial/
public class FileSystemWatcher implements Runnable {
    public static int POLL_TIMEOUT_SECONDS = 60;

    private enum State { NOT_STARTED, WATCHING, FINISHED }
    private volatile State state;
    private Thread thread;
    private final String threadName;
    private FileSystemTreeView fileSystemTreeView;

    public FileSystemWatcher(String name, FileSystemTreeView fileSystemTreeView)  {
        this.threadName = name;
        this.fileSystemTreeView = fileSystemTreeView;
        this.state = State.NOT_STARTED;
    }

    public State getState() {
        return state;
    }

    public void finish() {
        setState(State.FINISHED);
    }

    private void setState(State state) {
        this.state = state;
    }

    public boolean isFinished() {
        return state == State.FINISHED;
    }

    @Override
    public void run() {
        WatchService watchService = null;

        //create initial watchService
        System.out.println("Creating new watchService");
        try {
            watchService = createNewWatchService(fileSystemTreeView);
        } catch (IOException e) {
            System.out.println("Error ocurred initializing watchService. Thread exiting");
            return;
        }

        WatchKey watchKey = null;

        //set to WATCHING
        setState(State.WATCHING);

        System.out.println("Entered WATCHING State");
        while(!isFinished()) {
            try {
                if (watchService == null) {
                    setState(State.FINISHED);
                } else {
                    System.out.println("Triggered watchService.poll()");
                    //poll for 5 seconds and then exit
                    watchKey = watchService.poll(POLL_TIMEOUT_SECONDS, TimeUnit.SECONDS);

                    if (watchKey == null) {
                        System.out.println("No events on queue");
                    } else {
                        System.out.println("Triggered watchService.pollEvents()");
                        List<WatchEvent<?>> events = watchKey.pollEvents();

                        //reset watch key so it is available for picking up events
                        System.out.println("Resetting watchKey");
                        watchKey.reset();

                        //efficient to update once, regardless of event count
                        System.out.println("queuing an update to the filesystem update");
                        queueAddFileSystemTreeViewUpdate();

                        //set a new watchService because the current one is now out of sync
                        System.out.println("Re-creating watchService");
                        watchService = createNewWatchService(fileSystemTreeView);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.toString());
                Writer result = new StringWriter();
                PrintWriter printWriter = new PrintWriter(result);
                e.printStackTrace(printWriter);
                System.out.println(result.toString());
            }
        }

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

    private WatchService createNewWatchService(FileSystemTreeView fileSystemTreeView) throws IOException {
        WatchService watchService;
        TreeItem<Path> rootTreeItem = fileSystemTreeView.getRoot();

        if (rootTreeItem == null) {
            watchService = null;
        } else {
            Path rootDir = rootTreeItem.getValue();
            List<Path> dirPaths = new ArrayList<Path>();
            appendSubDirectories(dirPaths, rootDir);
            watchService = rootDir.getFileSystem().newWatchService();

            //register all directories for change
            for (Path dirPath : dirPaths) {
                dirPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            }
        }

        return watchService;
    }

    private void queueAddFileSystemTreeViewUpdate() {
        Platform.runLater(() -> {
            FileSystemTreeViewFactory.refresh(fileSystemTreeView);
        });
    }

    private void appendSubDirectories(List<Path> subdirPaths, Path dirPath) {
        File directory = dirPath.toFile();

        if (directory.isDirectory()) {

            subdirPaths.add(dirPath);
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    Path subdirPath = file.toPath();
                    appendSubDirectories(subdirPaths, subdirPath);
                }
            }
        }
    }

    public void start() {
        if (thread == null) {
            this.thread = new Thread(this, threadName);
            //prevent this thread from blocking the jvm exiting when gui thread ends
            thread.setDaemon(true);
            thread.start();
        }
    }

    public Thread getThread() {
        return thread;
    }

}