package com.jhlubucek.smart.controls;

import com.jhlubucek.smart.controls.entity.Light;
import com.jhlubucek.smart.controls.entity.Sensor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DemoController
{
    @RequestMapping("/")
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