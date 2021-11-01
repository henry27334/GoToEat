package com.yuhung.gotoeat.FragmentModel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yuhung.gotoeat.ProductProcessing.buyingCart;
import com.yuhung.gotoeat.ProductProcessing.productDetail;
import com.yuhung.gotoeat.R;
import com.yuhung.gotoeat.UsedComponent.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class menuFragmentModel extends Fragment {
    public String result = "Nothing";

    private View view;
//    private LinearLayout linearMenu;
    private ExpandableListView expandableListView;
    private Toolbar toolbar;

    private MyExpandableListAdapter myExpandableListAdapter;
    private HashMap<String, ArrayList> mainArray = new HashMap<>();//父層總陣列
    private ArrayList<HashMap<String, String>> childArray;//子層陣列
    private ArrayList<String> productSort;//子層陣列
    private ArrayList<String> productSortPic;//子層陣列

    private String[] productName_All = null;
    private String[] productPic_All = null;
    private String[] productSort_All = null;
    private String[] sortPhoto_All = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_menupage, container, false);

//        linearMenu = view.findViewById(R.id.linearMenu);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbar = view.findViewById(R.id.tbMenus);
        toolbar.setTitle("各類商品");
        activity.setSupportActionBar(toolbar);

        try {
            setData();
        }catch(Exception e){
            Toast.makeText(getActivity(), "伺服器維修中，請稍後再嘗試。\n錯誤代碼: 2", Toast.LENGTH_SHORT).show();
        }
        expandableListView = view.findViewById(R.id.expandAbleListView);
        expandableListView.setGroupIndicator(null);
        //expandableListView.setChildDivider(getResources().getDrawable(R.color.lightgray));//可以先不加入，這是去子層底線用的
        myExpandableListAdapter = new MyExpandableListAdapter();
        expandableListView.setAdapter(myExpandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int group, int child, long childRowId) {

                Object proObj = mainArray.get(productSort.get(group)).get(child);
                HashMap<String, Object> productName = (HashMap<String, Object>) proObj;
                String drinkStr = (String) productName.get("Child2");

                Intent intent = new Intent();
                intent.setClass(getContext(), productDetail.class);
                intent.putExtra("productName", drinkStr);
                startActivity(intent);
                return false;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });
        return view;

    }//end of view

    public void setData() {

        String dataBaseName = getActivity().getSharedPreferences("userData", getActivity().MODE_PRIVATE)
                .getString("selectedShop",null);

        String[] field, data;
        String result;
        PutData putData;

        field = new String[1];
        field[0] = "database";
        data = new String[1];
        data[0] = dataBaseName;
        putData = new PutData("http://"+getText(R.string.IPv4)+"/"+getText(R.string.SQLConnection)+"/RequireBase/RequireSort.php", "POST", field, data);
        putData.startPut();
        if(putData.onComplete()){
            result = putData.getData();
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo;

            productName_All = new String[ja.length()];
            productPic_All = new String[ja.length()];
            productSort_All = new String[ja.length()];
            sortPhoto_All = new String[ja.length()];

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                productName_All[i] = jo.getString("product_name");
                productPic_All[i] = "http://" + getText(R.string.IPv4) + "/images/" + dataBaseName + "/" + jo.getString("product_pic");
                productSort_All[i] = jo.getString("product_sort");
                sortPhoto_All[i] = "http://" + getText(R.string.IPv4) + "/images/" + dataBaseName + "/SortPic/" + jo.getString("sortPhoto");

            }

            } catch (Exception ex) {
                Log.e("商品菜單",result);
                Log.e("商品菜單", ex.getMessage());
            }
        }

        //sortPic
        Set<String> sortPhotoSet = new LinkedHashSet<String>();
        for(String str : sortPhoto_All){
            sortPhotoSet.add(str);
        }

        Iterator sortPicIt = sortPhotoSet.iterator();
        productSortPic = new ArrayList<>();

        while(sortPicIt.hasNext()){
            String sortPicStr = (String)sortPicIt.next();
            productSortPic.add(sortPicStr);
            Log.i("photo", sortPicStr);
        }

        //sort
        Set<String> sortSet = new LinkedHashSet<String>();
        for(String str : productSort_All){
            sortSet.add(str);
        }

        Iterator sortIt = sortSet.iterator();
        productSort = new ArrayList<>();

        while(sortIt.hasNext()){
            String sortStr = (String)sortIt.next();
            childArray = new ArrayList<>();//子層陣列
            productSort.add(sortStr);
            for (int s = 0; s < productSort_All.length; s++) {
                if (sortStr.equals(productSort_All[s])) {
                    HashMap<String, String> childName = new HashMap<>();//子層內容
                    childName.put("Child1", productPic_All[s]);
                    childName.put("Child2", productName_All[s]);
                    childArray.add(childName);
                }
            }
            mainArray.put(sortStr, childArray);
        }

    }//end of setData

    private class MyExpandableListAdapter extends BaseExpandableListAdapter {
        @Override
        public int getGroupCount() {//父陣列長度
            return mainArray.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {//子陣列長度
            return mainArray.get(productSort.get(groupPosition)).size();
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
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {//設置父項目的View
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.style_expandlistview_item, null);
            }
            convertView.setTag(R.layout.style_expandlistview_item, groupPosition);
            convertView.setTag(R.layout.style_expandlistview_item, -1);
            TextView textView = convertView.findViewById(R.id.textView_ItemTitle);
            ImageView imgSort = convertView.findViewById(R.id.imgSort);
            textView.setText(productSort.get(groupPosition));
            //Glide.with(menuFragmentModel.this).load(productPic[groupPosition]).into(imgSort);
            Glide.with(menuFragmentModel.this).load(productSortPic.get(groupPosition))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(50))).into(imgSort);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {//設置子項目的View
                LayoutInflater inflater =
                        (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.style_expandlistview_child, null);
            }
            convertView.setTag(R.layout.style_expandlistview_child, groupPosition);
            convertView.setTag(R.layout.style_expandlistview_child, -1);
            ImageView imgDrink = convertView.findViewById(R.id.imgProduct);
            TextView tvDrink = convertView.findViewById(R.id.textView_child2);

            Object proObj = mainArray.get(productSort.get(groupPosition)).get(childPosition);
            HashMap<String, String> productName = (HashMap<String, String>) proObj;

            //Glide.with(menuFragmentModel.this).load(productName.get("Child1")).into(imgDrink);
            Glide.with(menuFragmentModel.this).load(productName.get("Child1"))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imgDrink);
            tvDrink.setText((String) productName.get("Child2"));

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {//設置子項目是否可點擊
            return true;
        }
    }

    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.orderCart:
                Intent intent = new Intent(getContext(), buyingCart.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}//end of class