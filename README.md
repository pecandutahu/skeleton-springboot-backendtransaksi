1. Create salesdb in your local server postgresql
2. Run spring boot
3. import collection postman in this repo to your workspace
4. hit the /api/users/admin endpoint for the init registration as admin (in the first condition, if no users with "admin" role, this endpoint will create user with admin role with isActive true, if admin user is exist, the created user need to be activated)
5. hit the login endpoint
6. hit the login endpoint and use the respon token to access the all endpoint
7. hit /api/users to create user with kasir role
8. hit /api/users/activate/{id} to activate the user (using admin role)
9. hit /api/products to create and crud operation of product
10. hit /api/sales to operate crud of sales transaction (status default will be paid)
11. hit /api/sales/refund/{uuid} to refund paid transaction
12. hit /api/sales/report to access report with spesial criteria (start date and endDate for transaction date, status to filter refund or paid status)
