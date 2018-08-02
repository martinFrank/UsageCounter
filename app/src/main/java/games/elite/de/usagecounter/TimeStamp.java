package games.elite.de.usagecounter;

public class TimeStamp {

    private int id = -1;
    private final int counterId;
    private final int delta;
    private final long time;

    private int before;//transient, not stored in db
    private int after;//transient, not stored in db

    TimeStamp(int id, int counterId, int delta, long time){
        this(counterId, delta, time);
        this.id = id;
    }

    TimeStamp(int counterId, int delta, long time){
        this.counterId = counterId;
        this.delta = delta;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCounterId() {
        return counterId;
    }

    public int getDelta() {
        return delta;
    }


    public long getTime() {
        return time;
    }

    public int getBefore() {
        return before;
    }

    public void setBefore(int before) {
        this.before = before;
    }

    public int getAfter() {
        return after;
    }

    public void setAfter(int after) {
        this.after = after;
    }

    @Override
    public String toString() {
        return "TimeStamp{" +
                "id=" + id +
                ", counterId=" + counterId +
                ", delta=" + delta +
                ", time=" + time +
                ", before=" + before +
                ", after=" + after +
                '}';
    }
}
