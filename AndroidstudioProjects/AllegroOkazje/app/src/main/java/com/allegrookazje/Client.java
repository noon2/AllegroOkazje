package com.allegrookazje;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.Transport;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.allegrookazje.items.Item_Cel;
import com.allegrookazje.items.Item_Country;
import com.allegrookazje.items.Item_Feedback;
import com.allegrookazje.items.Item_Lista;
import com.allegrookazje.items.Item_Payment;
import com.allegrookazje.items.Item_Rating_Average;
import com.allegrookazje.items.Item_Shipment;
import com.allegrookazje.items.Item_User;
import com.allegrookazje.menu.Kate_Record;
import com.allegrookazje.soap.SoapArrayLong;
import com.example.allegrookazje.R;

public final class Client {
    private final String tag = "allegro";
    private final int soapVersion = SoapEnvelope.VER11;
    private final String API_NAMESPACE = "https://webapi.allegro.pl/service.php";
    private final String API_KEY = "ed02cd1d";
    public int COUNTRY_CODE = 1;
    private long LOCAL_VERSION;
    public String SESSION_HANDLE_PART;
    private String LOGIN;
    private String PASSWORD;
    public int CATS_COUNT;
    public int VERSION_CAT;
    SharedPreferences settings;

    // doSearch
    public static int count_search;
    public static int part_search;
    public static int search_limit = 20;
    public static Item_Cel item_cel;

    public Client(Context b) {
        settings = PreferenceManager.getDefaultSharedPreferences(b);
        LOGIN = settings.getString("et_login", "");
        PASSWORD = settings.getString("et_password", "");
        LOCAL_VERSION = settings.getLong("local_version", 0);
    }

    protected SoapObject sendSoap22(String methodName, String soapAction, PropertyInfo... properties) throws SoapFault {
        SoapObject request = new SoapObject(this.namespace, methodName);
        for (PropertyInfo property : properties) {
            request.addPropertyIfValue(property);
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(this.soapVersion);
        envelope.implicitTypes = true; // removes i:type attribute for SimpleTypes
        envelope.setAddAdornments(false); // removes id & c:root attributes
        envelope.setOutputSoapObject(request);

        Transport transport = new HttpTransportSE(this.endpointUri);
        try {
            transport.call(soapAction, envelope);
        } catch (XmlPullParserException | IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        if (envelope.bodyIn instanceof SoapFault) {
            throw (SoapFault) envelope.bodyIn;
        }

        return (SoapObject) envelope.bodyIn;
    }

    public Object connect_allegro(final SoapObject rpc, final Context context) {
        Object response = sendRequest(rpc);

        if (response == null) {
            for (int i = 0; i != 3; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                response = sendRequest(rpc);
                if (response != null) {
                    break;
                }
            }
        }
        if (SoapObject.class.isInstance(response)) {
            return response;
        } else if (SoapFault.class.isInstance(response)) {
            return response;
        } else {
            return "Problem z funkcj¹ " + rpc.getName()
                    + " ,spróbuj ponownie za chwile.";
        }
    }

    private void fault_code(Object obj, AsyncTask<Void, Void, Void> asyncTask,
                            Context context) {
        Log.d("allegro", "fault code!!");
        if (String.class.isInstance(obj)) {
            Log.d("allegro", "String!!");

            show_message(obj.toString(), "Error", context, true);
            return;
        }
        SoapFault fault = (SoapFault) obj;
        if (fault.faultcode.equals("ERR_NO_SESSION")
                || fault.faultcode.equals("ERR_SESSION_EXPIRED")) {
            Log.d("allegro", "ERR_NO_SESSION||ERR_SESSION_EXPIRED");
            doLoginEnc(context);
        } else if (fault.faultcode.equals("ERR_USER_PASSWD")) {
            Log.d("allegro", "ERR_USER_PASSWD");
            Toast.makeText(context, fault.getMessage(), Toast.LENGTH_LONG)
                    .show();
            logowanie(context);
        } else if (fault.faultcode
                .equals("ERR_INVALID_VERSION_CAT_SELL_FIELDS")) {
            Log.d("allegro", "ERR_INVALID_VERSION_CAT_SELL_FIELDS");
            doQuerySysStatus(context);
        } else {
            Log.d("allegro", "fault code else");
            show_message(fault.faultcode, "Error", context, true);
        }

    }

    public void sendRequest2(SoapObject rpc) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        new MarshalFloat().register(envelope);
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        envelope.setOutputSoapObject(rpc);
        @SuppressWarnings("unchecked")
        SoapFault response = null;
        try {
            TRANSPORT.debug = true;
            TRANSPORT.call("", envelope);
            System.setProperty("http.keepAlive", "false");
            response = (SoapFault) envelope.bodyIn;
            Log.d(tag, response.toString());
            // for (Object a : response) {
            // Log.d(tag, a.toString());
            // }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            Log.d(tag, e.toString() + "1");
        } catch (Exception e) {
            Log.d(tag, e.toString() + "2");
        }
        Log.d(tag, response.faultstring);
    }

    public void doQuerySysStatus(Context context) {
        new doQuerySysStatus(context).execute();
    }

    public class doQuerySysStatus extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        Context context;
        boolean update;
        boolean error;
        Object fault;

        public doQuerySysStatus(Context context) {
            this.context = context;
            error = false;
            dialog = new ProgressDialog(context);
            dialog.setMessage("£¹czenie z Allegro...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject request = new SoapObject(API_NAMESPACE,
                    "doQuerySysStatus");
            request.addProperty("sysvar", 3);
            request.addProperty("country-id", COUNTRY_CODE);
            request.addProperty("webapi-key", API_KEY);
            SoapObject response = null;
            Object response_object = connect_allegro(request, context);
            if (SoapObject.class.isInstance(response_object)) {
                response = (SoapObject) response_object;
            } else {
                error = true;
                fault = response_object;
                return null;
            }
            LOCAL_VERSION = Long.parseLong(response.getPropertyAsString(1));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            dialog.dismiss();
            if (error) {
                fault_code(fault, new doQuerySysStatus(context), context);
            } else {
                get_session(context);
            }
        }
    }

    public void doLoginEnc(Context context) {
        if (!settings.getBoolean("save_password", false)) {
            logowanie(context);
        } else {
            new doLoginEnc(context).execute();
        }

    }

    private void logowanie(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = MainActivity.mNavigationDrawerFragment
                .getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_login, null);
        final EditText login = (EditText) view.findViewById(R.id.log_login);
        final EditText haslo = (EditText) view.findViewById(R.id.log_haslo);
        final CheckBox zapisz_haslo = (CheckBox) view
                .findViewById(R.id.log_zapisz_haslo);
        login.setText(LOGIN);
        builder.setView(view).setTitle("Logowanie").setCancelable(false)
                .setPositiveButton("Zaloguj", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        LOGIN = login.getText().toString();
                        try {
                            PASSWORD = SHA256(haslo.getText().toString());
                            SharedPreferences.Editor editor = settings.edit();
                            if (zapisz_haslo.isChecked()) {
                                editor.putString("et_login", LOGIN);
                                editor.putString("et_password", PASSWORD);
                                editor.putBoolean("save_password", true);

                            } else {
                                editor.putBoolean("save_password", false);
                            }
                            editor.commit();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        new doLoginEnc(context).execute();
                    }
                }).setNegativeButton("Anuluj", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                MainActivity.mNavigationDrawerFragment.getActivity()
                        .finish();
            }
        });
        builder.show();

    }

    private void zapisywanie(final Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message + "\n Spróbuj ponownie póŸniej.")
                .setPositiveButton("Ok", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public class doLoginEnc extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        Context context;
        boolean error;
        Object fault;
        DbAdapter dbadapter;

        public doLoginEnc(Context context) {
            dbadapter = new DbAdapter(context);
            this.context = context;
            error = false;
            dialog = new ProgressDialog(context);
            dialog.setMessage("Logowanie...");
            dialog.setCancelable(false);
            LOGIN = settings.getString("et_login", "");
            PASSWORD = settings.getString("et_password", "");
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject request = new SoapObject(API_NAMESPACE, "doLoginEnc");
            request.addProperty("user-login", LOGIN);
            request.addProperty("user-hash-password", PASSWORD);
            request.addProperty("country-code", COUNTRY_CODE);
            request.addProperty("webapi-key", API_KEY);
            request.addProperty("local-version", LOCAL_VERSION);
            SoapObject response = null;
            Object response_object = connect_allegro(request, context);
            if (SoapObject.class.isInstance(response_object)) {
                response = (SoapObject) response_object;
            } else {
                error = true;
                fault = response_object;
                return null;
            }
            SESSION_HANDLE_PART = response.getPropertyAsString(0);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            dialog.dismiss();
            if (!error) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putLong("time_login", getUnixTime());
                editor.putString("session_handle_part", SESSION_HANDLE_PART);
                editor.commit();
                sprawdzenie_db(context);
            } else {
                fault_code(fault, new doLoginEnc(context), context);
            }

        }
    }

    public long getUnixTime() {
        long unixtime;
        Date currentDate = new Date();
        unixtime = currentDate.getTime() / 1000;
        return unixtime;
    }

    public void doGetCatsDataCount(Context context) {
        new doGetCatsDataCount(context).execute();
    }

    public class doGetCatsDataCount extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        Context context;
        boolean error;
        Object fault;

        public doGetCatsDataCount(Context context) {
            this.context = context;
            error = false;
            dialog = new ProgressDialog(context);
            dialog.setMessage("£¹czenie z Allegro");
            dialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject request = new SoapObject(API_NAMESPACE,
                    "doGetCatsDataCount");
            request.addProperty("country-id", COUNTRY_CODE);
            request.addProperty("local-version", LOCAL_VERSION);
            request.addProperty("webapi-key", API_KEY);
            SoapObject response = null;
            Object response_object = connect_allegro(request, context);
            if (SoapObject.class.isInstance(response_object)) {
                response = (SoapObject) response_object;
            } else {
                error = true;
                fault = response_object;
                return null;
            }
            CATS_COUNT = Integer.parseInt(response.getPropertyAsString(0));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            dialog.dismiss();
            if (!error) {
                doGetCatsDataLimit(context);
            } else {
                fault_code(fault, new doGetCatsDataCount(context), context);
            }
        }
    }

    public void doGetCatsDataLimit(Context context) {
        new doGetCatsDataLimit(context).execute();
    }

    public class doGetCatsDataLimit extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        Context context;
        boolean error;
        DbAdapter dbadapter;
        Object fault;

        public doGetCatsDataLimit(Context context) {
            this.context = context;
            dbadapter = new DbAdapter(context);
            error = false;
            dialog = new ProgressDialog(context);
            dialog.setMessage("Pobieranie kategorii...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMax(CATS_COUNT);
            dialog.show();
            dbadapter.open();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int ile = CATS_COUNT / 5000;
            for (int i = 0; i <= ile; i++) {
                SoapObject request = new SoapObject(API_NAMESPACE,
                        "doGetCatsDataLimit");
                request.addProperty("country-id", COUNTRY_CODE);
                request.addProperty("local-version", LOCAL_VERSION);
                request.addProperty("webapi-key", API_KEY);
                request.addProperty("offset", i);
                SoapObject response = null;
                Object response_object = connect_allegro(request, context);
                if (SoapObject.class.isInstance(response_object)) {
                    response = (SoapObject) response_object;
                } else {
                    error = true;
                    fault = response_object;
                    return null;
                }
                Vector<SoapObject> kategorie = (Vector<SoapObject>) response
                        .getProperty(0);
                Kate_Record tmp = new Kate_Record();
                for (SoapObject a : kategorie) {

                    tmp.id = Integer.parseInt(a.getPropertyAsString(0));
                    tmp.name = a.getPropertyAsString(1);
                    tmp.parent = Integer.parseInt(a.getPropertyAsString(2));
                    tmp.position = Integer.parseInt(a.getPropertyAsString(3));
                    dbadapter.createRowKate(tmp);

                    dialog.incrementProgressBy(1);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            dialog.dismiss();
            dbadapter.close();
            if (error) {
                fault_code(fault, new doGetCatsDataLimit(context), context);
            } else {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("db_allegro", true);
                editor.commit();
            }
        }
    }

    public void doGetCountries(Context context) {
        new doGetCountries(context).execute();
    }

    public class doGetCountries extends AsyncTask<Void, Void, Void> {
        Context context;
        boolean error;
        Object fault;
        DbAdapter dbadapter;

        public doGetCountries(Context context) {
            this.context = context;
            dbadapter = new DbAdapter(context);
            error = false;
        }

        @Override
        protected void onPreExecute() {
            dbadapter.open();
        }

        @Override
        protected Void doInBackground(Void... Voids) {
            SoapObject request = new SoapObject(API_NAMESPACE, "doGetCountries");
            request.addProperty("country-code", COUNTRY_CODE);
            request.addProperty("webapi-key", API_KEY);
            SoapObject response = null;
            Object response_object = connect_allegro(request, context);
            if (SoapObject.class.isInstance(response_object)) {
                response = (SoapObject) response_object;
            } else {
                error = true;
                fault = response_object;
                return null;
            }
            Vector<SoapObject> countries = (Vector<SoapObject>) response
                    .getProperty(0);
            for (SoapObject a : countries) {
                int id = Integer.parseInt(a.getPropertyAsString(0));
                String name = a.getPropertyAsString(1);
                Item_Country item = new Item_Country(id, name);
                dbadapter.createRowCountry(item);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            dbadapter.close();
            if (error) {
                fault_code(fault, new doGetCountries(context), context);
            }
        }
    }

    public void doSearch(int sortowanie, ViewFlipper vf_actual,
                         ArrayAdapter<Item_Lista> adapter, ArrayList<Item_Lista> lista,
                         Item_Cel cel, Context context, int part) {
        get_session(context);
        Log.d("allegro", "kod kraju!!!" + COUNTRY_CODE);
        new doSearch(sortowanie, vf_actual, adapter, lista, cel, context, part)
                .execute();
    }

    public class doSearch extends AsyncTask<Void, Void, Void> {

        private ArrayList<Item_Lista> lista;
        ArrayAdapter<Item_Lista> adapter;
        ViewFlipper vf_actual;
        Context context;
        Boolean error;
        SoapObject request;
        int porcja;
        int sortowanie;
        Object fault;
        DbAdapter dbadapter;

        public doSearch(int sortowanie, ViewFlipper vf_actual,
                        ArrayAdapter<Item_Lista> adapter, ArrayList<Item_Lista> lista,
                        Item_Cel cel, Context context, int porcja) {
            this.vf_actual = vf_actual;
            this.sortowanie = sortowanie;
            item_cel = cel;
            this.context = context;
            this.adapter = adapter;
            this.lista = lista;
            this.porcja = porcja;
            error = false;
            dbadapter = new DbAdapter(context);
        }

        @Override
        protected void onPreExecute() {
            dbadapter.open();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            request = new SoapObject(API_NAMESPACE, "doSearch");
            request.addProperty("session-handle", SESSION_HANDLE_PART);
            SoapObject searchquery = new SoapObject(API_NAMESPACE,
                    "search-query");
            searchquery.addProperty("search-string",
                    item_cel.nazwa_wyszukiwania);
            if (item_cel.oferta_typu == 0) {
                // searchquery.addProperty("search-options", "");
            }
            if (item_cel.oferta_typu == 1) {
                searchquery.addProperty("search-options", 8);
            }
            if (item_cel.oferta_typu == 2) {
                searchquery.addProperty("search-options", 8192);
            }

            searchquery.addProperty("search-order", 4);
            searchquery.addProperty("search-order-type", 0);
            searchquery.addProperty("search-country", COUNTRY_CODE);
            searchquery.addProperty("search-category", item_cel.kategoria_id);
            searchquery.addProperty("search-offset", porcja);
            // searchquery.addProperty("search-city", "");
            // searchquery.addProperty("search-state", "");
            searchquery.addProperty("search-price-from", item_cel.min);
            searchquery.addProperty("search-price-to", item_cel.max);
            searchquery.addProperty("search-limit", search_limit);
            // searchquery
            // .addProperty("search-order-fulfillment-time", "");
            // searchquery.addProperty("search-user", "");
            request.addSoapObject(searchquery);
            SoapObject response = null;
            Object response_object = connect_allegro(request, context);
            if (SoapObject.class.isInstance(response_object)) {
                response = (SoapObject) response_object;
            } else {
                error = true;
                fault = response_object;
                return null;
            }
            count_search = Integer.parseInt(response.getPropertyAsString(0));

            Vector<SoapObject> searchArray = (Vector<SoapObject>) response
                    .getProperty(2);
            for (SoapObject a : searchArray) {
                Item_Lista item = new Item_Lista();

                item.id = Long.parseLong(a.getPropertyAsString(0));
                item.name = a.getPropertyAsString(1);
                item.price = Float.parseFloat(a.getPropertyAsString(2));
                item.starting_price = Float
                        .parseFloat(a.getPropertyAsString(3));
                item.buy_now_active = Integer
                        .parseInt(a.getPropertyAsString(4));
                item.buy_now_price = Float.parseFloat(a.getPropertyAsString(5));
                item.bid_count = Integer.parseInt(a.getPropertyAsString(6));

                item.foto_count = Integer.parseInt(a.getPropertyAsString(7));
                item.starting_time = Long.parseLong(a.getPropertyAsString(8));
                item.ending_time = Long.parseLong(a.getPropertyAsString(9));
                item.time_left = Long.parseLong(a.getPropertyAsString(10));
                item.city = a.getPropertyAsString(11);
                item.state = Integer.parseInt(a.getPropertyAsString(12));
                item.country = Integer.parseInt(a.getPropertyAsString(13));
                item.category_id = Integer.parseInt(a.getPropertyAsString(14));
                item.featured = Integer.parseInt(a.getPropertyAsString(15));
                item.thumb_url = a.getPropertyAsString(16);
                item.postage = Float.parseFloat(a.getPropertyAsString(18));
                lista.add(item);
            }
            part_search++;
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if (count_search != item_cel.count) {
                item_cel.count = count_search;
                dbadapter.updateRowCele(item_cel);
                MainActivity.mNavigationDrawerFragment.adapter
                        .changeCursor(dbadapter.readData());
            }
            dbadapter.close();
            if (!error) {
                adapter.notifyDataSetChanged();
                if (count_search == 0) {
                    vf_actual.setDisplayedChild(3);
                } else {
                    vf_actual.setDisplayedChild(1);
                }
                if (part_search == 1) {
                    MainActivity.mTitle = MainActivity.mTitle + " ("
                            + count_search + ")";
                    MainActivity.actionBar.setTitle(MainActivity.mTitle);
                }
                // ((Lista_Adapter) adapter).setItemList(lista);
                // MainActivity.sortowanie_switch(context, null, adapter,
                // MainActivity.sortowanie);
            } else {
                fault_code(fault, new doSearch(sortowanie, vf_actual, adapter,
                        lista, item_cel, context, porcja), context);
            }
        }
    }

    public void doGetItemsInfo(Item_Lista item, Context context, Intent intent) {
        get_session(context);
        new doGetItemsInfo(context, intent, item).execute();
    }

    public class doGetItemsInfo extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        Context context;
        Item_Lista item;
        Intent intent;
        Boolean error;
        SoapObject request;
        Object fault;
        DbAdapter dbadapter;

        public doGetItemsInfo(Context context, Intent intent, Item_Lista item) {
            this.context = context;
            this.item = item;
            this.intent = intent;
            dbadapter = new DbAdapter(context);
            error = false;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("Ladowanie...");
            dialog.setCancelable(false);
            dialog.show();
            dbadapter.open();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (item.is_loaded)
                return null;
            SoapObject request = new SoapObject(API_NAMESPACE, "doGetItemsInfo");
            request.addProperty("session-handle", SESSION_HANDLE_PART);
            SoapArrayLong array = new SoapArrayLong();
            array.add(item.id);
            PropertyInfo id_property = new PropertyInfo();
            id_property.setName("items-id-array");
            id_property.setValue(array);
            id_property.setType(array.getClass());
            request.addProperty(id_property);
            request.addProperty("get-desc", 1);
            request.addProperty("get-image-url", 1);
            request.addProperty("get-attribs", 1);
            request.addProperty("get-postage-options", 1);
            SoapObject response = null;
            Object response_object = connect_allegro(request, context);
            if (SoapObject.class.isInstance(response_object)) {
                response = (SoapObject) response_object;
            } else {
                error = true;
                fault = response_object;
                return null;
            }
            Vector<SoapObject> item_list_info = (Vector<SoapObject>) response
                    .getProperty(0);
            SoapObject item_info = (SoapObject) item_list_info.get(0)
                    .getProperty(0);
            item.country = Integer.parseInt(item_info.getPropertyAsString(1));
            item.name = item_info.getPropertyAsString(2);
            item.price = Float.parseFloat(item_info.getPropertyAsString(3));
            item.bid_count = Integer.parseInt(item_info.getPropertyAsString(4));
            item.ending_time = Long.parseLong(item_info.getPropertyAsString(5));
            item.seller_id = Long.parseLong(item_info.getPropertyAsString(6));
            item.seller_login = item_info.getPropertyAsString(7);
            item.seller_rating = Integer.parseInt(item_info
                    .getPropertyAsString(8));
            item.starting_price = Float.parseFloat(item_info
                    .getPropertyAsString(10));
            item.quantity = Integer.parseInt(item_info.getPropertyAsString(11));
            item.foto_count = Integer.parseInt(item_info
                    .getPropertyAsString(12));
            item.location = item_info.getPropertyAsString(14);
            item.buy_now_price = Float.parseFloat(item_info
                    .getPropertyAsString(15));
            item.buy_now_active = Integer.parseInt(item_info
                    .getPropertyAsString(16));
            item.description = item_info.getPropertyAsString(19);
            item.state = Integer.parseInt(item_info.getPropertyAsString(21));
            item.hit_count = Long.parseLong(item_info.getPropertyAsString(23));
            item.order_fulfillment_time = Integer.parseInt(item_info
                    .getPropertyAsString(31));
            item.is_new_used = Integer.parseInt(item_info
                    .getPropertyAsString(34));
            Vector<SoapObject> item_images = (Vector<SoapObject>) item_list_info
                    .get(0).getProperty(2);
            for (SoapObject a : item_images) {
                if (a.getPropertyAsString(0).equals("1")) {
                    item.image_1.add(a.getPropertyAsString(1));
                }
                if (a.getPropertyAsString(0).equals("2")) {
                    item.image_2.add(a.getPropertyAsString(1));
                }
                if (a.getPropertyAsString(0).equals("3")) {
                    item.image_3.add(a.getPropertyAsString(1));
                }
            }
            if (item.foto_count != 0) {
                item.thumb_url = item.image_1.firstElement();
            }
            Vector<SoapObject> item_params = (Vector<SoapObject>) item_list_info
                    .get(0).getProperty(3);
            for (SoapObject a : item_params) {
                item.params_name.add(a.getPropertyAsString(0));
                Vector<String> params_value = (Vector<String>) a.getProperty(1);
                if (params_value.size() == 1) {
                    item.params_value1.add(params_value.get(0) + "");
                } else {
                    item.params_value1.add(params_value.get(0) + " - "
                            + params_value.get(1));
                }
            }
            // pobieranie danych o wysylce
            Vector<SoapObject> item_postage = (Vector<SoapObject>) item_list_info
                    .get(0).getProperty(4);
            for (SoapObject a : item_postage) {
                Item_Shipment ship = new Item_Shipment();
                ship.amount = Float.parseFloat(a.getPropertyAsString(0));
                ship.amount_add = Float.parseFloat(a.getPropertyAsString(1));
                ship.pack_size = Integer.parseInt(a.getPropertyAsString(2));
                ship.id = Integer.parseInt(a.getPropertyAsString(3));
                ship.free_shipping = Integer.parseInt(a.getPropertyAsString(4));
//				SoapObject params_value = (SoapObject) a.getProperty(5);
//				ship.time_from = Integer.parseInt(params_value
//						.getPropertyAsString(0));
//				ship.time_to = Integer.parseInt(params_value
//						.getPropertyAsString(1));
                Item_Shipment tmp = dbadapter.getRowShipment(ship.id);
                ship.name = tmp.name;
                ship.type = tmp.type;
                item.postage_options.add(ship);
            }
            // pobieranie danych o platnosci
            SoapObject item_payment = (SoapObject) item_list_info.get(0)
                    .getProperty(5);
            Item_Payment payment = new Item_Payment();
            payment.option_transfer = Integer.parseInt(item_payment
                    .getPropertyAsString(0));
            payment.option_on_delivery = Integer.parseInt(item_payment
                    .getPropertyAsString(1));
            payment.option_allegro_pay = Integer.parseInt(item_payment
                    .getPropertyAsString(2));
            payment.option_see_desc = Integer.parseInt(item_payment
                    .getPropertyAsString(3));
            item.payment = payment;
            item.is_loaded = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            dbadapter.close();
            if (!error) {
                intent.putExtra("item", item);
                context.startActivity(intent);
            } else {
                fault_code(fault, new doGetItemsInfo(context, intent, item),
                        context);
            }

        }
    }

    public void create_row(Item_Cel cel, Context context) {
        String url_create_row = "http://allegrookazje.eu/allegro/create_cat.php";
        new create_row(MainActivity.regid, context, url_create_row, 0, cel)
                .execute();
    }

    public void delete_row(Item_Cel cel, Context context) {
        String url_delete_row = "http://allegrookazje.eu/allegro/delete_cat.php";
        new create_row(MainActivity.regid, context, url_delete_row, 1, cel)
                .execute();
    }

    public void update_row(Item_Cel cel, Context context) {
        String url_update_row = "http://allegrookazje.eu/allegro/update_cat.php";
        new create_row(MainActivity.regid, context, url_update_row, 2, cel)
                .execute();
    }

    public class create_row extends AsyncTask<Void, Void, Void> {
        String regid;
        boolean error;
        String message;
        Context context;
        String url;
        int zadanie;
        Item_Cel cel;
        DbAdapter dbadapter;

        public create_row(String regid, Context context, String url,
                          int zadanie, Item_Cel cel) {
            this.context = context;
            this.zadanie = zadanie;
            this.regid = regid;
            this.url = url;
            this.cel = cel;
            error = false;
            dbadapter = new DbAdapter(context);
        }

        @Override
        protected void onPreExecute() {
            dbadapter.open();
            if (zadanie == 0) {
                cel.id = dbadapter.createRowCele(cel);
            }
            if (zadanie == 1) {
                dbadapter.deleteRowCele(cel);
            }
            MainActivity.mNavigationDrawerFragment.adapter
                    .changeCursor(dbadapter.readData());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<NameValuePair> myArgs = new ArrayList<NameValuePair>();
            myArgs.add(new BasicNameValuePair("cat_id", cel.id + ""));
            myArgs.add(new BasicNameValuePair("cat_name",
                    cel.nazwa_wyszukiwania + ""));
            myArgs.add(new BasicNameValuePair("reg_id", regid));
            myArgs.add(new BasicNameValuePair("cat_all_id", cel.kategoria_id
                    + ""));
            myArgs.add(new BasicNameValuePair("cat_min", cel.min + ""));
            myArgs.add(new BasicNameValuePair("cat_max", cel.max + ""));
            myArgs.add(new BasicNameValuePair("cat_offer", cel.oferta_typu + ""));
            HttpPost post = new HttpPost(url);
            HttpClient myClient = new DefaultHttpClient();
            try {
                post.setEntity(new UrlEncodedFormEntity(myArgs));
                HttpResponse response = myClient.execute(post);
                String result = EntityUtils.toString(response.getEntity());
                Log.d(tag, result);
                JSONObject json = new JSONObject(result);
                error = json.getBoolean("success");
                if (!error) {
                    message = json.getString("message");
                }
            } catch (UnsupportedEncodingException e) {
                show_message(e.toString(), "Error", context, true);
            } catch (ClientProtocolException e) {
                show_message(e.toString(), "Error", context, true);
            } catch (IOException e) {
                show_message(e.toString(), "Error", context, true);
            } catch (JSONException e) {
                show_message(e.toString(), "Error", context, true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (!error) {
                if (zadanie == 0) {
                    dbadapter.deleteRowCele(cel);
                }
                if (zadanie == 1) {
                    dbadapter.createRowCele(cel);
                }
                zapisywanie(context, message);
            } else {
                if (zadanie == 2) {
                    dbadapter.updateRowCele(cel);

                }
                MainActivity.mNavigationDrawerFragment.adapter
                        .changeCursor(dbadapter.readData());
            }
            dbadapter.close();
            Log.d("allegro", "update lista!!!");
            MainActivity.lista_adapter.notifyDataSetChanged();
        }
    }

    public void doGetShipmentData(Context context) {
        new doGetShipmentData(context).execute();
    }

    public class doGetShipmentData extends AsyncTask<Void, Void, Void> {
        Context context;
        boolean error;
        Object fault;
        DbAdapter dbadapter;

        public doGetShipmentData(Context context) {
            dbadapter = new DbAdapter(context);
            this.context = context;
            error = false;
        }

        @Override
        protected void onPreExecute() {
            dbadapter.open();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject request = new SoapObject(API_NAMESPACE,
                    "doGetShipmentData");
            request.addProperty("country-id", COUNTRY_CODE);
            request.addProperty("webapi-key", API_KEY);
            SoapObject response = null;
            Object response_object = connect_allegro(request, context);
            if (SoapObject.class.isInstance(response_object)) {
                response = (SoapObject) response_object;
            } else {
                error = true;
                fault = response_object;
                return null;
            }
            Vector<SoapObject> shipment = (Vector<SoapObject>) response
                    .getProperty(0);
            for (SoapObject a : shipment) {
                Item_Shipment ship = new Item_Shipment();
                ship.id = Integer.parseInt(a.getPropertyAsString(0));
                ship.name = a.getPropertyAsString(1);
                ship.type = Integer.parseInt(a.getPropertyAsString(2));
                SoapObject time = (SoapObject) a.getProperty(3);
                ship.time_from = Integer.parseInt(time.getPropertyAsString(0));
                ship.time_to = Integer.parseInt(time.getPropertyAsString(1));
                dbadapter.createRowShipment(ship);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dbadapter.close();
            if (error) {
                fault_code(fault, new doGetShipmentData(context), context);

            }
        }
    }

    public void doShowUser(Context context, String name, Item_User user) {
        new doShowUser(context, name, user).execute();
    }

    public class doShowUser extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        Context context;
        String name;
        SoapObject request;
        Item_User user;
        boolean error = false;
        Object fault;

        public doShowUser(Context context, String name, Item_User user) {
            this.context = context;
            this.name = name;
            this.user = user;
            error = false;
            // dialog = new ProgressDialog(context);
            // dialog.setMessage("Pobierania danych o sprzedawcy");
            // dialog.setCancelable(false);

        }

        @Override
        protected void onPreExecute() {
            // dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            request = new SoapObject(API_NAMESPACE, "doShowUser");
            request.addProperty("webapi-key", API_KEY);
            request.addProperty("country-id", COUNTRY_CODE);
            request.addProperty("user-login", name);
            SoapObject response = null;
            Object response_object = connect_allegro(request, context);
            if (SoapObject.class.isInstance(response_object)) {
                response = (SoapObject) response_object;
            } else {
                error = true;
                fault = response_object;
                return null;
            }
            user.id = Long.parseLong(response.getPropertyAsString(0));
            user.login = response.getPropertyAsString(1);
            user.country = Integer.parseInt(response.getPropertyAsString(2));
            user.create_date = Long.parseLong(response.getPropertyAsString(3));
            user.login_date = Long.parseLong(response.getPropertyAsString(4));
            user.rating = Integer.parseInt(response.getPropertyAsString(5));
            user.is_new_user = Integer
                    .parseInt(response.getPropertyAsString(6));
            user.not_activated = Integer.parseInt(response
                    .getPropertyAsString(7));
            user.closed = Integer.parseInt(response.getPropertyAsString(8));
            user.blocked = Integer.parseInt(response.getPropertyAsString(9));
            user.terminated = Integer
                    .parseInt(response.getPropertyAsString(10));
            user.has_page = Integer.parseInt(response.getPropertyAsString(11));
            user.is_sseller = Integer
                    .parseInt(response.getPropertyAsString(12));
            user.is_eco = Integer.parseInt(response.getPropertyAsString(13));

            // pozytywne komentarze
            Item_Feedback feedback = new Item_Feedback();
            SoapObject item = (SoapObject) response.getProperty(14);
            feedback.last_week = Integer.parseInt(item.getPropertyAsString(0));
            feedback.last_month = Integer.parseInt(item.getPropertyAsString(1));
            feedback.all = Integer.parseInt(item.getPropertyAsString(2));
            feedback.sold_items = Integer.parseInt(item.getPropertyAsString(3));
            feedback.buy_items = Integer.parseInt(item.getPropertyAsString(4));
            user.positive = feedback;

            // negatywne komentarze
            feedback = new Item_Feedback();
            item = (SoapObject) response.getProperty(15);
            feedback.last_week = Integer.parseInt(item.getPropertyAsString(0));
            feedback.last_month = Integer.parseInt(item.getPropertyAsString(1));
            feedback.all = Integer.parseInt(item.getPropertyAsString(2));
            feedback.sold_items = Integer.parseInt(item.getPropertyAsString(3));
            feedback.buy_items = Integer.parseInt(item.getPropertyAsString(4));
            user.negative = feedback;

            // neutralne komentarze
            feedback = new Item_Feedback();
            item = (SoapObject) response.getProperty(16);
            feedback.last_week = Integer.parseInt(item.getPropertyAsString(0));
            feedback.last_month = Integer.parseInt(item.getPropertyAsString(1));
            feedback.all = Integer.parseInt(item.getPropertyAsString(2));
            feedback.sold_items = Integer.parseInt(item.getPropertyAsString(3));
            feedback.buy_items = Integer.parseInt(item.getPropertyAsString(4));
            user.neutral = feedback;

            // dalsze informacje
            user.junior_status = Integer.parseInt(response
                    .getPropertyAsString(17));
            user.has_shop = Integer.parseInt(response.getPropertyAsString(18));
            user.company_icon = Integer.parseInt(response
                    .getPropertyAsString(19));
            user.sell_rating_count = Integer.parseInt(response
                    .getPropertyAsString(20));

            // srednia ocen sprzedazy

            Vector<SoapObject> rating_response = (Vector<SoapObject>) response
                    .getProperty(21);
            for (SoapObject a : rating_response) {
                Item_Rating_Average rating = new Item_Rating_Average();
                rating.group_title = a.getPropertyAsString(0);
                rating.rating_average = Float.parseFloat(a
                        .getPropertyAsString(1));
                user.rating_average.add(rating);
            }
            user.is_allegro_standard = Integer.parseInt(response
                    .getPropertyAsString(22));
            user.is_b2c_seller = Integer.parseInt(response
                    .getPropertyAsString(23));

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (error) {
                fault_code(fault, new doShowUser(context, name, user), context);
            }
        }
    }

    public void doBidItem(final Context context, final int is_buy_now,
                          final float price, final long item_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = MainActivity.mNavigationDrawerFragment
                .getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_bid, null);
        final EditText haslo = (EditText) view
                .findViewById(R.id.et_dialog_pass);
        builder.setMessage("Wpisz has³o aby potwierdziæ ofertê " + price)
                .setView(view).setPositiveButton("Ok", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String pass = SHA256(haslo.getText().toString());
                    if (pass.equals(pass)) {
                        dialog.dismiss();
                        new doBidItem(context, is_buy_now, price,
                                item_id).execute();
                    } else {
                        Toast.makeText(context, "B³êdne has³o!",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
        }).setNegativeButton("Anuluj", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public class doBidItem extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        Context context;
        int is_buy_now;
        float price;
        Object fault;
        long item_id;
        boolean error;
        String message;

        public doBidItem(Context context, int is_buy_now, float price,
                         long item_id) {
            this.context = context;
            this.is_buy_now = is_buy_now;
            this.price = price;
            this.item_id = item_id;
            dialog = new ProgressDialog(context);
            dialog.setMessage("£adowanie");
            dialog.setCancelable(false);
            error = false;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject request = new SoapObject(API_NAMESPACE, "doBidItem");
            request.addProperty("session-handle", SESSION_HANDLE_PART);
            request.addProperty("bid-it-id", item_id);
            request.addProperty("bid-user-price", price);
            request.addProperty("bid-quantity", 1);
            request.addProperty("bid-buy-now", is_buy_now);

            SoapObject response = null;
            Object response_object = connect_allegro(request, context);
            if (SoapObject.class.isInstance(response_object)) {
                response = (SoapObject) response_object;
            } else {
                error = true;
                fault = response_object;
                return null;
            }
            message = response.getPropertyAsString(0);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            if (error) {
                fault_code(fault, new doBidItem(context, is_buy_now, price,
                        item_id), context);
            } else {
                show_message(message, null, context, false);
            }
        }
    }

    public static void show_message(String message, String title,
                                    Context context, final boolean error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message)
                .setPositiveButton("Ok", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (error) {
                            System.exit(0);
                        } else {
                            dialog.dismiss();
                        }
                    }
                });
        builder.show();
    }

    public static String SHA256(String text) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes());
        byte[] digest = md.digest();

        return Base64.encodeToString(digest, Base64.DEFAULT);
    }

    public void start_aplikacji(Context context) {
        doQuerySysStatus(context);
    }

    public void sprawdzenie_db(Context context) {
        if (!settings.getBoolean("db_allegro", false)) {
            DbAdapter dbadapter = new DbAdapter(context);
            dbadapter.open();
            dbadapter.upgrade(1);
            dbadapter.close();
            Log.d("allegro", "pobieranie nowych danych");
            doGetShipmentData(context);
            doGetCountries(context);
            doGetCatsDataCount(context);
        }
    }

    public void get_session(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (settings.getLong("time_login", 0) < getUnixTime() - 3600) {

            doLoginEnc(context);
        } else {

            if (SESSION_HANDLE_PART == null) {

                SESSION_HANDLE_PART = settings.getString("session_handle_part",
                        "");
            }
            sprawdzenie_db(context);
        }
    }
}