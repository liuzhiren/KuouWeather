package practice.kuouweather.model;

/**
 * Created by a312689543 on 2016/4/30.
 */
public class CityListName {
    private int id;
    private String cityName;
    private String cityTemp;
    /*public CityListName (String cityName , String cityTemp){
        this.cityName = cityName;
        this.cityTemp = cityTemp;
    }
    public CityListName (String cityName ){
        this.cityName = cityName;
    }
*/
    public String getCityName() {
        return cityName;
    }

    public String getCityTemp() {
        return cityTemp;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityTemp(String cityTemp) {
        this.cityTemp = cityTemp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


