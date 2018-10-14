package lan.greystoke.mealplanner;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GREYSTOKE\daniel on 18/01/18.
 */

class Meal {
    private int id;
    private String name;
    private String source;
    private String day;

    public Meal(String day, int id, String name, String source) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getDay() {
        return day;
    }

    public JSONObject toJSON() throws JSONException
    {
        JSONObject obj = new JSONObject();

        obj.accumulate("day", day);
        obj.accumulate("name", name);
        obj.accumulate("source", source);
        obj.accumulate("id", id);

        return obj;

    }
}