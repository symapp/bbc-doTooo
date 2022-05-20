package ch.bbcag.dotooo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ch.bbcag.dotooo.R;
import ch.bbcag.dotooo.entity.Color;

public class ColorAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Color> colors;
    private LayoutInflater inflater;

    public ColorAdapter(Context applicationContext, boolean haveAll) {
        this.context = applicationContext;
        inflater = (LayoutInflater.from(applicationContext));

        colors = new ArrayList<>();
        if (haveAll) colors.add(null);
        colors.addAll(Arrays.asList(Objects.requireNonNull(Color.class.getEnumConstants())));
    }

    @Override
    public int getCount() {
        return colors.size();
    }

    @Override
    public Object getItem(int i) {
        return colors.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.color_spinner_items, null);

        CardView cardView = (CardView) view.findViewById(R.id.color_card);
        TextView name = (TextView) view.findViewById(R.id.color_name);

        if (colors.get(i) != null) {
            cardView.setCardBackgroundColor(android.graphics.Color.parseColor(colors.get(i).getHex()));
            name.setText(colors.get(i).getDisplayName());
        } else {
            cardView.setCardBackgroundColor(android.graphics.Color.parseColor("#d1d1d1"));
            name.setText("All");
            cardView.setRadius(100);
        }

        return view;
    }
}
