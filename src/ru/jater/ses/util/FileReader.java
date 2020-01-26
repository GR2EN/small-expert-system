package ru.jater.ses.util;

import ru.jater.ses.entity.Hypothese;
import ru.jater.ses.entity.Probability;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {

    private static final String FILE_ENCODING = "Windows-1251";

    private ArrayList<String> questions;
    private List<Hypothese> hypotheses;
    private String author;
    private File file;

    public FileReader(File file) {
        questions = new ArrayList<>();
        hypotheses = new ArrayList<>();
        this.file = file;
        parse();
    }

    private void parse() {
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), FILE_ENCODING));
            Scanner scanner = new Scanner(bf);
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
                    buffer = scanner.nextLine();
                    while (buffer.length() != 0) {
                        questions.add(buffer);
                        buffer = scanner.nextLine();
                    }
                }
                if (buffer.length() != 0) {
                    String[] buf = buffer.split(",");
                    Hypothese hypothese = new Hypothese(buf[0], Double.parseDouble(buf[1]));
                    for (int i = 2, j = 1; i < buf.length; i += 3, j++) {
                        Probability prob = new Probability(Double.parseDouble(buf[i+1]), Double.parseDouble(buf[i+2]));
                        hypothese.addQuestion(j, prob);
                    }
                    hypotheses.add(hypothese);
                }
            }
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bf != null) {
                    bf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAuthor() {
        return author;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public List<Hypothese> getHypotheses() {
        return hypotheses;
    }

}
