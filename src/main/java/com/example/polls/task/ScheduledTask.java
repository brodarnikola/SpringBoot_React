package com.example.polls.task;

import com.example.polls.bank.interfaces.AccountService;
import com.example.polls.bank.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    // second
    // minute
    // hour
    // day of month
    // month
    // day of week
//    @Scheduled(cron = "0 0 */2 * * *")
    @Scheduled(cron = "*/10 * * * * *") // execute every 5 seconds cron job
    public void task1() {
        accountService.getAllOriginalAccounts().forEach(account -> accountService.getLastMonthTurnOver(account, transactionService));
    }
}
