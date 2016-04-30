package practice.kuouweather.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import practice.kuouweather.R;
import practice.kuouweather.model.City;
import practice.kuouweather.model.CityListName;
import practice.kuouweather.model.CoolWeatherDB;
import practice.kuouweather.model.Province;
import practice.kuouweather.util.CityListNameAdapter;

/**
 * Created by a312689543 on 2016/4/29.
 */


public class ListCityActivity extends Activity implements View.OnClickListener {
    /*
 初始化ListCity
  */
    private ListView listcity;
    /*
    储存城市
     */
    private List<CityListName> cityNameList;

    private List<String> datalist = new ArrayList<String>();
    //private CityListNameAdapter madapter;
    /*
    * 添加城市
     */
    private Button addCity;
    /*
    删除城市
     */
    private Button deleteCity;


    private ArrayAdapter<String> mAdapter;

    private CoolWeatherDB mCoolWeatherDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_city);
        addCity = (Button)findViewById(R.id.add_city);
        deleteCity = (Button)findViewById(R.id.delete_city);
        listcity = (ListView)findViewById(R.id.list_city);
        mCoolWeatherDB=CoolWeatherDB.getInstance(this);
        //CityListNameAdapter madapter = new CityListNameAdapter(ListCityActivity.this,
              //  R.layout.city_name_list,cityNameList);
        mAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
        listcity.setAdapter(mAdapter);
        addCity.setOnClickListener(this);
        listcity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }); initCity();


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_city:
                Intent intent = new Intent(ListCityActivity.this,ChooseAreaActivity.class);
                intent.putExtra("from_list_city_activity", true);
                startActivity(intent);
        }
    }
    private void initCity() {
        try {
            Log.d("addCity","是否进了TRY");
            cityNameList=mCoolWeatherDB.LoadCityNameList();
            Log.d("initCity","size"+cityNameList.size());
            if(cityNameList.size()>0){
                datalist.clear();
                for(CityListName c:cityNameList){
                    datalist.add(c.getCityName());
                }
                mAdapter.notifyDataSetChanged();//// 适配器的内容改变时需要强制调用getView来刷新每个Item的内容
                listcity.setSelection(0);
            }else {
                initCitys();
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("addCity","异常");
        }

    }

    private void initCitys() {
        String name = getIntent().getStringExtra("city_name");
        CityListName cityListName = new CityListName();
        cityListName.setCityName(name);
        datalist.add(cityListName.getCityName());
    }


}


