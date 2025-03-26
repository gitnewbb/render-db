package org.andh.stocknotrecommend.controller;

import org.andh.stocknotrecommend.model.dto.Account;
import org.andh.stocknotrecommend.model.repository.AccountRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {
    final AccountRepository accountRepository;
    public MainController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @GetMapping
    public String root(Model model) throws Exception {
        List<Account> accounts =accountRepository.findAll();
        model.addAttribute("accounts", accounts);
        return "main";
    }
    @PostMapping
    public String post(Model model, Account account) throws Exception {
        accountRepository.save(account);
        return "redirect:/";
    }
    @GetMapping("/unsubscribe")
    public String unsubscribe(@RequestParam("id") long id) throws Exception {
        accountRepository.delete(id);
        return "redirect:/";
    }
}