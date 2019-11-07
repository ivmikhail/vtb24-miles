package dev.ivmikhail.vtb24.miles.reward;

import dev.ivmikhail.vtb24.miles.app.Settings;
import dev.ivmikhail.vtb24.miles.statement.Operation;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {

    private Calculator calculator;
    private Operation operation;

    @Before
    public void setUp() {
        Properties properties = new Properties();
        properties.setProperty("RewardRule.operations.ignore.description", "ignore, another ignore");

        Settings s = new Settings();
        s.setMinDate(LocalDate.MIN);
        s.setMaxDate(LocalDate.MAX);
        s.setProperties(properties);

        calculator = new Calculator(s);

        operation = new Operation();
        operation.setAccountNumberMasked("123XXX1234");
        operation.setDateTime(LocalDateTime.now());
        operation.setProcessedDate(LocalDate.now());
        operation.setAmount(new BigDecimal("-100"));
        operation.setCurrencyCode("RUR");
        operation.setAccountCurrencyCode("RUR");
        operation.setAmountInAccountCurrency(new BigDecimal("-100"));
        operation.setDescription("UBER RU JUN29 ABCD HELP;");
        operation.setStatus("Исполнено");
    }

    @Test
    public void testRefill() {
        operation.setAmount(BigDecimal.ONE);
        operation.setAmountInAccountCurrency(BigDecimal.ONE);
        RewardSummary result = calculator.process(Collections.singletonList(operation));
        List<Transaction> transactions = result.getTransactionsMap().get(Transaction.Type.REFILL);

        assertEquals(1, transactions.size());
    }

    @Test
    public void testWithdrawNormal() {
        RewardSummary result = calculator.process(Collections.singletonList(operation));
        List<Transaction> transactions = result.getTransactionsMap().get(Transaction.Type.WITHDRAW);

        assertEquals(1, transactions.size());
    }

    @Test
    public void testWithdrawIgnoreByDescription() {
        operation.setDescription("Ignore this operation, please ignore");
        RewardSummary result = calculator.process(Collections.singletonList(operation));
        List<Transaction> transactions = result.getTransactionsMap().get(Transaction.Type.WITHDRAW_IGNORE);

        assertEquals(1, transactions.size());
    }

    @Test
    public void testWithdrawIgnoreMoneyTransfer() {
        operation.setDescription("И**в Иван Иваноич");
        RewardSummary result = calculator.process(Collections.singletonList(operation));
        List<Transaction> transactions = result.getTransactionsMap().get(Transaction.Type.WITHDRAW_IGNORE);

        assertEquals(1, transactions.size());
    }
}