package com.app.bespokino.fragment;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.MonocolorAdapter;
import com.app.bespokino.adapter.ThreadAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.model.ItemModel;
import com.app.bespokino.model.ThreadModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sunilvg on 17/07/17.
 */

public class ThreadColorDialogFragment extends DialogFragment {

    GridView gv;
    Button cancel,save;

    List monoThreadList;
    String threadColor = "";
    int preSelectedIndex = -1;
    String buttonHoleThread = "";
    int currentPos;
    ThreadModel threadModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            SharedPreferences shared = getActivity().getSharedPreferences("threadPref", MODE_PRIVATE);
            currentPos = (shared.getInt("code", 0));


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.thread_dialog,null);

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            currentPos = savedInstanceState.getInt("position", 0);
        }

        gv= (GridView) rootView.findViewById(R.id.gridview);
        cancel = (Button)rootView.findViewById(R.id.cancelButton);
        save = (Button)rootView.findViewById(R.id.saveButton);
        monoThreadList = new ArrayList();
        addThread();

        final ThreadAdapter adapter = new ThreadAdapter(getActivity(),monoThreadList);
        gv.setAdapter(adapter);


        threadModel = (ThreadModel) monoThreadList.get(currentPos);
        threadModel.setSelected(true);
        int k = currentPos;
        monoThreadList.set(currentPos, threadModel);
        buttonHoleThread = String.valueOf(threadModel.getImgCode());
        preSelectedIndex = currentPos;
        adapter .updateRecords(monoThreadList);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                threadModel = (ThreadModel) monoThreadList.get(position);
                threadModel.setSelected(true);
                currentPos = position;
                monoThreadList.set(position, threadModel);

                buttonHoleThread = String.valueOf(threadModel.getImgCode());

                if (preSelectedIndex > -1){

                    ThreadModel preRecord = (ThreadModel) monoThreadList.get(preSelectedIndex);
                    preRecord.setSelected(false);

                    monoThreadList.set(preSelectedIndex, preRecord);

                }
                preSelectedIndex = position;

                adapter .updateRecords(monoThreadList);




            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConfig.buttonholeColor = "";
                getDialog().dismiss();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConfig.buttonholeColor = buttonHoleThread;
                getDialog().dismiss();
            }
        });
        return rootView;


    }
    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    public void addThread(){




        monoThreadList.add(new ThreadModel(false,R.drawable.t6811,1));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1796,2));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6010,3));
        monoThreadList.add(new ThreadModel(false,R.drawable.t2480,4));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6614,5));


        monoThreadList.add(new ThreadModel(false,R.drawable.t1861,6));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6711,7));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6768,8));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6463,9));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6040,10));

        monoThreadList.add(new ThreadModel(false,R.drawable.t6046,11));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1502,12));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6536,13));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6537,14));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1629,15));

        monoThreadList.add(new ThreadModel(false,R.drawable.t2331,16));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6603,17));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1614,18));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6800,19));
        monoThreadList.add(new ThreadModel(false,R.drawable.t2424,20));

        monoThreadList.add(new ThreadModel(false,R.drawable.t6403,21));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1419,22));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6211,23));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6224,24));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6213,25));

        monoThreadList.add(new ThreadModel(false,R.drawable.t6211,26));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1193,27));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1700,28));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1532,29));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1987,30));

        monoThreadList.add(new ThreadModel(false,R.drawable.t1853,31));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6543,32));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6132,33));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6237,34));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6141,35));

        monoThreadList.add(new ThreadModel(false,R.drawable.t1717,36));

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPos);

    }
}
