package lan.greystoke.mealplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    /** Called when the user taps the MealPlan button */
    public void launchMealPlan(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMealPlan.class);
        startActivity(intent);

    }

    /** Called when the user taps the MealPlan button */
    public void launchShoppingList(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ShoppingList.class);
        startActivity(intent);
    }

}
