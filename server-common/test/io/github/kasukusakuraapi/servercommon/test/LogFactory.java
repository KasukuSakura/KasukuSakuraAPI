package io.github.kasukusakuraapi.servercommon.test;

import io.github.karlatemp.mxlib.common.utils.SimpleClassLocator;
import io.github.karlatemp.mxlib.logger.AnsiMessageFactory;
import io.github.karlatemp.mxlib.logger.SimpleLogger;
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender;
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender.PrefixSupplier;
import io.github.karlatemp.mxlib.logger.renders.SimpleRender;
import io.github.karlatemp.mxlib.utils.StringUtils;

import java.time.format.DateTimeFormatter;

public class LogFactory {
    public static PrefixedRender newRender() {
        var mf = new AnsiMessageFactory(new SimpleClassLocator());
        return new PrefixedRender(
                new SimpleRender(mf),
                PrefixSupplier.constant(StringUtils.BkColors._B)
                        .plus(PrefixSupplier.dated(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .plus(" " + StringUtils.BkColors._5)
                        .plus(PrefixSupplier.dated(DateTimeFormatter.ofPattern("HH:mm:ss")))
                        .plus(StringUtils.BkColors.RESET + " [" + StringUtils.BkColors._6)
                        .plus(PrefixSupplier.loggerName().aligned(PrefixedRender.AlignedSupplier.AlignType.LEFT))
                        .plus(StringUtils.BkColors.RESET + "] [" + StringUtils.BkColors._B)
                        .plus(PrefixSupplier.loggerLevel().aligned(PrefixedRender.AlignedSupplier.AlignType.CENTER))
                        .plus(StringUtils.BkColors.RESET + "] ")
        );
    }

    public static class Lg extends SimpleLogger {
        public static final PrefixedRender renderX = newRender();

        public Lg(String n) {
            super(n, renderX);
        }

        @Override
        protected void render(StringBuilder message) {
            System.out.println(message);
        }
    }
}
