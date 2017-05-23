package com.tsp.controllers;

import com.tsp.dao.ClientDao;
import com.tsp.dao.StockDao;
import com.tsp.model.Client;
import com.tsp.model.Driver;
import com.tsp.model.User;
import com.tsp.service.ClientService;
import com.tsp.service.ClientServiceImpl;
import com.tsp.service.DriverService;
import com.tsp.service.DriverServiceImpl;
import com.tsp.service.SecurityService;
import com.tsp.service.UserService;
import com.tsp.validator.UserValidator;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private StockDao stockDao;
    
//    @Autowired
//    private UserService userService;
    
    @Autowired
    private ClientService clientService;
    
    @Autowired
    private DriverService driverService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

//    @RequestMapping(value = "/registration", method = RequestMethod.GET)
//    public String adminRegistration(Model model) {
//        model.addAttribute("userForm", new User());
//
//        return "registration";
//    }
    
    @RequestMapping(value = "/client/registration", method = RequestMethod.GET)
    public String clientRegistration(Model model) {
        model.addAttribute("userForm", new Client());
        model.addAttribute("userClass", Client.class.getCanonicalName());
        return "registration";
    }
    
    @RolesAllowed(value = "ROLE_ADMIN")
    @RequestMapping(value = "/driver/registration", method = RequestMethod.GET)
    public String driverRegistration(Model model) {
        model.addAttribute("userForm", new Driver());
        model.addAttribute("userClass", Driver.class.getCanonicalName());
        model.addAttribute("stocks", stockDao.getAllStocks());
        return "registration";
    }
    
    @RolesAllowed(value = "ROLE_ADMIN")
    @RequestMapping(value = "/driver/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") Driver userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            log.error("!!!ERRORS!!!");
            for (Map.Entry<String,Object> a : bindingResult.getModel().entrySet()) {
                log.error("!!!ERROR!!!"+a.getKey() + "/");
                if (a.getValue() instanceof org.springframework.validation.BeanPropertyBindingResult)
                {
                    BeanPropertyBindingResult br = (BeanPropertyBindingResult) a.getValue();
                    for (ObjectError e : br.getAllErrors())
                        log.error("!!!ERROR!!! "+e.toString());
                }
            }
            model.addAttribute("userClass", Driver.class.getCanonicalName());
            model.addAttribute("stocks", stockDao.getAllStocks());
            return "registration";
        }
        driverService.save(userForm);
        return "redirect:/admin/drivers";
    }
    
    @RequestMapping(value = "client/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") Client userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            log.error("!!!ERRORS!!!");
            for (Map.Entry<String,Object> a : bindingResult.getModel().entrySet()) {
                log.error("!!!ERROR!!!"+a.getKey() + "/");
                if (a.getValue() instanceof org.springframework.validation.BeanPropertyBindingResult)
                {
                    BeanPropertyBindingResult br = (BeanPropertyBindingResult) a.getValue();
                    for (ObjectError e : br.getAllErrors())
                        log.error("!!!ERROR!!! "+e.toString());
                }
            }
            model.addAttribute("userClass", Client.class.getCanonicalName());
            return "registration";
        }
        clientService.save(userForm);
        
        securityService.autoLogin(userForm.getUsername(), userForm.getConfirmPassword());

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Имя пользователя или пароль неверны.");
        }
        
        if (logout != null) {
            model.addAttribute("message", "Успешно вышли из аккаунта.");
        }

        return "login";
    }
}
