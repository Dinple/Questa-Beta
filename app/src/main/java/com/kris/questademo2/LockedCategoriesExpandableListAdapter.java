package com.kris.questademo2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class LockedCategoriesExpandableListAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private Activity activity;
    private HashMap<String, String> child;
    private LayoutInflater inflater;
    protected static ArrayList<HashMap<String, String>> parentItems = new ArrayList<>();
    protected static ArrayList<ArrayList<HashMap<String, String>>> childItems =new ArrayList<>();

    public LockedCategoriesExpandableListAdapter(Activity activity, ArrayList<HashMap<String, String>> parentItems,
                                                 ArrayList<ArrayList<HashMap<String, String>>> childItems,Context mContext){
        this.parentItems = parentItems;
        this.childItems = childItems;
        this.activity = activity;
        this.mContext = mContext;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (childItems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderParent viewHolderParent;

        if (convertView == null) {
                convertView = inflater.inflate(R.layout.locked_group_list_layout, null);

            viewHolderParent = new ViewHolderParent();

            viewHolderParent.tvMainCategoryName = convertView.findViewById(R.id.tvMainCategoryName);
            viewHolderParent.tvMainCategoryInfo = convertView.findViewById(R.id.tv_quest_info);
            viewHolderParent.cbMainCategory = convertView.findViewById(R.id.cbMainCategory);
            viewHolderParent.viewDivider = convertView.findViewById(R.id.view_divider);
            convertView.setTag(viewHolderParent);
        } else {

            viewHolderParent = (ViewHolderParent) convertView.getTag();

        }

        ConstantManager.childItems = childItems;
        ConstantManager.parentItems = parentItems;

        viewHolderParent.tvMainCategoryName.setText(parentItems.get(groupPosition).get(ConstantManager.Parameter.CATEGORY_NAME));
        viewHolderParent.tvMainCategoryInfo.setText(parentItems.get(groupPosition).get(ConstantManager.Parameter.CATEGORY_INFO));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolderChild viewHolderChild;
        child = childItems.get(groupPosition).get(childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.locked_child_list, null);

            viewHolderChild = new ViewHolderChild();

            viewHolderChild.tvSubCategoryName = convertView.findViewById(R.id.tvSubCategoryName);
            viewHolderChild.ivSubCategory = convertView.findViewById(R.id.cbSubCategory);

            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

        viewHolderChild.tvSubCategoryName.setText(child.get(ConstantManager.Parameter.SUB_CATEGORY_NAME));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class ViewHolderParent {
        TextView tvMainCategoryName;
        TextView tvMainCategoryInfo;
        CheckBox cbMainCategory;
        View viewDivider;
    }

    private class ViewHolderChild {

        TextView tvSubCategoryName;
        ImageView ivSubCategory;

    }
}
