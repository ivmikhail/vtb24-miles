package com.github.ivmikhail;

import com.github.ivmikhail.reward.RewardResult;
import com.github.ivmikhail.reward.TransactionRewardResult;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ExportTo {

    private static final String TEMPLATES_CLASSPATH_DIR = "/templates";
    private static final String TEMPLATE_REWARD_RESULT = "rewardResult.ftl";

    private ExportTo() { /* helper class */}

    public static String txt(RewardResult reward) {
        Map model = new HashMap();
        model.put("reward", reward);

        try (Writer out = new StringWriter()) {
            Template template = createTemplateEngine().getTemplate(TEMPLATE_REWARD_RESULT);
            template.process(model, out);

            return out.toString();
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Configuration createTemplateEngine() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
        cfg.setClassForTemplateLoading(App.class, TEMPLATES_CLASSPATH_DIR);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setAPIBuiltinEnabled(true);
        return cfg;
    }

    public static void csv(String filePath, RewardResult reward) throws IOException {
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
    }

    private static void printTransactions(CSVPrinter printer, String title, List<TransactionRewardResult> transactions) throws IOException {
        printer.printRecord(title);
        printer.printRecord("СЧЕТ", "ДАТА ОБР", "ОПИСАНИЕ", "СУММА", "ВАЛЮТА", "МИЛИ");

        if (transactions == null || transactions.isEmpty()) {
            printer.printRecord("<нет операций>");
            return;
        }

        for (TransactionRewardResult t : transactions) {
            printer.printRecord(
                    t.getTransaction().getAccountNumberMasked(),
                    t.getTransaction().getProcessedDate(),
                    t.getTransaction().getDescription(),
                    t.getTransaction().getAmountInAccountCurrency(),
                    t.getTransaction().getAccountCurrencyCode(),
                    t.getMiles()
            );
        }
    }
}