package com.github.ivmikhail.reward;

import com.github.ivmikhail.Transaction;

import java.util.List;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public interface MilesRewardRule {

     RewardResult process(List<Transaction> transactionList);

     TransactionRewardResult process(Transaction transaction);
}
