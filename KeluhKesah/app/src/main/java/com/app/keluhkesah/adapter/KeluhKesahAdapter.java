package com.app.keluhkesah.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.keluhkesah.DetailCurhatActivity;
import com.app.keluhkesah.R;
import com.app.keluhkesah.model.KeluhKesah;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class KeluhKesahAdapter extends FirestoreRecyclerAdapter<KeluhKesah, KeluhKesahAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     */
    public KeluhKesahAdapter(@NonNull FirestoreRecyclerOptions<KeluhKesah> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull KeluhKesahAdapter.ViewHolder holder, int position, KeluhKesah model) {
        holder.txtKeluhKesah.setText(model.konten);
        holder.txtNama.setText(model.nama);
        Context context = holder.itemLayout.getContext();
        if (model.avatar != null) {
            Glide.with(context)
                    .setDefaultRequestOptions(
                            RequestOptions.placeholderOf(
                                    ContextCompat.getDrawable(
                                            context,
                                            R.drawable.ic_baseline_account_circle_24
                                    )
                            )
                    )
                    .load(model.avatar)
                    .into(holder.avatar);
        }

        model.uid = getSnapshots().getSnapshot(position).getId();

        holder.itemLayout.setOnClickListener(view -> {
            Intent it = new Intent(context, DetailCurhatActivity.class);
            it.putExtra("current_keluhkesah", model);
            context.startActivity(it);
        });
    }

    @NonNull
    @Override
    public KeluhKesahAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.curhat_item, parent, false));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtKeluhKesah, txtNama;
        ImageView avatar;
        public CardView itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtKeluhKesah = itemView.findViewById(R.id.txtItemKonten);
            avatar = itemView.findViewById(R.id.avatar);
            txtNama = itemView.findViewById(R.id.txtItemNama);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }
}
