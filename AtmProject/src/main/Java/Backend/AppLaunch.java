package Backend;

import java.util.Scanner;

public class AppLaunch {

    public static void main(String[] args) throws InvalidInputException {

        UserCache userCache = new MongoDbCache();
        Account account = new Account();
        Scanner scanner = new Scanner(System.in);

        boolean mainMenuLoop = true;
        boolean bankDetailsLoop = true;
        boolean optionsLoop = true;
        int loginOption;
        int userOption;



        System.out.println("Welcome to National Banking ");

        while (mainMenuLoop) {
            System.out.println("would you like to: \n 1. Register a Current Account \n 2. Login \n 3. Exit ");
            System.out.print("Selected Option: ");
            try {
                loginOption = scanner.nextInt();
            } catch (Exception e) {
                throw new InvalidInputException("Invalid input. Please enter a number from the list below.");
            }

            switch (loginOption) {

                case 1:
                    System.out.print("Enter your first name: ");
                    String registerFirstName = scanner.next();
                    System.out.print("Enter your last name: ");
                    String registerLastName = scanner.next();
                    long generatedBankId = userCache.generateBankId();
                    long generatedBankPin = userCache.generateBankPin();

                    while (bankDetailsLoop) {
                        if (userCache.existingAccount(generatedBankId, generatedBankPin)) {
                            System.out.println("PRINT NEW BANK ID AND PIN");
                            generatedBankId = userCache.generateBankId();
                            generatedBankPin = userCache.generateBankPin();
                        } else {
                            System.out.println("This is your new Bank ID: " + generatedBankId + " and this is your new Bank Pin: " +
                                    generatedBankPin + ", please do not share these with anyone.");
                            userCache.addAccount(new Account(generatedBankId, registerFirstName.toLowerCase(), registerLastName.toLowerCase(), generatedBankPin));
                            System.out.println("RETURNING BACK TO MAIN MENU");
                            bankDetailsLoop = false;
                        }
                    }
                    continue;

                case 2:
                    System.out.print("Please enter your Bank ID: ");
                    long bankId = scanner.nextLong();
                    account.setId(bankId);
                    System.out.print("Please enter your Bank Pin: ");
                    long bankPin = scanner.nextLong();
                    account.setBankPin(bankPin);
                    System.out.print("Enter your first name: ");
                    String firstName = scanner.next();
                    account.setFirstName(firstName.toLowerCase());
                    System.out.print("Enter your last name: ");
                    String lastName = scanner.next();
                    account.setLastName(lastName.toLowerCase());

                    if (!userCache.accountExists(bankId, bankPin, firstName.toLowerCase(), lastName.toLowerCase())) {
                        System.out.println("Incorrect Information! Returning back to main menu.");
                        continue;
                    } else {
                        System.out.println(account);
                        System.out.println("Enter Y to continue");
                        System.out.print("Entered: ");
                        String continueOption = scanner.next();
                        if (continueOption.equals("Y") || continueOption.equals("y")) {
                            break;
                        }
                    }

                case 3:
                    System.out.println("Terminating Application");
                    break;

                default:
                    System.out.println("Invalid Option! Returning back to main menu.");
                    continue;
            }
            mainMenuLoop = false;


            if (loginOption == 3) {
                break;
            }
            else {

                while (optionsLoop) {
                    System.out.println("Would you like to: \n 1. Deposit \n 2. Withdraw \n 3. View Balance \n 4. Exit ");
                    System.out.print("Selected Option: ");
                    try {
                        userOption = scanner.nextInt();
                    } catch (Exception e) {
                        throw new InvalidInputException("Invalid input. Please enter a number from the list below.");
                    }

                    double userBalance = userCache.getAccount(account.getId(), account.getBankPin()).getBalance();

                    switch (userOption) {

                        case 1:
                            System.out.print("Deposit Amount: ");
                            double depositAmount = scanner.nextDouble();
                            account.setBalance(userBalance + depositAmount);
                            System.out.println("Current balance: " + account.getBalance());
                            userCache.updateBalance(account.getId(), account.getBalance());
                            System.out.print("Enter a key to continue: ");
                            scanner.next();
                            continue;


                        case 2:
                            System.out.println("Current balance: " + userBalance);
                            System.out.print("Withdraw Amount: ");
                            double withdrawAmount = scanner.nextDouble();
                            if (withdrawAmount > userBalance)
                                System.out.println("Insufficient Funds!");
                            else {
                                account.setBalance(userBalance - withdrawAmount);
                                System.out.println("Balance: " + account.getBalance());
                                userCache.updateBalance(account.getId(), account.getBalance());
                                System.out.print("Enter a key to continue: ");
                                scanner.next();
                                continue;
                            }

                            //675474149

                        case 3:
                            System.out.println("Balance: " + userBalance);
                            System.out.print("Enter a key to continue: ");
                            scanner.next();
                            continue;


                        case 4:
                            System.out.println("Terminating Application.");
                            break;


                        default:
                            if (userOption < 1 || userOption > 4) {
                                System.out.println("Invalid Option! Returning to the main menu.");
                                continue;
                            }
                    }
                    optionsLoop = false;
                }
            }
        }
    }
}
