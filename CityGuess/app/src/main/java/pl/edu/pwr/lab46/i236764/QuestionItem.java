package pl.edu.pwr.lab46.i236764;

public class QuestionItem {

    int id, level;
    String answer, longitude, latitude, imageName;

    public QuestionItem() {

    }

    public QuestionItem(int id, int level, String answer, String longitude, String latitude, String imageName) {
        this.id = id;
        this.level = level;
        this.answer = answer;
        this.longitude = longitude;
        this.latitude = latitude;
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }
}
