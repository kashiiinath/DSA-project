# Coupon & Discount Manager

**DSA with Java Mini Project**  
**Student:** Kashi Nath Kumar  
**Core concepts:** Binary Search Tree (BST), Hashing, Greedy Algorithm, Sorting

## 1. Overview
Coupon & Discount Manager is a menu-driven Java console application that manages discount coupons and helps a customer select the coupon that gives the highest eligible saving on a cart.

## 2. Features
- Add percentage-based and flat-amount coupons.
- Search coupons by code using a `HashMap`.
- Store and display coupons in alphabetical code order using a Binary Search Tree.
- Select the eligible coupon with the maximum immediate discount using a greedy strategy.
- Sort coupons by estimated discount.
- Calculate the payable amount after applying the best coupon.
- Deactivate coupons and validate expiry/minimum-purchase conditions.

## 3. DSA Concepts
| Concept | Use | Expected complexity |
|---|---|---|
| Hashing (`HashMap`) | Coupon lookup by code | Average O(1) |
| BST | Ordered coupon storage and inorder display | Average O(log n) insert; O(n) worst case |
| Greedy | Choose maximum eligible discount for current cart | O(n) |
| Sorting | Rank coupons by estimated discount | O(n log n) |

The BST is not self-balancing; its worst-case height can become `n`. The greedy choice maximizes the discount for one coupon on the current cart; it does not solve multi-coupon combinations.

## 4. Requirements
- JDK 17 or later (switch-arrow syntax is used).
- Terminal / Command Prompt.

## 5. Run
From the project root:

```bash
javac -d out src/CouponDiscountManager.java
java -cp out CouponDiscountManager
```

## 6. Menu
1. Add coupon
2. Display all coupons (BST order)
3. Search coupon by code (Hashing)
4. Find best coupon (Greedy)
5. Sort coupons by discount
6. Calculate bill and apply best coupon
7. Deactivate coupon
0. Exit

## 7. Sample walkthrough
The application starts with sample coupons: `SAVE10`, `FLAT100`, `MEGA20`, and `WELCOME50`. Enter a cart amount (for example, `1200`) and select **Find best coupon**. The program checks active, unexpired coupons whose minimum purchase is met, calculates their discounts, and prints the coupon with the highest saving.

> Expiry dates in the sample data are generated relative to the day the program is run.

## 8. Limitations / Future scope
- Data is stored in memory and resets when the program restarts.
- No graphical user interface or user authentication.
- Future versions can add file/database persistence, category-specific coupons, coupon stacking rules, and a web/mobile interface.

## 9. Author
**Kashi Nath Kumar**  
B.Tech CSE — DSA with Java
