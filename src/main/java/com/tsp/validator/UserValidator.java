package com.tsp.validator;

import com.tsp.controllers.UserController;
import com.tsp.model.Client;
import com.tsp.model.User;
import com.tsp.service.ClientService;
import com.tsp.service.DriverService;
import com.tsp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for {@link com.tsp.model.User} class,
 * implements {@link Validator} interface.
 *
 * @author Linar Abzaltdinov
 * @version 1.0
 */

@Component
public class UserValidator implements Validator {
    Logger log = LoggerFactory.getLogger(UserValidator.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private DriverService driverService;
    
    @Autowired
    private ClientService clientService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Required");
        if (user.getUsername().length() < 4 || user.getUsername().length() > 32) {
            log.debug("!!! size of username error");
            errors.rejectValue("username", "Size.userForm.username");
        }

        if (driverService.findByUsername(user.getUsername()) != null || 
                userService.findByUsername(user.getUsername()) != null ||
                clientService.findByUsername(user.getUsername()) != null) {
            log.debug("!!! duplicate username error");
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Required");
        if (user.getPassword().length() < 4 || user.getPassword().length() > 32) {
            log.debug("!!! size of password error");
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!user.getConfirmPassword().equals(user.getPassword())) {
            log.debug("!!! confirm password error");
            errors.rejectValue("confirmPassword", "Different.userForm.password");
        }
        
        if (o instanceof Client) {
            Client c = (Client) o;
            String address = c.getPlace().getAddress();
            if (address.equals("") || !address.contains("Россия") )
                errors.rejectValue("place", "Unrecognized.userForm.place");
        }
    }
}
