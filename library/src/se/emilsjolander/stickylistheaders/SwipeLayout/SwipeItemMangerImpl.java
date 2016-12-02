package se.emilsjolander.stickylistheaders.SwipeLayout;

import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SwipeItemMangerImpl is a helper class to help all the adapters to maintain open status.
 */
public class SwipeItemMangerImpl {

    private Attributes.Mode mode = Attributes.Mode.Single;
    protected String mOpenPosition;

    protected Set<String> openKeys = new HashSet<String>();
    protected Set<SwipeLayout> mShownLayouts = new HashSet<SwipeLayout>();


    public Attributes.Mode getMode() {
        return mode;
    }

    public void setMode(Attributes.Mode mode) {
        this.mode = mode;
        openKeys.clear();
        mShownLayouts.clear();
    }

    public void bind(View view, String id, int resId) {
        SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(resId);
        if (swipeLayout == null)
            throw new IllegalStateException("can not find SwipeLayout in target view");

        if (swipeLayout.getTag(resId) == null) {
            OnLayoutListener onLayoutListener = new OnLayoutListener(id);
            SwipeMemory swipeMemory = new SwipeMemory(id);

            swipeLayout.addSwipeListener(swipeMemory);
            swipeLayout.addOnLayoutListener(onLayoutListener);
            swipeLayout.setTag(resId, new ValueBox(id, swipeMemory, onLayoutListener));
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        } else {
            ValueBox valueBox = (ValueBox) swipeLayout.getTag(resId);
            valueBox.swipeMemory.setKey(id);
            valueBox.onLayoutListener.setPosition(id);
            valueBox.key = id;
        }

        mShownLayouts.add(swipeLayout);
    }

    public void openItem(String key) {
        if (mode == Attributes.Mode.Multiple) {
            if (!openKeys.contains(key))
                openKeys.add(key);
        } else {
            mOpenPosition = key;
        }
    }


    public void closeItem(String key) {
        if (mode == Attributes.Mode.Multiple) {
            openKeys.remove(key);
        } else {
            if (TextUtils.equals(mOpenPosition, key))
                mOpenPosition = null;
        }
    }

    public void closeAllExcept(SwipeLayout layout) {
        for (SwipeLayout s : mShownLayouts) {
            if (s != layout)
                s.close();
        }
    }

    public void closeAllItems() {
        if (mode == Attributes.Mode.Multiple) {
            openKeys.clear();
        } else {
            mOpenPosition = null;
        }
        for (SwipeLayout s : mShownLayouts) {
            s.close();
        }
    }

    public void removeShownLayouts(SwipeLayout layout) {
        mShownLayouts.remove(layout);
    }

    public List<String> getOpenItems() {
        if (mode == Attributes.Mode.Multiple) {
            return new ArrayList<String>(openKeys);
        } else {
            return Collections.singletonList(mOpenPosition);
        }
    }

    public List<SwipeLayout> getOpenLayouts() {
        return new ArrayList<SwipeLayout>(mShownLayouts);
    }

    public boolean isOpen(String key) {
        if (mode == Attributes.Mode.Multiple) {
            return openKeys.contains(key);
        } else {
            return TextUtils.equals(mOpenPosition , key);
        }
    }

    class ValueBox {
        OnLayoutListener onLayoutListener;
        SwipeMemory swipeMemory;
        String key;

        ValueBox(String key, SwipeMemory swipeMemory, OnLayoutListener onLayoutListener) {
            this.swipeMemory = swipeMemory;
            this.onLayoutListener = onLayoutListener;
            this.key = key;
        }
    }

    class OnLayoutListener implements SwipeLayout.OnLayout {

        private String key;

        OnLayoutListener(String key) {
            this.key = key;
        }

        public void setPosition(String id) {
            this.key = id;
        }

        @Override
        public void onLayout(SwipeLayout v) {
            if (isOpen(key)) {
                v.open(false, false);
            } else {
                v.close(false, false);
            }
        }

    }

    class SwipeMemory extends SimpleSwipeListener {

        private String key;

        SwipeMemory(String position) {
            this.key = position;
        }

        @Override
        public void onClose(SwipeLayout layout) {
            if (mode == Attributes.Mode.Multiple) {
                openKeys.remove(key);
            } else {
                mOpenPosition = null;
            }
        }

        @Override
        public void onStartOpen(SwipeLayout layout) {
            if (mode == Attributes.Mode.Single) {
                closeAllExcept(layout);
            }
        }

        @Override
        public void onOpen(SwipeLayout layout) {
            if (mode == Attributes.Mode.Multiple)
                openKeys.add(key);
            else {
                closeAllExcept(layout);
                mOpenPosition = key;
            }
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

}
