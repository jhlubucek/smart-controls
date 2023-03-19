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

    @RequestMapping("/dashboard/{id}" )
    public ModelAndView index(@PathVariable("id") int id)
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
        modelAndView.addObject("dashboardName", dashboard.getName());
        modelAndView.addObject("sensors", sensors);
        modelAndView.addObject("msg", "moje message");
        modelAndView.setViewName("mainPage");
        return modelAndView;
    }

    @RequestMapping("/add_light")
    public String addLight(Model model)
    {
        model.addAttribute("light", new Light());
        return "add_light_form";
    }

    @RequestMapping("/edit_light/{id}")
    public String editLight(Model model, @PathVariable("id") int id)
    {
        Light light = databaseConnector.getLightById(id);
        model.addAttribute("light", light);
        return "edit_light_form";
    }

    @PostMapping("/add_light_process_form")
    public RedirectView addLightSubmit(@ModelAttribute Light light, ModelMap model) {
        log.info("light: {}", light);
        databaseConnector.saveLight(light);
        return new RedirectView("dashboard");
    }

    @PostMapping("/edit_light_process_form")
    public RedirectView editLightSubmit(@ModelAttribute Light light, ModelMap model) {
        log.info("light: {}", light);
        databaseConnector.updateLight(light);
        return new RedirectView("dashboard");
    }

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        Greeting greeting = new Greeting();
        greeting.setContent("test");
        model.addAttribute("greeting", greeting);
        return "greeting";
    }

    @PostMapping("/greeting")
    public ModelAndView greetingSubmit(@ModelAttribute Greeting greeting, ModelMap model) {
        model.addAttribute("greeting", greeting);
//        return "result";
//        return new RedirectView("/dashboard");
        return new ModelAndView("forward:/dashboard", model);
    }
}