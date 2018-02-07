package com.github.ivmikhail;

import com.github.ivmikhail.reward.MilesRewardRule;
import com.github.ivmikhail.reward.RewardResult;
import com.github.ivmikhail.reward.TransactionRewardResult;
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

        rule = new MilesRewardRule(properties);

        transaction = new Transaction();
        transaction.setCardNumberMasked("123XXX1234");
        transaction.setDateTime(LocalDateTime.now());
        transaction.setProcessedDate(LocalDate.now());
        transaction.setAmount(new BigDecimal("-950"));
        transaction.setCurrencyCode("RUR");
        transaction.setAccountCurrencyCode("RUR");
        transaction.setAmountInAccountCurrency(new BigDecimal("-950"));
        transaction.setDescription("UBER RU JUN29 ABCD HELP;");
        transaction.setStatus("Исполнено");
    }

    @Test
    public void testForeignByCurrencyCode() {
        transaction.setCurrencyCode("USD");
        RewardResult result = rule.process(Collections.singletonList(transaction));
        List<TransactionRewardResult> foreignTransactions = result.getTransactionsMap().get(Transaction.Type.WITHDRAW_FOREIGN);

        assertEquals(1, foreignTransactions.size());
    }

    @Test
    public void testForeignByDescription() {
        transaction.setDescription("my foreign transaction");
        RewardResult result = rule.process(Collections.singletonList(transaction));
        List<TransactionRewardResult> foreignTransactions = result.getTransactionsMap().get(Transaction.Type.WITHDRAW_FOREIGN);

        assertEquals(1, foreignTransactions.size());
    }

    @Test
    public void testIgnoreByDescription() {
        transaction.setDescription("Ignore this transaction, please ignore");
        RewardResult result = rule.process(Collections.singletonList(transaction));

        List<TransactionRewardResult> ignored = result.getTransactionsMap().get(Transaction.Type.IGNORE);
        assertEquals(1, ignored.size());
    }
}