package ch.bbcag.dotooo.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;


import ch.bbcag.dotooo.R;
import ch.bbcag.dotooo.entity.Color;

public class ColorAdapter extends BaseAdapter {
    Context context;
    Color[] colors = Color.class.getEnumConstants();
    LayoutInflater inflater;

    public ColorAdapter(Context applicationContext) {
        this.context = applicationContext;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.color_spinner_items, null);
        CardView cardView = (CardView) view.findViewById(R.id.color_card);
        TextView name = (TextView) view.findViewById(R.id.color_name);
        cardView.setCardBackgroundColor(android.graphics.Color.parseColor(colors[i].getHex()));
        name.setText(colors[i].getDisplayName());
        return view;
    }
}
