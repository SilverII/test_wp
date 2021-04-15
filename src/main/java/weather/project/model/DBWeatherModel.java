package weather.project.model;

public class DBWeatherModel {
    private int id;
    private String city;
    private String src;
    private String temp;
    private String wind;
    private String cloud;
    private String api;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String source) {
        this.src = source;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemp() { return temp; }

    public void setTemp(String temp) { this.temp = temp; }

    public String getWind() { return wind; }

    public void setWind(String wind) { this.wind = wind; }

    public String getCloud() { return cloud; }

    public void setCloud(String cloud) { this.cloud = cloud; }

    public DBWeatherModel() { }
}
