package view;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Class: FormatText
 * * @version 1.3
 * * @author: Alex Xiong, Khales Rahman
 *  * Course: ITEC 3860 Spring 2023
 *  * Written: July 5, 2023
 * This class console output format
 */

public class FormatText {
    public String convertText(String text) {
        String styledText = "";

        HashMap<Character, String> charFont = new HashMap<>();
        charFont.put('a', "𝑎");
        charFont.put('b', "𝑏");
        charFont.put('c', "𝑐");
        charFont.put('d', "𝑑");
        charFont.put('e', "𝑒");
        charFont.put('f', "𝑓");
        charFont.put('g', "𝑔");
        charFont.put('h', "ℎ");
        charFont.put('i', "𝑖");
        charFont.put('j', "𝑗");
        charFont.put('k', "𝑘");
        charFont.put('l', "𝑙");
        charFont.put('m', "𝑚");
        charFont.put('n', "𝑛");
        charFont.put('o', "𝑜");
        charFont.put('p', "𝑝");
        charFont.put('q', "𝑞");
        charFont.put('r', "𝑟");
        charFont.put('s', "𝑠");
        charFont.put('t', "𝑡");
        charFont.put('u', "𝑢");
        charFont.put('v', "𝑣");
        charFont.put('w', "𝑤");
        charFont.put('x', "𝑥");
        charFont.put('y', "𝑦");
        charFont.put('z', "𝑧");
        charFont.put('A', "𝐴");
        charFont.put('B', "𝐵");
        charFont.put('C', "𝐶");
        charFont.put('D', "𝐷");
        charFont.put('E', "𝐸");
        charFont.put('F', "𝐹");
        charFont.put('G', "𝐺");
        charFont.put('H', "𝐻");
        charFont.put('I', "𝐼");
        charFont.put('J', "𝐽");
        charFont.put('K', "𝐾");
        charFont.put('L', "𝐿");
        charFont.put('M', "𝑀");
        charFont.put('N', "𝑁");
        charFont.put('O', "𝑂");
        charFont.put('P', "𝑃");
        charFont.put('Q', "𝑄");
        charFont.put('R', "𝑅");
        charFont.put('S', "𝑆");
        charFont.put('T', "𝑇");
        charFont.put('U', "𝑈");
        charFont.put('V', "𝑉");
        charFont.put('W', "𝑊");
        charFont.put('X', "𝑋");
        charFont.put('Y', "𝑌");
        charFont.put('Z', "𝑍");

        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i);

            if (charFont.containsKey(c)) {
                styledText += charFont.get(c);
            } else {
                styledText += c;
            }
        }

        return styledText;
    }

    public void printWithDelay(String data, TimeUnit unit, long delay) throws InterruptedException {
        for (char ch : data.toCharArray()) {
            System.out.print(convertText(String.valueOf(ch)));
            unit.sleep(delay);
        }
        System.out.println();
    }

    public String breakLines(String input) {
        String[] words = input.split(" ");
        int wordCount = words.length;

        if (wordCount <= 12) {
            return input;
        }

        StringBuilder lines = new StringBuilder("-> ");
        int currentWordCount = 0;

        for (String word : words) {
            lines.append(word).append(" ");
            currentWordCount++;

            if (currentWordCount % 12 == 0) {
                lines.append("\n");
            }
        }

        return lines.toString().trim();
    }

}
