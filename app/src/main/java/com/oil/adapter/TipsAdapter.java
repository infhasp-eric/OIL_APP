package com.oil.adapter;

import java.util.List;
import java.util.Map;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.oil.activity.AskNoticeDetailActivity;
import com.oil.activity.Itco_manage_View;
import com.oil.activity.R;
import com.oil.activity.TmpNoticeDetailActivity;
import com.oil.activity.WebViewActivity;
import com.oil.utils.Urls;

public class TipsAdapter extends BaseAdapter{
	private Context context;                        //运行上下文   
    public List<Map<String, Object>> listItems;    //商品信息集合   
    private LayoutInflater listContainer;           //视图容器   
    private boolean[] hasChecked;                   //记录商品选中状态   
    public final class ListItemView{                //自定义控件集合          
        public TextView txt_date;
        public TextView txt_content;
        public LinearLayout lin_tip;
    }     
       
       
    public TipsAdapter(Context context, List<Map<String, Object>> listItems) {   
        this.context = context;            
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
        this.listItems = listItems;   
        hasChecked = new boolean[getCount()];   
    }   
  
    public int getCount() {   
        // TODO Auto-generated method stub   
    	if(listItems != null) {
        return listItems.size();   
    	} else {
    		return 0;
    	}
    }   
  
    public Object getItem(int arg0) {   
        // TODO Auto-generated method stub   
        return null;   
    }   
  
    public long getItemId(int arg0) {   
        // TODO Auto-generated method stub   
        return 0;   
    }   
       
    /**  
     * 记录勾选了哪个物品  
     * @param checkedID 选中的物品序号  
     */  
    private void checkedChange(int checkedID) {   
        hasChecked[checkedID] = !hasChecked[checkedID];   
    }   
       
    /**  
     * 判断物品是否选择  
     * @param checkedID 物品序号  
     * @return 返回是否选中状态  
     */  
    public boolean hasChecked(int checkedID) {   
        return hasChecked[checkedID];   
    }   
       
    /**  
     * 显示通知详情  
     * @param clickID  
     */  
    private void showDetailInfo(int type, String id) {
    	Intent intent = new Intent();
		Bundle bundle = new Bundle(); // 创建Bundle对象
    	switch (type) {
		case 1:
			intent.setClass(context, Itco_manage_View.class);
			bundle.putString("listid", id); // 装入数据
			intent.putExtras(bundle);
			break;
		case 2:
			intent.setClass(context, AskNoticeDetailActivity.class);
			//Log.v("iddddddddddddd", id);
			bundle.putString("id", id);
			intent.putExtras(bundle);
			break;
		case 3:
			intent.setClass(context, TmpNoticeDetailActivity.class);
			//Log.v("iddddddddddddd", id);
			bundle.putString("id", id);
			intent.putExtras(bundle);
			break;
		default:
			intent.setClass(context, WebViewActivity.class);
			bundle.putString("url", Urls.WEBSHOW + id);
			intent.putExtras(bundle);
			break;
		}
        context.startActivity(intent);
    }   
       
          
    /**  
     * ListView Item设置  
     */  
    public View getView(int position, View convertView, ViewGroup parent) {   
        // TODO Auto-generated method stub   
        Log.e("method", "getView");   
        //自定义视图   
        ListItemView  listItemView = null;   
        if (convertView == null) {   
            listItemView = new ListItemView();    
            //获取list_item布局文件的视图   
            convertView = listContainer.inflate(R.layout.tips_layout, null);   
            //获取控件对象    
            listItemView.lin_tip = (LinearLayout) convertView.findViewById(R.id.lin_tip);
            listItemView.txt_date = (TextView)convertView.findViewById(R.id.txt_date);   
            listItemView.txt_content = (TextView)convertView.findViewById(R.id.txt_content);
            
            //设置控件集到convertView   
            convertView.setTag(listItemView);   
        }else {   
            listItemView = (ListItemView)convertView.getTag();   
        }   
//      Log.e("image", (String) listItems.get(position).get("title"));  //测试   
//      Log.e("image", (String) listItems.get(position).get("info"));   
           
        //设置文字和图片    
        listItemView.txt_date.setText((String) listItems.get(position)   
                .get("date"));
        listItemView.txt_content.setText((String) listItems.get(position)   
                .get("content"));
        
        final String id = listItems.get(position).get("id").toString();
        final int type = (Integer) listItems.get(position).get("type");
        
        //设置列表点击事件
        listItemView.lin_tip.setOnClickListener(new View.OnClickListener() {   
            @Override  
            public void onClick(View v) {   
                //显示物品详情   
                showDetailInfo(type, id);   
            }   
        }); 
        
        return convertView; 
    }   
}
