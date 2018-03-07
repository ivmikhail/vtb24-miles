package com.github.ivmikhail.reward;

import com.github.ivmikhail.transactions.Transaction;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ExportAs {

    private static final String TEMPLATES_CLASSPATH_DIR = "/templates";
    private static final String TEMPLATE_REWARD_RESULT = "rewardResult.ftl";

    private ExportAs() { /* helper class */}

    public static String txt(RewardResult reward) {
        return txt(reward, TEMPLATES_CLASSPATH_DIR);
    }

    public static String txt(RewardResult reward, String templatesDir) {
        Map model = new HashMap();
        model.put("reward", reward);

        try (Writer out = new StringWriter()) {
            Template template = createTemplateEngine(templatesDir).getTemplate(TEMPLATE_REWARD_RESULT);
            template.process(model, out);

            return out.toString();
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }

    public static File csv(RewardResult reward, String filePath) throws IOException {
        File f = new File(filePath);
        if (!f.exists()) f.createNewFile();

        try (
                FileWriter out = new FileWriter(f);
                CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL)
        ) {
            printTransactions(printer, "Операции с кэшбеком 4%", reward.getTransactionsMap().get(Transaction.Type.WITHDRAW_NORMAL));
            printer.printRecord("");

            printTransactions(printer, "Операции с кэшбеком 5%", reward.getTransactionsMap().get(Transaction.Type.WITHDRAW_FOREIGN));
            printer.printRecord("");

            printTransactions(printer, "Операции, кешбэк за которые не положен", reward.getTransactionsMap().get(Transaction.Type.WITHDRAW_IGNORE));
            printer.printRecord("");

            printTransactions(printer, "Пополнения", reward.getTransactionsMap().get(Transaction.Type.REFILL));
            printer.printRecord("");

            printer.printRecord("Период с" + reward.getSettings().getMinDate() + " c " + reward.getSettings().getMaxDate());
            printer.printRecord("Всего миль получено     " + reward.getTotalRewardMiles());
            printer.printRecord("Всего пополнений, в руб " + reward.getTotalRefillRUR());
            printer.printRecord("Всего списаний  , в руб " + reward.getTotalWithdrawRUR());
            printer.printRecord("Эффективный кэшбек %    " + reward.getEffectiveCashback());
        }
        return f;
    }

    private static Configuration createTemplateEngine(String templatesClasspathDir) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
        cfg.setClassForTemplateLoading(ExportAs.class, templatesClasspathDir);
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setAPIBuiltinEnabled(true);
        return cfg;
    }

    private static void printTransactions(CSVPrinter printer,
                                          String title,
                                          List<TransactionReward> transactionRewards) throws IOException {
        printer.printRecord(title);
        printer.printRecord("СЧЕТ", "ДАТА ОБР", "ОПИСАНИЕ", "СУММА", "ВАЛЮТА", "МИЛИ");

        if (transactionRewards == null || transactionRewards.isEmpty()) {
            printer.printRecord("<нет операций>");
            return;
        }

        for (TransactionReward tr : transactionRewards) {
            printer.printRecord(
                    tr.getTransaction().getAccountNumberMasked(),
                    tr.getTransaction().getProcessedDate(),
                    tr.getTransaction().getDescription(),
                    tr.getTransaction().getAmountInAccountCurrency(),
                    tr.getTransaction().getAccountCurrencyCode(),
                    tr.getMiles()
            );
        }
    }
}