package com.jhlubucek.smart.controls;

import com.jhlubucek.smart.controls.entity.*;
import com.jhlubucek.smart.controls.sevices.DatabaseConnector;
import com.jhlubucek.smart.controls.sevices.MqttService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class DemoController
{
    @Autowired
    private MqttService mqttService;

    @Autowired
    private DatabaseConnector databaseConnector;

    @RequestMapping(value = "/light/{id}/state/{state}", method = RequestMethod.POST)
    @ResponseBody
    public  String updateLight(@PathVariable("id") int id, @PathVariable("state") int state) throws MqttException {
        Light light = databaseConnector.getLightById(id);
        mqttService.publish(light.getTopicState(), Integer.toString(state), 0, false);
        return "state set";
    };

    @RequestMapping(value = "/light/{id}/brightness/{value}", method = RequestMethod.POST)
    @ResponseBody
    public  String updateBroghtness(@PathVariable("id") int id, @PathVariable("value") int value) throws MqttException {
        Light light = databaseConnector.getLightById(id);
        mqttService.publish(light.getTopicBrightness(), Integer.toString(value), 0, false);
        return "brightness set";
    };

    @RequestMapping(value = "/light/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public  String deleteLight(@PathVariable("id") int id) throws MqttException {
        databaseConnector.deleteLight(id);
        return "deleted";
    };

    @RequestMapping(value = "/sensor/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public  String deleteSensor(@PathVariable("id") int id) throws MqttException {
        databaseConnector.deleteSensor(id);
        return "deleted";
    };

    @RequestMapping("/dashboard/{id}" )
    public ModelAndView dashboard(@PathVariable("id") int id)
    {
        ModelAndView modelAndView = new ModelAndView();
        List<Dashboard> dashboards = databaseConnector.findAllDashboards();
        List<Light> lights = databaseConnector.getAllLightsFromDashboard(id);
        Dashboard dashboard = databaseConnector.getDashboardById(id);

        List<Sensor> sensors = databaseConnector.findAllSensorsFromDashboard(id);
        sensors.forEach(sensor -> sensor.setReadings(databaseConnector.getReadingsForLast24Hours(sensor.getId())));
        sensors.forEach(s -> s.getReadings().forEach(r -> System.out.println(r.getFormatedDate())));

        modelAndView.addObject("lights", lights);
        modelAndView.addObject("dashboards", dashboards);
        modelAndView.addObject("dashboard", dashboard);
        modelAndView.addObject("sensors", sensors);
        modelAndView.addObject("msg", "moje message");
        modelAndView.setViewName("mainPage");
        return modelAndView;
    }

    @RequestMapping("/" )
    public RedirectView index()
    {
        Dashboard dashboard = databaseConnector.getMainDashboard();
        String url = String.format("dashboard/%d",dashboard.getId());
        return new RedirectView(url);
    }

    @RequestMapping("/dashboard/{id}/add_light")
    public String addLight(Model model, @PathVariable("id") int id)
    {
        Light light = new Light();
        light.setDashboardId(id);
        model.addAttribute("light", light);
        return "add_light_form";
    }

    @RequestMapping("/dashboard/{id}/add_sensor")
    public String addSensor(Model model, @PathVariable("id") int id)
    {
        Sensor sensor = new Sensor();
        sensor.setDashboardId(id);
        model.addAttribute("sensor", sensor);
        return "add_sensor_form";
    }

    @RequestMapping("/edit_light/{id}")
    public String editLight(Model model, @PathVariable("id") int id)
    {
        Light light = databaseConnector.getLightById(id);
        model.addAttribute("light", light);
        return "edit_light_form";
    }

    @RequestMapping("/edit_sensor/{id}")
    public String editSensor(Model model, @PathVariable("id") int id)
    {
        Sensor sensor = databaseConnector.getSensorById(id);
        model.addAttribute("sensor", sensor);
        return "edit_sensor_form";
    }

    @PostMapping("/add_light_process_form")
    public RedirectView addLightSubmit(@ModelAttribute Light light, ModelMap model) {
        log.info("light: {}", light);
        databaseConnector.saveLight(light);
        String redirectUrl = String.format("dashboard/%d",light.getDashboardId());
        return new RedirectView(redirectUrl);
    }

    @PostMapping("/edit_light_process_form")
    public RedirectView editLightSubmit(@ModelAttribute Light light, ModelMap model) {
        log.info("light: {}", light);
        databaseConnector.updateLight(light);
        String redirectUrl = String.format("dashboard/%d",light.getDashboardId());
        return new RedirectView(redirectUrl);
    }

    @PostMapping("/add_sensor_process_form")
    public RedirectView addSensorSubmit(@ModelAttribute Sensor sensor, ModelMap model) {
        databaseConnector.saveSensor(sensor);
        String redirectUrl = String.format("dashboard/%d",sensor.getDashboardId());
        return new RedirectView(redirectUrl);
    }

    @PostMapping("/edit_sensor_process_form")
    public RedirectView editSensorSubmit(@ModelAttribute Sensor sensor, ModelMap model) {
        databaseConnector.updateSensor(sensor);
        String redirectUrl = String.format("dashboard/%d",sensor.getDashboardId());
        return new RedirectView(redirectUrl);
    }

    @RequestMapping("/add_dashboard")
    public String addSensor(Model model)
    {
        Dashboard dashboard = new Dashboard();
        model.addAttribute("dashboard", dashboard);
        return "add_dashboard_form";
    }

    @PostMapping("/add_dashboard_process_form")
    public RedirectView addDashboardSubmit(@ModelAttribute Dashboard dashboard, ModelMap model) {
        databaseConnector.saveDashboard(dashboard);
        String redirectUrl = String.format("dashboard/%d",databaseConnector.getLastInsertedId());
        return new RedirectView(redirectUrl);
    }

    @RequestMapping("/edit_dashboard/{id}")
    public String editDashboard(Model model, @PathVariable("id") int id)
    {
        Dashboard dashboard = databaseConnector.getDashboardById(id);
        model.addAttribute("dashboard", dashboard);
        return "edit_dashboard_form";
    }

    @PostMapping("/edit_dashboard_process_form")
    public RedirectView editDashboardSubmit(@ModelAttribute Dashboard dashboard, ModelMap model) {
        databaseConnector.updateDashboard(dashboard);
        String redirectUrl = String.format("dashboard/%d",dashboard.getId());
        return new RedirectView(redirectUrl);
    }


    @RequestMapping(value = "/dashboard/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public  String deleteDashboard(@PathVariable("id") int id) throws MqttException {
        databaseConnector.deleteDashboard(id);
        return "deleted";
    };
}