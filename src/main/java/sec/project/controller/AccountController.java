package sec.project.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.domain.Signup;
import sec.project.repository.AccountRepository;
import sec.project.repository.SignupRepository;

@Controller
public class AccountController {

    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String getAccountForm() {
        return "registration";
    }
    
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String createAccount(@RequestParam String username, @RequestParam String password) {
        if (accountRepository.findByUsername(username) != null) {
            return "redirect:/login";
        }
        String pw = passwordEncoder.encode(password);
        Account a = new Account();
        a.setUsername(username);
        a.setPassword(pw);
        
        accountRepository.save(a);
        
        return "redirect:/form";
    }

    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    public String viewAccount(@PathVariable Long id, Model model) {
        Account account = accountRepository.findOne(id);
        Signup info = signupRepository.findByAccount(account);
        model.addAttribute("info", info);
        model.addAttribute("account", account);
        return "account";
    }

    @RequestMapping(value = "/account/{id}", method = RequestMethod.DELETE)
    public String cancelSignup(@PathVariable Long id, Model model) {
        Account account = accountRepository.findOne(id);
        Signup signup = signupRepository.findByAccount(account);
        signupRepository.delete(signup);
        return "redirect:/form";
    }

}
