package de.captag.util;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * A {@link RecyclerView.ItemDecoration} implementation that draws dividers between item views.
 * @author Ulrich Raab
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {


   public static final int HORIZONTAL = 0;
   public static final int VERTICAL = 1;


   private Drawable dividerDrawable;
   private int orientation;


   public DividerItemDecoration (Context context, int orientation, @DrawableRes int dividerDrawableId) {

      setOrientation(orientation);
      setDividerDrawable(context, dividerDrawableId);
   }


   private void setOrientation (int orientation) {

      if (orientation != HORIZONTAL && orientation != VERTICAL) {
         String message = "invalid orientation " + orientation;
         throw new IllegalArgumentException(message);
      }

      this.orientation = orientation;
   }


   private void setDividerDrawable (Context context, @DrawableRes int dividerDrawableId) {

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         dividerDrawable = context.getDrawable(dividerDrawableId);
      } else {
         Resources resources = context.getResources();
         //noinspection deprecation
         dividerDrawable = resources.getDrawable(dividerDrawableId);
      }
   }


   @Override
   public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

      switch (orientation) {
         case HORIZONTAL: {
            outRect.set(0, 0, dividerDrawable.getIntrinsicWidth(), 0);
            break;
         }
         case VERTICAL: {
            outRect.set(0, 0, 0, dividerDrawable.getIntrinsicHeight());
            break;
         }
         default: {
            super.getItemOffsets(outRect, view, parent, state);
         }
      }
   }


   @Override
   public void onDraw (Canvas canvas, RecyclerView parent, RecyclerView.State state) {

      switch (orientation) {
         case HORIZONTAL: {
            drawHorizontal(canvas, parent);
            break;
         }
         case VERTICAL: {
            drawVertical(canvas, parent);
            break;
         }
         default: {
            super.onDraw(canvas, parent, state);
         }
      }
   }


   private void drawHorizontal (Canvas canvas, RecyclerView parent) {

      int top = parent.getPaddingTop();
      int bottom = parent.getHeight() - parent.getPaddingBottom();

      int childCount = parent.getChildCount();
      for (int i = 0; i < childCount; i++) {
         View child = parent.getChildAt(i);
         RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
         int left = child.getRight() + params.rightMargin;
         int right = left + dividerDrawable.getIntrinsicHeight();
         dividerDrawable.setBounds(left, top, right, bottom);
         dividerDrawable.draw(canvas);
      }
   }


   private void drawVertical (Canvas canvas, RecyclerView parent) {

      int left = parent.getPaddingLeft();
      int right = parent.getWidth() - parent.getPaddingRight();

      int childCount = parent.getChildCount();
      for (int i = 0; i < childCount; i++) {
         View child = parent.getChildAt(i);
         RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
         int top = child.getBottom() + params.bottomMargin;
         int bottom = top + dividerDrawable.getIntrinsicHeight();
         dividerDrawable.setBounds(left, top, right, bottom);
         dividerDrawable.draw(canvas);
      }
   }
}
