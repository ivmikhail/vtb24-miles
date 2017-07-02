package com.github.ivmikhail.writer;

import com.github.ivmikhail.reward.RewardResult;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ivmikhail on 02/07/2017.
 */
public abstract class AbstractWriter {
    protected OutputStream out;

    protected AbstractWriter(OutputStream out) {
        this.out = out;
    }

    public void write(RewardResult rewardResult) throws IOException {
        String text = format(rewardResult);
        out.write(text.getBytes());
    }

    abstract protected String format(RewardResult rewardResult);
}
