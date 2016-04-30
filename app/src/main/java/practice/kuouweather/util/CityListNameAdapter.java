package practice.kuouweather.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import practice.kuouweather.R;
import practice.kuouweather.model.CityListName;

/**
 * Created by a312689543 on 2016/4/30.
 */
public class CityListNameAdapter extends ArrayAdapter<CityListName> {
    private int resourceId;
    public CityListNameAdapter(Context context, int resource, List<CityListName> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityListName city = getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView city_Name = (TextView)view.findViewById(R.id.city_name_text);
        city_Name.setText(city.getCityName());
        return view;
    }
}
