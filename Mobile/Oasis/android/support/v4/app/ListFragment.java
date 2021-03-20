package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.TransportMediator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListFragment extends Fragment {
	static final int INTERNAL_EMPTY_ID = 16711681;
	static final int INTERNAL_LIST_CONTAINER_ID = 16711683;
	static final int INTERNAL_PROGRESS_CONTAINER_ID = 16711682;
	ListAdapter mAdapter;
	CharSequence mEmptyText;
	View mEmptyView;
	private final Handler mHandler;
	ListView mList;
	View mListContainer;
	boolean mListShown;
	private final OnItemClickListener mOnClickListener;
	View mProgressContainer;
	private final Runnable mRequestFocus;
	TextView mStandardEmptyView;

	class AnonymousClass_1 implements Runnable {
		final /* synthetic */ ListFragment this$0;

		AnonymousClass_1(ListFragment r1_ListFragment) {
			super();
			this$0 = r1_ListFragment;
		}

		public void run() {
			this$0.mList.focusableViewAvailable(this$0.mList);
		}
	}

	class AnonymousClass_2 implements OnItemClickListener {
		final /* synthetic */ ListFragment this$0;

		AnonymousClass_2(ListFragment r1_ListFragment) {
			super();
			this$0 = r1_ListFragment;
		}

		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			this$0.onListItemClick((ListView) parent, v, position, id);
		}
	}


	public ListFragment() {
		super();
		mHandler = new Handler();
		mRequestFocus = new AnonymousClass_1(this);
		mOnClickListener = new AnonymousClass_2(this);
	}

	private void ensureList() {
		if (mList != null) {
		} else {
			View root = getView();
			if (root == null) {
				throw new IllegalStateException("Content view not yet created");
			} else {
				if (root instanceof ListView) {
					mList = (ListView) root;
				} else {
					mStandardEmptyView = (TextView) root.findViewById(INTERNAL_EMPTY_ID);
					if (mStandardEmptyView == null) {
						mEmptyView = root.findViewById(16908292);
					} else {
						mStandardEmptyView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
					}
					mProgressContainer = root.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
					mListContainer = root.findViewById(INTERNAL_LIST_CONTAINER_ID);
					View rawListView = root.findViewById(16908298);
					if (!(rawListView instanceof ListView)) {
						if (rawListView == null) {
							throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
						} else {
							throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView class");
						}
					} else {
						mList = (ListView) rawListView;
						if (mEmptyView != null) {
							mList.setEmptyView(mEmptyView);
						} else if (mEmptyText != null) {
							mStandardEmptyView.setText(mEmptyText);
							mList.setEmptyView(mStandardEmptyView);
						}
					}
				}
				mListShown = true;
				mList.setOnItemClickListener(mOnClickListener);
				if (mAdapter != null) {
					mAdapter = null;
					setListAdapter(mAdapter);
				} else if (mProgressContainer != null) {
					setListShown(false, false);
				}
				mHandler.post(mRequestFocus);
			}
		}
	}

	private void setListShown(boolean shown, boolean animate) {
		ensureList();
		if (mProgressContainer == null) {
			throw new IllegalStateException("Can't be used with a custom content view");
		} else if (mListShown == shown) {
		} else {
			mListShown = shown;
			if (shown) {
				if (animate) {
					mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432577));
					mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432576));
				} else {
					mProgressContainer.clearAnimation();
					mListContainer.clearAnimation();
				}
				mProgressContainer.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
				mListContainer.setVisibility(0);
			} else {
				if (animate) {
					mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432576));
					mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432577));
				} else {
					mProgressContainer.clearAnimation();
					mListContainer.clearAnimation();
				}
				mProgressContainer.setVisibility(0);
				mListContainer.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
			}
		}
	}

	public ListAdapter getListAdapter() {
		return mAdapter;
	}

	public ListView getListView() {
		ensureList();
		return mList;
	}

	public long getSelectedItemId() {
		ensureList();
		return mList.getSelectedItemId();
	}

	public int getSelectedItemPosition() {
		ensureList();
		return mList.getSelectedItemPosition();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Context context = getActivity();
		View root = new FrameLayout(context);
		View pframe = new LinearLayout(context);
		pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);
		pframe.setOrientation(1);
		pframe.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
		pframe.setGravity(17);
		pframe.addView(new ProgressBar(context, null, 16842874), new LayoutParams(-2, -2));
		root.addView(pframe, new LayoutParams(-1, -1));
		View lframe = new FrameLayout(context);
		lframe.setId(INTERNAL_LIST_CONTAINER_ID);
		View tv = new TextView(getActivity());
		tv.setId(INTERNAL_EMPTY_ID);
		tv.setGravity(17);
		lframe.addView(tv, new LayoutParams(-1, -1));
		View lv = new ListView(getActivity());
		lv.setId(16908298);
		lv.setDrawSelectorOnTop(false);
		lframe.addView(lv, new LayoutParams(-1, -1));
		root.addView(lframe, new LayoutParams(-1, -1));
		root.setLayoutParams(new LayoutParams(-1, -1));
		return root;
	}

	public void onDestroyView() {
		mHandler.removeCallbacks(mRequestFocus);
		mList = null;
		mListShown = false;
		mListContainer = null;
		mProgressContainer = null;
		mEmptyView = null;
		mStandardEmptyView = null;
		super.onDestroyView();
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ensureList();
	}

	public void setEmptyText(CharSequence text) {
		ensureList();
		if (mStandardEmptyView == null) {
			throw new IllegalStateException("Can't be used with a custom content view");
		} else {
			mStandardEmptyView.setText(text);
			if (mEmptyText == null) {
				mList.setEmptyView(mStandardEmptyView);
			}
			mEmptyText = text;
		}
	}

	public void setListAdapter(ListAdapter adapter) {
		boolean r2z = false;
		if (mAdapter != null) {
			hadAdapter = true;
		} else {
			hadAdapter = false;
		}
		mAdapter = adapter;
		if (mList != null) {
			mList.setAdapter(adapter);
			if (mListShown || hadAdapter) {
			} else {
				if (getView().getWindowToken() != null) {
					r2z = true;
				}
				setListShown(true, r2z);
			}
		}
	}

	public void setListShown(boolean shown) {
		setListShown(shown, true);
	}

	public void setListShownNoAnimation(boolean shown) {
		setListShown(shown, false);
	}

	public void setSelection(int position) {
		ensureList();
		mList.setSelection(position);
	}
}
