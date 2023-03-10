package com.jhlubucek.smart.controls;

import com.jhlubucek.smart.controls.entity.Light;
import com.jhlubucek.smart.controls.entity.Sensor;
import com.jhlubucek.smart.controls.sevices.MqttService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DemoController
{
    @Autowired
    private MqttService mqttService;

    @RequestMapping(value = "/light/{id}/state/{state}", method = RequestMethod.POST)
    @ResponseBody
    public  String updateLight(@PathVariable("id") int id, @PathVariable("state") int state) throws MqttException {
        mqttService.publish("/test/topic", Integer.toString(state), 0, false);
        return "ok";
    };

    @RequestMapping("/dashboard")
    public ModelAndView index()
    {
        ModelAndView modelAndView = new ModelAndView();
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(1, "nazev" ,"test", "testik", 10,100, true, 20));
        lights.add(new Light(2, "nazev" ,"test", "testik", 10,100, false, 80));

        List<Sensor> sensors = new ArrayList<>();
        sensors.add(new Sensor(1, "teplota-obyvak" ,"topic", "°C"));
        sensors.add(new Sensor(2, "teplota-kuchyn" ,"topic", "°C"));

        modelAndView.addObject("lights", lights);
        modelAndView.addObject("sensors", sensors);
        modelAndView.addObject("msg", "moje message");
        modelAndView.setViewName("mainPage");
        return modelAndView;
    }

    @RequestMapping("/add-light")
    public ModelAndView addLight()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit_light_form");
        modelAndView.addObject("msg", "moje message");
        return modelAndView;
//        return "edit_light_form";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public ModelAndView save(@ModelAttribute User user)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-data");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}