package practice.kuouweather.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import practice.kuouweather.R;
import practice.kuouweather.model.CityListName;
import practice.kuouweather.model.CoolWeatherDB;
import practice.kuouweather.service.AutoUpdateWeatherService;
import practice.kuouweather.util.HttpCallbackListener;
import practice.kuouweather.util.HttpUtil;
import practice.kuouweather.util.SmartWeatherUrlUtil;
import practice.kuouweather.util.Utility;

public class WeatherLActivity extends Activity implements View.OnClickListener {
    private LinearLayout weather_text_info;
    //cityName
    private TextView cityName;
    //cityCode
    private TextView cityCode;
    //temp1
    private TextView temp1;
    //temp2
    private TextView temp2;
    //ptime
    private TextView punlishTime;
    //weatherDesp
    private TextView weatherDesp;
    //currentTime
    private TextView currentTime;
    /**
     *  切换城市按钮
     */
    private Button switchCity;
    /**
     *  更新天气按钮
     */
    private Button refreshWeather;
    private CoolWeatherDB mCoolWeatherDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        cityName= (TextView) findViewById(R.id.city_name_text);
        temp1= (TextView) findViewById(R.id.temp1_data);
        temp2= (TextView) findViewById(R.id.temp2_data);
        punlishTime= (TextView) findViewById(R.id.update_date);
        weatherDesp= (TextView) findViewById(R.id.weather_data);
        currentTime= (TextView) findViewById(R.id.now_date);
        weather_text_info= (LinearLayout) findViewById(R.id.weather_info_layout);
        switchCity= (Button) findViewById(R.id.switch_city);
        refreshWeather= (Button) findViewById(R.id.refresh_data);
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
        String countyCode=getIntent().getStringExtra("county_code");
        Log.d("Main", "1");
        if(!TextUtils.isEmpty(countyCode)){
            // 有县级代号时就去查询天气
            punlishTime.setText("同步中...");
            weather_text_info.setVisibility(View.INVISIBLE);
            cityName.setVisibility(View.INVISIBLE);
            Log.d("Main", "2");
            queryWeatherCode(countyCode);
        }
        else {
            // 没有县级代号时就直接显示本地天气
            Log.d("Main","3");
            showWeather();
        }
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_city:
                Intent intent =new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_data:
                punlishTime.setText("同步中...");
                SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode=pref.getString("citycode","");
                if(!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }

                break;
            default:break;
        }

    }
    /**
     * 查询县级代号所对应的天气代号。
     */
    private void queryWeatherCode(String countyCode) {
        String adress="http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        Log.d("Main",countyCode);
        queryFromServe(adress, "countyCode");

    }
  /*新Api调用
    private void queryWeatherInfo(String countyCode) {
        String adress= SmartWeatherUrlUtil.getInterfaceURL(countyCode,"forecast_f");
        queryFromServe(adress, "weatherCode");

    }*/
    /**
     * 查询天气代号所对应的天气。
     */
    private void queryWeatherInfo(String weatherCode) {
        String adress="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        Log.d("Main",weatherCode);
        queryFromServe(adress, "weatherCode");

    }
    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息。
     */

    private void queryFromServe(final String adress,final String type) {
        HttpUtil.sendRequestHttp(adress, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array != null && array.length > 0) {
                            String weatherCode = array[1];//areaid 城市编号
                            Log.d("Main", "4");
                            queryWeatherInfo(weatherCode);
                        }

                    }
                } else if ("weatherCode".equals(type)) {
                    Log.d("Main", "5");
                    Utility.handleWeatherResponse(WeatherLActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        punlishTime.setText("同步失败");
                    }
                });

            }
        });
    }
    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
     */
    private void showWeather() {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        cityName.setText(pref.getString("cityName",""));
        punlishTime.setText("今天"+(pref.getString("publishTime",""))+"发布");
        if(pref.getString("temp1","").compareTo(pref.getString("temp2","")) > 0 ){
            temp1.setText(pref.getString("temp2",""));
            temp2.setText(pref.getString("temp1",""));
        }else{
            temp1.setText(pref.getString("temp1",""));
            temp2.setText(pref.getString("temp2",""));
        }

        weatherDesp.setText(pref.getString("weatherDesp",""));
        currentTime.setText(pref.getString("currentDate", ""));
        weather_text_info.setVisibility(View.VISIBLE);
        cityName.setVisibility(View.VISIBLE);
        Intent intent=new Intent(this, AutoUpdateWeatherService.class);
        this.startService(intent);

    }
    /*
    设置一个Setting菜单按钮
     */
    public boolean  onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close_Auto_Upadate_Service:
                //是否关闭自动更新
                Intent intent=new Intent(this, AutoUpdateWeatherService.class);
                stopService(intent);
                Toast.makeText(WeatherLActivity.this,"close_Auto_Upadate_Service",Toast.LENGTH_SHORT).show();
                break;
            case R.id.open_Auto_Upadate_Service:
                Intent intent1=new Intent(this, AutoUpdateWeatherService.class);
                startService(intent1);
                Toast.makeText(WeatherLActivity.this,"open_Auto_Upadate_Service",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
                Intent intent2 = new Intent(WeatherLActivity.this,ListCityActivity.class);
                intent2.putExtra("city_name",pref.getString("cityName",""));
                intent2.putExtra("city_temp1",pref.getString("temp1",""));
                intent2.putExtra("city_temp2",pref.getString("temp2",""));
                intent2.putExtra("from_weather_city_activity", true);
                Log.d("set", pref.getString("cityName", ""));
                //CityListName cityListName = new CityListName();
                //cityListName.setCityName(pref.getString("cityName",""));
                //mCoolWeatherDB.saveCityListName(cityListName);
                //Toast.makeText(WeatherLActivity.this,"打开了设置界面",Toast.LENGTH_SHORT).show();
                startActivity(intent2);

                break;
            default: break;
        }
        return true;
    }




}
