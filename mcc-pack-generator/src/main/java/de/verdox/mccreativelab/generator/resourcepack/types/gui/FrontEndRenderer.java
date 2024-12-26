package de.verdox.mccreativelab.generator.resourcepack.types.gui;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrontEndRenderer extends Thread {

    private final LinkedBlockingQueue<Runnable> updateQueue = new LinkedBlockingQueue<>();
    private final ActiveGUI activeGUI;
    private boolean running = true;

    public FrontEndRenderer(ActiveGUI activeGUI) {
        this.activeGUI = activeGUI;
    }

    public void stopRenderer() {
        this.running = false;
        this.updateQueue.clear();
    }

    public void offer(Runnable runnable) {
        updateQueue.offer(runnable);
    }

    @Override
    public void run() {
        try {
            while (running) {
                Runnable update = updateQueue.take();
                update.run();
            }
        } catch (Throwable e) {
            Logger.getLogger(FrontEndRenderer.class.getSimpleName()).log(Level.SEVERE, "An error occured while rendering the active gui " + activeGUI.getComponentRendered().key().asString(), e);
        }
    }
}
