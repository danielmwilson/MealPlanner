package lan.greystoke.mealplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class DisplayMealPlan extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    ListView lstMeals;

    List<Meal> mealList;

    boolean isUpdating = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_meal_plan);

        lstMeals = (ListView) findViewById(R.id.lstMeals);

        mealList = new ArrayList<>();

        if (savedInstanceState != null) {

            String strJSON = savedInstanceState.getString("MealList");

            try {
                JSONArray aryMeals = new JSONArray(strJSON);
                refreshMealList(aryMeals);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            readMeals();
        }


    }

    //Use onSaveInstanceState(Bundle) and onRestoreInstanceState

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        JSONObject obj;
        JSONArray aryMeals = new JSONArray();

        for (Meal myMeal : mealList)
        {
            try {
                obj =myMeal.toJSON();
                aryMeals.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        savedInstanceState.putString("MealList", aryMeals.toString());


        // etc.

        super.onSaveInstanceState(savedInstanceState);
    }


//onRestoreInstanceState

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.



    }

    // Get the Intent that started this activity
    Intent intent = getIntent();



    private void readMeals() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_MEALPLAN, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshMealList(JSONArray meals) throws JSONException {
        mealList.clear();

        for (int i = 0; i < meals.length(); i++) {
            JSONObject obj = meals.getJSONObject(i);

            mealList.add(new Meal(
                    obj.getString("day"),
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getString("source")

            ));
        }

        MealAdapter adapter = new MealAdapter(mealList);
        lstMeals.setAdapter(adapter);
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshMealList(object.getJSONArray("meals"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    class MealAdapter extends ArrayAdapter<Meal> {
        List<Meal> mealList;

        public MealAdapter(List<Meal> mealList) {
            super(DisplayMealPlan.this, R.layout.layout_lstmeals, mealList);
            this.mealList = mealList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_lstmeals, null, true);

            TextView textViewDay = listViewItem.findViewById(R.id.txtDay);

            TextView textViewDish = listViewItem.findViewById(R.id.txtDish);
            TextView textViewSource = listViewItem.findViewById(R.id.txtSource);

            final Meal meal = mealList.get(position);

            textViewDay.setText(meal.getDay());
            textViewDish.setText(meal.getName());
            textViewSource.setText(meal.getSource());



            return listViewItem;
        }
    }

}