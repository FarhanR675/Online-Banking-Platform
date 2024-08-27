document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('registerForm');
    const loginForm = document.getElementById('loginForm');
    const depositForm = document.getElementById('depositForm');
    const withdrawForm = document.getElementById('withdrawForm');
    const balanceDisplay = document.getElementById('balanceDisplay');

    let currentAccount = JSON.parse(sessionStorage.getItem('currentAccount'));

    if (registerForm) {
        registerForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const firstName = document.getElementById('registerFirstName').value.trim().toLowerCase();
            const lastName = document.getElementById('registerLastName').value.trim().toLowerCase();
            try {
                const response = await fetch ('/accounts/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify ({ firstName, lastName })
                });
                const data = await response.json();
                if (response.ok) {
                    alert(`Registered successfully! This is your Bank ID: ${data.id} and Bank Pin: ${data.bankPin}. Use both information to login`);
                    window.location.href = "login.html";
                } else {
                    alert('Registration Failed!');
                }
            } catch (error) {
                console.error('Error', error);
            }
        });
    }

    if (loginForm) {
        loginForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const bankId = document.getElementById('loginBankId').value.trim();
            const bankPin = document.getElementById('loginBankPin').value.trim();
            const firstName = document.getElementById('loginFirstName').value.trim().toLowerCase() || '';
            const lastName = document.getElementById('loginLastName').value.trim().toLowerCase();
            try {
                const response = await fetch ('/accounts/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify ({id: bankId, bankPin, firstName, lastName})
                });
                const data = await response.json();
                if (response.ok && data) {
                    currentAccount = data;
                    sessionStorage.setItem('currentAccount', JSON.stringify(currentAccount));
                    window.location.href = "actions.html";
                } else {
                    alert('Login Failed! Please check your credentials.');
                }
            } catch (error) {
                console.error("Error", error);
            }
        });
    }

    async function fetchBalance() {
        try {
            const response = await fetch (`/accounts/balance?bankId=${currentAccount.id}&bankPin=${currentAccount.bankPin}`);
            if (response.ok) {
                const data = await response.json();
                balanceDisplay.textContent = `£${data.balance.toFixed(2)}`;
            } else {
                balanceDisplay.textContent = "Failed to retrieve balance";
            }
        } catch (error) {
            console.error("Error fetching balance:", error);
            balanceDisplay.textContent = "An unexpected error occurred";
        }
    }

    fetchBalance();

    if (depositForm) {
        depositForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const depositAmount = parseFloat(document.getElementById('depositAmount').value);

            if (!currentAccount) {
                alert ("No account found. Please log in again")
                return
            } else if (isNaN(depositAmount || depositAmount <= 0)) {
                alert ("Invalid deposit amount");
                return;
            }

            try {
                const response = await fetch ('/accounts/deposit', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        bankId: currentAccount.id,
                        bankPin: currentAccount.bankPin,
                        deposit: depositAmount
                    })
                });

                if (response.ok) {
                    currentAccount = await response.json();
                    alert("Deposit Successful");
                    sessionStorage.setItem('currentAccount', JSON.stringify(currentAccount));
                    fetchBalance();

                } else {
                    const errorText = await response.text();
                    alert(`Deposit Failed: ${errorText}`);
                }
            } catch (error) {
                console.error("Error", error);
                alert("An unexpected error occurred. Please try again.");
            }
        });
    }

    if (withdrawForm) {
        withdrawForm.addEventListener("submit", async (event) => {
            event.preventDefault()
            const withdrawAmount = parseFloat(document.getElementById('withdrawAmount').value);

            if (!currentAccount) {
                alert ("No account found. Please log in again")
                return;
            } else if (isNaN(withdrawAmount || withdrawAmount <= 0)) {
                alert ("Invalid deposit amount");
                return;
            }

            try {
                const response = await fetch ("/accounts/withdraw", {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify ({
                        bankId: currentAccount.id,
                        bankPin: currentAccount.bankPin,
                        withdraw: parseFloat(withdrawAmount)
                    })
                });

                if (response.ok) {
                    currentAccount = await response.json();
                    alert("Withdrawal Successful");
                    sessionStorage.setItem('currentAccount', JSON.stringify(currentAccount));
                    fetchBalance();

                } else {
                    const errorText = await response.text();
                    alert(`Withdrawal Failed: ${errorText}`);
                }
            } catch (error) {
                console.error("Error", error);
                alert("An unexpected error occurred. Please try again.");
            }
        });
    }
});
