package com.oil.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class LineBreakLayout extends ViewGroup {

	private final static String TAG = "LineBreakLayout";

	private final static int VIEW_MARGIN = 2;

	public LineBreakLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public LineBreakLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public LineBreakLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Log.d(TAG, "widthMeasureSpec = " + widthMeasureSpec+
		// " heightMeasureSpec" + heightMeasureSpec);
		for (int index = 0; index < getChildCount(); index++) {
			final View child = getChildAt(index);
			// measure
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		 Log.d("Tag", " left = " + arg1 + " top = " + arg2 +" right=" + arg3 +"bootom="+ arg4);
		// arg2+ " right = " + arg3 + " botom = " + arg4);
		final int count = getChildCount();
		int row = 1;// which row lay you view relative to parent
		int lengthX = 0; // right position of child relative to parent
		int lengthY = 0; // bottom position of child relative to parent
		for (int i = 0; i < count; i++) {

			final View child = this.getChildAt(i);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			Log.e("Tag", "width = "+ width + " height = " + height);
			int leftLength = lengthX + arg1;
			lengthX += width + VIEW_MARGIN;
			lengthY = row * (height + VIEW_MARGIN);
			// if it can't drawing on a same line , skip to next line
			if (width + VIEW_MARGIN > arg3 - leftLength) {
				lengthX = width + VIEW_MARGIN;
				row++;
				lengthY = row * (height + VIEW_MARGIN);

			}
			Log.e("Tag","lengthx = "+ lengthX + " heighty = " + lengthY + " leftLength = " + leftLength);

			child.layout(lengthX - width, lengthY - height - VIEW_MARGIN, lengthX, lengthY - VIEW_MARGIN);
		}

	}

}
