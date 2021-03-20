package android.support.v4.widget;

import android.view.View;
import android.widget.ListView;

public class ListViewAutoScrollHelper extends AutoScrollHelper {
	private final ListView mTarget;

	public ListViewAutoScrollHelper(ListView target) {
		super(target);
		mTarget = target;
	}

	public boolean canTargetScrollHorizontally(int direction) {
		return false;
	}

	public boolean canTargetScrollVertically(int direction) {
		ListView target = mTarget;
		int itemCount = target.getCount();
		if (itemCount == 0) {
			return false;
		} else {
			int childCount = target.getChildCount();
			int firstPosition = target.getFirstVisiblePosition();
			int lastPosition = firstPosition + childCount;
			if (direction > 0) {
				if (lastPosition < itemCount || target.getChildAt(childCount - 1).getBottom() > target.getHeight()) {
					return true;
				} else {
					return false;
				}
			} else if (direction < 0) {
				if (firstPosition > 0 || target.getChildAt(0).getTop() < 0) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
			return true;
		}
	}

	public void scrollTargetBy(int deltaX, int deltaY) {
		ListView target = mTarget;
		int firstPosition = target.getFirstVisiblePosition();
		if (firstPosition == -1) {
		} else {
			View firstView = target.getChildAt(0);
			if (firstView != null) {
				target.setSelectionFromTop(firstPosition, firstView.getTop() - deltaY);
			}
		}
	}
}
