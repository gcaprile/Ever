package com.app.checkinmap.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.checkinmap.R;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends SalesforceActivity {

  private RestClient client;
  private ArrayAdapter<String> listAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Setup view
    setContentView(R.layout.activity_main_b);
  }

  @Override
  public void onResume() {
    // Hide everything until we are logged in
    findViewById(R.id.root).setVisibility(View.INVISIBLE);

    // Create list adapter
    listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
    ((ListView) findViewById(R.id.contacts_list)).setAdapter(listAdapter);

    super.onResume();
  }

  @Override
  public void onResume(RestClient client) {
    // Keeping reference to rest client
    this.client = client;

    // Show everything
    findViewById(R.id.root).setVisibility(View.VISIBLE);
  }

  /**
   * Called when "Logout" button is clicked.
   *
   * @param v
   */
  public void onLogoutClick(View v) {
    SalesforceSDKManager.getInstance().logout(this);
  }

  /**
   * Called when "Clear" button is clicked.
   *
   * @param v
   */
  public void onClearClick(View v) {
    listAdapter.clear();
  }

  /**
   * Called when "Fetch Contacts" button is clicked
   *
   * @param v
   * @throws UnsupportedEncodingException
   */
  public void onFetchContactsClick(View v) throws UnsupportedEncodingException {
    sendRequest("SELECT Name FROM Contact");
  }

  /**
   * Called when "Fetch Accounts" button is clicked
   *
   * @param v
   * @throws UnsupportedEncodingException
   */
  public void onFetchAccountsClick(View v) throws UnsupportedEncodingException {
    //sendRequest("SELECT Name FROM Account");
    sendRequest("SELECT Name FROM Opportunity");
  }

  private void sendRequest(String soql) throws UnsupportedEncodingException {
    RestRequest restRequest = RestRequest.getRequestForQuery(ApiVersionStrings.getVersionNumber(this), soql);

    client.sendAsync(restRequest, new AsyncRequestCallback() {
      @Override
      public void onSuccess(RestRequest request, final RestResponse result) {
        result.consumeQuietly(); // consume before going back to main thread
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            try {
              listAdapter.clear();
              JSONArray records = result.asJSONObject().getJSONArray("records");
              for (int i = 0; i < records.length(); i++) {
                listAdapter.add(records.getJSONObject(i).getString("Name"));
              }
            } catch (Exception e) {
              onError(e);
            }
          }
        });
      }

      @Override
      public void onError(final Exception exception) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Toast.makeText(MainActivity.this,
                MainActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
                Toast.LENGTH_LONG).show();
          }
        });
      }
    });
  }
}
