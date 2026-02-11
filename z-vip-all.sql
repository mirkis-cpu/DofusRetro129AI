-- Set VIP=1 (subscription) for all accounts
UPDATE accounts SET vip = 1 WHERE vip = 0;
