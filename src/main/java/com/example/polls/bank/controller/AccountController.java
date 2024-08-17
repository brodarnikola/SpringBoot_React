package com.example.polls.bank.controller;

import com.example.polls.bank.interfaces.AccountService;
import com.example.polls.bank.interfaces.TransactionService;
import com.example.polls.bank.model.Account;
import com.example.polls.bank.model.dto.AccountResponseDTO;
import com.example.polls.bank.model.dto.CustomerResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<AccountResponseDTO>> getAccounts() {
        List<AccountResponseDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/calculatePreviousMonthTurnover")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<AccountResponseDTO>> getPreviousMonthTurnover() {
        List<Account> accounts = accountService.getAllOriginalAccounts();
        accounts.forEach(account -> accountService.getLastMonthTurnOver(account, transactionService));
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());

        List<AccountResponseDTO> accountsResponse = accounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());;
        return ResponseEntity.ok(accountsResponse);
    }

    private AccountResponseDTO convertToDTO(Account account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountId(account.getAccountId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        dto.setPastMonthTurnover(account.getPreviousMonthTurnover());

        CustomerResponseDTO customerDTO = new CustomerResponseDTO();
        customerDTO.setCustomerId(account.getCustomer().getCustomerId());
        customerDTO.setName(account.getCustomer().getName());
        customerDTO.setAddress(account.getCustomer().getAddress());
        customerDTO.setEmail(account.getCustomer().getEmail());

        dto.setCustomer(customerDTO);
        return dto;
    }
}
