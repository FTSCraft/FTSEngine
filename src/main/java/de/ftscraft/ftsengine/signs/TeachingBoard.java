package de.ftscraft.ftsengine.signs;


import java.util.List;

public class TeachingBoard {
    private List<String> lines;
    private List<String> owners;

    public TeachingBoard(List<String> lines, List<String> owners) {
        this.lines = lines;
        this.owners = owners;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public List<String> getOwners() {
        return owners;
    }

    public void setOwners(List<String> owners) {
        this.owners = owners;
    }
}
