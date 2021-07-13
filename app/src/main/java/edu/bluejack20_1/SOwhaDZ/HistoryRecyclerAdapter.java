package edu.bluejack20_1.SOwhaDZ;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Model.History;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryViewHolder>{
        private ArrayList<History> data1,data2;
//        private String data1[], data2[];
        private Context context;

        public HistoryRecyclerAdapter(Context ctx, ArrayList<History> s1, ArrayList<History> s2){
            context = ctx;
            data1 = s1;
            data2 = s2;
        }

    @NonNull
    @Override
            public HistoryRecyclerAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.history_recycler, parent, false);
                return new HistoryViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull HistoryRecyclerAdapter.HistoryViewHolder holder, final int position) {
                holder.history_from.setText(data1.get(position).getWord());
                holder.history_to.setText(data2.get(position).getDef());

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DetailTranslationActivity.class);
                        intent.putExtra("data1", data1.get(position).getWord());
                        intent.putExtra("data2", data1.get(position).getDef());
                        context.startActivity(intent);
                    }
                });


            }

    @Override
    public int getItemCount() {
        return data2.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView history_from, history_to;
        LinearLayout mainLayout;

                public HistoryViewHolder(@NonNull View itemView) {
                    super(itemView);
                    history_from = itemView.findViewById(R.id.history_from);
                    history_to = itemView.findViewById(R.id.history_to);

                    mainLayout = itemView.findViewById(R.id.mainLayout);
                }
            }


    public void showMenu(int position) {
        notifyDataSetChanged();
    }
}
