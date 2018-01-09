package com.skylix.videostatus.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skylix.videostatus.Gif.CategorySpinnerAdapter;
import com.skylix.videostatus.Gif.LanguageMainAdapter;
import com.skylix.videostatus.Gif.LanguageSpinnerAdapter;
import com.skylix.videostatus.ModelClass.CategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ir.hamsaa.RtlMaterialSpinner;

/**
 * Created by SkylixPC 1 on 02-09-2017.
 */

public class Functions {


    public static final ArrayList<CategoryModel> CategorySpinnerArrayList = new ArrayList<CategoryModel>();
    public static final ArrayList<CategoryModel> LanguageSpinnerArrayList = new ArrayList<CategoryModel>();

    /*--------------------------------------------Getting DP Spinner Start-----------------------------------------------*/

    public static void GetCategorySpinnerData(final Activity activity, final RtlMaterialSpinner sp1) {

        StringRequest stringRequest = new StringRequest(Config.URL_FILL_CATEGORY_SPINNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CategorySpinnerArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_ARRAY);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    CategoryModel cm = new CategoryModel();
                                    cm.setCat_id(jsonObject1.getInt(Config.KEY_CATEGORY_ID));
                                    cm.setCat_type(jsonObject1.getString(Config.KEY_CATEGORY_TYPE));
                                    Log.d("Result json object", jsonObject1.toString());
                                    CategorySpinnerArrayList.add(cm);
                                    Log.d("Result", String.valueOf(CategorySpinnerArrayList));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            CategorySpinnerAdapter categorySpinnerAdapter = new CategorySpinnerAdapter(activity, CategorySpinnerArrayList);
                            sp1.setAdapter(categorySpinnerAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    /*--------------------------------------------Getting DP Spinner Stop------------------------------------------------*/

    /*-------------------Getting Gif Spinner Start----------------------*/

    public static void GetLanguageSpinnerData(final Activity activity, final RtlMaterialSpinner sp1) {

        StringRequest stringRequest = new StringRequest(Config.URL_FILL_LANGUAGE_SPINNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LanguageSpinnerArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_ARRAY);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    CategoryModel cm = new CategoryModel();
                                    cm.setId(jsonObject1.getInt(Config.KEY_ID));
                                    cm.setLanguage(jsonObject1.getString(Config.KEY_LANGUAGE));
                                    Log.d("Result json object", jsonObject1.toString());
                                    LanguageSpinnerArrayList.add(cm);
                                    Log.d("Result", String.valueOf(LanguageSpinnerArrayList));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            LanguageSpinnerAdapter languageSpinnerArrayList = new LanguageSpinnerAdapter(activity, LanguageSpinnerArrayList);
                            sp1.setAdapter(languageSpinnerArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
    /*-------------------Getting Gif Spinner Stop----------------------*/

    /*-------------------Getting Gif Spinner Start----------------------*/

    public static void updateGifCounter(final Activity activity, final String urlUpdateVideoMaster,
                                        final String keyId, final String id,
                                        final String keyLikes, final String Likes,
                                        final String KeyDislikes, final String Dislikes,
                                        final String KeyFavCounter, final String FavCounter
                                        ) {

        class Updategifcounter extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(keyId, id);
                hashMap.put(keyLikes, Likes);
                hashMap.put(KeyDislikes, Dislikes);
                hashMap.put(KeyFavCounter, FavCounter);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(urlUpdateVideoMaster, hashMap);
                return s;

            }
        }

        Updategifcounter updategifcounter = new Updategifcounter();
        updategifcounter.execute();
    }

    /*-------------------Getting Gif Spinner Stop----------------------*/

    /*-------------------Getting STATUS Spinner Start----------------------*/

//    public static void GetStatusSpinnerData(final Activity activity, final Spinner sp1) {
//        StatusSpinnerArrayList.clear();
//        StringRequest stringRequest = new StringRequest(Config.STATUS_FILL_CATGORY_SPINNER,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_ARRAY);
//                            for (int i = 0; i < response.length(); i++) {
//                                try {
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                    StatusModel statusModel = new StatusModel();
//                                    statusModel.setStatus_id(jsonObject1.getInt(Config.KEY_STATUS_ID));
//                                    statusModel.setStatus_type(jsonObject1.getString(Config.KEY_STATUS_TYPE));
//                                    Log.d("Result", jsonObject.toString());
//                                    StatusSpinnerArrayList.add(statusModel);
//                                    Log.d("Result", String.valueOf(StatusSpinnerArrayList));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            StatusSpinnerAdapter statusSpinnerAdapter = new StatusSpinnerAdapter(activity, StatusSpinnerArrayList);
//                            sp1.setAdapter(statusSpinnerAdapter);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//
//        //Creating a request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//
//        //Adding request to the queue
//        requestQueue.add(stringRequest);
//    }
//
//    /*-------------------Getting STATUS Spinner Stop----------------------*/
//
//    /*-------------------Getting JOKE Spinner Start----------------------*/
//
//
//    public static void GetJokeSpinnerData(final Activity activity, final Spinner sp1) {
//        JokeSpinnerArrayList.clear();
//        StringRequest stringRequest = new StringRequest(Config.JOKE_FILL_CATGORY_SPINNER,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_ARRAY);
//
//                            for (int i = 0; i < response.length(); i++) {
//                                try {
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                    JokeModel jokeModel = new JokeModel();
//                                    jokeModel.setJoke_id(jsonObject1.getInt(Config.KEY_JOKE_ID));
//                                    jokeModel.setJoke_type(jsonObject1.getString(Config.KEY_JOKE_TYPE));
//
//                                    Log.d("Result", jsonObject.toString());
//                                    JokeSpinnerArrayList.add(jokeModel);
//                                    Log.d("Result", String.valueOf(JokeSpinnerArrayList));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            JokeSpinnerAdapter jokeSpinnerAdapter = new JokeSpinnerAdapter(activity, JokeSpinnerArrayList);
//                            sp1.setAdapter(jokeSpinnerAdapter);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//
//        //Creating a request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//
//        //Adding request to the queue
//        requestQueue.add(stringRequest);
//    }
//
//    /*-------------------Getting JOKE Spinner Stop----------------------*/
//
//    /*-------------------Getting Dp Sub Category Spinner Start----------------------*/
//
//    public static void GetDpSubCategorySpinnerData(final Activity activity, final Spinner spinner) {
//        DpSpinnerArrayList.clear();
//        StringRequest stringRequest = new StringRequest(Config.DP_FILL_SUB_CATEGORY_SPINNER + "dp",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_ARRAY);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                try {
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                    DPModel dpModel = new DPModel();
//                                    dpModel.setDp_sub_id(jsonObject1.getInt(Config.KEY_DP_SUB_ID));
//                                    dpModel.setDp_sub_cat(jsonObject1.getString(Config.KEY_DP_SUB_CAT));
//                                    Log.d("Result json object", jsonObject1.toString());
//                                    DpSpinnerArrayList.add(dpModel);
//                                    Log.d("Result", String.valueOf(DpSpinnerArrayList));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            DpSpinnerSubCategoryAdapter dpSpinnerAdapter = new DpSpinnerSubCategoryAdapter(activity, DpSpinnerArrayList);
//                            spinner.setAdapter(dpSpinnerAdapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(stringRequest);
//    }
//
//    /*-------------------Getting Dp Sub Category Spinner End----------------------*/
//
//    /*-------------------Getting Dp Sub Category Spinner Start----------------------*/
//
//    public static void GetGifSubCategorySpinnerData(final Activity activity, final Spinner spinner) {
//        GifSpinnerArrayList.clear();
//        StringRequest stringRequest = new StringRequest(Config.DP_FILL_SUB_CATEGORY_SPINNER + "gif",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_ARRAY);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                try {
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                    CategoryModel gm = new CategoryModel();
//                                    gm.setGif_sub_id(jsonObject1.getInt(Config.KEY_GIF_SUB_ID));
//                                    gm.setGif_sub_cat(jsonObject1.getString(Config.KEY_GIF_SUB_CAT));
//                                    Log.d("Result json object", jsonObject1.toString());
//                                    GifSpinnerArrayList.add(gm);
//                                    Log.d("Result", String.valueOf(GifSpinnerArrayList));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            GifSpinnerSubCategoryAdapter gifSpinnerSubCategoryAdapter = new GifSpinnerSubCategoryAdapter(activity, GifSpinnerArrayList);
//                            spinner.setAdapter(gifSpinnerSubCategoryAdapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(stringRequest);
//    }
//
//    /*-------------------Getting Dp Sub Category Spinner End----------------------*/
//
//    /*-------------------Getting Dp Sub Category Spinner Start----------------------*/
//
//    public static void GetStatusSubCategorySpinnerData(final Activity activity, final Spinner spinner) {
//
//        StatusSpinnerArrayList.clear();
//        StringRequest stringRequest = new StringRequest(Config.DP_FILL_SUB_CATEGORY_SPINNER + "status",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_ARRAY);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                try {
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                    StatusModel sm = new StatusModel();
//                                    sm.setStatus_sub_id(jsonObject1.getInt(Config.KEY_STATUS_SUB_ID));
//                                    sm.setStatus_sub_cat(jsonObject1.getString(Config.KEY_STATUS_SUB_CAT));
//                                    Log.d("Result json object", jsonObject1.toString());
//                                    StatusSpinnerArrayList.add(sm);
//                                    Log.d("Result", String.valueOf(StatusSpinnerArrayList));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            StatusSpinnerSubCategoryAdapter statusSpinnerSubCategoryAdapter = new StatusSpinnerSubCategoryAdapter(activity, StatusSpinnerArrayList);
//                            spinner.setAdapter(statusSpinnerSubCategoryAdapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(stringRequest);
//    }
//
//    /*-------------------Getting Dp Sub Category Spinner End----------------------*/
//
//    /*-------------------Getting Dp Sub Category Spinner Start----------------------*/
//
//    public static void GetJokeSubCategorySpinnerData(final Activity activity, final Spinner spinner) {
//
//        JokeSpinnerArrayList.clear();
//        StringRequest stringRequest = new StringRequest(Config.DP_FILL_SUB_CATEGORY_SPINNER + "joke",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_ARRAY);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                try {
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                    JokeModel jm = new JokeModel();
//                                    jm.setJoke_sub_id(jsonObject1.getInt(Config.KEY_JOKE_SUB_ID));
//                                    jm.setJoke_sub_cat(jsonObject1.getString(Config.KEY_JOKE_SUB_CAT));
//                                    Log.d("Result json object", jsonObject1.toString());
//                                    JokeSpinnerArrayList.add(jm);
//                                    Log.d("Result", String.valueOf(StatusSpinnerArrayList));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            JokeSpinnerSubCategoryAdapter jokeSpinnerSubCategoryAdapter = new JokeSpinnerSubCategoryAdapter(activity, JokeSpinnerArrayList);
//                            spinner.setAdapter(jokeSpinnerSubCategoryAdapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(stringRequest);
//    }
//
//    /*-------------------Getting Dp Sub Category Spinner End----------------------*/



//
//    public static void updateDpCounter(final Activity activity, final String keyId, final String id, final String keyCounter, final String counter, final String urlDpFavMaster) {
//
//        class Updatedpcounter extends AsyncTask<Void, Void, String> {
//            ProgressDialog loading;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(activity, "Updating...", "Wait...", false, false);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put(keyId, id);
//                hashMap.put(keyCounter, counter);
//
//                RequestHandler rh = new RequestHandler();
//                String s = rh.sendPostRequest(urlDpFavMaster, hashMap);
//                return s;
//
//            }
//        }
//
//        Updatedpcounter updatedpcounter = new Updatedpcounter();
//        updatedpcounter.execute();
//    }
//
//    public static void updateStatusCounter(final Activity activity, final String keyId, final String id, final String keyCounter, final String counter, final String urlStatusFavMaster) {
//
//        class Updatestatuscounter extends AsyncTask<Void, Void, String> {
//            ProgressDialog loading;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(activity, "Updating...", "Wait...", false, false);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put(keyId, id);
//                hashMap.put(keyCounter, counter);
//
//                RequestHandler rh = new RequestHandler();
//                String s = rh.sendPostRequest(urlStatusFavMaster, hashMap);
//                return s;
//
//            }
//        }
//
//        Updatestatuscounter updatestatuscounter = new Updatestatuscounter();
//        updatestatuscounter.execute();
//    }
//
//    public static void updateJokeCounter(final Activity activity, final String keyId, final String id, final String keyCounter, final String counter, final String urlJokeFavMaster) {
//
//        class Updatjokecounter extends AsyncTask<Void, Void, String> {
//            ProgressDialog loading;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(activity, "Updating...", "Wait...", false, false);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put(keyId, id);
//                hashMap.put(keyCounter, counter);
//
//                RequestHandler rh = new RequestHandler();
//                String s = rh.sendPostRequest(urlJokeFavMaster, hashMap);
//                return s;
//
//            }
//        }
//
//        Updatjokecounter updatjokecounter = new Updatjokecounter();
//        updatjokecounter.execute();
//    }
//
//    public static void deleteSubCategory(final Activity activity, final String keyId, final String id, final String urlJokeFavMaster) {
//
//        class DeleteSubCategory extends AsyncTask<Void, Void, String> {
//            ProgressDialog loading;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(activity, "Updating...", "Wait...", false, false);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put(keyId, id);
//
//                RequestHandler rh = new RequestHandler();
//                String s = rh.sendPostRequest(urlJokeFavMaster, hashMap);
//                return s;
//
//            }
//        }
//        DeleteSubCategory deleteSubCategory = new DeleteSubCategory();
//        deleteSubCategory.execute();
//    }
//
//    public static void countSubCategoryrow(final TextView textView, final String keyType,
//                                           final String url, final String extension) {
//
//
//        class JSONTask extends AsyncTask<String, Void, String> {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//                return Config.readURL(params[0]);
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_ARRAY);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        if (jsonObject1.getString(keyType)!= null) {
//                            textView.setText(jsonObject1.getString(keyType) + " " + extension);
//                            Log.d("DpSubCategory", "DpSubCategoryMainAdapter" + jsonObject1.getString(keyType));
//                            Log.d("DpSubCategory", "Length:----" + jsonArray.length());
//
//                        }else {
//                            textView.setText("0 " + extension);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        }
//        new JSONTask().execute(url);
//    }


}