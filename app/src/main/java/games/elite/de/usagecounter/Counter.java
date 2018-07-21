package games.elite.de.usagecounter;

import java.text.MessageFormat;

public class Counter {

    private int count;
    private Integer id;
    private String text;

    public void add() {
        //FIXME date
        count = count + 1;
    }

    public void remove() {
        //FIXME date
        count = count - 1;
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
