package games.elite.de.usagecounter;

import java.text.MessageFormat;

public class Counter {

    private int id;
    private String text;
    private int count;

    Counter(int id, String text, int count){
        this(text, count);
        this.id = id;
    }

    Counter(String text, int count){
        this.text = text;
        this.count = count;
    }

    public int increase() {
        int delta = + 1;
        count = count + delta;
        return delta;
    }

    public int decrease() {
        int delta = - 1;
        count = count + delta;
        return delta;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getFormatted(){
        return MessageFormat.format(getText(), getCount());
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Counter{" +
                "count=" + count +
                ", id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
