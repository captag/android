package eu.captag.util;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import eu.captag.R;


/**
 * A {@link RecyclerView.ItemDecoration} implementation that draws dividers between item views.
 * @author Ulrich Raab
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {


   public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
   public static final int VERTICAL = LinearLayoutManager.VERTICAL;

   private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

   private Drawable dividerDrawable;
   private int orientation;


   public DividerItemDecoration (Context context, int orientation) {

      TypedArray a = context.obtainStyledAttributes(ATTRS);
      dividerDrawable = a.getDrawable(0);
      a.recycle();

      setOrientation(orientation);
   }


   public void setOrientation (int orientation) {

      if (orientation != HORIZONTAL && orientation != VERTICAL) {
         throw new IllegalArgumentException("invalid orientation");
      }

      this.orientation = orientation;
   }


   @Override
   public void onDraw (Canvas c, RecyclerView parent, RecyclerView.State state) {

      switch (orientation) {
         case HORIZONTAL: {
            drawHorizontal(c, parent);
            break;
         }
         case VERTICAL: {
            drawVertical(c, parent);
            break;
         }
      }
   }


   @Override
   public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

      if (orientation == VERTICAL) {
         outRect.set(0, 0, 0, dividerDrawable.getIntrinsicHeight());
      } else {
         outRect.set(0, 0, dividerDrawable.getIntrinsicWidth(), 0);
      }
   }


   public void drawVertical (Canvas c, RecyclerView parent) {

      int left = parent.getPaddingLeft();
      int right = parent.getWidth() - parent.getPaddingRight();

      int childCount = parent.getChildCount();
      for (int i = 0; i < childCount; i++) {
         View child = parent.getChildAt(i);
         if (!isSubheader(child)) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + dividerDrawable.getIntrinsicHeight();
            dividerDrawable.setBounds(left, top, right, bottom);
            dividerDrawable.draw(c);
         }
      }
   }


   public void drawHorizontal (Canvas c, RecyclerView parent) {

      int top = parent.getPaddingTop();
      int bottom = parent.getHeight() - parent.getPaddingBottom();

      int childCount = parent.getChildCount();
      for (int i = 0; i < childCount; i++) {
         View child = parent.getChildAt(i);
         RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
         int left = child.getRight() + params.rightMargin;
         int right = left + dividerDrawable.getIntrinsicHeight();
         dividerDrawable.setBounds(left, top, right, bottom);
         dividerDrawable.draw(c);
      }
   }


   private boolean isSubheader (View view) {

      return false;
      /*
      int viewId = view.getId();
      return viewId == R.id.textView_subheader;
      */
   }
}
