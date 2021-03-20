package android.support.v4.app;

import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.util.LogWriter;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

final class BackStackRecord extends FragmentTransaction implements BackStackEntry, Runnable {
	static final int OP_ADD = 1;
	static final int OP_ATTACH = 7;
	static final int OP_DETACH = 6;
	static final int OP_HIDE = 4;
	static final int OP_NULL = 0;
	static final int OP_REMOVE = 3;
	static final int OP_REPLACE = 2;
	static final int OP_SHOW = 5;
	static final String TAG = "FragmentManager";
	boolean mAddToBackStack;
	boolean mAllowAddToBackStack;
	int mBreadCrumbShortTitleRes;
	CharSequence mBreadCrumbShortTitleText;
	int mBreadCrumbTitleRes;
	CharSequence mBreadCrumbTitleText;
	boolean mCommitted;
	int mEnterAnim;
	int mExitAnim;
	Op mHead;
	int mIndex;
	final FragmentManagerImpl mManager;
	String mName;
	int mNumOp;
	int mPopEnterAnim;
	int mPopExitAnim;
	Op mTail;
	int mTransition;
	int mTransitionStyle;

	static final class Op {
		int cmd;
		int enterAnim;
		int exitAnim;
		Fragment fragment;
		BackStackRecord.Op next;
		int popEnterAnim;
		int popExitAnim;
		BackStackRecord.Op prev;
		ArrayList<Fragment> removed;

		Op() {
			super();
		}
	}


	public BackStackRecord(FragmentManagerImpl manager) {
		super();
		mAllowAddToBackStack = true;
		mIndex = -1;
		mManager = manager;
	}

	private void doAddOp(int containerViewId, Fragment fragment, String tag, int opcmd) {
		fragment.mFragmentManager = mManager;
		if (tag != null) {
			if (fragment.mTag == null || tag.equals(fragment.mTag)) {
				fragment.mTag = tag;
			} else {
				throw new IllegalStateException("Can't change tag of fragment " + fragment + ": was " + fragment.mTag + " now " + tag);
			}
		}
		if (containerViewId != 0) {
			if (fragment.mFragmentId == 0 || fragment.mFragmentId == containerViewId) {
				fragment.mFragmentId = containerViewId;
				fragment.mContainerId = containerViewId;
			} else {
				throw new IllegalStateException("Can't change container ID of fragment " + fragment + ": was " + fragment.mFragmentId + " now " + containerViewId);
			}
		}
		Op op = new Op();
		op.cmd = opcmd;
		op.fragment = fragment;
		addOp(op);
	}

	public FragmentTransaction add(int containerViewId, Fragment fragment) {
		doAddOp(containerViewId, fragment, null, OP_ADD);
		return this;
	}

	public FragmentTransaction add(int containerViewId, Fragment fragment, String tag) {
		doAddOp(containerViewId, fragment, tag, OP_ADD);
		return this;
	}

	public FragmentTransaction add(Fragment fragment, String tag) {
		doAddOp(OP_NULL, fragment, tag, OP_ADD);
		return this;
	}

	void addOp(Op op) {
		if (mHead == null) {
			mTail = op;
			mHead = op;
		} else {
			op.prev = mTail;
			mTail.next = op;
			mTail = op;
		}
		op.enterAnim = mEnterAnim;
		op.exitAnim = mExitAnim;
		op.popEnterAnim = mPopEnterAnim;
		op.popExitAnim = mPopExitAnim;
		mNumOp++;
	}

	public FragmentTransaction addToBackStack(String name) {
		if (!mAllowAddToBackStack) {
			throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
		} else {
			mAddToBackStack = true;
			mName = name;
			return this;
		}
	}

	public FragmentTransaction attach(Fragment fragment) {
		Op op = new Op();
		op.cmd = 7;
		op.fragment = fragment;
		addOp(op);
		return this;
	}

	void bumpBackStackNesting(int amt) {
		if (!mAddToBackStack) {
		} else {
			if (FragmentManagerImpl.DEBUG) {
				Log.v(TAG, "Bump nesting in " + this + " by " + amt);
			}
			Op op = mHead;
			while (op != null) {
				if (op.fragment != null) {
					Fragment r3_Fragment = op.fragment;
					r3_Fragment.mBackStackNesting += amt;
					if (FragmentManagerImpl.DEBUG) {
						Log.v(TAG, "Bump nesting of " + op.fragment + " to " + op.fragment.mBackStackNesting);
					}
				}
				if (op.removed != null) {
					int i = op.removed.size() - 1;
					while (i >= 0) {
						Fragment r = (Fragment) op.removed.get(i);
						r.mBackStackNesting += amt;
						if (FragmentManagerImpl.DEBUG) {
							Log.v(TAG, "Bump nesting of " + r + " to " + r.mBackStackNesting);
						}
						i--;
					}
				}
				op = op.next;
			}
		}
	}

	public int commit() {
		return commitInternal(false);
	}

	public int commitAllowingStateLoss() {
		return commitInternal(true);
	}

	int commitInternal(boolean allowStateLoss) {
		if (mCommitted) {
			throw new IllegalStateException("commit already called");
		} else {
			if (FragmentManagerImpl.DEBUG) {
				Log.v(TAG, "Commit: " + this);
				dump("  ", null, new PrintWriter(new LogWriter(TAG)), null);
			}
			mCommitted = true;
			if (mAddToBackStack) {
				mIndex = mManager.allocBackStackIndex(this);
			} else {
				mIndex = -1;
			}
			mManager.enqueueAction(this, allowStateLoss);
			return mIndex;
		}
	}

	public FragmentTransaction detach(Fragment fragment) {
		Op op = new Op();
		op.cmd = 6;
		op.fragment = fragment;
		addOp(op);
		return this;
	}

	public FragmentTransaction disallowAddToBackStack() {
		if (mAddToBackStack) {
			throw new IllegalStateException("This transaction is already being added to the back stack");
		} else {
			mAllowAddToBackStack = false;
			return this;
		}
	}

	public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
		dump(prefix, writer, true);
	}

	public void dump(String prefix, PrintWriter writer, boolean full) {
		if (full) {
			writer.print(prefix);
			writer.print("mName=");
			writer.print(mName);
			writer.print(" mIndex=");
			writer.print(mIndex);
			writer.print(" mCommitted=");
			writer.println(mCommitted);
			if (mTransition != 0) {
				writer.print(prefix);
				writer.print("mTransition=#");
				writer.print(Integer.toHexString(mTransition));
				writer.print(" mTransitionStyle=#");
				writer.println(Integer.toHexString(mTransitionStyle));
			}
			if (mEnterAnim != 0 || mExitAnim != 0) {
				writer.print(prefix);
				writer.print("mEnterAnim=#");
				writer.print(Integer.toHexString(mEnterAnim));
				writer.print(" mExitAnim=#");
				writer.println(Integer.toHexString(mExitAnim));
			}
			if (mPopEnterAnim != 0 || mPopExitAnim != 0) {
				writer.print(prefix);
				writer.print("mPopEnterAnim=#");
				writer.print(Integer.toHexString(mPopEnterAnim));
				writer.print(" mPopExitAnim=#");
				writer.println(Integer.toHexString(mPopExitAnim));
			}
			if (mBreadCrumbTitleRes != 0 || mBreadCrumbTitleText != null) {
				writer.print(prefix);
				writer.print("mBreadCrumbTitleRes=#");
				writer.print(Integer.toHexString(mBreadCrumbTitleRes));
				writer.print(" mBreadCrumbTitleText=");
				writer.println(mBreadCrumbTitleText);
			}
			if (mBreadCrumbShortTitleRes != 0 || mBreadCrumbShortTitleText != null) {
				writer.print(prefix);
				writer.print("mBreadCrumbShortTitleRes=#");
				writer.print(Integer.toHexString(mBreadCrumbShortTitleRes));
				writer.print(" mBreadCrumbShortTitleText=");
				writer.println(mBreadCrumbShortTitleText);
			}
		}
		if (mHead != null) {
			writer.print(prefix);
			writer.println("Operations:");
			String innerPrefix = prefix + "    ";
			Op op = mHead;
			int num = OP_NULL;
			while (op != null) {
				String cmdStr;
				int i;
				switch(op.cmd) {
				case OP_NULL:
					cmdStr = "NULL";
					writer.print(prefix);
					writer.print("  Op #");
					writer.print(num);
					writer.print(": ");
					writer.print(cmdStr);
					writer.print(" ");
					writer.println(op.fragment);
					if (full) {
						if (op.enterAnim != 0 || op.exitAnim != 0) {
							writer.print(prefix);
							writer.print("enterAnim=#");
							writer.print(Integer.toHexString(op.enterAnim));
							writer.print(" exitAnim=#");
							writer.println(Integer.toHexString(op.exitAnim));
						}
						if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
							writer.print(prefix);
							writer.print("popEnterAnim=#");
							writer.print(Integer.toHexString(op.popEnterAnim));
							writer.print(" popExitAnim=#");
							writer.println(Integer.toHexString(op.popExitAnim));
						}
					}
					if (op.removed == null || op.removed.size() <= 0) {
						op = op.next;
						num++;
					} else {
						i = OP_NULL;
						while (i < op.removed.size()) {
							writer.print(innerPrefix);
							if (op.removed.size() == 1) {
								writer.print("Removed: ");
							} else {
								if (i == 0) {
									writer.println("Removed:");
								}
								writer.print(innerPrefix);
								writer.print("  #");
								writer.print(i);
								writer.print(": ");
							}
							writer.println(op.removed.get(i));
							i++;
						}
						op = op.next;
						num++;
					}
					break;
				case OP_ADD:
					cmdStr = "ADD";
					writer.print(prefix);
					writer.print("  Op #");
					writer.print(num);
					writer.print(": ");
					writer.print(cmdStr);
					writer.print(" ");
					writer.println(op.fragment);
					if (full) {
						if (op.removed == null || op.removed.size() <= 0) {
							op = op.next;
							num++;
						} else {
							i = OP_NULL;
							while (i < op.removed.size()) {
								writer.print(innerPrefix);
								if (op.removed.size() == 1) {
									if (i == 0) {
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									} else {
										writer.println("Removed:");
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									}
								} else {
									writer.print("Removed: ");
								}
								writer.println(op.removed.get(i));
								i++;
							}
							op = op.next;
							num++;
						}
					} else {
						if (op.enterAnim != 0 || op.exitAnim != 0) {
							writer.print(prefix);
							writer.print("enterAnim=#");
							writer.print(Integer.toHexString(op.enterAnim));
							writer.print(" exitAnim=#");
							writer.println(Integer.toHexString(op.exitAnim));
						}
						if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
							writer.print(prefix);
							writer.print("popEnterAnim=#");
							writer.print(Integer.toHexString(op.popEnterAnim));
							writer.print(" popExitAnim=#");
							writer.println(Integer.toHexString(op.popExitAnim));
							if (op.removed == null || op.removed.size() <= 0) {
								op = op.next;
								num++;
							} else {
								i = OP_NULL;
								while (i < op.removed.size()) {
									writer.print(innerPrefix);
									if (op.removed.size() == 1) {
										writer.print("Removed: ");
									} else {
										if (i == 0) {
											writer.println("Removed:");
										}
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									}
									writer.println(op.removed.get(i));
									i++;
								}
								op = op.next;
								num++;
							}
						} else if (op.removed == null || op.removed.size() <= 0) {
							op = op.next;
							num++;
						} else {
							i = OP_NULL;
							while (i < op.removed.size()) {
								writer.print(innerPrefix);
								if (op.removed.size() == 1) {
									if (i == 0) {
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									} else {
										writer.println("Removed:");
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									}
								} else {
									writer.print("Removed: ");
								}
								writer.println(op.removed.get(i));
								i++;
							}
							op = op.next;
							num++;
						}
					}
					break;
				case OP_REPLACE:
					cmdStr = "REPLACE";
					writer.print(prefix);
					writer.print("  Op #");
					writer.print(num);
					writer.print(": ");
					writer.print(cmdStr);
					writer.print(" ");
					writer.println(op.fragment);
					if (full) {
						if (op.enterAnim != 0 || op.exitAnim != 0) {
							writer.print(prefix);
							writer.print("enterAnim=#");
							writer.print(Integer.toHexString(op.enterAnim));
							writer.print(" exitAnim=#");
							writer.println(Integer.toHexString(op.exitAnim));
						}
						if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
							writer.print(prefix);
							writer.print("popEnterAnim=#");
							writer.print(Integer.toHexString(op.popEnterAnim));
							writer.print(" popExitAnim=#");
							writer.println(Integer.toHexString(op.popExitAnim));
						}
					}
					if (op.removed == null || op.removed.size() <= 0) {
						op = op.next;
						num++;
					} else {
						i = OP_NULL;
						while (i < op.removed.size()) {
							writer.print(innerPrefix);
							if (op.removed.size() == 1) {
								writer.print("Removed: ");
							} else {
								if (i == 0) {
									writer.println("Removed:");
								}
								writer.print(innerPrefix);
								writer.print("  #");
								writer.print(i);
								writer.print(": ");
							}
							writer.println(op.removed.get(i));
							i++;
						}
						op = op.next;
						num++;
					}
					break;
				case OP_REMOVE:
					cmdStr = "REMOVE";
					writer.print(prefix);
					writer.print("  Op #");
					writer.print(num);
					writer.print(": ");
					writer.print(cmdStr);
					writer.print(" ");
					writer.println(op.fragment);
					if (full) {
						if (op.removed == null || op.removed.size() <= 0) {
							op = op.next;
							num++;
						} else {
							i = OP_NULL;
							while (i < op.removed.size()) {
								writer.print(innerPrefix);
								if (op.removed.size() == 1) {
									if (i == 0) {
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									} else {
										writer.println("Removed:");
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									}
								} else {
									writer.print("Removed: ");
								}
								writer.println(op.removed.get(i));
								i++;
							}
							op = op.next;
							num++;
						}
					} else {
						if (op.enterAnim != 0 || op.exitAnim != 0) {
							writer.print(prefix);
							writer.print("enterAnim=#");
							writer.print(Integer.toHexString(op.enterAnim));
							writer.print(" exitAnim=#");
							writer.println(Integer.toHexString(op.exitAnim));
						}
						if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
							writer.print(prefix);
							writer.print("popEnterAnim=#");
							writer.print(Integer.toHexString(op.popEnterAnim));
							writer.print(" popExitAnim=#");
							writer.println(Integer.toHexString(op.popExitAnim));
							if (op.removed == null || op.removed.size() <= 0) {
								op = op.next;
								num++;
							} else {
								i = OP_NULL;
								while (i < op.removed.size()) {
									writer.print(innerPrefix);
									if (op.removed.size() == 1) {
										writer.print("Removed: ");
									} else {
										if (i == 0) {
											writer.println("Removed:");
										}
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									}
									writer.println(op.removed.get(i));
									i++;
								}
								op = op.next;
								num++;
							}
						} else if (op.removed == null || op.removed.size() <= 0) {
							op = op.next;
							num++;
						} else {
							i = OP_NULL;
							while (i < op.removed.size()) {
								writer.print(innerPrefix);
								if (op.removed.size() == 1) {
									if (i == 0) {
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									} else {
										writer.println("Removed:");
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									}
								} else {
									writer.print("Removed: ");
								}
								writer.println(op.removed.get(i));
								i++;
							}
							op = op.next;
							num++;
						}
					}
					break;
				case OP_HIDE:
					cmdStr = "HIDE";
					writer.print(prefix);
					writer.print("  Op #");
					writer.print(num);
					writer.print(": ");
					writer.print(cmdStr);
					writer.print(" ");
					writer.println(op.fragment);
					if (full) {
						if (op.enterAnim != 0 || op.exitAnim != 0) {
							writer.print(prefix);
							writer.print("enterAnim=#");
							writer.print(Integer.toHexString(op.enterAnim));
							writer.print(" exitAnim=#");
							writer.println(Integer.toHexString(op.exitAnim));
						}
						if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
							writer.print(prefix);
							writer.print("popEnterAnim=#");
							writer.print(Integer.toHexString(op.popEnterAnim));
							writer.print(" popExitAnim=#");
							writer.println(Integer.toHexString(op.popExitAnim));
						}
					}
					if (op.removed == null || op.removed.size() <= 0) {
						op = op.next;
						num++;
					} else {
						i = OP_NULL;
						while (i < op.removed.size()) {
							writer.print(innerPrefix);
							if (op.removed.size() == 1) {
								writer.print("Removed: ");
							} else {
								if (i == 0) {
									writer.println("Removed:");
								}
								writer.print(innerPrefix);
								writer.print("  #");
								writer.print(i);
								writer.print(": ");
							}
							writer.println(op.removed.get(i));
							i++;
						}
						op = op.next;
						num++;
					}
					break;
				case OP_SHOW:
					cmdStr = "SHOW";
					writer.print(prefix);
					writer.print("  Op #");
					writer.print(num);
					writer.print(": ");
					writer.print(cmdStr);
					writer.print(" ");
					writer.println(op.fragment);
					if (full) {
						if (op.removed == null || op.removed.size() <= 0) {
							op = op.next;
							num++;
						} else {
							i = OP_NULL;
							while (i < op.removed.size()) {
								writer.print(innerPrefix);
								if (op.removed.size() == 1) {
									if (i == 0) {
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									} else {
										writer.println("Removed:");
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									}
								} else {
									writer.print("Removed: ");
								}
								writer.println(op.removed.get(i));
								i++;
							}
							op = op.next;
							num++;
						}
					} else {
						if (op.enterAnim != 0 || op.exitAnim != 0) {
							writer.print(prefix);
							writer.print("enterAnim=#");
							writer.print(Integer.toHexString(op.enterAnim));
							writer.print(" exitAnim=#");
							writer.println(Integer.toHexString(op.exitAnim));
						}
						if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
							writer.print(prefix);
							writer.print("popEnterAnim=#");
							writer.print(Integer.toHexString(op.popEnterAnim));
							writer.print(" popExitAnim=#");
							writer.println(Integer.toHexString(op.popExitAnim));
							if (op.removed == null || op.removed.size() <= 0) {
								op = op.next;
								num++;
							} else {
								i = OP_NULL;
								while (i < op.removed.size()) {
									writer.print(innerPrefix);
									if (op.removed.size() == 1) {
										writer.print("Removed: ");
									} else {
										if (i == 0) {
											writer.println("Removed:");
										}
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									}
									writer.println(op.removed.get(i));
									i++;
								}
								op = op.next;
								num++;
							}
						} else if (op.removed == null || op.removed.size() <= 0) {
							op = op.next;
							num++;
						} else {
							i = OP_NULL;
							while (i < op.removed.size()) {
								writer.print(innerPrefix);
								if (op.removed.size() == 1) {
									if (i == 0) {
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									} else {
										writer.println("Removed:");
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									}
								} else {
									writer.print("Removed: ");
								}
								writer.println(op.removed.get(i));
								i++;
							}
							op = op.next;
							num++;
						}
					}
					break;
				case OP_DETACH:
					cmdStr = "DETACH";
					writer.print(prefix);
					writer.print("  Op #");
					writer.print(num);
					writer.print(": ");
					writer.print(cmdStr);
					writer.print(" ");
					writer.println(op.fragment);
					if (full) {
						if (op.enterAnim != 0 || op.exitAnim != 0) {
							writer.print(prefix);
							writer.print("enterAnim=#");
							writer.print(Integer.toHexString(op.enterAnim));
							writer.print(" exitAnim=#");
							writer.println(Integer.toHexString(op.exitAnim));
						}
						if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
							writer.print(prefix);
							writer.print("popEnterAnim=#");
							writer.print(Integer.toHexString(op.popEnterAnim));
							writer.print(" popExitAnim=#");
							writer.println(Integer.toHexString(op.popExitAnim));
						}
					}
					if (op.removed == null || op.removed.size() <= 0) {
						op = op.next;
						num++;
					} else {
						i = OP_NULL;
						while (i < op.removed.size()) {
							writer.print(innerPrefix);
							if (op.removed.size() == 1) {
								writer.print("Removed: ");
							} else {
								if (i == 0) {
									writer.println("Removed:");
								}
								writer.print(innerPrefix);
								writer.print("  #");
								writer.print(i);
								writer.print(": ");
							}
							writer.println(op.removed.get(i));
							i++;
						}
						op = op.next;
						num++;
					}
					break;
				case OP_ATTACH:
					cmdStr = "ATTACH";
					writer.print(prefix);
					writer.print("  Op #");
					writer.print(num);
					writer.print(": ");
					writer.print(cmdStr);
					writer.print(" ");
					writer.println(op.fragment);
					if (full) {
						if (op.removed == null || op.removed.size() <= 0) {
							op = op.next;
							num++;
						} else {
							i = OP_NULL;
							while (i < op.removed.size()) {
								writer.print(innerPrefix);
								if (op.removed.size() == 1) {
									if (i == 0) {
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									} else {
										writer.println("Removed:");
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									}
								} else {
									writer.print("Removed: ");
								}
								writer.println(op.removed.get(i));
								i++;
							}
							op = op.next;
							num++;
						}
					} else {
						if (op.enterAnim != 0 || op.exitAnim != 0) {
							writer.print(prefix);
							writer.print("enterAnim=#");
							writer.print(Integer.toHexString(op.enterAnim));
							writer.print(" exitAnim=#");
							writer.println(Integer.toHexString(op.exitAnim));
						}
						if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
							writer.print(prefix);
							writer.print("popEnterAnim=#");
							writer.print(Integer.toHexString(op.popEnterAnim));
							writer.print(" popExitAnim=#");
							writer.println(Integer.toHexString(op.popExitAnim));
							if (op.removed == null || op.removed.size() <= 0) {
								op = op.next;
								num++;
							} else {
								i = OP_NULL;
								while (i < op.removed.size()) {
									writer.print(innerPrefix);
									if (op.removed.size() == 1) {
										writer.print("Removed: ");
									} else {
										if (i == 0) {
											writer.println("Removed:");
										}
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									}
									writer.println(op.removed.get(i));
									i++;
								}
								op = op.next;
								num++;
							}
						} else if (op.removed == null || op.removed.size() <= 0) {
							op = op.next;
							num++;
						} else {
							i = OP_NULL;
							while (i < op.removed.size()) {
								writer.print(innerPrefix);
								if (op.removed.size() == 1) {
									if (i == 0) {
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									} else {
										writer.println("Removed:");
										writer.print(innerPrefix);
										writer.print("  #");
										writer.print(i);
										writer.print(": ");
									}
								} else {
									writer.print("Removed: ");
								}
								writer.println(op.removed.get(i));
								i++;
							}
							op = op.next;
							num++;
						}
					}
					break;
				}
				cmdStr = "cmd=" + op.cmd;
				writer.print(prefix);
				writer.print("  Op #");
				writer.print(num);
				writer.print(": ");
				writer.print(cmdStr);
				writer.print(" ");
				writer.println(op.fragment);
				if (full) {
					if (op.enterAnim != 0 || op.exitAnim != 0) {
						writer.print(prefix);
						writer.print("enterAnim=#");
						writer.print(Integer.toHexString(op.enterAnim));
						writer.print(" exitAnim=#");
						writer.println(Integer.toHexString(op.exitAnim));
					}
					if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
						writer.print(prefix);
						writer.print("popEnterAnim=#");
						writer.print(Integer.toHexString(op.popEnterAnim));
						writer.print(" popExitAnim=#");
						writer.println(Integer.toHexString(op.popExitAnim));
					}
				}
				if (op.removed == null || op.removed.size() <= 0) {
					op = op.next;
					num++;
				} else {
					i = OP_NULL;
					while (i < op.removed.size()) {
						writer.print(innerPrefix);
						if (op.removed.size() == 1) {
							writer.print("Removed: ");
						} else {
							if (i == 0) {
								writer.println("Removed:");
							}
							writer.print(innerPrefix);
							writer.print("  #");
							writer.print(i);
							writer.print(": ");
						}
						writer.println(op.removed.get(i));
						i++;
					}
					op = op.next;
					num++;
				}
			}
		}
	}

	public CharSequence getBreadCrumbShortTitle() {
		if (mBreadCrumbShortTitleRes != 0) {
			return mManager.mActivity.getText(mBreadCrumbShortTitleRes);
		} else {
			return mBreadCrumbShortTitleText;
		}
	}

	public int getBreadCrumbShortTitleRes() {
		return mBreadCrumbShortTitleRes;
	}

	public CharSequence getBreadCrumbTitle() {
		if (mBreadCrumbTitleRes != 0) {
			return mManager.mActivity.getText(mBreadCrumbTitleRes);
		} else {
			return mBreadCrumbTitleText;
		}
	}

	public int getBreadCrumbTitleRes() {
		return mBreadCrumbTitleRes;
	}

	public int getId() {
		return mIndex;
	}

	public String getName() {
		return mName;
	}

	public int getTransition() {
		return mTransition;
	}

	public int getTransitionStyle() {
		return mTransitionStyle;
	}

	public FragmentTransaction hide(Fragment fragment) {
		Op op = new Op();
		op.cmd = 4;
		op.fragment = fragment;
		addOp(op);
		return this;
	}

	public boolean isAddToBackStackAllowed() {
		return mAllowAddToBackStack;
	}

	public boolean isEmpty() {
		if (mNumOp == 0) {
			return true;
		} else {
			return false;
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	public void popFromBackStack(boolean r13_doStateMove) {
		r12_this = this;
		r10 = 0;
		r9 = 0;
		r11 = -1;
		r6 = android.support.v4.app.FragmentManagerImpl.DEBUG;
		if (r6 == 0) goto L_0x0030;
	L_0x0007:
		r6 = "FragmentManager";
		r7 = new java.lang.StringBuilder;
		r7.<init>();
		r8 = "popFromBackStack: ";
		r7 = r7.append(r8);
		r7 = r7.append(r12);
		r7 = r7.toString();
		android.util.Log.v(r6, r7);
		r2 = new android.support.v4.util.LogWriter;
		r6 = "FragmentManager";
		r2.<init>(r6);
		r5 = new java.io.PrintWriter;
		r5.<init>(r2_logw);
		r6 = "  ";
		r12.dump(r6, r10, r5_pw, r10);
	L_0x0030:
		r12.bumpBackStackNesting(r11);
		r4 = r12.mTail;
	L_0x0035:
		if (r4_op == 0) goto L_0x0100;
	L_0x0037:
		r6 = r4_op.cmd;
		switch(r6) {
			case 1: goto L_0x0057;
			case 2: goto L_0x006d;
			case 3: goto L_0x00a3;
			case 4: goto L_0x00af;
			case 5: goto L_0x00c3;
			case 6: goto L_0x00d7;
			case 7: goto L_0x00eb;
			default: goto L_0x003c;
		}
	L_0x003c:
		r6 = new java.lang.IllegalArgumentException;
		r7 = new java.lang.StringBuilder;
		r7.<init>();
		r8 = "Unknown cmd: ";
		r7 = r7.append(r8);
		r8 = r4_op.cmd;
		r7 = r7.append(r8);
		r7 = r7.toString();
		r6.<init>(r7);
		throw r6;
	L_0x0057:
		r0 = r4_op.fragment;
		r6 = r4_op.popExitAnim;
		r0_f.mNextAnim = r6;
		r6 = r12.mManager;
		r7 = r12.mTransition;
		r7 = android.support.v4.app.FragmentManagerImpl.reverseTransit(r7);
		r8 = r12.mTransitionStyle;
		r6.removeFragment(r0_f, r7, r8);
	L_0x006a:
		r4_op = r4_op.prev;
		goto L_0x0035;
	L_0x006d:
		r0_f = r4_op.fragment;
		if (r0_f == 0) goto L_0x0082;
	L_0x0071:
		r6 = r4_op.popExitAnim;
		r0_f.mNextAnim = r6;
		r6 = r12.mManager;
		r7 = r12.mTransition;
		r7 = android.support.v4.app.FragmentManagerImpl.reverseTransit(r7);
		r8 = r12.mTransitionStyle;
		r6.removeFragment(r0_f, r7, r8);
	L_0x0082:
		r6 = r4_op.removed;
		if (r6 == 0) goto L_0x006a;
	L_0x0086:
		r1 = 0;
	L_0x0087:
		r6 = r4_op.removed;
		r6 = r6.size();
		if (r1_i >= r6) goto L_0x006a;
	L_0x008f:
		r6 = r4_op.removed;
		r3 = r6.get(r1_i);
		r3 = (android.support.v4.app.Fragment) r3;
		r6 = r4_op.popEnterAnim;
		r3_old.mNextAnim = r6;
		r6 = r12.mManager;
		r6.addFragment(r3_old, r9);
		r1_i++;
		goto L_0x0087;
	L_0x00a3:
		r0_f = r4_op.fragment;
		r6 = r4_op.popEnterAnim;
		r0_f.mNextAnim = r6;
		r6 = r12.mManager;
		r6.addFragment(r0_f, r9);
		goto L_0x006a;
	L_0x00af:
		r0_f = r4_op.fragment;
		r6 = r4_op.popEnterAnim;
		r0_f.mNextAnim = r6;
		r6 = r12.mManager;
		r7 = r12.mTransition;
		r7 = android.support.v4.app.FragmentManagerImpl.reverseTransit(r7);
		r8 = r12.mTransitionStyle;
		r6.showFragment(r0_f, r7, r8);
		goto L_0x006a;
	L_0x00c3:
		r0_f = r4_op.fragment;
		r6 = r4_op.popExitAnim;
		r0_f.mNextAnim = r6;
		r6 = r12.mManager;
		r7 = r12.mTransition;
		r7 = android.support.v4.app.FragmentManagerImpl.reverseTransit(r7);
		r8 = r12.mTransitionStyle;
		r6.hideFragment(r0_f, r7, r8);
		goto L_0x006a;
	L_0x00d7:
		r0_f = r4_op.fragment;
		r6 = r4_op.popEnterAnim;
		r0_f.mNextAnim = r6;
		r6 = r12.mManager;
		r7 = r12.mTransition;
		r7 = android.support.v4.app.FragmentManagerImpl.reverseTransit(r7);
		r8 = r12.mTransitionStyle;
		r6.attachFragment(r0_f, r7, r8);
		goto L_0x006a;
	L_0x00eb:
		r0_f = r4_op.fragment;
		r6 = r4_op.popEnterAnim;
		r0_f.mNextAnim = r6;
		r6 = r12.mManager;
		r7 = r12.mTransition;
		r7 = android.support.v4.app.FragmentManagerImpl.reverseTransit(r7);
		r8 = r12.mTransitionStyle;
		r6.detachFragment(r0_f, r7, r8);
		goto L_0x006a;
	L_0x0100:
		if (r13_doStateMove == 0) goto L_0x0114;
	L_0x0102:
		r6 = r12.mManager;
		r7 = r12.mManager;
		r7 = r7.mCurState;
		r8 = r12.mTransition;
		r8 = android.support.v4.app.FragmentManagerImpl.reverseTransit(r8);
		r9 = r12.mTransitionStyle;
		r10 = 1;
		r6.moveToState(r7, r8, r9, r10);
	L_0x0114:
		r6 = r12.mIndex;
		if (r6 < 0) goto L_0x0121;
	L_0x0118:
		r6 = r12.mManager;
		r7 = r12.mIndex;
		r6.freeBackStackIndex(r7);
		r12.mIndex = r11;
	L_0x0121:
		return;
	}
	*/
	public void popFromBackStack(boolean doStateMove) {
		if (FragmentManagerImpl.DEBUG) {
			Log.v(TAG, "popFromBackStack: " + this);
			dump("  ", null, new PrintWriter(new LogWriter(TAG)), null);
		}
		bumpBackStackNesting(-1);
		Op op = mTail;
		while (op != null) {
			Fragment f;
			switch(op.cmd) {
			case OP_ADD:
				f = op.fragment;
				f.mNextAnim = op.popExitAnim;
				mManager.removeFragment(f, FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
				op = op.prev;
				break;
			case OP_REPLACE:
				f = op.fragment;
				if (f != null) {
					f.mNextAnim = op.popExitAnim;
					mManager.removeFragment(f, FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
				}
				if (op.removed != null) {
					int i = OP_NULL;
					while (i < op.removed.size()) {
						Fragment old = (Fragment) op.removed.get(i);
						old.mNextAnim = op.popEnterAnim;
						mManager.addFragment(old, false);
						i++;
					}
				}
				op = op.prev;
				break;
			case OP_REMOVE:
				f = op.fragment;
				f.mNextAnim = op.popEnterAnim;
				mManager.addFragment(f, false);
				op = op.prev;
				break;
			case OP_HIDE:
				f = op.fragment;
				f.mNextAnim = op.popEnterAnim;
				mManager.showFragment(f, FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
				op = op.prev;
				break;
			case OP_SHOW:
				f = op.fragment;
				f.mNextAnim = op.popExitAnim;
				mManager.hideFragment(f, FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
				op = op.prev;
				break;
			case OP_DETACH:
				f = op.fragment;
				f.mNextAnim = op.popEnterAnim;
				mManager.attachFragment(f, FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
				op = op.prev;
				break;
			case OP_ATTACH:
				f = op.fragment;
				f.mNextAnim = op.popEnterAnim;
				mManager.detachFragment(f, FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
				op = op.prev;
				break;
			}
		}
		if (doStateMove) {
			mManager.moveToState(mManager.mCurState, FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle, true);
		}
		if (mIndex >= 0) {
			mManager.freeBackStackIndex(mIndex);
			mIndex = -1;
		}
	}

	public FragmentTransaction remove(Fragment fragment) {
		Op op = new Op();
		op.cmd = 3;
		op.fragment = fragment;
		addOp(op);
		return this;
	}

	public FragmentTransaction replace(int containerViewId, Fragment fragment) {
		return replace(containerViewId, fragment, null);
	}

	public FragmentTransaction replace(int containerViewId, Fragment fragment, String tag) {
		if (containerViewId == 0) {
			throw new IllegalArgumentException("Must use non-zero containerViewId");
		} else {
			doAddOp(containerViewId, fragment, tag, OP_REPLACE);
			return this;
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	public void run() {
		r9_this = this;
		r8 = 1;
		r7 = 0;
		r4 = android.support.v4.app.FragmentManagerImpl.DEBUG;
		if (r4 == 0) goto L_0x001e;
	L_0x0006:
		r4 = "FragmentManager";
		r5 = new java.lang.StringBuilder;
		r5.<init>();
		r6 = "Run: ";
		r5 = r5.append(r6);
		r5 = r5.append(r9);
		r5 = r5.toString();
		android.util.Log.v(r4, r5);
	L_0x001e:
		r4 = r9.mAddToBackStack;
		if (r4 == 0) goto L_0x002e;
	L_0x0022:
		r4 = r9.mIndex;
		if (r4 >= 0) goto L_0x002e;
	L_0x0026:
		r4 = new java.lang.IllegalStateException;
		r5 = "addToBackStack() called after commit()";
		r4.<init>(r5);
		throw r4;
	L_0x002e:
		r9.bumpBackStackNesting(r8);
		r3 = r9.mHead;
	L_0x0033:
		if (r3_op == 0) goto L_0x0168;
	L_0x0035:
		r4 = r3_op.cmd;
		switch(r4) {
			case 1: goto L_0x0055;
			case 2: goto L_0x0063;
			case 3: goto L_0x0113;
			case 4: goto L_0x0124;
			case 5: goto L_0x0135;
			case 6: goto L_0x0146;
			case 7: goto L_0x0157;
			default: goto L_0x003a;
		}
	L_0x003a:
		r4 = new java.lang.IllegalArgumentException;
		r5 = new java.lang.StringBuilder;
		r5.<init>();
		r6 = "Unknown cmd: ";
		r5 = r5.append(r6);
		r6 = r3_op.cmd;
		r5 = r5.append(r6);
		r5 = r5.toString();
		r4.<init>(r5);
		throw r4;
	L_0x0055:
		r0 = r3_op.fragment;
		r4 = r3_op.enterAnim;
		r0_f.mNextAnim = r4;
		r4 = r9.mManager;
		r4.addFragment(r0_f, r7);
	L_0x0060:
		r3_op = r3_op.next;
		goto L_0x0033;
	L_0x0063:
		r0_f = r3_op.fragment;
		r4 = r9.mManager;
		r4 = r4.mAdded;
		if (r4 == 0) goto L_0x0106;
	L_0x006b:
		r1 = 0;
	L_0x006c:
		r4 = r9.mManager;
		r4 = r4.mAdded;
		r4 = r4.size();
		if (r1_i >= r4) goto L_0x0106;
	L_0x0076:
		r4 = r9.mManager;
		r4 = r4.mAdded;
		r2 = r4.get(r1_i);
		r2 = (android.support.v4.app.Fragment) r2;
		r4 = android.support.v4.app.FragmentManagerImpl.DEBUG;
		if (r4 == 0) goto L_0x00a6;
	L_0x0084:
		r4 = "FragmentManager";
		r5 = new java.lang.StringBuilder;
		r5.<init>();
		r6 = "OP_REPLACE: adding=";
		r5 = r5.append(r6);
		r5 = r5.append(r0_f);
		r6 = " old=";
		r5 = r5.append(r6);
		r5 = r5.append(r2_old);
		r5 = r5.toString();
		android.util.Log.v(r4, r5);
	L_0x00a6:
		if (r0_f == 0) goto L_0x00ae;
	L_0x00a8:
		r4 = r2_old.mContainerId;
		r5 = r0_f.mContainerId;
		if (r4 != r5) goto L_0x00b3;
	L_0x00ae:
		if (r2_old != r0_f) goto L_0x00b6;
	L_0x00b0:
		r0_f = 0;
		r3_op.fragment = r0_f;
	L_0x00b3:
		r1_i++;
		goto L_0x006c;
	L_0x00b6:
		r4 = r3_op.removed;
		if (r4 != 0) goto L_0x00c1;
	L_0x00ba:
		r4 = new java.util.ArrayList;
		r4.<init>();
		r3_op.removed = r4;
	L_0x00c1:
		r4 = r3_op.removed;
		r4.add(r2_old);
		r4 = r3_op.exitAnim;
		r2_old.mNextAnim = r4;
		r4 = r9.mAddToBackStack;
		if (r4 == 0) goto L_0x00fc;
	L_0x00ce:
		r4 = r2_old.mBackStackNesting;
		r4++;
		r2_old.mBackStackNesting = r4;
		r4 = android.support.v4.app.FragmentManagerImpl.DEBUG;
		if (r4 == 0) goto L_0x00fc;
	L_0x00d8:
		r4 = "FragmentManager";
		r5 = new java.lang.StringBuilder;
		r5.<init>();
		r6 = "Bump nesting of ";
		r5 = r5.append(r6);
		r5 = r5.append(r2_old);
		r6 = " to ";
		r5 = r5.append(r6);
		r6 = r2_old.mBackStackNesting;
		r5 = r5.append(r6);
		r5 = r5.toString();
		android.util.Log.v(r4, r5);
	L_0x00fc:
		r4 = r9.mManager;
		r5 = r9.mTransition;
		r6 = r9.mTransitionStyle;
		r4.removeFragment(r2_old, r5, r6);
		goto L_0x00b3;
	L_0x0106:
		if (r0_f == 0) goto L_0x0060;
	L_0x0108:
		r4 = r3_op.enterAnim;
		r0_f.mNextAnim = r4;
		r4 = r9.mManager;
		r4.addFragment(r0_f, r7);
		goto L_0x0060;
	L_0x0113:
		r0_f = r3_op.fragment;
		r4 = r3_op.exitAnim;
		r0_f.mNextAnim = r4;
		r4 = r9.mManager;
		r5 = r9.mTransition;
		r6 = r9.mTransitionStyle;
		r4.removeFragment(r0_f, r5, r6);
		goto L_0x0060;
	L_0x0124:
		r0_f = r3_op.fragment;
		r4 = r3_op.exitAnim;
		r0_f.mNextAnim = r4;
		r4 = r9.mManager;
		r5 = r9.mTransition;
		r6 = r9.mTransitionStyle;
		r4.hideFragment(r0_f, r5, r6);
		goto L_0x0060;
	L_0x0135:
		r0_f = r3_op.fragment;
		r4 = r3_op.enterAnim;
		r0_f.mNextAnim = r4;
		r4 = r9.mManager;
		r5 = r9.mTransition;
		r6 = r9.mTransitionStyle;
		r4.showFragment(r0_f, r5, r6);
		goto L_0x0060;
	L_0x0146:
		r0_f = r3_op.fragment;
		r4 = r3_op.exitAnim;
		r0_f.mNextAnim = r4;
		r4 = r9.mManager;
		r5 = r9.mTransition;
		r6 = r9.mTransitionStyle;
		r4.detachFragment(r0_f, r5, r6);
		goto L_0x0060;
	L_0x0157:
		r0_f = r3_op.fragment;
		r4 = r3_op.enterAnim;
		r0_f.mNextAnim = r4;
		r4 = r9.mManager;
		r5 = r9.mTransition;
		r6 = r9.mTransitionStyle;
		r4.attachFragment(r0_f, r5, r6);
		goto L_0x0060;
	L_0x0168:
		r4 = r9.mManager;
		r5 = r9.mManager;
		r5 = r5.mCurState;
		r6 = r9.mTransition;
		r7 = r9.mTransitionStyle;
		r4.moveToState(r5, r6, r7, r8);
		r4 = r9.mAddToBackStack;
		if (r4 == 0) goto L_0x017e;
	L_0x0179:
		r4 = r9.mManager;
		r4.addBackStackState(r9);
	L_0x017e:
		return;
	}
	*/
	public void run() {
		if (FragmentManagerImpl.DEBUG) {
			Log.v(TAG, "Run: " + this);
		}
		if (!mAddToBackStack || mIndex >= 0) {
			bumpBackStackNesting(OP_ADD);
			Op op = mHead;
			while (op != null) {
				Fragment f;
				switch(op.cmd) {
				case OP_ADD:
					f = op.fragment;
					f.mNextAnim = op.enterAnim;
					mManager.addFragment(f, false);
					op = op.next;
					break;
				case OP_REPLACE:
					f = op.fragment;
					if (mManager.mAdded != null) {
						int i = OP_NULL;
						while (i < mManager.mAdded.size()) {
							Fragment old = (Fragment) mManager.mAdded.get(i);
							if (FragmentManagerImpl.DEBUG) {
								Log.v(TAG, "OP_REPLACE: adding=" + f + " old=" + old);
							}
							if (f == null || old.mContainerId == f.mContainerId) {
								if (op.removed == null) {
									op.removed = new ArrayList();
								}
								op.removed.add(old);
								old.mNextAnim = op.exitAnim;
								if (mAddToBackStack) {
									old.mBackStackNesting++;
									if (FragmentManagerImpl.DEBUG) {
										Log.v(TAG, "Bump nesting of " + old + " to " + old.mBackStackNesting);
									}
								}
								mManager.removeFragment(old, mTransition, mTransitionStyle);
							} else {
								i++;
							}
							i++;
						}
					}
					if (f != null) {
						f.mNextAnim = op.enterAnim;
						mManager.addFragment(f, false);
					}
					op = op.next;
					break;
				case OP_REMOVE:
					f = op.fragment;
					f.mNextAnim = op.exitAnim;
					mManager.removeFragment(f, mTransition, mTransitionStyle);
					op = op.next;
					break;
				case OP_HIDE:
					f = op.fragment;
					f.mNextAnim = op.exitAnim;
					mManager.hideFragment(f, mTransition, mTransitionStyle);
					op = op.next;
					break;
				case OP_SHOW:
					f = op.fragment;
					f.mNextAnim = op.enterAnim;
					mManager.showFragment(f, mTransition, mTransitionStyle);
					op = op.next;
					break;
				case OP_DETACH:
					f = op.fragment;
					f.mNextAnim = op.exitAnim;
					mManager.detachFragment(f, mTransition, mTransitionStyle);
					op = op.next;
					break;
				case OP_ATTACH:
					f = op.fragment;
					f.mNextAnim = op.enterAnim;
					mManager.attachFragment(f, mTransition, mTransitionStyle);
					op = op.next;
					break;
				}
			}
			mManager.moveToState(mManager.mCurState, mTransition, mTransitionStyle, true);
			if (mAddToBackStack) {
				mManager.addBackStackState(this);
			}
		} else {
			throw new IllegalStateException("addToBackStack() called after commit()");
		}
	}

	public FragmentTransaction setBreadCrumbShortTitle(int res) {
		mBreadCrumbShortTitleRes = res;
		mBreadCrumbShortTitleText = null;
		return this;
	}

	public FragmentTransaction setBreadCrumbShortTitle(CharSequence text) {
		mBreadCrumbShortTitleRes = 0;
		mBreadCrumbShortTitleText = text;
		return this;
	}

	public FragmentTransaction setBreadCrumbTitle(int res) {
		mBreadCrumbTitleRes = res;
		mBreadCrumbTitleText = null;
		return this;
	}

	public FragmentTransaction setBreadCrumbTitle(CharSequence text) {
		mBreadCrumbTitleRes = 0;
		mBreadCrumbTitleText = text;
		return this;
	}

	public FragmentTransaction setCustomAnimations(int enter, int exit) {
		return setCustomAnimations(enter, exit, OP_NULL, OP_NULL);
	}

	public FragmentTransaction setCustomAnimations(int enter, int exit, int popEnter, int popExit) {
		mEnterAnim = enter;
		mExitAnim = exit;
		mPopEnterAnim = popEnter;
		mPopExitAnim = popExit;
		return this;
	}

	public FragmentTransaction setTransition(int transition) {
		mTransition = transition;
		return this;
	}

	public FragmentTransaction setTransitionStyle(int styleRes) {
		mTransitionStyle = styleRes;
		return this;
	}

	public FragmentTransaction show(Fragment fragment) {
		Op op = new Op();
		op.cmd = 5;
		op.fragment = fragment;
		addOp(op);
		return this;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		sb.append("BackStackEntry{");
		sb.append(Integer.toHexString(System.identityHashCode(this)));
		if (mIndex >= 0) {
			sb.append(" #");
			sb.append(mIndex);
		}
		if (mName != null) {
			sb.append(" ");
			sb.append(mName);
		}
		sb.append("}");
		return sb.toString();
	}
}
