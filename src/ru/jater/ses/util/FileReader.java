package ru.jater.ses.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    private static final String FILE_ENCODING = "Windows-1251";

    private ArrayList<String> questions;
    private ArrayList<String> hypotheses;
    private String author;
    private File file;

    public FileReader(File file) {
        questions = new ArrayList<>();
        hypotheses = new ArrayList<>();
        this.file = file;
        parse();
    }

    private void parse() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), FILE_ENCODING));
            Scanner scanner = new Scanner(bufferedReader);
            StringBuilder sb = new StringBuilder();
            String buffer;
            while (scanner.hasNextLine()) {
                buffer = scanner.nextLine();
                sb.append(buffer);
                sb.append("\n");
                if (buffer.isEmpty()) {
                    author = sb.toString();
                    break;
                }
            }
            while (scanner.hasNextLine()) {
                buffer = scanner.nextLine();
                if (buffer.equals("Вопросы:")) {
                    while (buffer.length() != 0) {
                        questions.add(buffer);
                        buffer = scanner.nextLine();
                    }
                }
                if (buffer.isEmpty()) continue;
                hypotheses.add(buffer);
            }
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getAuthor() {
        return author;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public ArrayList<String> getHypotheses() {
        return hypotheses;
    }
}
