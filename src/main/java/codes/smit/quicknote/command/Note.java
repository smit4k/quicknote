package codes.smit.quicknote.command;

import java.util.ArrayList;
import java.util.List;

public class Note {
    public int id;
    public String message;
    public double x, y, z;
    public long timestamp;
    public List<String> tags;

    public Note(int id, String message, double x, double y, double z, long timestamp) {
        this.id = id;
        this.message = message;
        this.x = x;
        this.y = y;
        this.z = z;
        this.timestamp = timestamp;
        this.tags = new ArrayList<>();
    }
    
    public Note(int id, String message, double x, double y, double z, long timestamp, List<String> tags) {
        this.id = id;
        this.message = message;
        this.x = x;
        this.y = y;
        this.z = z;
        this.timestamp = timestamp;
        this.tags = tags != null ? tags : new ArrayList<>();
    }
}
