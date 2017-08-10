package experiments.directorytree.threads;

import experiments.directorytree.factories.FileSystemTreeViewFactory;
import experiments.directorytree.tree_views.FileSystemTreeView;
import javafx.application.Platform;

import java.nio.file.*;
import java.util.List;

/**
 * Created by tfisher on 10/08/2017.
 */

public class FileSystemWatcher implements Runnable {

    private Thread thread;
    private final String threadName;
    private FileSystemTreeView fileSystemTreeView;

    public FileSystemWatcher(String name, FileSystemTreeView fileSystemTreeView)  {
        this.threadName = name;
        this.fileSystemTreeView = fileSystemTreeView;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Path myDir = fileSystemTreeView.getRoot().getValue();
                WatchService watcher = myDir.getFileSystem().newWatchService();
                myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

                WatchKey watckKey = watcher.take();

                List<WatchEvent<?>> events = watckKey.pollEvents();
                for (WatchEvent event : events) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE ||
                            event.kind() == StandardWatchEventKinds.ENTRY_DELETE ||
                            event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                        updateFileSystemTreeView();
                    }
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.toString());
            }
        }
    }

    private void updateFileSystemTreeView() {
        Platform.runLater(() -> {
            FileSystemTreeViewFactory.refresh(fileSystemTreeView);
        });
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