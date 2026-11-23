# Test Scenario Inventory: automationexercise.com

Version 1.0 (Phase 3). This inventory is the single source of truth for what the framework should cover. Later phases link automated tests and AI agents back to the IDs below.

## 1. How to read this document

### Scenario types

| Code | Meaning |
|---|---|
| POS | Positive: valid input or the expected happy path |
| NEG | Negative: invalid input or an unauthorised or impossible action |
| BND | Boundary: minimum, maximum, empty, or just-outside-limit values |
| VAL | Validation: field rules, data consistency, displayed values |
| E2E | End-to-end: a flow that crosses several pages |
| REG | Regression: guards behaviour that must keep working after changes (state, persistence, navigation) |

### Priorities

| Priority | Definition | Suite |
|---|---|---|
| P0 | Core business flow or data loss risk. If it fails, the site is unusable. | Smoke and regression |
| P1 | Major feature or common failure path | Regression |
| P2 | Secondary feature, validation, or boundary | Regression |
| P3 | Cosmetic, rare, or low-impact | Extended (nightly) |

### Evidence level (important)

| Code | Meaning | Expected result known? |
|---|---|---|
| O | **Official**: taken from the site's own published test cases (https://automationexercise.com/test_cases). The TC number is shown. | Yes, messages and steps are as published |
| B | **Observed**: seen directly on a page that was opened for this analysis | Yes, for what was observed |
| V | **To verify**: a sensible scenario whose real behaviour has **not** been observed yet | **No.** Open the page, record the actual behaviour, then write the expected result. |

No scenario marked V may be automated until its behaviour has been confirmed in a browser. This keeps the suite based on evidence, not assumptions.

## 2. Evidence boundaries

**Inspected for this analysis:** the home page, the Test Cases page (all 26 official cases), the Login/Signup page, and the Contact Us page.

**Not inspected (no direct access):** the Products page, product detail pages, the Cart, Checkout, Payment, order confirmation, account pages, category and brand pages, and the API Testing page. For these modules, scenarios come from the official test cases (O) or are marked V. Page structure and element locators for every module will be captured with Selenium at the start of Phase 4.

**Address management:** the only address handling seen in the official test cases is the address entered at registration and shown at checkout (TC23). No address edit screen is known. Scenarios cover what is documented; anything beyond it is to be verified.

## 3. Summary

**Total scenarios: 170**

| Priority | Count | | Type | Count | | Evidence | Count |
|---|---|---|---|---|---|---|---|
| P0 | 29 | | POS | 67 | | O Official | 65 |
| P1 | 50 | | NEG | 19 | | B Observed | 9 |
| P2 | 53 | | BND | 21 | | V To verify | 96 |
| P3 | 38 | | VAL | 37 | |  |  |
|  |  | | E2E | 11 | |  |  |
|  |  | | REG | 15 | |  |  |

| Module | Total | P0 | P1 | P2 | P3 | O | B | V |
|---|---|---|---|---|---|---|---|---|
| HOM: Home page | 12 | 1 | 4 | 2 | 5 | 4 | 5 | 3 |
| REG: User registration | 20 | 3 | 7 | 7 | 3 | 7 | 0 | 13 |
| LOG: Login | 16 | 2 | 5 | 3 | 6 | 4 | 0 | 12 |
| OUT: Logout | 5 | 1 | 1 | 3 | 0 | 1 | 0 | 4 |
| DEL: Delete account | 2 | 1 | 1 | 0 | 0 | 1 | 0 | 1 |
| CON: Contact us | 10 | 0 | 3 | 3 | 4 | 3 | 1 | 6 |
| PRD: Products listing | 7 | 1 | 1 | 3 | 2 | 3 | 0 | 4 |
| SRC: Product search | 10 | 0 | 6 | 2 | 2 | 4 | 0 | 6 |
| PDT: Product details and reviews | 12 | 2 | 3 | 6 | 1 | 5 | 0 | 7 |
| CAT: Categories | 7 | 0 | 1 | 4 | 2 | 3 | 0 | 4 |
| BRD: Brands | 4 | 0 | 0 | 2 | 2 | 3 | 0 | 1 |
| ADD: Add to cart | 12 | 5 | 4 | 3 | 0 | 8 | 1 | 3 |
| RMV: Remove from cart | 4 | 1 | 2 | 1 | 0 | 1 | 0 | 3 |
| CRT: Shopping cart page | 6 | 3 | 2 | 0 | 1 | 3 | 0 | 3 |
| CHK: Checkout | 10 | 6 | 2 | 2 | 0 | 5 | 0 | 5 |
| ADR: Address details | 5 | 0 | 2 | 1 | 2 | 2 | 0 | 3 |
| PAY: Payment | 9 | 2 | 2 | 4 | 1 | 2 | 0 | 7 |
| ORD: Order confirmation and invoice | 6 | 1 | 2 | 2 | 1 | 3 | 0 | 3 |
| SUB: Subscription | 6 | 0 | 1 | 3 | 2 | 2 | 1 | 3 |
| TCP: Test Cases page | 3 | 0 | 0 | 1 | 2 | 1 | 1 | 1 |
| API: API Testing page | 1 | 0 | 0 | 0 | 1 | 0 | 0 | 1 |
| X: Cross-cutting | 3 | 0 | 1 | 1 | 1 | 0 | 0 | 3 |

## 4. Scenarios by module

### HOM: Home page

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| HOM-001 | Home page loads and the browser title is "Automation Exercise" | POS | P0 | B |
| HOM-002 | Top navigation shows Home, Products, Cart, Signup / Login, Test Cases, API Testing, Video Tutorials, Contact us | POS | P1 | B |
| HOM-003 | Each navigation link opens its page (/products, /view_cart, /login, /test_cases, /api_list, /contact_us) | POS | P1 | B |
| HOM-004 | Video Tutorials opens the external YouTube channel (check whether it opens in a new tab) | POS | P3 | V |
| HOM-005 | Features Items grid lists products, each with name, price, Add to cart and View Product | POS | P1 | B |
| HOM-006 | Left sidebar shows Category (Women, Men, Kids) and Brands | POS | P2 | B |
| HOM-007 | Scrolling to the bottom shows RECOMMENDED ITEMS (TC22) | POS | P2 | O |
| HOM-008 | Every recommended item shows a product name; one item was seen with a price but no name | VAL | P3 | V |
| HOM-009 | Scroll to the footer shows SUBSCRIPTION; the up-arrow returns to the top and the text "Full-Fledged practice website for Automation Engineers" is visible (TC25) | POS | P3 | O |
| HOM-010 | Scrolling back up without the arrow shows the same top-of-page text (TC26) | POS | P3 | O |
| HOM-011 | When logged in, the header shows "Logged in as username" (TC1, TC2) | POS | P1 | O |
| HOM-012 | Page layout at a mobile viewport width has no overlapping or hidden navigation | VAL | P3 | V |

### REG: User registration

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| REG-001 | Register a new user end to end: signup name and email, ENTER ACCOUNT INFORMATION, fill all details, Create Account, ACCOUNT CREATED!, Continue (TC1) | E2E | P0 | O |
| REG-002 | "New User Signup!" section is visible on the login page (TC1) | POS | P1 | O |
| REG-003 | Signup with an already registered email shows "Email Address already exist!" (TC5) | NEG | P0 | O |
| REG-004 | After valid name and email, the page shows ENTER ACCOUNT INFORMATION (TC1) | POS | P1 | O |
| REG-005 | The "Sign up for our newsletter!" and "Receive special offers from our partners!" checkboxes can be selected (TC1) | POS | P2 | O |
| REG-006 | Account details accept Title, Name, Email, Password, Date of birth; address details accept First name, Last name, Company, Address, Address2, Country, State, City, Zipcode, Mobile number (TC1) | POS | P1 | O |
| REG-007 | After account creation the header shows "Logged in as username" (TC1) | POS | P0 | O |
| REG-008 | Signup with an empty name | VAL | P1 | V |
| REG-009 | Signup with an empty email | VAL | P1 | V |
| REG-010 | Signup with an invalid email format (for example no @) | VAL | P1 | V |
| REG-011 | Create Account with required fields left empty (find out which fields are required) | VAL | P1 | V |
| REG-012 | Password length boundaries: shortest accepted, longest accepted, one beyond each | BND | P2 | V |
| REG-013 | Name and address fields with maximum length, special characters and Unicode text | BND | P2 | V |
| REG-014 | Date of birth boundaries: first and last year offered, 29 Feb, impossible dates such as 31 Feb | BND | P3 | V |
| REG-015 | Mobile number containing letters, or too short or too long | VAL | P2 | V |
| REG-016 | Zipcode format variants (letters, spaces, very long) | VAL | P3 | V |
| REG-017 | The email on the account information page matches the signup email | VAL | P2 | V |
| REG-018 | Markup or script characters in text fields are stored and shown as plain text, not executed | VAL | P2 | V |
| REG-019 | After the account is deleted, the same email can register again | REG | P2 | V |
| REG-020 | Browser Back after ACCOUNT CREATED! does not create a duplicate or break the session | REG | P3 | V |

### LOG: Login

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| LOG-001 | "Login to your account" is visible on the login page (TC2, TC3) | POS | P1 | O |
| LOG-002 | Login with correct email and password shows "Logged in as username" (TC2) | POS | P0 | O |
| LOG-003 | Login with incorrect email and password shows "Your email or password is incorrect!" (TC3) | NEG | P0 | O |
| LOG-004 | Registered email with the wrong password | NEG | P1 | V |
| LOG-005 | Well-formed email that is not registered | NEG | P1 | V |
| LOG-006 | Empty email and empty password | VAL | P1 | V |
| LOG-007 | Email filled, password empty | VAL | P2 | V |
| LOG-008 | Invalid email format | VAL | P2 | V |
| LOG-009 | Email in different letter case than at registration | BND | P3 | V |
| LOG-010 | Leading or trailing spaces around the email | BND | P3 | V |
| LOG-011 | Very long password or password with special characters | BND | P3 | V |
| LOG-012 | The password field masks what is typed | VAL | P2 | V |
| LOG-013 | Products added to the cart before login are still in the cart after login (TC20) | E2E | P1 | O |
| LOG-014 | A logged-in user opening the login page directly | REG | P3 | V |
| LOG-015 | Repeated failed logins: check whether any lockout or throttling exists | NEG | P3 | V |
| LOG-016 | Pressing Enter in the password field submits the login form | POS | P3 | V |

### OUT: Logout

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| OUT-001 | Logout takes the user to the login page (TC4) | POS | P0 | O |
| OUT-002 | After logout the header shows Signup / Login again and not "Logged in as" | POS | P1 | V |
| OUT-003 | After logout, browser Back does not restore the logged-in state | NEG | P2 | V |
| OUT-004 | Cart contents after logout and login again | REG | P2 | V |
| OUT-005 | After logout, opening the checkout page directly requires login | NEG | P2 | V |

### DEL: Delete account

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| DEL-001 | Delete Account shows ACCOUNT DELETED! and Continue returns to the site (TC1, TC2) | POS | P0 | O |
| DEL-002 | After deletion, login with the old credentials fails with "Your email or password is incorrect!" | NEG | P1 | V |

### CON: Contact us

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| CON-001 | Contact page shows "GET IN TOUCH" and the note that the form is for testing purposes (TC6) | POS | P1 | O |
| CON-002 | Submit name, email, subject, message and a file; confirm the OK dialog; "Success! Your details have been submitted successfully." appears (TC6) | E2E | P1 | O |
| CON-003 | The Home button after submission returns to the home page (TC6) | POS | P2 | O |
| CON-004 | Submit without attaching a file | POS | P2 | V |
| CON-005 | Submit with empty required fields | VAL | P1 | V |
| CON-006 | Invalid email format | VAL | P2 | V |
| CON-007 | Very long subject and message | BND | P3 | V |
| CON-008 | File upload boundaries: empty file, very large file, unusual file types | BND | P3 | V |
| CON-009 | Cancel instead of OK in the confirmation dialog | NEG | P3 | V |
| CON-010 | The Feedback For Us section shows a contact email address | POS | P3 | B |

### PRD: Products listing

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| PRD-001 | Products opens the ALL PRODUCTS page with a visible product list (TC8) | POS | P0 | O |
| PRD-002 | Each listed product shows image, price, name, Add to cart and View Product | POS | P1 | V |
| PRD-003 | Hovering a product reveals its Add to cart overlay (TC12) | POS | P2 | O |
| PRD-004 | Prices are shown in Rs. and are positive numbers | VAL | P2 | V |
| PRD-005 | The listing is complete: check product count and whether pagination exists | VAL | P3 | V |
| PRD-006 | All product images load (no broken images) | REG | P3 | V |
| PRD-007 | Category and Brands sidebars are present on the Products page (TC18, TC19) | POS | P2 | O |

### SRC: Product search

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| SRC-001 | Search a product name: SEARCHED PRODUCTS is visible (TC9) | POS | P1 | O |
| SRC-002 | All results shown are related to the search term (TC9) | VAL | P1 | O |
| SRC-003 | Partial search term returns matching products | POS | P1 | V |
| SRC-004 | Search is case-insensitive | BND | P2 | V |
| SRC-005 | Search for a product that does not exist (check what the page shows) | NEG | P1 | V |
| SRC-006 | Empty search | NEG | P2 | V |
| SRC-007 | Whitespace-only search | BND | P3 | V |
| SRC-008 | Special characters and very long search text | BND | P3 | V |
| SRC-009 | Add searched products to the cart and see them in Cart (TC20) | E2E | P1 | O |
| SRC-010 | Searched products are still in the cart after login (TC20) | E2E | P1 | O |

### PDT: Product details and reviews

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| PDT-001 | View Product on the first product opens its detail page (TC8) | POS | P0 | O |
| PDT-002 | Detail page shows product name, category, price, availability, condition and brand (TC8) | POS | P1 | O |
| PDT-003 | Name and price on the detail page match the listing | REG | P1 | V |
| PDT-004 | Default quantity value | VAL | P2 | V |
| PDT-005 | Set quantity to 4 and Add to cart; Cart shows that product with quantity 4 (TC13) | POS | P0 | O |
| PDT-006 | Quantity boundaries: 0, negative, non-numeric, very large | BND | P1 | V |
| PDT-007 | "Write Your Review" is visible (TC21) | POS | P2 | O |
| PDT-008 | Submit a review with name, email and text; "Thank you for your review." appears (TC21) | POS | P2 | O |
| PDT-009 | Review with empty fields | VAL | P2 | V |
| PDT-010 | Review with an invalid email format | VAL | P2 | V |
| PDT-011 | Detail page for a product id that does not exist | NEG | P3 | V |
| PDT-012 | Add to cart from the detail page shows the confirmation with View Cart and Continue Shopping | POS | P2 | V |

### CAT: Categories

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| CAT-001 | Categories are visible in the left sidebar (TC18) | POS | P2 | O |
| CAT-002 | Women, then a sub-category (for example Tops): the category page opens with heading "WOMEN - TOPS PRODUCTS" (TC18) | POS | P1 | O |
| CAT-003 | A Men sub-category opens its category page (TC18) | POS | P2 | O |
| CAT-004 | Kids sub-categories open their category pages | POS | P2 | V |
| CAT-005 | Products on a category page belong to that category | VAL | P2 | V |
| CAT-006 | Switching directly between categories shows the correct products each time | REG | P3 | V |
| CAT-007 | A category with no products (if one exists) | NEG | P3 | V |

### BRD: Brands

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| BRD-001 | Brands are visible in the left sidebar (TC19) | POS | P2 | O |
| BRD-002 | Clicking a brand shows that brand's products (TC19) | POS | P2 | O |
| BRD-003 | Switching from one brand to another shows the new brand's products (TC19) | REG | P3 | O |
| BRD-004 | Brand counts in the sidebar, if shown, match the products listed | VAL | P3 | V |

### ADD: Add to cart

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| ADD-001 | Hover the first product, Add to cart, then Continue Shopping (TC12) | POS | P0 | O |
| ADD-002 | Add a second product and View Cart: both products are in the cart (TC12) | POS | P0 | O |
| ADD-003 | Cart shows each product's price, quantity and total (TC12) | VAL | P0 | O |
| ADD-004 | Each line total equals price times quantity (TC12) | VAL | P0 | O |
| ADD-005 | Add from the detail page with quantity 4: Cart shows quantity 4 (TC13) | E2E | P0 | O |
| ADD-006 | Add the same product twice: check whether quantity increases or a second row appears | BND | P1 | V |
| ADD-007 | Add a product from RECOMMENDED ITEMS and see it in Cart (TC22) | POS | P2 | O |
| ADD-008 | After Add to cart a confirmation offers View Cart and Continue Shopping | POS | P1 | B |
| ADD-009 | Add to cart from search results (TC20) | POS | P1 | O |
| ADD-010 | Add to cart from a category page or brand page | POS | P2 | V |
| ADD-011 | Very large quantity (maximum accepted) | BND | P2 | V |
| ADD-012 | Cart contents added while logged out are kept after login (TC20) | E2E | P1 | O |

### RMV: Remove from cart

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| RMV-001 | The X button removes that product from the cart (TC17) | POS | P0 | O |
| RMV-002 | Remove one of two products: the other stays and the totals are correct | POS | P1 | V |
| RMV-003 | Remove the last product: check the empty-cart state | BND | P1 | V |
| RMV-004 | After removing a product, refreshing the page keeps it removed | REG | P2 | V |

### CRT: Shopping cart page

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| CRT-001 | Clicking Cart opens the cart page (TC14) | POS | P0 | O |
| CRT-002 | Empty cart page content | POS | P1 | V |
| CRT-003 | Cart contents persist while navigating between pages | REG | P1 | V |
| CRT-004 | Proceed To Checkout is available when the cart has products (TC14) | POS | P0 | O |
| CRT-005 | Proceed To Checkout while logged out offers Register / Login (TC14) | POS | P0 | O |
| CRT-006 | Product name in the cart links to its detail page | POS | P3 | V |

### CHK: Checkout

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| CHK-001 | Checkout shows Address Details and Review Your Order (TC14, TC15, TC16) | POS | P0 | O |
| CHK-002 | Enter a description in the comment box and click Place Order (TC14) | POS | P0 | O |
| CHK-003 | Guest flow: add products, Proceed To Checkout, Register / Login, create account, return to cart, checkout and pay (TC14) | E2E | P0 | O |
| CHK-004 | Register before checkout: create account, add products, checkout and pay (TC15) | E2E | P0 | O |
| CHK-005 | Login before checkout: login, add products, checkout and pay (TC16) | E2E | P0 | O |
| CHK-006 | Review Your Order lists the same products, quantities and total as the cart | VAL | P0 | V |
| CHK-007 | Comment box boundaries: empty, very long, special characters | BND | P2 | V |
| CHK-008 | Opening the checkout page directly with an empty cart | NEG | P2 | V |
| CHK-009 | Opening the checkout page directly while logged out | NEG | P1 | V |
| CHK-010 | Order total equals the sum of the item totals | VAL | P1 | V |

### ADR: Address details

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| ADR-001 | The delivery address at checkout equals the address entered at registration (TC23) | VAL | P1 | O |
| ADR-002 | The billing address at checkout equals the address entered at registration (TC23) | VAL | P1 | O |
| ADR-003 | All registered address fields are shown at checkout (name, company, address lines, city, state, zipcode, country, phone) | VAL | P2 | V |
| ADR-004 | Special characters and Unicode in the address are shown unchanged | BND | P3 | V |
| ADR-005 | An empty Address2 or Company does not leave odd blanks or text such as "null" | BND | P3 | V |

### PAY: Payment

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| PAY-001 | The payment page accepts Name on Card, Card Number, CVC and Expiration date (TC14) | POS | P0 | O |
| PAY-002 | Pay and Confirm Order shows "Your order has been placed successfully!" (TC14, TC15, TC16) | E2E | P0 | O |
| PAY-003 | Pay with all payment fields empty | VAL | P1 | V |
| PAY-004 | Card number with letters, too few digits or too many digits | VAL | P1 | V |
| PAY-005 | CVC length boundaries (2, 3, 4, 5 digits) | BND | P2 | V |
| PAY-006 | Expiration date in the past | NEG | P2 | V |
| PAY-007 | Card number length boundaries | BND | P2 | V |
| PAY-008 | Name on Card with special characters or very long text | BND | P3 | V |
| PAY-009 | Double-clicking Pay and Confirm Order does not create a duplicate order | NEG | P2 | V |

### ORD: Order confirmation and invoice

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| ORD-001 | After payment the confirmation page shows the success message and a Download Invoice button (TC24) | POS | P0 | O |
| ORD-002 | Download Invoice downloads a file (TC24) | POS | P1 | O |
| ORD-003 | Continue after the order returns to the site (TC24) | POS | P2 | O |
| ORD-004 | The cart is empty after a successful order | REG | P1 | V |
| ORD-005 | Invoice content matches the order (customer, items, total) | VAL | P2 | V |
| ORD-006 | Refreshing or going Back on the confirmation page does not place a second order | NEG | P3 | V |

### SUB: Subscription

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| SUB-001 | Home page footer: SUBSCRIPTION text, enter an email, click the arrow, "You have been successfully subscribed!" appears (TC10) | POS | P1 | O |
| SUB-002 | Cart page footer: same subscription flow (TC11) | POS | P2 | O |
| SUB-003 | Invalid email format | VAL | P2 | V |
| SUB-004 | Empty email | VAL | P2 | V |
| SUB-005 | Subscribing the same email twice | NEG | P3 | V |
| SUB-006 | The subscription section appears in the footer of the login, contact and test cases pages | REG | P3 | B |

### TCP: Test Cases page

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| TCP-001 | Test Cases in the navigation opens the Test Cases page (TC7) | POS | P2 | O |
| TCP-002 | The page lists 26 test cases | VAL | P3 | B |
| TCP-003 | Clicking a test case title expands or reveals its steps | POS | P3 | V |

### API: API Testing page

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| API-001 | API Testing in the navigation opens the API list page (/api_list) | POS | P3 | V |

### X: Cross-cutting

| ID | Scenario | Type | Pri | Ev |
|---|---|---|---|---|
| X-001 | Each main page opens by direct URL and shows its own content | REG | P2 | V |
| X-002 | Browser Back and Forward behave consistently across the main flows | REG | P3 | V |
| X-003 | The smoke flows give the same results in Chrome and Edge | REG | P1 | V |

## 5. Smoke suite candidates

Small, fast, and covering the core path. Everything else belongs in regression.

| Order | Scenario | Notes |
|---|---|---|
| 1 | HOM-001 | Already automated (`HomePageSmokeTest`) |
| 2 | LOG-002 | Needs a user created at runtime |
| 3 | LOG-003 | Fast negative check of authentication |
| 4 | PRD-001 | Products page loads |
| 5 | PDT-001 | Product detail opens |
| 6 | ADD-002 | Two products in the cart |
| 7 | RMV-001 | Remove from cart |
| 8 | CHK-004 | Full purchase: register, add, checkout, pay, confirm (also covers REG-001, REG-007, PAY-002, ORD-001) |
| 9 | OUT-001 | Logout |
| 10 | DEL-001 | Account cleanup, also used in every flow that creates a user |

## 6. Test data rules

1. Every test that needs a user creates one with a unique, generated email and deletes it at the end. No shared accounts.
2. No real personal data and no real card numbers. Payment tests use dummy values only, and passwords are never stored in the repo (they come from environment variables).
3. The site documents API endpoints for creating and deleting accounts (the API Testing page). If confirmed in Phase 4, the `api` package uses them for fast setup and cleanup so UI tests stay short.

## 7. Open questions to settle in Phase 4

| # | Question | Related IDs |
|---|---|---|
| 1 | Which registration fields are mandatory? | REG-011 |
| 2 | What does the site show for a search with no results? | SRC-005 |
| 3 | Does adding the same product twice increase the quantity or add a row? | ADD-006 |
| 4 | Does the site use pop-up advertisements that can block clicks? | All click-based flows |
| 5 | Is the recommended item with a price but no name a real defect? | HOM-008 |
| 6 | Are the API account endpoints available as documented? | Section 6, rule 3 |
