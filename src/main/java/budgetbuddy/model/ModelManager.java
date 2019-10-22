package budgetbuddy.model;

import static budgetbuddy.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import budgetbuddy.commons.core.GuiSettings;
import budgetbuddy.commons.core.LogsCenter;
import budgetbuddy.model.person.Person;
import budgetbuddy.model.transaction.Transaction;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Represents the in-memory model of the budget buddy data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final LoansManager loansManager;
    private final AccountBook accountBook;
    private final AccountsManager accountsManager;
    private final RuleManager ruleManager;

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Transaction> filteredTransactions;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */

    public ModelManager(LoansManager loansManager, RuleManager ruleManager, AccountsManager accountsManager,
                        ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(loansManager, ruleManager, accountsManager, addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.loansManager = new LoansManager(loansManager.getLoans());
        this.ruleManager = new RuleManager(ruleManager);
        this.accountsManager = new AccountsManager(accountsManager.getAccountsList());
        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        this.accountBook = new AccountBook();
        filteredTransactions = new FilteredList<>(this.accountBook.getTransactionList());
    }

    public ModelManager() {
        this(new LoansManager(), new RuleManager(), new AccountsManager(), new AddressBook(),
                new UserPrefs());
    }

    //=========== Loan Manager ===============================================================================

    @Override
    public LoansManager getLoansManager() {
        return loansManager;
    }

    //=========== Account Book ===============================================================================

    @Override
    public AccountBook getAccountBook() {
        //TODO return the AccountBook when it's added, for now just returns a new AccountBook
        return new AccountBook();
    }

    @Override
    public void updateFilteredTransactionList(Predicate<Transaction> predicate) {
        requireNonNull(predicate);
        filteredTransactions.setPredicate(predicate);
    }

    @Override
    public FilteredList<Transaction> getFilteredTransactions() {
        return filteredTransactions;
    }

    @Override
    public void deleteTransaction(Transaction target) {
        accountBook.removeTransaction(target);
    }

    @Override
    public RuleManager getRuleManager() {
        return ruleManager;
    }

    @Override
    public AccountsManager getAccountsManager() {
        return accountsManager;
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return ruleManager.equals(other.ruleManager)
                && accountsManager.equals(other.accountsManager)
                && loansManager.equals(other.loansManager)
                && addressBook.equals(other.addressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredPersons.equals(other.filteredPersons);
    }

}