package citizenm.citizenm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by markrose on 12/30/16.
 */

public class issueAdaptor extends RecyclerView.Adapter<issueAdaptor.MyIssueHolder> {

    Context c=getApplicationContext();
    private List<issue> issueList;

        public class MyIssueHolder extends RecyclerView.ViewHolder {
            public TextView headline, age, fulltext;
            public ImageView url;

            public MyIssueHolder(View view) {
                super(view);
                headline = (TextView) view.findViewById(R.id.headline);
                age = (TextView) view.findViewById(R.id.age);
                fulltext = (TextView) view.findViewById(R.id.fulltext);
                url=(ImageView) view.findViewById(R.id.url);
            }
        }

        public issueAdaptor(List<issue> issueList) {
            this.issueList = issueList;
        }

        @Override
        public MyIssueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.issue, parent, false);

            return new MyIssueHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyIssueHolder holder, int position) {
            issue issue = issueList.get(position);
            //holder.url.setText(issue.getUrl());
            holder.headline.setText(issue.getHeadline());
            holder.fulltext.setText(issue.getFulltext());
            holder.age.setText(issue.getAge());
            String urlx=issue.getUrl();
            Picasso.with(c).load(urlx).into(holder.url);
        }

        @Override
        public int getItemCount() {
            return issueList.size();
        }

}
