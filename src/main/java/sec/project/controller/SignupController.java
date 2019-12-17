package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;
import sec.project.repository.AccountRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model, Authentication auth) {
        Account acc = accountRepository.findByUsername(auth.getName());
        Signup signup = signupRepository.findByAccount(acc);
        
        if (signup != null) {
            return "redirect:/done";
        }

        model.addAttribute("account", acc);
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address,
            @RequestParam String creditCard, Authentication auth) {
        Account acc = accountRepository.findByUsername(auth.getName());
        signupRepository.save(new Signup(name, address, creditCard, acc));
        return "redirect:/done";
    }

    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public String getSignups(Model model, Authentication auth) {
        Account acc = accountRepository.findByUsername(auth.getName());
        model.addAttribute("account", acc);
        model.addAttribute("list", signupRepository.findAll());

        return "done";
    }

}
