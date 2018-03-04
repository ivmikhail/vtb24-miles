package com.github.ivmikhail.reward;

import com.github.ivmikhail.app.Settings;
import com.github.ivmikhail.transactions.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class MilesRewardRuleTest {

    private MilesRewardRule rule;
    private Transaction transaction;

    @Before
    public void setUp() {
        Properties properties = new Properties();
        properties.setProperty("MilesRewardRule.transactions.ignore.description", "ignore, another ignore");
        properties.setProperty("MilesRewardRule.transactions.foreign.description", "foreign");

        Settings s = new Settings();
        s.setProperties(properties);

        rule = new MilesRewardRule(s);

        transaction = new Transaction();
        transaction.setAccountNumberMasked("123XXX1234");
        transaction.setDateTime(LocalDateTime.now());
        transaction.setProcessedDate(LocalDate.now());
        transaction.setAmount(new BigDecimal("-100"));
        transaction.setCurrencyCode("RUR");
        transaction.setAccountCurrencyCode("RUR");
        transaction.setAmountInAccountCurrency(new BigDecimal("-100"));
        transaction.setDescription("UBER RU JUN29 ABCD HELP;");
        transaction.setStatus("Исполнено");
    }

    @Test
    public void testRefill() {
        transaction.setAmount(BigDecimal.ONE);
        transaction.setAmountInAccountCurrency(BigDecimal.ONE);
        RewardResult result = rule.process(Collections.singletonList(transaction));
        List<TransactionReward> tRewards = result.getTransactionsMap().get(Transaction.Type.REFILL);

        assertEquals(1, tRewards.size());
        assertEquals(0, tRewards.get(0).getMiles().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testWithdrawNormal() {
        RewardResult result = rule.process(Collections.singletonList(transaction));
        List<TransactionReward> tRewards = result.getTransactionsMap().get(Transaction.Type.WITHDRAW_NORMAL);

        assertEquals(1, tRewards.size());
        assertEquals(0, tRewards.get(0).getMiles().compareTo(new BigDecimal("4")));
    }

    @Test
    public void testWithdrawNormalLessThan100() {
        transaction.setAmount(new BigDecimal("-99.9"));
        transaction.setAmountInAccountCurrency(new BigDecimal("-99.9"));
        RewardResult result = rule.process(Collections.singletonList(transaction));
        List<TransactionReward> tRewards = result.getTransactionsMap().get(Transaction.Type.WITHDRAW_NORMAL);

        assertEquals(1, tRewards.size());
        assertEquals(0, tRewards.get(0).getMiles().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testWithdrawForeignByCurrencyCode() {
        transaction.setCurrencyCode("USD");
        RewardResult result = rule.process(Collections.singletonList(transaction));
        List<TransactionReward> tRewards = result.getTransactionsMap().get(Transaction.Type.WITHDRAW_FOREIGN);

        assertEquals(1, tRewards.size());
        assertEquals(0, tRewards.get(0).getMiles().compareTo(new BigDecimal("5")));
    }

    @Test
    public void testWithdrawForeignByDescription() {
        transaction.setDescription("my foreign transaction");
        RewardResult result = rule.process(Collections.singletonList(transaction));
        List<TransactionReward> tRewards = result.getTransactionsMap().get(Transaction.Type.WITHDRAW_FOREIGN);

        assertEquals(0, tRewards.get(0).getMiles().compareTo(new BigDecimal("5")));
        assertEquals(1, tRewards.size());
    }

    @Test
    public void testWithdrawIgnoreByDescription() {
        transaction.setDescription("Ignore this transaction, please ignore");
        RewardResult result = rule.process(Collections.singletonList(transaction));
        List<TransactionReward> ignored = result.getTransactionsMap().get(Transaction.Type.WITHDRAW_IGNORE);


        assertEquals(0, ignored.get(0).getMiles().compareTo(BigDecimal.ZERO));
        assertEquals(1, ignored.size());
    }
}