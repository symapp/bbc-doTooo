package ch.bbcag.dotooo.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import ch.bbcag.dotooo.R;
import ch.bbcag.dotooo.adapter.TaskAdapter;

public abstract class swipeCallback extends ItemTouchHelper.Callback {

    private final Paint mClearPaint;
    private final ColorDrawable mBackground;
    private final int backgroundColor;
    private final Drawable editDrawable;
    private final Drawable doneDrawable;
    private final int intrinsicWidth;
    private final int intrinsicHeight;

    protected swipeCallback(Context context) {
        mBackground = new ColorDrawable();
        backgroundColor = context.getColor(R.color.DoToooBlue);
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        editDrawable = ContextCompat.getDrawable(context, R.drawable.baseline_edit_white_48);
        doneDrawable = ContextCompat.getDrawable(context, R.drawable.baseline_done_white_48);
        assert editDrawable != null;
        intrinsicWidth = (int) (editDrawable.getIntrinsicWidth() / 1.5);
        intrinsicHeight = (int) (editDrawable.getIntrinsicHeight() / 1.5);
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof TaskAdapter.ViewHolderTask) {
            return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        return makeMovementFlags(0, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, false);
            return;
        }


        mBackground.setColor(backgroundColor);

        int editIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int editIconMargin = (itemHeight - intrinsicHeight) / 2;
        int editIconBottom = editIconTop + intrinsicHeight;

        int editIconLeft;
        int editIconRight;
        if (dX < 0) {
            mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            mBackground.draw(c);

            editIconLeft = (int) (itemView.getRight() + editIconMargin * 2 + dX);
            editIconRight = (int) (itemView.getRight() + intrinsicWidth + editIconMargin * 2 + dX);

            if (editIconLeft < itemView.getRight() / 2 - intrinsicWidth / 2) {
                editIconLeft = itemView.getRight() / 2 - intrinsicWidth / 2;
                editIconRight = itemView.getRight() / 2 + intrinsicWidth / 2;
            }

            editDrawable.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom);
            editDrawable.draw(c);
        } else {
            mBackground.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
            mBackground.draw(c);

            editIconLeft = (int) (itemView.getLeft() + dX - editIconMargin * 2 - intrinsicWidth);
            editIconRight = (int) (itemView.getLeft() + dX - editIconMargin * 2);

            if (editIconLeft > itemView.getRight() / 2 - intrinsicWidth / 2) {
                editIconLeft = itemView.getRight() / 2 - intrinsicWidth / 2;
                editIconRight = itemView.getRight() / 2 + intrinsicWidth / 2;
            }

            doneDrawable.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom);
            doneDrawable.draw(c);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }
}
