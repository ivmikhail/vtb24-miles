package com.github.ivmikhail.reward;

import com.github.ivmikhail.app.Settings;
import com.github.ivmikhail.statement.Operation;
import org.junit.Before;
import org.junit.Ignore;
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

@Ignore
public class ExportAsTest {

    private RewardSummary rewardSummary;

    @Before
    public void setUp() {
        Operation o = new Operation();
        o.setAmount(new BigDecimal("-100.0"));
        o.setAmountInAccountCurrency(new BigDecimal("-100.0"));
        o.setDescription("Test transaction");
        o.setCurrencyCode("RUR");
        o.setAccountCurrencyCode("RUR");
        o.setProcessedDate(LocalDate.MIN);
        o.setDateTime(LocalDateTime.MIN);
        o.setAccountNumberMasked("12**34");
        o.setStatus("Обработано");

        Calculator calculator = new Calculator(new Settings());
        rewardSummary = calculator.process(Collections.singletonList(o));
    }

    @Test
    public void testExportCsv() throws IOException {
        File temp = File.createTempFile("junit.test.export.csv.", "tmp");
        temp.deleteOnExit();

        File csv = ExportAs.csv(rewardSummary, temp.getAbsolutePath());

        List<String> lines = Files.readAllLines(csv.toPath(), StandardCharsets.UTF_8);
        String lastLine = lines.get(lines.size() - 1).replace("\"", "");
        assertEquals("Эффективный кэшбек %    4.00", lastLine);
    }

    @Test
    public void testExportTxt() {
        String result = ExportAs.txt(rewardSummary);
        String lines[] = result.split("\\r?\\n");
        String lastLine = lines[lines.length - 1];

        assertEquals("Эффективный кэшбек %    4.00", lastLine);
    }
}