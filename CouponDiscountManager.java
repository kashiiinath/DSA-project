import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Coupon & Discount Manager
 * DSA with Java mini-project demonstrating BST, Hashing, Greedy and Sorting.
 * Run: javac -d out src/CouponDiscountManager.java
 *      java -cp out CouponDiscountManager
 */
public class CouponDiscountManager {
    private final Scanner sc = new Scanner(System.in);
    private final CouponStore store = new CouponStore();

    public static void main(String[] args) {
        new CouponDiscountManager().run();
    }

    private void run() {
        seedCoupons();
        System.out.println("======================================");
        System.out.println("       COUPON & DISCOUNT MANAGER");
        System.out.println("======================================");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addCoupon();
                case 2 -> store.displayInOrder();
                case 3 -> searchCoupon();
                case 4 -> findBestCoupon();
                case 5 -> sortCoupons();
                case 6 -> calculateBill();
                case 7 -> deactivateCoupon();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
            System.out.println();
        }
        System.out.println("Thank you for using Coupon & Discount Manager!");
    }

    private void printMenu() {
        System.out.println("1. Add coupon");
        System.out.println("2. Display all coupons (BST order)");
        System.out.println("3. Search coupon by code (Hashing)");
        System.out.println("4. Find best coupon (Greedy)");
        System.out.println("5. Sort coupons by discount");
        System.out.println("6. Calculate bill and apply best coupon");
        System.out.println("7. Deactivate coupon");
        System.out.println("0. Exit");
    }

    private void seedCoupons() {
        store.add(new Coupon("SAVE10", DiscountType.PERCENT, 10, 500, 100,
                LocalDate.now().plusDays(30), true));
        store.add(new Coupon("FLAT100", DiscountType.FLAT, 100, 700, 0,
                LocalDate.now().plusDays(20), true));
        store.add(new Coupon("MEGA20", DiscountType.PERCENT, 20, 1000, 250,
                LocalDate.now().plusDays(45), true));
        store.add(new Coupon("WELCOME50", DiscountType.FLAT, 50, 300, 0,
                LocalDate.now().plusDays(15), true));
    }

    private void addCoupon() {
        System.out.println("--- Add Coupon ---");
        String code = readNonEmpty("Coupon code: ").toUpperCase(Locale.ROOT);
        if (store.search(code) != null) {
            System.out.println("Coupon code already exists.");
            return;
        }
        System.out.println("1. Percentage  2. Flat amount");
        int typeChoice = readInt("Discount type: ");
        if (typeChoice != 1 && typeChoice != 2) {
            System.out.println("Invalid discount type.");
            return;
        }
        DiscountType type = typeChoice == 1 ? DiscountType.PERCENT : DiscountType.FLAT;
        double value = readDouble("Discount value: ");
        double minPurchase = readDouble("Minimum purchase: ");
        double maxDiscount = type == DiscountType.PERCENT
                ? readDouble("Maximum discount cap: ") : 0;
        LocalDate expiry = readDate("Expiry date (YYYY-MM-DD): ");
        if (value <= 0 || minPurchase < 0 || maxDiscount < 0 ||
                (type == DiscountType.PERCENT && value > 100)) {
            System.out.println("Invalid coupon values.");
            return;
        }
        store.add(new Coupon(code, type, value, minPurchase, maxDiscount, expiry, true));
        System.out.println("Coupon added successfully.");
    }

    private void searchCoupon() {
        String code = readNonEmpty("Enter coupon code: ").toUpperCase(Locale.ROOT);
        Coupon c = store.search(code);
        if (c == null) System.out.println("Coupon not found.");
        else System.out.println(c);
    }

    private void findBestCoupon() {
        double amount = readDouble("Enter cart amount: ");
        if (amount < 0) { System.out.println("Amount cannot be negative."); return; }
        Coupon best = store.bestCoupon(amount);
        if (best == null) System.out.println("No eligible coupon found.");
        else {
            double discount = best.calculateDiscount(amount);
            System.out.printf("Best coupon: %s | Discount: %.2f | Payable: %.2f%n",
                    best.getCode(), discount, amount - discount);
        }
    }

    private void sortCoupons() {
        List<Coupon> sorted = store.getCouponsSortedByDiscount();
        sorted.sort(Comparator.comparingDouble((Coupon c) -> c.calculateDiscount(
                Math.max(10000, c.getMinPurchase()))).reversed());
        System.out.println("--- Coupons sorted by estimated discount ---");
        for (Coupon c : sorted) System.out.println(c);
    }

    private void calculateBill() {
        double amount = readDouble("Enter cart amount: ");
        if (amount < 0) { System.out.println("Amount cannot be negative."); return; }
        Coupon best = store.bestCoupon(amount);
        System.out.printf("Original amount: %.2f%n", amount);
        if (best == null) {
            System.out.printf("No eligible coupon. Final amount: %.2f%n", amount);
        } else {
            double discount = best.calculateDiscount(amount);
            System.out.println("Applied coupon: " + best.getCode());
            System.out.printf("Discount: %.2f%n", discount);
            System.out.printf("Final amount: %.2f%n", amount - discount);
        }
    }

    private void deactivateCoupon() {
        String code = readNonEmpty("Coupon code to deactivate: ").toUpperCase(Locale.ROOT);
        Coupon c = store.search(code);
        if (c == null) System.out.println("Coupon not found.");
        else if (!c.isActive()) System.out.println("Coupon is already inactive.");
        else {
            c.setActive(false);
            System.out.println("Coupon deactivated.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid integer."); }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                if (Double.isFinite(value)) return value;
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter a valid number.");
        }
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("This field cannot be empty.");
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return LocalDate.parse(sc.nextLine().trim()); }
            catch (DateTimeParseException e) { System.out.println("Use YYYY-MM-DD format."); }
        }
    }
}

enum DiscountType { PERCENT, FLAT }

class Coupon {
    private final String code;
    private final DiscountType type;
    private final double value;
    private final double minPurchase;
    private final double maxDiscount;
    private final LocalDate expiryDate;
    private boolean active;

    Coupon(String code, DiscountType type, double value, double minPurchase,
           double maxDiscount, LocalDate expiryDate, boolean active) {
        this.code = code.toUpperCase(Locale.ROOT);
        this.type = type;
        this.value = value;
        this.minPurchase = minPurchase;
        this.maxDiscount = maxDiscount;
        this.expiryDate = expiryDate;
        this.active = active;
    }

    String getCode() { return code; }
    double getMinPurchase() { return minPurchase; }
    boolean isActive() { return active; }
    void setActive(boolean active) { this.active = active; }

    boolean isEligible(double amount) {
        return active && !LocalDate.now().isAfter(expiryDate) && amount >= minPurchase;
    }

    double calculateDiscount(double amount) {
        if (!isEligible(amount)) return 0;
        double discount = type == DiscountType.PERCENT
                ? amount * value / 100.0 : value;
        if (type == DiscountType.PERCENT && maxDiscount > 0)
            discount = Math.min(discount, maxDiscount);
        return Math.min(discount, amount);
    }

    @Override public String toString() {
        String offer = type == DiscountType.PERCENT
                ? value + "% (cap " + maxDiscount + ")" : "Flat " + value;
        return String.format("%-12s | %-20s | Min: %.2f | Expires: %s | %s",
                code, offer, minPurchase, expiryDate, active ? "ACTIVE" : "INACTIVE");
    }
}

class CouponStore {
    // HashMap provides average O(1) coupon lookup by unique code.
    private final Map<String, Coupon> couponMap = new HashMap<>();
    // BST stores coupons by code and supports ordered traversal.
    private final CouponBST couponTree = new CouponBST();

    void add(Coupon coupon) {
        couponMap.put(coupon.getCode(), coupon);
        couponTree.insert(coupon);
    }

    Coupon search(String code) {
        return couponMap.get(code.toUpperCase(Locale.ROOT));
    }

    Coupon bestCoupon(double amount) {
        Coupon best = null;
        double maxDiscount = 0;
        for (Coupon coupon : couponMap.values()) {
            if (coupon.isEligible(amount)) {
                double discount = coupon.calculateDiscount(amount);
                if (discount > maxDiscount) {
                    maxDiscount = discount;
                    best = coupon;
                }
            }
        }
        return best; // Greedy choice: maximum immediate saving for this cart.
    }

    List<Coupon> getCouponsSortedByDiscount() {
        return new ArrayList<>(couponMap.values());
    }

    void displayInOrder() {
        if (couponMap.isEmpty()) { System.out.println("No coupons available."); return; }
        System.out.println("--- Coupons in code order (BST Inorder) ---");
        couponTree.inorder();
    }
}

class CouponBST {
    private static class Node {
        Coupon coupon;
        Node left, right;
        Node(Coupon coupon) { this.coupon = coupon; }
    }
    private Node root;

    void insert(Coupon coupon) { root = insertRec(root, coupon); }

    private Node insertRec(Node node, Coupon coupon) {
        if (node == null) return new Node(coupon);
        int cmp = coupon.getCode().compareTo(node.coupon.getCode());
        if (cmp < 0) node.left = insertRec(node.left, coupon);
        else if (cmp > 0) node.right = insertRec(node.right, coupon);
        return node;
    }

    void inorder() { inorderRec(root); }

    private void inorderRec(Node node) {
        if (node == null) return;
        inorderRec(node.left);
        System.out.println(node.coupon);
        inorderRec(node.right);
    }
}
