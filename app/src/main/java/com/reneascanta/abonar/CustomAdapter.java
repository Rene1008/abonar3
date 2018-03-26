package com.reneascanta.abonar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

//import com.reneascanta.listview_with_checkbox.R;import com.reneascanta.listview_with_checkbox.UserModel;import java.util.List;

/**
 * Created by Rene on 27/10/2017.
 */

public class CustomAdapter extends BaseAdapter {

    Activity activity;
    List<UserModel> users;
    LayoutInflater inflater;

    public CustomAdapter(Activity activity) {
        this.activity = activity;
    }

    public CustomAdapter(Activity activity, List<UserModel> users) {
        this.activity = activity;
        this.users = users;
        inflater = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.list_view_item,parent,false);

            holder =  new viewHolder();

            holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.iv_checkBox = (ImageView) convertView.findViewById(R.id.iv_check_box);

            convertView.setTag(holder);
        }else{
            holder = (viewHolder) convertView.getTag();

            UserModel model = users.get(position);

            holder.tv_userName.setText(model.getUserName());

            if(model.isSelected){
                holder.iv_checkBox.setBackgroundResource(R.drawable.checked);
            }else{
                holder.iv_checkBox.setBackgroundResource(R.drawable.notcheck);

            }
        }
        return  convertView;
    }


    public void updateRecords(List<UserModel> user){

        this.users =  user;
        notifyDataSetChanged();
    }

     class viewHolder{
         TextView tv_userName;
         ImageView iv_checkBox;
     }
}
