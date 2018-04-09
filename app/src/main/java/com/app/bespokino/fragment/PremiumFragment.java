package com.app.bespokino.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.FabricAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.RecyclerItemClickListener;
import com.app.bespokino.model.FabricModel;
import com.app.bespokino.model.ItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static com.app.bespokino.helper.AppConfig.fabricImageSelected;

/**
 * A simple {@link Fragment} subclass.
 */
public class PremiumFragment extends Fragment {

    private RecyclerView recyclerView;
    private FabricAdapter adapter;
    private List<FabricModel> iFabric;
    Dialog dialog;
    Bundle bundle;
    private SpotsDialog progressDialog;


    public PremiumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_included, container, false);

        // new FetchFabricImageTask().execute();
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_images);
       // progressDialog = new SpotsDialog(getActivity(),R.style.Custom1);
        bundle = new Bundle();
        /*mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);*/

        final FragmentManager fm = getFragmentManager();
        final ShirtDisplyDialogFragment shirtDisplyDialogFragment = new ShirtDisplyDialogFragment();

        iFabric = new ArrayList<>();
        //adapter = new FabricAdapter(getActivity(), iFabric);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }

        //  RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        // recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        FabricModel fabricModel = iFabric.get(position);
                        iFabric.size();
                        // Toast.makeText(getActivity(),position+"clicked", Toast.LENGTH_LONG).show();
                        String img = fabricModel.getShirt_img();
                        bundle.putString("url",img);
                        bundle.putInt("fabId",fabricModel.getFabricID());
                        bundle.putString("type","premium");

                        shirtDisplyDialogFragment.setArguments(bundle);
                        shirtDisplyDialogFragment.show(fm,"shirtdisply");

                    }
                })
        );

        new FetchFabricImageTask().execute();
        return view;
    }

    public class FetchFabricImageTask extends AsyncTask<Object, Object, String> {


        @Override
        protected String doInBackground(Object... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String result = null;

            try {

                // URL url = new URL(AppConfig.serverConnectionURL+"fabricCategoryAPI/include");
                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=populateFabrics&categoryID=2");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                result = buffer.toString();
                return result;
            } catch (IOException e) {
                Log.e("Include Fragment", "Error ", e);

                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Include", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //progressDialog.dismiss();



            if(s==null){

                showMessage("Alert","Check your internet connection !");

            }else {

                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = null;
                    boolean error = false;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject = (JSONObject) jsonArray.get(i);

                        error = jsonObject.getBoolean("Error");

                        if (!error) {
                            String image = jsonObject.getString("image");
                            String shirtImage = jsonObject.getString("image");
                            int fabID = jsonObject.getInt("fabricID");

                            iFabric.add(new FabricModel("http://www.bespokino.com/images/fabric/thumb/" + image, "http://www.bespokino.com/images/fabric/" + image, fabID));

                        }
                    }
                    adapter = new FabricAdapter(getActivity(), iFabric);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }




    public void showMessage(String title,String message){

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // new FetchFabricImageTask().execute();
                getActivity().finish();
            }
        });
        builder.show();

    }

}
