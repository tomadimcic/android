package dunavnet.com.mojiodzaci.activities.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dunavnet.com.mojiodzaci.R;
import dunavnet.com.mojiodzaci.activities.adapters.MyEventListAdapter;
import dunavnet.com.mojiodzaci.interfaces.ResponseHandler;
import dunavnet.com.mojiodzaci.model.MyEvents;
import dunavnet.com.mojiodzaci.tasks.ImageLoader.ImageLoader;
import dunavnet.com.mojiodzaci.tasks.LoadEventsTask;

public class EventsListFragment extends Fragment implements ResponseHandler {

    public ListView mEventsView;
    public TextView emptyList;
    public ArrayList<MyEvents> mEventsList;
    public MyEventListAdapter mEventsListAdapter;
    public Button report;
    private ImageLoader imageLoader;
    Activity activity;

    // TODO: Rename and change types and number of parameters
    public static EventsListFragment newInstance() {
        EventsListFragment fragment = new EventsListFragment();
        return fragment;
    }

    public EventsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        mEventsView = (ListView) activity.findViewById(R.id.events_list);
        emptyList = (TextView) activity.findViewById(R.id.empty_list);
        report = (Button) activity.findViewById(R.id.report_button);

        mEventsList = new ArrayList<>();

        check();

        imageLoader = new ImageLoader(activity.getApplicationContext(),
                R.drawable.icon);

        //new LoadEventsTask(activity).execute();

    }

    public void check(){
        if (mEventsList.size() == 0) {
            mEventsView.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        } else {
            mEventsListAdapter = new MyEventListAdapter(activity, mEventsList);
            mEventsView.setAdapter(mEventsListAdapter);
        }
    }

    public ArrayList<MyEvents> getEventList(){
        return mEventsList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main_activity_list, null);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume(){
        super.onResume();
        check();
        new LoadEventsTask(activity, this).execute();
    }

    @Override
    public void onResponseReceived(String response) {
        mEventsList = MyEvents.toJson(response);
        if(mEventsList.size() != 0) {
            mEventsListAdapter = new MyEventListAdapter(activity, mEventsList);
            mEventsView.setAdapter(mEventsListAdapter);
            mEventsListAdapter.refreshList(mEventsList);
            mEventsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MyEvents e = mEventsList.get(position);
                    final Dialog d = new Dialog(activity);
                    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    d.setContentView(R.layout.info_content);
                    ImageView ivPhoto = (ImageView)d.findViewById(R.id.info_window_image);
                    TextView date = (TextView) d.findViewById(R.id.observation_date);
                    TextView time = (TextView) d.findViewById(R.id.observation_time_text);
                    TextView desc = (TextView) d.findViewById(R.id.observation_description_text);
                    TextView category = (TextView) d.findViewById(R.id.observation_category_text);

                    Date date1 = null;
                    String datetime = "";
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                    try {
                        date1 = format.parse(e.getDate().split(" ")[0]);

                        datetime = dateFormat.format(date1);
                    } catch (Exception ex) {
                        // TODO Auto-generated catch block
                        ex.printStackTrace();
                    }

                    date.setText(datetime);
                    time.setText(e.getTime());
                    desc.setText(e.getDescription());
                    category.setText(e.getIssueCategoryName());

                    imageLoader.setDeafultImage(R.drawable.icon);
                    imageLoader.DisplayImage(e.getImagePath(), ivPhoto);

                    d.show();
                }
            });
            mEventsView.setVisibility(View.VISIBLE);
            emptyList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
