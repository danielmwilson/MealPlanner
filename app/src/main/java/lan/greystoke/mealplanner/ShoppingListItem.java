package lan.greystoke.mealplanner;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GREYSTOKE\daniel on 25/01/18.
 */

public class ShoppingListItem {
    private String ingredient;
    private String quantity;
    private String dish;
    private boolean needed;
    private boolean ticked;

    public ShoppingListItem(String ingredient, String quantity, String dish, boolean needed, boolean ticked) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.dish = dish;
        this.needed = needed;
        this.ticked = ticked;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDish() { return dish; }

    public boolean getNeeded() {
        return needed;
    }

    public boolean getTicked() {
        return ticked;
    }

    public void setTicked(boolean newValue){ ticked = newValue;}

    public JSONObject toJSON() throws JSONException
    {
        JSONObject obj = new JSONObject();

        obj.accumulate("ingredient", ingredient);
        obj.accumulate("quantity", quantity);
        obj.accumulate("dish", dish);
        obj.accumulate("needed", needed);
        obj.accumulate("ticked", ticked);

        return obj;

    }
}
