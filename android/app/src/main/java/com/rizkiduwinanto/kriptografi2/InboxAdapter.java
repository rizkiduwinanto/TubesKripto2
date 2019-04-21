package com.rizkiduwinanto.kriptografi2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {
    private List<InboxModel> inboxModelList;
    private Context context;

    public InboxAdapter(List<InboxModel> inboxModelList, Context context) {
        this.inboxModelList = inboxModelList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inbox_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        InboxModel inboxModel = inboxModelList.get(position);

        Random rand = new Random();
        int color = Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        ((GradientDrawable) viewHolder.textCircle.getBackground()).setColor(color);

        viewHolder.textCircle.setText(inboxModel.getCircleText());
        viewHolder.textHead.setText(inboxModel.getHeadText());
        viewHolder.textSub.setText(inboxModel.getSubText());
        viewHolder.textDes.setText(inboxModel.getDesText());
        viewHolder.textDate.setText(inboxModel.getDateText());
    }

    @Override
    public int getItemCount() {
        return inboxModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textCircle;
        public TextView textHead;
        public TextView textSub;
        public TextView textDes;
        public TextView textDate;


        public ViewHolder(View itemView) {
            super(itemView);

            textCircle = itemView.findViewById(R.id.circleText);
            textHead = itemView.findViewById(R.id.headText);
            textSub = itemView.findViewById(R.id.subText);
            textDes = itemView.findViewById(R.id.desText);
            textDate = itemView.findViewById(R.id.dateText);
        }
    }
}
