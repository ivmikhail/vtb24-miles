package com.github.ivmikhail.writer;

import com.github.ivmikhail.Settings;
import com.github.ivmikhail.reward.RewardResult;

import java.io.OutputStream;

/**
 * Created by ivmikhail on 02/07/2017.
 */
public class HTMLWriter extends AbstractWriter {

    public HTMLWriter(OutputStream out) {
        super(out);
    }

    @Override
    public String format(Settings settings, RewardResult rewardResult) {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
