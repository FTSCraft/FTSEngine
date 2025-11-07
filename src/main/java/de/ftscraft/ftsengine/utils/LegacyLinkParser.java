package de.ftscraft.ftsengine.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LegacyLinkParser {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "(https?://\\S+)" +
                    "|(www\\.[\\p{L}0-9\\-._~%]+\\S*)" +
                    "|([\\p{L}0-9\\-._~%]+\\.[a-zA-Z]{2,}\\S*)"
    );

    private LegacyLinkParser() {}

    public static Component parse(String input) {
        TextComponent.Builder root = Component.text();

        TextColor currentColor = NamedTextColor.WHITE;
        Set<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);
        boolean obfuscated = false; // &k

        StringBuilder buffer = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);

            if (c == '&' && i + 1 < input.length()) {
                if (!buffer.isEmpty()) {
                    root.append(applyStyle(buffer.toString(), currentColor, decorations, obfuscated));
                    buffer.setLength(0);
                }

                char code = Character.toLowerCase(input.charAt(i + 1));
                switch (code) {
                    // Farben
                    case '0' -> currentColor = NamedTextColor.BLACK;
                    case '1' -> currentColor = NamedTextColor.DARK_BLUE;
                    case '2' -> currentColor = NamedTextColor.DARK_GREEN;
                    case '3' -> currentColor = NamedTextColor.DARK_AQUA;
                    case '4' -> currentColor = NamedTextColor.DARK_RED;
                    case '5' -> currentColor = NamedTextColor.DARK_PURPLE;
                    case '6' -> currentColor = NamedTextColor.GOLD;
                    case '7' -> currentColor = NamedTextColor.GRAY;
                    case '8' -> currentColor = NamedTextColor.DARK_GRAY;
                    case '9' -> currentColor = NamedTextColor.BLUE;
                    case 'a' -> currentColor = NamedTextColor.GREEN;
                    case 'b' -> currentColor = NamedTextColor.AQUA;
                    case 'c' -> currentColor = NamedTextColor.RED;
                    case 'd' -> currentColor = NamedTextColor.LIGHT_PURPLE;
                    case 'e' -> currentColor = NamedTextColor.YELLOW;
                    case 'f' -> currentColor = NamedTextColor.WHITE;


                    // Formatierungen
                    case 'l' -> decorations.add(TextDecoration.BOLD);
                    case 'n' -> decorations.add(TextDecoration.UNDERLINED);
                    case 'o' -> decorations.add(TextDecoration.ITALIC);
                    case 'm' -> decorations.add(TextDecoration.STRIKETHROUGH);
                    case 'k' -> obfuscated = true;


                    // reset
                    case 'r' -> {
                        currentColor = NamedTextColor.WHITE;
                        decorations.clear();
                        obfuscated = false;
                    }
                    default ->
                        // unbekannter code -> einfach anzeigen
                            buffer.append(c).append(code);
                }

                i += 2;
                continue;
            }

            Matcher m = URL_PATTERN.matcher(input);
            m.region(i, input.length());
            if (m.lookingAt()) {
                if (!buffer.isEmpty()) {
                    root.append(applyStyle(buffer.toString(), currentColor, decorations, obfuscated));
                    buffer.setLength(0);
                }

                String matched = m.group();
                String urlToOpen = normalizeUrl(matched);

                Component linkComp = applyStyle(matched, currentColor, decorations, obfuscated)
                        .clickEvent(ClickEvent.openUrl(urlToOpen));

                root.append(linkComp);

                i = m.end();
                continue;
            }

            buffer.append(c);
            i++;
        }

        if (!buffer.isEmpty()) {
            root.append(applyStyle(buffer.toString(), currentColor, decorations, obfuscated));
        }

        return root.build();
    }

    private static Component applyStyle(String text,
                                        TextColor color,
                                        Set<TextDecoration> decorations,
                                        boolean obfuscated) {

        Style.Builder sb = Style.style().color(color);

        for (TextDecoration deco : decorations) {
            sb.decoration(deco, TextDecoration.State.TRUE);
        }

        if (obfuscated) {
            sb.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.TRUE);
        }

        return Component.text(text, sb.build());
    }

    private static String normalizeUrl(String raw) {
        if (raw.startsWith("http://") || raw.startsWith("https://")) {
            return raw;
        }
        return "https://" + raw;
    }
}

