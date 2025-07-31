package codes.smit.quicknote.command;

public class Note {
    public int id;
    public String message;
    public double x, y, z;
    public long timestamp;

    public Note(int id, String message, double x, double y, double z, long timestamp) {
        this.id = id;
        this.message = message;
        this.x = x;
        this.y = y;
        this.z = z;
        this.timestamp = timestamp;
    }
}
