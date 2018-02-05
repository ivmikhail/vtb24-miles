package com.github.ivmikhail.writer;

import com.github.ivmikhail.Settings;
import com.github.ivmikhail.reward.RewardResult;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ivmikhail on 02/07/2017.
 */
public abstract class AbstractWriter {
    private OutputStream out;

    AbstractWriter(OutputStream out) {
        this.out = out;
    }

    public void write(Settings settings, RewardResult rewardResult) throws IOException {
        String text = format(settings, rewardResult);
        out.write(text.getBytes());
    }

    abstract protected String format(Settings settings, RewardResult rewardResult);
}
