package com.github.ivmikhail.vtb24.miles.reward;

import com.github.ivmikhail.vtb24.miles.app.Settings;
import com.github.ivmikhail.vtb24.miles.reward.rule.RulesFactory;
import com.github.ivmikhail.vtb24.miles.statement.CSVLoader;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class MulticardRuleCalculationTest {
    private Settings settings;

    @Before
    public void setUp() {
        String pathToCsv = getClass().getClassLoader().getResource("multicard-test.csv").getPath();

        settings = new Settings();
        settings.setMinDate(LocalDate.MIN);
        settings.setMaxDate(LocalDate.MAX);
        settings.setProperties(new Properties());
        settings.setPathsToStatement(new String[]{pathToCsv});
        settings.setRuleId(RulesFactory.RuleId.MC_TRAVEL);
    }

    @Test
    public void testTotalMiles() {
        Calculator c = new Calculator(settings);
        RewardSummary reward = c.process(CSVLoader.load(settings));

        assertEquals(0, new BigDecimal("1252").compareTo(reward.getTotalMiles()));
    }
}