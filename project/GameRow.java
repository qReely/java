package project;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class GameRow {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleDoubleProperty rating;
    private SimpleStringProperty comment;
    private SimpleStringProperty finished;

    GameRow(int id, String name, double rating, String comment, boolean finished){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.rating = new SimpleDoubleProperty(rating);
        this.comment = new SimpleStringProperty(comment);
        this.finished = new SimpleStringProperty(finished ? "Yes" : "No");
    }

    GameRow(){
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.rating = new SimpleDoubleProperty();
        this.comment = new SimpleStringProperty();
        this.finished = new SimpleStringProperty("No");
    }

    public int getId() {
        return id.get();
    }
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public double getRating() {
        return rating.get();
    }
    public void setRating(double rating) {
        this.rating.set(rating);
    }
    public String getComment() {
        return comment.get();
    }
    public void setComment(String comment) {
        this.comment.set(comment);
    }
    public boolean isFinished() {
        return finished.get().equals("Yes");
    }
    public void setFinished(String finished) {
        this.finished.set(finished);
    }
}
