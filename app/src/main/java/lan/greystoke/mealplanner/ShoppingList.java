package lan.greystoke.mealplanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingList extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    ListView lstShoppingList;

    List<ShoppingListItem> itemList;

    boolean hideTicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        lstShoppingList = (ListView) findViewById(R.id.lstShoppingListItems);


        itemList = new ArrayList<>();

        if (savedInstanceState != null) {

            hideTicked = savedInstanceState.getBoolean("HideTicked");
            String strJSON = savedInstanceState.getString("ShoppingList");

            try {
                JSONArray ShopList = new JSONArray(strJSON);
                refreshMealList(ShopList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            readShoppingList();
        }
    }

    //Use onSaveInstanceState(Bundle) and onRestoreInstanceState

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        JSONObject obj;
        JSONArray shopList = new JSONArray();

        for (ShoppingListItem shopItem : itemList)
        {
            try {
                obj =shopItem.toJSON();
                shopList.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        savedInstanceState.putString("ShoppingList", shopList.toString());

        savedInstanceState.putBoolean("HideTicked",hideTicked);


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

    private void readShoppingList() {
        ShoppingList.PerformNetworkRequest request = new ShoppingList.PerformNetworkRequest(Api.URL_GET_SHOPPINGLIST, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshMealList(JSONArray meals) throws JSONException {
        itemList.clear();

        for (int i = 0; i < meals.length(); i++) {
            JSONObject obj = meals.getJSONObject(i);

            if (!obj.has("needed"))
            {
                obj.put("needed", true);
            }
            if (!obj.has("ticked"))
            {
                obj.put("ticked", false);
            }

            if ((hideTicked==false)||(obj.getBoolean("ticked")==false)) {

                itemList.add(new ShoppingListItem(
                        obj.getString("ingredient"),
                        obj.getString("quantity"),
                        obj.getString("dish"),
                        obj.getBoolean("needed"),
                        obj.getBoolean("ticked")

                ));
            }
        }

        ShoppingList.ItemAdapter adapter = new ShoppingList.ItemAdapter(itemList);
        lstShoppingList.setAdapter(adapter);
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

    class ItemAdapter extends ArrayAdapter<ShoppingListItem> {
        List<ShoppingListItem> itemList;

        public ItemAdapter(List<ShoppingListItem> itemList) {
            super(ShoppingList.this, R.layout.layout_lstmeals, itemList);
            this.itemList  = itemList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_lstshoppinglist, null, true);

            TextView textViewItem = listViewItem.findViewById(R.id.txtItem);
            TextView textViewDish = listViewItem.findViewById(R.id.txtItemDish);
            final CheckBox chkTicked = listViewItem.findViewById(R.id.chkTicked);

            final ShoppingListItem item = itemList.get(position);

            textViewItem.setText(item.getQuantity() + " " + item.getIngredient());
            textViewDish.setText(item.getDish());
            chkTicked.setChecked(item.getTicked());

            chkTicked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setTicked(chkTicked.isChecked());
                }
            });




            return listViewItem;
        }
    }
}
