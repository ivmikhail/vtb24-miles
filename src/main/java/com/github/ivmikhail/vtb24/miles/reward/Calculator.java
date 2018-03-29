package com.github.ivmikhail.vtb24.miles.reward;

import com.github.ivmikhail.vtb24.miles.app.Settings;
import com.github.ivmikhail.vtb24.miles.fx.FxProvider;
import com.github.ivmikhail.vtb24.miles.fx.FxRate;
import com.github.ivmikhail.vtb24.miles.fx.vtb.VTBFxProvider;
import com.github.ivmikhail.vtb24.miles.reward.rule.RewardRule;
import com.github.ivmikhail.vtb24.miles.reward.rule.RulesFactory;
import com.github.ivmikhail.vtb24.miles.statement.Operation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static com.github.ivmikhail.vtb24.miles.util.PropsUtil.getAsSet;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class Calculator {
    private static final String RUR = "RUR";
    private static final String COMMA = ",";

    private Set<String> ignoreWords;
    private Set<String> foreignTransactionWords;
    private Settings settings;
    private FxProvider fxProvider;

    public Calculator(Settings settings) {
        this.settings = settings;

        Properties props = settings.getProperties();

        this.fxProvider = new VTBFxProvider(props);
        this.ignoreWords = getAsSet(props, "RewardRule.operations.ignore.description", COMMA);
        this.foreignTransactionWords = getAsSet(props, "RewardRule.KMRule.operations.foreign.description", COMMA);
    }

    public RewardSummary process(List<Operation> ops) {
        List<Transaction> transactions = toTransactions(ops);
        BigDecimal totalWithdraw = getTotalWithdraw(transactions);
        RewardRule rule = createRule(totalWithdraw);
        RewardSummary result = new RewardSummary(totalWithdraw, rule);

        for (Transaction t : transactions) {
            if (t.getType() == Transaction.Type.WITHDRAW) {
                t.setRewards(rule.calculate(t));
            }
            result.add(t);
        }

        LocalDate[] range = getRange(ops);
        result.setMinDate(range[0]);
        result.setMaxDate(range[1]);

        return result;
    }

    private List<Transaction> toTransactions(List<Operation> ops) {
        List<Transaction> transactions = new ArrayList<>();
        for (Operation op : ops) {

            FxRate accountCurrencyRate = fxProvider.getRate(op.getAccountCurrencyCode(), RUR, op.getProcessedDate());
            BigDecimal amountInRUR = op.getAmountInAccountCurrency().multiply(accountCurrencyRate.getValue());

            Transaction t = new Transaction();
            t.setOperation(op);
            t.setType(determineType(op));
            t.setAmountInRUR(amountInRUR);
            t.setAccountCurrencyRate(accountCurrencyRate);

            transactions.add(t);
        }

        fxProvider.clear();

        return transactions;
    }

    private RewardRule createRule(BigDecimal totalWithdraw) {
        RulesFactory f = new RulesFactory();
        f.setForeignTransactionWords(foreignTransactionWords);
        f.setWithdrawAbs(totalWithdraw.abs());

        return f.create(settings.getRuleId());
    }

    private Transaction.Type determineType(Operation op) {
        if (isRefill(op)) {
            return Transaction.Type.REFILL;
        } else if (isIgnore(op)) {
            return Transaction.Type.WITHDRAW_IGNORE;
        } else {
            return Transaction.Type.WITHDRAW;
        }
    }

    private boolean isRefill(Operation o) {
        return o.getAmountInAccountCurrency().signum() == 1;
    }

    private boolean isIgnore(Operation o) {
        for (String word : ignoreWords) {
            if (o.getDescription().toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private BigDecimal getTotalWithdraw(List<Transaction> transactions) {
        BigDecimal totalWithdraw = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            if (t.getType() != Transaction.Type.REFILL) {
                totalWithdraw = totalWithdraw.add(t.getAmountInRUR());
            }
        }
        return totalWithdraw;
    }

    private LocalDate[] getRange(List<Operation> ops) {
        Operation first = ops.get(0);
        Operation last = ops.get(ops.size() - 1);

        LocalDate min = settings.getMinDate();
        if (min == null || min.isEqual(LocalDate.MIN)) {
            min = first.getProcessedDate();
        }

        LocalDate max = settings.getMaxDate();
        if (max == null || max.isEqual(LocalDate.MAX)) {
            max = last.getProcessedDate();
        }

        return new LocalDate[]{min, max};
    }
}