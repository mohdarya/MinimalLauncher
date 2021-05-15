package com.minimallauncher;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link landing_page.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link landing_page#newInstance} factory method to
 * create an instance of this fragment.
 */

//TODO: add the timer to show the phone usage time, currently set to invisible
public class landing_page extends Fragment
{

    static int timesLaunched;
    static long lastLaunchedTime = 0;
    static long restrictedLaunched = 0;
    static Thread restrictedLaunchThread;
    Calendar calendar = Calendar.getInstance();
    landing_page_recycler_adapter adapter;
    static landing_page_recycler_adapter copyOfAdapter;
    static boolean restricedPressed = false;
    private OnFragmentInteractionListener mListener;
    private List<App> regularApps;

    public landing_page()
    {
        // Required empty public constructor
    }


    public static landing_page newInstance(String param1, String param2)
    {
        landing_page fragment = new landing_page();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.landing_page, container, false);
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        RecyclerView deckrv = (RecyclerView) view.findViewById(R.id.regular_apps_recycler);

        adapter = new landing_page_recycler_adapter(getApps());
        deckrv.setAdapter(adapter);
        setCopyOfAdapter();
        deckrv.setLayoutManager(new GridLayoutManager(getContext(), 1));

        adapter.setOnItemClickListener(new landing_page_recycler_adapter.OnItemClickListener()
        {
            @Override
            public void onAppClicked(int position)
            {
                Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(regularApps.get(position).getPackageName());
                getContext().startActivity(intent);
                MainActivity.categoryLaunched = "N";


            }
        });
        Button allApps = view.findViewById(R.id.all_button);
        allApps.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(!restricedPressed)
                {
                    Navigation.findNavController(view).navigate(landing_pageDirections.actionLandingPageToAllApps());
                    MainActivity.fragmentLaunched++;
                }
            }
        });
        final Button restrictedApps = view.findViewById(R.id.restricted_button);
        restrictedApps.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v)
            {

                calendar = Calendar.getInstance();
                Log.e("last launched ", Long.toString(lastLaunchedTime));
                Log.e("current time ", Long.toString(calendar.getTimeInMillis()));
                Log.e("time Passed ", Long.toString(calendar.getTimeInMillis() - lastLaunchedTime));
                if (calendar.getTimeInMillis() - lastLaunchedTime > 3600000 * 2)
                {

                    timesLaunched = 0;
                    Toast.makeText(MainActivity.context, "Wait time multiplier reset", Toast.LENGTH_SHORT).show();
                    Log.e("last launched ", "reset");

                }
                if (lastLaunchedTime - restrictedLaunched > 1020000 && restrictedLaunched != 0)
                {
                    timesLaunched += ((lastLaunchedTime - restrictedLaunched) / 300000);
                    restrictedLaunched = 0;
                    Log.e("times launched ", "incremented by " + ((lastLaunchedTime - restrictedLaunched) / 300000));
                }
                Random random = new Random();
                int waitTime = random.nextInt(15000 - 10000) + 10000;


                if (!restricedPressed)
                {
                    restricedPressed = true;
                  final Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                   vib.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                  //  Toast.makeText(getContext(),"Launching Restricted Apps", Toast.LENGTH_LONG).show();
                    setRestrictedLaunchThread(waitTime, view);
                    restrictedLaunchThread.start();
                    Log.e("Restricted Launch Thread ", "Thraed Started");
                    Log.e("Restricted Launch wait time ", TimeUnit.MILLISECONDS.toSeconds(waitTime + 10000 * timesLaunched) + " seconds");
                    if (waitTime + 10000 * timesLaunched > 60000)
                    {
                        Toast.makeText(getContext(), "wait time: " + String.format("%d mins",
                                TimeUnit.MILLISECONDS.toMinutes(waitTime + 10000 * timesLaunched)
                        ), Toast.LENGTH_LONG).show();
                    }


                }

            }
        });

        TextClock clock = view.findViewById(R.id.landing_page_clock);
        clock.setTypeface(MainActivity.font);
        TextView textViewTimer = view.findViewById(R.id.time_remaining);
        textViewTimer.setTypeface(MainActivity.font);
        if(MainActivity.timeRemainingString == null)
        {
            textViewTimer.setText("00:00:00");
        }
        else
        {
            textViewTimer.setText(MainActivity.timeRemainingString);
        }

    }

    private void setCopyOfAdapter()
    {
        copyOfAdapter = adapter;
    }


    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {

        void onFragmentInteraction(Uri uri);
    }

    List<App> getApps()
    {
        MainActivity activity = (MainActivity) getActivity();
        regularApps = new ArrayList<App>()
        {
            public boolean add(App data)
            {
                super.add(data);
                Collections.sort(regularApps, new Comparator<App>()
                {
                    @Override
                    public int compare(App o1, App o2)
                    {
                        return o1.getApplicationName().toLowerCase().compareTo(o2.getApplicationName().toLowerCase());
                    }
                });
                return true;
            }
        };
        for (int i = 0; i < activity.allApplications.size(); i++)
        {
            if (activity.allApplications.get(i).isRegular())
            {
                regularApps.add(activity.allApplications.get(i));
            }
        }
        return regularApps;
    }

    private void setRestrictedLaunchThread(int WaitTime, View appView)
    {
        final int waitTime = WaitTime;
        final View view = appView;
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {


                    Thread.sleep(waitTime + 10000 * timesLaunched);
                    KeyguardManager km = (KeyguardManager) MainActivity.context.getSystemService(Context.KEYGUARD_SERVICE);
                    boolean locked = km.inKeyguardRestrictedInputMode();

                    if (locked || !MainActivity.isActivityVisible())
                    {
                        restricedPressed = false;
                        Log.e("system status ", "locked");
                    }
                    if (!locked && MainActivity.isActivityVisible())
                    {

                            Navigation.findNavController(view).navigate(landing_pageDirections.actionLandingToRestricted());
                            MainActivity.fragmentLaunched++;
                            restricedPressed = false;
                            calendar = Calendar.getInstance();
                            restrictedLaunched = calendar.getTimeInMillis();



                    }

                }
                catch (InterruptedException consumed)
                {
                    Log.e("Restricted Launch Thread ", "Thraed Stopped");
                    Message message = new Message();
                    message.arg1 = 1;
                    message.obj = "Restricted section launch stopped";
                    MainActivity.mhandler.sendMessage(message);
                    restricedPressed = false;

                }
            }
        };

        restrictedLaunchThread = new Thread(runnable);

    }


    static public void setlastLaunchedTime(long lastLaunchedTimeToSet)
    {
        lastLaunchedTime = lastLaunchedTimeToSet;
    }
    static public void setlastRestrictedLaunch(long lastLaunchedTimeToSet)
    {
        restrictedLaunched = lastLaunchedTimeToSet;
    }

    static public void setTimesLaunched(int toSet){
        timesLaunched = toSet;
    }


}
