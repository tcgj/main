package seedu.address.model.transaction;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.account.Account;
import seedu.address.model.attributes.Category;
import seedu.address.model.attributes.Description;
import seedu.address.model.attributes.Direction;



/**
 * Represents a Transaction in a TransactionList.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Transaction {

    private final Date date;
    private final Amount amount;
    private final Direction direction;
    private final Account account;
    private final Description description;
    private final Set<Category> categories = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Transaction(Date date, Amount amount, Direction direction, Description description,
                       Account account, Set<Category> categories) {
        requireAllNonNull(date, amount, direction, categories);
        this.date = date;
        this.amount = amount;
        this.direction = direction;
        this.account = account;
        this.description = description;
        this.categories.addAll(categories);
    }

    public Date getDate() {
        return date;
    }

    public Amount getAmount() {
        return amount;
    }

    public Account getAccount() {
        return account;
    }

    public Direction getDirection() {
        return direction;
    }

    public Description getDescription() {
        return description;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    /**
     * Returns true if both Transactions have all the same fields (date, amount, description, categories).
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Transaction)) {
            return false;
        }

        Transaction otherTransaction = (Transaction) other;
        return otherTransaction.getDate().equals(date)
                && otherTransaction.getAmount().equals(amount)
                && otherTransaction.getDirection().equals(direction)
                && otherTransaction.getAccount().equals(account)
                && otherTransaction.getDescription().equals(description)
                && otherTransaction.getCategories().equals(categories);

    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount, direction, account, description, categories);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Amount: ")
                .append(getAmount())
                .append(direction.toString())
                .append(" on ")
                .append(getDate())
                .append(" in account: ")
                .append(getAccount())
                .append(" Description: ")
                .append(getDescription())
                .append(" Categories: ");
        getCategories().forEach(builder::append);
        return builder.toString();
    }
}
