package tcss450.uw.edu.team8app.connections;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import tcss450.uw.edu.team8app.R;
import tcss450.uw.edu.team8app.model.Connection;
import tcss450.uw.edu.team8app.utils.SendPostAsyncTask;
import tcss450.uw.edu.team8app.utils.WaitFragment;

public class ConnectionsRecyclerViewAdapter  extends RecyclerView.Adapter<ConnectionsRecyclerViewAdapter.ViewHolder> implements WaitFragment.OnFragmentInteractionListener{

    private final List<Connection> mData;
    private Context mContext;

    public ConnectionsRecyclerViewAdapter(List<Connection> data, Context context) {
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_connection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mData.get(position);
        holder.mFirstView.setText(mData.get(position).getFirstName());
        holder.mLastView.setText(mData.get(position).getLastName());
        holder.mUsernameView.setText(mData.get(position).getUsername());
        holder.mEmailView.setText(mData.get(position).getEmail());
        holder.mVerifiedView.setText("" + mData.get(position).getVerified());
        holder.mSenderView.setText("" + mData.get(position).getSender());
        if (mData.get(position).getVerified() == 1) {
            holder.mButton.setText("Send Message");
        } else {
            if (mData.get(position).getSender() == 1) {
                holder.mButton.setText("Cancel Invite");
                holder.mDeleteButton.setVisibility(View.INVISIBLE);
            } else if (mData.get(position).getSender() == 2) {
                holder.mButton.setText("Accept Invite");
            } else {
                holder.mDeleteButton.setVisibility(View.INVISIBLE);
            }
        }
        holder.mButton.setOnClickListener(view -> primaryButtonOnClick(view, mData.get(position).getEmail(), position));
        holder.mDeleteButton.setOnClickListener(view -> deleteButtonOnClick(view, mData.get(position).getEmail(), position));
    }

    private void deleteButtonOnClick(View view, String email, int position) {
        buttonHelper(mContext.getString(R.string.ep_remove), email, position);
    }

    private void primaryButtonOnClick(View view, String email, int position) {
        Button button = (Button) view;
        if (button.getText().toString().equals("Send Message")) {
            // TODO
        } else if (button.getText().toString().equals("Cancel Invite")) {
            buttonHelper(mContext.getString(R.string.ep_remove), email, position);
        } else if (button.getText().toString().equals("Accept Invite")) {
            buttonHelper(mContext.getString(R.string.ep_add), email, position);
        } else {
            buttonHelper(mContext.getString(R.string.ep_add), email, position);
        }
    }

    private void buttonHelper(String endpoint, String email, int position) {
        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme(mContext.getString(R.string.ep_scheme))
                .encodedAuthority(mContext.getString(R.string.ep_base_url))
                .appendPath(mContext.getString(R.string.ep_connections))
                .appendPath(endpoint);
        Uri uri = uriBuilder.build();
        JSONObject msg = new JSONObject();
        try {
            msg.put("token", FirebaseInstanceId.getInstance().getToken());
            msg.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleButtonOnPre)
                .onPostExecute(view -> handleButtonOnPost(position))
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private void handleButtonOnPost(int position) {
        //notifyItemChanged(position);
        //Log.e("ASYNC_TASK_ERROR", "TESSSSSSSSSSSSSSSSSSST");
        //notifyItemChanged(position);
        //notifyDataSetChanged();
    }

    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR", result);
        onWaitFragmentInteractionHide();
    }

    private void handleButtonOnPre() {
        onWaitFragmentInteractionShow();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onWaitFragmentInteractionShow() {

    }

    @Override
    public void onWaitFragmentInteractionHide() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mFirstView;
        final TextView mLastView;
        final TextView mUsernameView;
        final TextView mEmailView;
        final TextView mVerifiedView;
        final TextView mSenderView;
        final Button mButton;
        final ImageButton mDeleteButton;
        Connection mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mFirstView = view.findViewById(R.id.textView_recyclerView_connection_firstName);
            mLastView = view.findViewById(R.id.textView_recyclerView_connection_lastName);
            mUsernameView = view.findViewById(R.id.textView_recyclerView_connection_username);
            mEmailView = view.findViewById(R.id.textView_recyclerView_connection_email);
            mVerifiedView = view.findViewById(R.id.textView_recyclerView_connection_verified);
            mSenderView = view.findViewById(R.id.textView_recyclerView_connection_sender);
            mButton = view.findViewById(R.id.button_recyclerView_connection);
            mDeleteButton = view.findViewById(R.id.imageButton_recyclerView_connection_delete);
        }
    }
}
