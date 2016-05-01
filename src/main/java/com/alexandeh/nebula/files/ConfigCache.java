package com.alexandeh.nebula.files;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Copyright (c) 2016, Alexander Maxwell. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - The name of Alexander Maxwell may not be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
@Getter
public class ConfigCache {

    private ConfigFile configFile;

    public ConfigCache(ConfigFile configFile) {
        this.configFile = configFile;
    }

    @Getter
    public static class LangConfigCache extends ConfigCache {

        private List<List<String>> help;
        private Map<String, String> tooFewArguments, errors, announcements;

        public LangConfigCache(ConfigFile configFile) {
            super(configFile);

            help = new ArrayList<>(4);
            help.add(configFile.getStringList("FACTION_HELP.1"));
            help.add(configFile.getStringList("FACTION_HELP.2"));
            help.add(configFile.getStringList("FACTION_HELP.3"));
            help.add(configFile.getStringList("FACTION_HELP.4"));

            tooFewArguments = new HashMap<>();
            tooFewArguments.put("CREATE", configFile.getString("TOO_FEW_ARGS.CREATE"));

            errors = new HashMap<>();
            errors.put("NOT_IN_FACTION", configFile.getString("ERROR.NOT_IN_FACTION"));
            errors.put("ALREADY_IN_FACTION", configFile.getString("ERROR.ALREADY_IN_FACTION"));
            errors.put("NOT_ALPHANUMERIC", configFile.getString("ERROR.NOT_ALPHANUMERIC"));
            errors.put("BLOCKED_NAME", configFile.getString("ERROR.BLOCKED_NAME"));
            errors.put("TAG_TOO_SHORT", configFile.getString("ERROR.TAG_TOO_SHORT"));
            errors.put("TAG_TOO_LONG", configFile.getString("ERROR.TAG_TOO_LONG"));

            announcements = new HashMap<>();
            announcements.put("FACTION_CREATED", configFile.getString("ANNOUNCEMENTS.FACTION_CREATED"));
        }
    }

    @Getter
    public static class MainConfigCache extends ConfigCache {

        private int[] factionNameLimit;
        private List<String> blockedFactionNames;

        public MainConfigCache(ConfigFile configFile) {
            super(configFile);

            factionNameLimit = new int[]{configFile.getInt("FACTION_NAME.MIN_CHARACTERS"), configFile.getInt("FACTION_NAME.MAX_CHARACTERS")};
            blockedFactionNames = configFile.getStringList("FACTION_NAME.BLOCKED_NAMES");
        }

    }

}
