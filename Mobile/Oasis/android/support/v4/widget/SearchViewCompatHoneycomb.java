package android.support.v4.widget;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

class SearchViewCompatHoneycomb {
	static class AnonymousClass_1 implements OnQueryTextListener {
		final /* synthetic */ SearchViewCompatHoneycomb.OnQueryTextListenerCompatBridge val$listener;

		AnonymousClass_1(SearchViewCompatHoneycomb.OnQueryTextListenerCompatBridge r1_SearchViewCompatHoneycomb_OnQueryTextListenerCompatBridge) {
			super();
			val$listener = r1_SearchViewCompatHoneycomb_OnQueryTextListenerCompatBridge;
		}

		public boolean onQueryTextChange(String newText) {
			return val$listener.onQueryTextChange(newText);
		}

		public boolean onQueryTextSubmit(String query) {
			return val$listener.onQueryTextSubmit(query);
		}
	}

	static class AnonymousClass_2 implements OnCloseListener {
		final /* synthetic */ SearchViewCompatHoneycomb.OnCloseListenerCompatBridge val$listener;

		AnonymousClass_2(SearchViewCompatHoneycomb.OnCloseListenerCompatBridge r1_SearchViewCompatHoneycomb_OnCloseListenerCompatBridge) {
			super();
			val$listener = r1_SearchViewCompatHoneycomb_OnCloseListenerCompatBridge;
		}

		public boolean onClose() {
			return val$listener.onClose();
		}
	}

	static interface OnCloseListenerCompatBridge {
		public boolean onClose();
	}

	static interface OnQueryTextListenerCompatBridge {
		public boolean onQueryTextChange(String r1_String);

		public boolean onQueryTextSubmit(String r1_String);
	}


	SearchViewCompatHoneycomb() {
		super();
	}

	public static CharSequence getQuery(View searchView) {
		return ((SearchView) searchView).getQuery();
	}

	public static boolean isIconified(View searchView) {
		return ((SearchView) searchView).isIconified();
	}

	public static boolean isQueryRefinementEnabled(View searchView) {
		return ((SearchView) searchView).isQueryRefinementEnabled();
	}

	public static boolean isSubmitButtonEnabled(View searchView) {
		return ((SearchView) searchView).isSubmitButtonEnabled();
	}

	public static Object newOnCloseListener(OnCloseListenerCompatBridge listener) {
		return new AnonymousClass_2(listener);
	}

	public static Object newOnQueryTextListener(OnQueryTextListenerCompatBridge listener) {
		return new AnonymousClass_1(listener);
	}

	public static View newSearchView(Context context) {
		return new SearchView(context);
	}

	public static void setIconified(View searchView, boolean iconify) {
		((SearchView) searchView).setIconified(iconify);
	}

	public static void setMaxWidth(View searchView, int maxpixels) {
		((SearchView) searchView).setMaxWidth(maxpixels);
	}

	public static void setOnCloseListener(Object searchView, Object listener) {
		((SearchView) searchView).setOnCloseListener((OnCloseListener) listener);
	}

	public static void setOnQueryTextListener(Object searchView, Object listener) {
		((SearchView) searchView).setOnQueryTextListener((OnQueryTextListener) listener);
	}

	public static void setQuery(View searchView, CharSequence query, boolean submit) {
		((SearchView) searchView).setQuery(query, submit);
	}

	public static void setQueryHint(View searchView, CharSequence hint) {
		((SearchView) searchView).setQueryHint(hint);
	}

	public static void setQueryRefinementEnabled(View searchView, boolean enable) {
		((SearchView) searchView).setQueryRefinementEnabled(enable);
	}

	public static void setSearchableInfo(View searchView, ComponentName searchableComponent) {
		SearchView sv = (SearchView) searchView;
		sv.setSearchableInfo(((SearchManager) sv.getContext().getSystemService("search")).getSearchableInfo(searchableComponent));
	}

	public static void setSubmitButtonEnabled(View searchView, boolean enabled) {
		((SearchView) searchView).setSubmitButtonEnabled(enabled);
	}
}
