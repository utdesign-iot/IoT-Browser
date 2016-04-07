package listitems;

/**
 * Created by Ray on 3/3/2016.
 */
public class Action {
    private String name;
    private int icon;

    public Action(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public int getIcon(){
        return icon;
    }
}
