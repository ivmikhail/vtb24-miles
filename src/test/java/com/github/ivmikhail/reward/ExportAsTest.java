package com.github.ivmikhail.reward;

import com.github.ivmikhail.app.Settings;
import com.github.ivmikhail.transactions.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExportAsTest {

    private RewardResult rewardResult;

    @Before
    public void setUp() {
        Transaction t = new Transaction();
        t.setAmount(new BigDecimal("-100.0"));
        t.setAmountInAccountCurrency(new BigDecimal("-100.0"));
        t.setDescription("Test transaction");
        t.setCurrencyCode("RUR");
        t.setAccountCurrencyCode("RUR");
        t.setProcessedDate(LocalDate.MIN);
        t.setDateTime(LocalDateTime.MIN);
        t.setAccountNumberMasked("12**34");
        t.setStatus("Обработано");

        MilesRewardRule rule = new MilesRewardRule(new Settings());
        rewardResult = rule.process(Collections.singletonList(t));
    }

    @Test
    public void testExportCsv() throws IOException {
        File temp = File.createTempFile("junit.test.export.csv.", "tmp");
        temp.deleteOnExit();

        File csv = ExportAs.csv(rewardResult, temp.getAbsolutePath());

        List<String> lines = Files.readAllLines(csv.toPath(), StandardCharsets.UTF_8);
        String lastLine = lines.get(lines.size() - 1).replace("\"", "");
        assertEquals("Эффективный кэшбек %    4.00", lastLine);
    }

    @Test
    public void testExportTxt() {
        String result = ExportAs.txt(rewardResult);
        String lines[] = result.split("\\r?\\n");
        String lastLine = lines[lines.length - 1];

        assertEquals("Эффективный кэшбек %    4", lastLine);
    }

    @Test(expected = IllegalStateException.class)
    public void testExportTxtFail() {
        ExportAs.txt(rewardResult, "/this-templates-path-is-fake");
    }
}