package cn.cncgroup.tv.modle;

/**
 * Created by zhang on 2015/10/21.
 */
public class Subject {
    private int id;
    private String name;
    private String image;
    private String imageBig;
    private String actors;
    private String directors;
    private String focus;
    private int totalCount;
    private int updateCount;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageBig() {
        return imageBig;
    }

    public void setImageBig(String imageBig) {
        this.imageBig = imageBig;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", imageBig='" + imageBig + '\'' +
                ", actors='" + actors + '\'' +
                ", directors='" + directors + '\'' +
                ", focus='" + focus + '\'' +
                ", totalCount=" + totalCount +
                ", updateCount=" + updateCount +
                ", type='" + type + '\'' +
                '}';
    }
}
