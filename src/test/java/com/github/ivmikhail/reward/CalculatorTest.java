package com.github.ivmikhail.reward;

import com.github.ivmikhail.app.Settings;
import com.github.ivmikhail.statement.Operation;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

@Ignore
public class CalculatorTest {

    private Calculator calculator;
    private Operation operation;

    @Before
    public void setUp() {
        Properties properties = new Properties();
        properties.setProperty("MilesRewardRule.statement.ignore.description", "ignore, another ignore");
        properties.setProperty("MilesRewardRule.statement.foreign.description", "foreign");

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

//    @Test
//    public void testRefill() {
//        operation.setAmount(BigDecimal.ONE);
//        operation.setAmountInAccountCurrency(BigDecimal.ONE);
//        RewardSummary result = calculator.process(Collections.singletonList(operation));
//        List<Transaction> transactions = result.getTransactionsMap().get(Transaction.Type.REFILL);
//
//        assertEquals(1, transactions.size());
//        assertEquals(0, result.get.compareTo(BigDecimal.ZERO));
//    }
//
//    @Test
//    public void testWithdrawNormal() {
//        RewardSummary result = calculator.process(Collections.singletonList(operation));
//        List<Reward> tRewards = result.getTransactionsMap().get(Transaction.Type.WITHDRAW);
//
//        assertEquals(1, tRewards.size());
//        assertEquals(0, tRewards.get(0).getMiles().compareTo(new BigDecimal("4")));
//    }
//
//    @Test
//    public void testWithdrawNormalLessThan100() {
//        operation.setAmount(new BigDecimal("-99.9"));
//        operation.setAmountInAccountCurrency(new BigDecimal("-99.9"));
//        RewardSummary result = calculator.process(Collections.singletonList(operation));
//        List<Reward> tRewards = result.getTransactionsMap().get(Transaction.Type.WITHDRAW);
//
//        assertEquals(1, tRewards.size());
//        assertEquals(0, tRewards.get(0).getMiles().compareTo(BigDecimal.ZERO));
//    }

//    @Test
//    public void testWithdrawForeignByCurrencyCode() {
//        operation.setCurrencyCode("USD");
//        RewardSummary result = calculator.process(Collections.singletonList(operation));
//        List<Reward> tRewards = result.getTransactionsMap().get(Operation.Type.WITHDRAW_FOREIGN);
//
//        assertEquals(1, tRewards.size());
//        assertEquals(0, tRewards.get(0).getMiles().compareTo(new BigDecimal("5")));
//    }

//    @Test
//    public void testWithdrawForeignByDescription() {
//        operation.setDescription("my foreign operation");
//        RewardSummary result = calculator.process(Collections.singletonList(operation));
//        List<Reward> tRewards = result.getTransactionsMap().get(Operation.Type.WITHDRAW_FOREIGN);
//
//        assertEquals(0, tRewards.get(0).getMiles().compareTo(new BigDecimal("5")));
//        assertEquals(1, tRewards.size());
//    }
//
//    @Test
//    public void testWithdrawIgnoreByDescription() {
//        operation.setDescription("Ignore this operation, please ignore");
//        RewardSummary result = calculator.process(Collections.singletonList(operation));
//        List<Reward> ignored = result.getTransactionsMap().get(Transaction.Type.WITHDRAW_IGNORE);
//
//
//        assertEquals(0, ignored.get(0).getMiles().compareTo(BigDecimal.ZERO));
//        assertEquals(1, ignored.size());
//    }
}