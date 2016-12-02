package se.emilsjolander.stickylistheaders.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.SwipeLayout.Attributes;
import se.emilsjolander.stickylistheaders.SwipeLayout.SwipeItemMangerImpl;
import se.emilsjolander.stickylistheaders.SwipeLayout.SwipeLayout;

public class TestBaseAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;
    private String[] mCountries;
    private int[] mSectionIndices;
    private Character[] mSectionLetters;
    private LayoutInflater mInflater;
    private SwipeItemMangerImpl swipeItemManger;

    public TestBaseAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mCountries = context.getResources().getStringArray(R.array.countries);
        mSectionIndices = getSectionIndices();//26个字母的位置
        mSectionLetters = getSectionLetters();//到底是哪个字母
        swipeItemManger = new SwipeItemMangerImpl();
        swipeItemManger.setMode(Attributes.Mode.Single);
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        char lastFirstChar = mCountries[0].charAt(0);
        sectionIndices.add(0);
        for (int i = 1; i < mCountries.length; i++) {
            if (mCountries[i].charAt(0) != lastFirstChar) {
                lastFirstChar = mCountries[i].charAt(0);
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private Character[] getSectionLetters() {
        Character[] letters = new Character[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = mCountries[mSectionIndices[i]].charAt(0);
        }
        return letters;
    }

    @Override
    public int getCount() {
        return mCountries.length;
    }

    @Override
    public Object getItem(int position) {
        return mCountries[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        //if (convertView == null) {

            convertView = mInflater.inflate(R.layout.item_swipe_delete, parent, false);
            SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe_item);
            View view = mInflater.inflate(R.layout.test_list_item_layout, swipeLayout, false);
            holder = new ViewHolder(view);
            if (view != null && holder != null) {
                view.setTag(holder);
            }
            if (view != null && swipeLayout != null) {
                swipeLayout.addView(view, 1);

            }
        //}
        SwipeLayout swipeItem = (SwipeLayout) convertView.findViewById(R.id.swipe_item);
        //swipeItem.close();
        holder = (ViewHolder) ((swipeItem.getChildAt(1)).getTag());

        holder.text.setText(mCountries[position]);
        View iv_del = convertView.findViewById(R.id.iv_del);

        iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mCountries[position] + "", Toast.LENGTH_SHORT).show();
                ArrayList<String> list = new ArrayList<>(Arrays.asList(mCountries));
                list.remove(position);
                String[] array = new String[list.size()];
                list.toArray(array);
                mCountries = array;
                notifyDataSetChanged();
            }
        });

        swipeItemManger.bind(convertView, getKey(mCountries[position]), R.id.swipe_item);


        return convertView;
    }

    private String getKey(String item) {
        return item + "_" + item.charAt(0);

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        // set header text as first char in name
        CharSequence headerChar = mCountries[position].subSequence(0, 1);
        holder.text.setText(headerChar);

        return convertView;
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        // return the first character of the country as ID because this is what
        // headers are based upon
        return mCountries[position].subSequence(0, 1).charAt(0);
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    public void clear() {
        mCountries = new String[0];
        mSectionIndices = new int[0];
        mSectionLetters = new Character[0];
        notifyDataSetChanged();
    }

    public void restore() {
        mCountries = mContext.getResources().getStringArray(R.array.countries);
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;

        ViewHolder(View view) {
            text = (TextView) view.findViewById(R.id.text);
        }
    }

}
