package Frontend;

import Backend.Account;
import Backend.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private UserCache userCache;

    @PostMapping("/register")
    public Account register(@RequestBody Account account) {
        long generatedBankId = userCache.generateBankId();
        long generatedBankPin = userCache.generateBankPin();
        while (userCache.existingAccount(generatedBankId, generatedBankPin)) {
            generatedBankId = userCache.generateBankId();
            generatedBankPin = userCache.generateBankPin();
        }
        account.setId(generatedBankId);
        account.setBankPin(generatedBankPin);
        userCache.addAccount(account);
        return account;
    }

    @PostMapping("/login")
    public Account login(@RequestBody Account account) {
        if (userCache.accountExists(account.getId(),
                account.getBankPin(),
                account.getFirstName().toLowerCase(),
                account.getLastName().toLowerCase())) {
            return userCache.getAccount(account.getId(), account.getBankPin());
        }
        return null;
    }


    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody Map<String, Object> request) {
        if (request.get("bankId") == null || request.get("bankPin") == null || request.get("deposit") == null) {
            return ResponseEntity.badRequest().body("Invalid request data. Bank ID, Bank Pin, and Deposit amount are required.");
        }

        long bankId = ((Number) request.get("bankId")).longValue();
        long bankPin = ((Number) request.get("bankPin")).longValue();
        double deposit = ((Number) request.get("deposit")).doubleValue();

        Account account = userCache.getAccount(bankId, bankPin);
        if (account != null) {
            account.setBalance(account.getBalance() + deposit);
            userCache.updateBalance(bankId, account.getBalance());
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found or invalid credentials.");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody Map<String, Object> request) {
        if (request.get("bankId") == null || request.get("bankPin") == null || request.get("withdraw") == null) {
            return ResponseEntity.badRequest().body("Invalid request data. Bank ID, Bank Pin, and Withdraw amount are required.");
        }

        long bankId = ((Number) request.get("bankId")).longValue();
        long bankPin = ((Number) request.get("bankPin")).longValue();
        double withdraw = ((Number) request.get("withdraw")).doubleValue();

        Account account = userCache.getAccount(bankId, bankPin);
        if (account != null) {
            if (withdraw <= account.getBalance()) {
                account.setBalance(account.getBalance() - withdraw);
                userCache.updateBalance(bankId, account.getBalance());
                return ResponseEntity.ok(account);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found or invalid credentials.");
    }


    @GetMapping("/balance")
    public ResponseEntity<?> balance(@RequestParam("bankId") long bankId,
                                     @RequestParam("bankPin") long bankPin) {
         double balance = userCache.getAccount(bankId, bankPin).getBalance();
         if (balance == -1) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account Not Found");
        }
        return ResponseEntity.ok(Map.of("balance", balance));
    }
}

