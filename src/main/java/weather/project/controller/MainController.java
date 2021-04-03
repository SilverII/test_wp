package weather.project.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.web.client.RestTemplate;
import weather.project.controller.jsonOpenWeather.JsonModel;
import weather.project.controller.jsonWeatherBit.Json2Model;
import weather.project.model.DBUserModel;
import weather.project.model.LPModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.*;

@Controller
public class MainController {
    private static SessionFactory sessionFactory;
    String log;
    String src;
    String cty;

    //редирект на страницу индекс
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "/index";
    }

    //создание вью страницы аутентификации
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public ModelAndView aut() {
        return new ModelAndView("auth", "command", new LPModel());
    }

    //основная страница с отображением данных из апи
    @RequestMapping(value = "/main", method = RequestMethod.POST)
    public String mainPage(@ModelAttribute("main") LPModel lpModel, ModelMap model) {

        //создание фабрики сессий (бд) и получение значений со страницы аутентификации
        sessionFactory = new Configuration().configure().buildSessionFactory();
        List<DBUserModel> info = listInfo(lpModel.getLogin());
        log = lpModel.getLogin();
        RestTemplate rt = new RestTemplate();
        src = info.get(info.size()-1).getSource();
        cty = info.get(info.size()-1).getCity();

        //проверка аутентификации и отправка запросов к апи
        if (lpModel.getPassword().contentEquals(info.get(info.size()-1).getPassword())){
            if (info.get(info.size()-1).getSource().contentEquals("src1")){
                JsonModel api_results = rt.getForObject("http://api.openweathermap.org/data/2.5/weather?id="+info.get(info.size()-1).getCity()+"&appid=18dc867da3ee24c0d9bbb6d86b6f9d34", JsonModel.class);

                model.addAttribute("temp", api_results.getMain().getTemp());
                model.addAttribute("wind", api_results.getWind().getSpeed());
                model.addAttribute("clouds", api_results.getClouds().getAll());
                return "result";
            }
            else if (info.get(info.size()-1).getSource().contentEquals("src2")){
                Json2Model api_results = rt.getForObject("https://api.weatherbit.io/v2.0/current?&city_id="+info.get(info.size()-1).getCity()+"&key=6afae13fa97e4b9296c6ce0354e14ab8&include=minutely", Json2Model.class);

                model.addAttribute("temp", api_results.getData().get(api_results.getData().size()-1).getTemp());
                model.addAttribute("wind", api_results.getData().get(api_results.getData().size()-1).getWindSpd());
                model.addAttribute("clouds", api_results.getData().get(api_results.getData().size()-1).getClouds());
                return "result";
            }
            else return "/settings";
        }
        else {
            return "/index";
        }
    }

    //создание вью настроек
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ModelAndView setPage(@ModelAttribute ModelMap model) {
        ModelAndView mav = new ModelAndView("settings", "command", new LPModel());
        mav.addObject("src", src);
        mav.addObject("cty", cty);

        return mav;
    }

    //создание фабрики сессий (бд) и отправка транзакции
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("save") LPModel lpModel, ModelMap model) {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        List<DBUserModel> info = listInfo(log);
        String city = null;
        //login as city + password as source
        if (lpModel.getPassword().contentEquals("src1")){
            if (lpModel.getLogin().contentEquals("EKB")){city = "1486209";}
            if (lpModel.getLogin().contentEquals("SPB")){city = "498817";}
            if (lpModel.getLogin().contentEquals("MSK")){city = "524894";}
        }
        else if (lpModel.getPassword().contentEquals("src2")){
            if (lpModel.getLogin().contentEquals("EKB")){city = "1486209";}
            if (lpModel.getLogin().contentEquals("SPB")){city = "498817";}
            if (lpModel.getLogin().contentEquals("MSK")){city = "524901";}
        }
        else return "/settings";

        updateInfo(info.get(info.size()-1).getId(), city, lpModel.getPassword());
        return "/index";
    }

    //функция получения данных из бд
    public List<DBUserModel> listInfo(String login) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        List<DBUserModel> pass = session.createQuery("FROM DBUserModel WHERE login = " + login).list();

        transaction.commit();
        session.close();
        return pass;
    }

    //функция обновления данных в бд
    public void updateInfo(int id,String city, String source) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = null;
        DBUserModel info = (DBUserModel) session.get(DBUserModel.class, id);
        info.setCity(city);
        info.setSource(source);
        transaction = session.beginTransaction();
        session.update(info);
        transaction.commit();
        session.close();
    }
}
