package com.github.ivmikhail.reward;

import com.github.ivmikhail.app.Settings;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExportAsTest {

    private RewardResult rewardResult;

    @Before
    public void setUp() {
        Settings s = new Settings();
        s.setMinDate(LocalDate.MIN);
        s.setMaxDate(LocalDate.MAX);

        rewardResult = new RewardResult();
        rewardResult.setSettings(s);
    }

    @Test
    public void testExportCsv() throws IOException {
        Path tempFilePath = Files.createTempFile("junit.test.export.csv.", "tmp");
        File csv = ExportAs.csv(tempFilePath.toAbsolutePath().toString(), rewardResult);

        List<String> lines = Files.readAllLines(csv.toPath(), StandardCharsets.UTF_8);
        String lastLine = lines.get(lines.size() - 1).replace("\"", "");
        assertEquals("Эффективный кэшбек %    0", lastLine);
    }

    @Test
    public void testExportTxt() {
        String result = ExportAs.txt(rewardResult);

        assertTrue(result.endsWith("Эффективный кэшбек %    0"));
    }
}