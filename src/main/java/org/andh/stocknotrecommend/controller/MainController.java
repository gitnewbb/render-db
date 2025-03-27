package org.andh.stocknotrecommend.controller;

import jakarta.servlet.http.HttpServletRequest;
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
    // Controller
    @GetMapping
    public String root(@RequestParam(name="page",defaultValue = "1") int page, Model model) throws Exception {
        int pageSize = 10;

        List<Account> accounts = accountRepository.findByPage(page, pageSize);
        int totalPages = accountRepository.countTotalPages(pageSize);
        List<Account> top3 = accountRepository.findTop3ByRecommend();

        model.addAttribute("accounts", accounts);
        model.addAttribute("top3", top3);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "main";
    }

    @PostMapping("/write")
    public String write(@RequestParam("nickname") String nickname,
                        @RequestParam("password") String password,
                        @RequestParam("title") String title,
                        @RequestParam("body_data") String body_data) throws Exception {
        accountRepository.save(new Account(null, nickname, password, title, body_data,null,0,0));
        return "redirect:/";
    }

    @PostMapping("/unsubscribe")
    public String delete(@RequestParam("account_id") Long accountId,
                         @RequestParam("password") String password) throws Exception {
        accountRepository.unsubscribe(accountId, password);
        return "redirect:/";
    }
    @PostMapping("/recommend")
    public String recommend(@RequestParam("account_id") Long accountId, HttpServletRequest request) throws Exception {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        boolean success = accountRepository.vote(accountId, ip, true); // true = 추천
        // redirect with 결과 표시 가능
        return "redirect:/";
    }
    @PostMapping("/notrecommend")
    public String notRecommend(@RequestParam("account_id") Long accountId, HttpServletRequest request) throws Exception {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        boolean success = accountRepository.vote(accountId, ip, false); // false = 비추천
        return "redirect:/";
    }



}