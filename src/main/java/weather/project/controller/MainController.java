package weather.project.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import weather.project.controller.jsonOpenWeather.JsonModel;
import weather.project.controller.jsonWeatherBit.Json2Model;
import weather.project.model.DBUserModel;
import weather.project.model.DBWeatherModel;
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
    public ModelAndView auth() {
        return new ModelAndView("auth", "command", new LPModel());
    }

    //класс проверки аутентификации и получения некоторых данных
    @RequestMapping(value = "/checkAuth", method = RequestMethod.POST)
    public String checkAuth(@ModelAttribute LPModel lpModel){
        sessionFactory = new Configuration().configure().buildSessionFactory();
        List<DBUserModel> info = listInfo(lpModel.getLogin());

        if (lpModel.getPassword().contentEquals(info.get(info.size()-1).getPassword())){
            log = lpModel.getLogin();
            src = info.get(info.size()-1).getSource();
            cty = info.get(info.size()-1).getCity();
            return "forward:/main";
        }
        else return "forward:/index";
    }

    //основная страница с отображением данных из апи
    @RequestMapping(value = "/main", method = RequestMethod.POST)//post
    public ModelAndView mainPage(){
        ModelAndView model = new ModelAndView("result");
        sessionFactory = new Configuration().configure().buildSessionFactory();
        List<DBWeatherModel> info = listDataW();
        //создание фабрики сессий (бд) и получение значений со страницы аутентификации
        RestTemplate rt = new RestTemplate();

        //отправка запросов к апи
            if (src.contentEquals("src1")){
                JsonModel api_results = rt.getForObject(info.get(info.size()-1).getApi(), JsonModel.class);

                model.addObject("temp", Math.round(api_results.getMain().getTemp()));
                model.addObject("wind", api_results.getWind().getSpeed());
                model.addObject("clouds", api_results.getClouds().getAll());
                return model;
            }
            else if (src.contentEquals("src2")){
                Json2Model api_results = rt.getForObject(info.get(info.size()-1).getApi(), Json2Model.class);

                model.addObject("temp", api_results.getData().get(api_results.getData().size()-1).getTemp());
                model.addObject("wind", api_results.getData().get(api_results.getData().size()-1).getWindSpd());
                model.addObject("clouds", api_results.getData().get(api_results.getData().size()-1).getClouds());
                return model;
            }
            else return setPage();
    }

    //создание вью настроек
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ModelAndView setPage() {
        ModelAndView mav = new ModelAndView("settings", "command", new LPModel());
        mav.addObject("src", src);
        mav.addObject("cty", cty);

        return mav;
    }

    //создание фабрики сессий (бд) и отправка транзакции
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("save") LPModel lpModel) {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        List<DBUserModel> info = listInfo(log);

        updateInfo(info.get(info.size()-1).getId(), lpModel.getLogin(), lpModel.getPassword());
        return "forward:/main";
    }

    //функция получения данных из бд table1
    public List<DBUserModel> listInfo(String login) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        List<DBUserModel> pass = session.createQuery("FROM DBUserModel WHERE login = " + login).list();

        transaction.commit();
        session.close();
        return pass;
    }

    //функция обновления данных в бд table1
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

    //функция получения данных из бд table 2
    public List<DBWeatherModel> listDataW() {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        List<DBWeatherModel> pass = session.createQuery("FROM DBWeatherModel WHERE city = '" + cty + "' and src = '" + src + "'").list();

        transaction.commit();
        session.close();
        return pass;
    }
}
