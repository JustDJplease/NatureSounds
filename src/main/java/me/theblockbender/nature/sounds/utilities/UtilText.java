/*
 * NatureSounds plugin - Adds ambient sounds to Minecraft.
 * Copyright (C) 2018 Floris Jolink (TheBlockBender / JustDJplease) - All Rights Reserved
 *
 * You are allowed to:
 * - Modify this code, and use it for personal projects. (Private servers, small networks)
 * - Take ideas and / or formats of this plugin and use it for personal projects. (Private servers, small networks)
 *
 * You are NOT allowed to:
 * - Resell the original plugin or a modification of it.
 * - Claim this plugin as your own.
 * - Distribute the source-code or a modification of it without prior consent of the original author.
 *
 */

package me.theblockbender.nature.sounds.utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UtilText {
    private final static int CENTER_PX = 154;

    public static void sendCenteredMessage(Player player, String message) {
        sendCenteredMessage((CommandSender) player, message);
    }

    public static void sendCenteredMessage(CommandSender player, String message) {
        if (message == null || message.equals("")) {
            player.sendMessage("");
            return;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);
        int pixelSize = 0;
        boolean isColourCode = false;
        boolean isBold = false;
        for (char character : message.toCharArray()) {
            if (character == '§') {
                isColourCode = true;
            } else if (isColourCode) {
                isColourCode = false;
                isBold = character == 'l' || character == 'L';
            } else {
                FontInformation dFI = FontInformation.getDefaultFontInfo(character);
                pixelSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                pixelSize++;
            }
        }
        int halvedMessageSize = pixelSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = FontInformation.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (compensated < toCompensate) {
            stringBuilder.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(stringBuilder.toString() + message);
    }

    public enum FontInformation {

        A('A', 5),
        a('a', 5),
        B('B', 5),
        b('b', 5),
        C('C', 5),
        c('c', 5),
        D('D', 5),
        d('d', 5),
        E('E', 5),
        e('e', 5),
        F('F', 5),
        f('f', 4),
        G('G', 5),
        g('g', 5),
        H('H', 5),
        h('h', 5),
        I('I', 3),
        i('i', 1),
        J('J', 5),
        j('j', 5),
        K('K', 5),
        k('k', 4),
        L('L', 5),
        l('l', 1),
        M('M', 5),
        m('m', 5),
        N('N', 5),
        n('n', 5),
        O('O', 5),
        o('o', 5),
        P('P', 5),
        p('p', 5),
        Q('Q', 5),
        q('q', 5),
        R('R', 5),
        r('r', 5),
        S('S', 5),
        s('s', 5),
        T('T', 5),
        t('t', 4),
        U('U', 5),
        u('u', 5),
        V('V', 5),
        v('v', 5),
        W('W', 5),
        w('w', 5),
        X('X', 5),
        x('x', 5),
        Y('Y', 5),
        y('y', 5),
        Z('Z', 5),
        z('z', 5),
        NUM_1('1', 5),
        NUM_2('2', 5),
        NUM_3('3', 5),
        NUM_4('4', 5),
        NUM_5('5', 5),
        NUM_6('6', 5),
        NUM_7('7', 5),
        NUM_8('8', 5),
        NUM_9('9', 5),
        NUM_0('0', 5),
        EXCLAMATION_POINT('!', 1),
        AT_SYMBOL('@', 6),
        NUM_SIGN('#', 5),
        DOLLAR_SIGN('$', 5),
        PERCENT('%', 5),
        UP_ARROW('^', 5),
        AMPERSAND('&', 5),
        ASTERISK('*', 5),
        LEFT_PARENTHESIS('(', 4),
        RIGHT_PARENTHESIS(')', 4),
        MINUS('-', 5),
        UNDERSCORE('_', 5),
        PLUS_SIGN('+', 5),
        EQUALS_SIGN('=', 5),
        LEFT_CURL_BRACE('{', 4),
        RIGHT_CURL_BRACE('}', 4),
        LEFT_BRACKET('[', 3),
        RIGHT_BRACKET(']', 3),
        COLON(':', 1),
        SEMI_COLON(';', 1),
        DOUBLE_QUOTE('"', 3),
        SINGLE_QUOTE('\'', 1),
        LEFT_ARROW('<', 4),
        RIGHT_ARROW('>', 4),
        QUESTION_MARK('?', 5),
        SLASH('/', 5),
        BACK_SLASH('\\', 5),
        LINE('|', 1),
        TILDE('~', 5),
        TICK('`', 2),
        PERIOD('.', 1),
        COMMA(',', 1),
        SPACE(' ', 3),
        DEFAULT('a', 4);

        private final char character;
        private final int length;

        FontInformation(char character, int length) {
            this.character = character;
            this.length = length;
        }

        static FontInformation getDefaultFontInfo(char c) {
            for (FontInformation dFI : FontInformation.values()) {
                if (dFI.getCharacter() == c) return dFI;
            }
            return FontInformation.DEFAULT;
        }

        char getCharacter() {
            return this.character;
        }

        int getLength() {
            return this.length;
        }

        int getBoldLength() {
            if (this == FontInformation.SPACE) return this.getLength();
            return this.length + 1;
        }
    }
}
