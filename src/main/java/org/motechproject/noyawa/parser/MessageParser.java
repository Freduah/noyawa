package org.motechproject.noyawa.parser;

import org.motechproject.noyawa.domain.ProgramType;
import org.motechproject.noyawa.domain.SMS;
import org.motechproject.noyawa.domain.ShortCode;
import org.motechproject.noyawa.repository.AllProgramTypes;
import org.motechproject.noyawa.repository.AllShortCodes;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

import static ch.lambdaj.Lambda.*;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public abstract class MessageParser {

    @Autowired
    protected AllProgramTypes allProgramTypes;

    @Autowired
    protected AllShortCodes allShortCodes;

    protected Pattern pattern;

    public abstract SMS parse(String message, String senderMobileNumber);

    public String getProgramCodePatterns() {
        return joinFrom(flatten(extract(allProgramTypes.getAll(), on(ProgramType.class).getShortCodes())), "|").toString();
    }

    protected Pattern pattern() {
        if (pattern == null) recompilePatterns();
        return pattern;
    }

    protected String shortCodePattern(String key) {
        ShortCode shortCode = allShortCodes.getShortCodeFor(key);
        return isNotEmpty(shortCode.getCodes()) ? join(shortCode.getCodes(), "|") : null;
    }

    public abstract void recompilePatterns();
}
