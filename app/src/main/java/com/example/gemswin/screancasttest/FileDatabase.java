package com.example.gemswin.screancasttest;

/**
 * Created by this pc on 15-02-17.
 */

public class FileDatabase {

    int id;
    String extension;
    String path;
    String name;

    public FileDatabase() {
    }

    public FileDatabase(int id, String extension, String name,String path) {
        this.id = id;
        this.extension = extension;
        this.path = path;
        this.name = name;
    }

    public FileDatabase( String extension, String name,String path) {
        this.extension = extension;
        this.path = path;
        this.name = name;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;

    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}