package fr.lbischung.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class Model {
    private String _sourcePath;
    private String _targetPath;

    private String _propertiesPath;
    private Properties _props;

    private String _lastSavedDate;

    public boolean isCopying;
    public int resultCode;

    public Model() throws Exception {
        this.loadProperties();
    }

    private void loadProperties() throws Exception {
        String rootPath = "";
        _propertiesPath = rootPath + "app.properties";
        if(!new File(_propertiesPath).exists()) {
            new File(_propertiesPath).createNewFile();
        }
        _propertiesPath = _propertiesPath.replace("%20", " ");

        _props = new Properties();
        _props.load(new FileInputStream(_propertiesPath));

        _targetPath = _props.getProperty("targetPath");
        _sourcePath = _props.getProperty("sourcePath");

        _lastSavedDate = _props.getProperty("lastSavedDate");
    }


    public void saveProperties() {
        try {
            _props.store(new FileWriter(_propertiesPath), "");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSourcePath() {
        return _sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        _sourcePath = sourcePath;
        _props.setProperty("sourcePath", _sourcePath);
        this.saveProperties();
    }

    public String getTargetPath() {
        return _targetPath;
    }

    public void setTargetPath(String targetPath) {
        _targetPath = targetPath;
        _props.setProperty("targetPath", _targetPath);
        this.saveProperties();
    }

    public String getLastSavedDate() {
        return _lastSavedDate;
    }

    public void setLastSavedDate(String lastSavedDate) {
        _lastSavedDate = lastSavedDate;
        _props.setProperty("lastSavedDate", _lastSavedDate);
        this.saveProperties();

    }


    public int save() throws Exception {
        copy(Paths.get(_sourcePath), Paths.get(_targetPath));
        return resultCode;
    }

    private void copy(Path sourceDir, Path destinationDir) throws Exception {
        this.resultCode = 0;

        Files.walk(sourceDir)
                .forEach(sourcePath -> {
                    try {
                        Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                        System.out.printf("Copying %s to %s%n", sourcePath, targetPath);
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        this.resultCode = 0;
                    } catch (IOException ex) {
                        System.out.format("I/O error: %s%n", ex);
                        this.resultCode = 1;
                    }
                });
    }
}
