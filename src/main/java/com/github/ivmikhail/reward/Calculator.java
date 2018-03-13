package com.github.ivmikhail.reward;

import com.github.ivmikhail.app.Settings;
import com.github.ivmikhail.fx.FxProvider;
import com.github.ivmikhail.fx.FxRate;
import com.github.ivmikhail.fx.vtb.VTBFxProvider;
import com.github.ivmikhail.reward.rule.RewardRule;
import com.github.ivmikhail.reward.rule.RulesFactory;
import com.github.ivmikhail.statement.Operation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.github.ivmikhail.util.PropsUtil.*;

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
        RewardRule[] rules = createRules(totalWithdraw);
        RewardSummary result = new RewardSummary(totalWithdraw, rules);

        for (Transaction t : transactions) {
            t.setRewards(calculateRewardsFor(t, rules));
            result.add(t);
        }

        LocalDate[] range = getRange(ops);
        result.setMinDate(range[0]);
        result.setMaxDate(range[1]);

        return result;
    }

    private Transaction.Reward[] calculateRewardsFor(Transaction t, RewardRule[] rules) {
        Transaction.Reward[] rewards = new Transaction.Reward[rules.length];

        if (t.getType() == Transaction.Type.WITHDRAW) {
            for (int i = 0; i < rules.length; i++) {
                rewards[i] = rules[i].calculate(t);
            }
        }

        return rewards;
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

    private RewardRule[] createRules(BigDecimal totalWithdraw) {

        RulesFactory f = new RulesFactory();

        BigDecimal withdrawAbs = totalWithdraw.abs();
        RewardRule[] rules = new RewardRule[]{
                f.createKMPlatinum(foreignTransactionWords),
                f.createKMGold(foreignTransactionWords),
                f.createCashback(withdrawAbs),
                f.createTravel(withdrawAbs)
        };

        return rules;
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