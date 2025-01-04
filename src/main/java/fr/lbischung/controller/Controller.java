package fr.lbischung.controller;

import fr.lbischung.model.Model;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class Controller {
    private static Model _model;
    private static volatile boolean running = true; // For loop termination
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public Controller(Model model) {
        _model = model;
        checkSaveLoop();
    }

    public String getSourcePath() {
        return _model.getSourcePath();
    }

    public String getTargetPath() {
        return _model.getTargetPath();
    }

    public String getLastSaveDate() {
        return _model.getLastSavedDate();
    }

    public void setSourcePath(String sourcePath) {
        _model.setSourcePath(sourcePath);
    }

    public void setTargetPath(String targetPath) {
        _model.setTargetPath(targetPath);
    }

    public synchronized static String save() {
        if (!_model.isCopying) {
            try {
                _model.isCopying = true;
                _model.save();
                _model.isCopying = false;

                // Utilisation de DATE_FORMAT pour enregistrer la date dans un format cohérent
                String formattedDate = DATE_FORMAT.format(new Date());
                _model.setLastSavedDate(formattedDate);

                return "Saved";
            } catch (Exception e) {
                e.printStackTrace();
                return "Error while saving";
            }
        } else {
            return "Already saving...";
        }
    }

    public static CompletableFuture<Void> checkSaveLoop() {
        return CompletableFuture.runAsync(() -> {
            while (running) {
                try {
                    Thread.sleep(5000); // Pause de 5 secondes
                    System.out.println("Checking save loop...");

                    String lastSavedDateStr = _model.getLastSavedDate();

                    if (lastSavedDateStr == null || lastSavedDateStr.isEmpty()) {
                        System.out.println("No last save date found. Triggering auto-save...");
                        save();
                        continue; // Passer à l'itération suivante après auto-save
                    }

                    // Analyse de la dernière date enregistrée
                    Date lastSavedDate = DATE_FORMAT.parse(lastSavedDateStr);

                    // Vérification si la dernière sauvegarde a plus de 4 heures
                    Date fourHoursAgo = new Date(System.currentTimeMillis() - (4 * 60 * 60 * 1000));
                    if (lastSavedDate.before(fourHoursAgo)) {
                        System.out.println("Auto saving...");
                        save();
                        System.out.println("Auto saving done");
                    } else {
                        System.out.println("Nothing to save");
                    }

                    System.out.println("Checking save loop done");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Check save loop interrupted");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Error occurred in checkSaveLoop: " + e.getMessage());
                }
            }
        });
    }

    public static void stopCheckSaveLoop() {
        running = false;
    }
}
