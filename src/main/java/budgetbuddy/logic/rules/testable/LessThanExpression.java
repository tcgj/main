package budgetbuddy.logic.rules.testable;

import static budgetbuddy.commons.util.CollectionUtil.requireAllNonNull;

import budgetbuddy.commons.core.index.Index;
import budgetbuddy.logic.rules.RuleEngine;
import budgetbuddy.model.account.Account;
import budgetbuddy.model.rule.expression.Attribute;
import budgetbuddy.model.rule.expression.Value;

/**
 * Represents a less-than expression.
 */
public class LessThanExpression extends TestableExpression {
    /**
     * Constructs a LessThanExpression with the given attribute and value.
     *
     * @param attribute the attribute to be tested with.
     * @param value the value to be tested against.
     */
    public LessThanExpression(Attribute attribute, Value value) {
        super(attribute, value);
    }

    @Override
    public boolean test(Index txnIndex, Account account) {
        requireAllNonNull(txnIndex, account);
        double left = (double) RuleEngine.extractAttribute(attribute, txnIndex, account);
        double right = Double.parseDouble(value.toString());
        return left >= 0 && left < right;
    }
}
