

package dev.madcat.rebirth.util;

import java.util.*;

public class NicknameUtil
{
    private static Map<String, String> nicknames;
    
    public static void addNickname(final String name, final String nick) {
        NicknameUtil.nicknames.put(name, nick);
    }
    
    public static void removeNickname(final String name) {
        NicknameUtil.nicknames.remove(name);
    }
    
    public static String getNickname(final String name) {
        return NicknameUtil.nicknames.get(name);
    }
    
    public static boolean hasNickname(final String name) {
        return NicknameUtil.nicknames.containsKey(name);
    }
    
    public static Map<String, String> getAllNicknames() {
        return NicknameUtil.nicknames;
    }
    
    static {
        NicknameUtil.nicknames = new HashMap<String, String>();
    }
}
