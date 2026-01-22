package jpp.gol.console;

import jpp.gol.io.StandardWorldLoader;
import jpp.gol.io.WorldLoader;
import jpp.gol.logic.GameLogic;
import jpp.gol.logic.StandardGameLogic;
import jpp.gol.model.CellState;
import jpp.gol.model.World;
import jpp.gol.rules.Rules;
import jpp.gol.rules.StandardRules;

import java.io.*;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameOfLife {

    private String step2(BufferedReader reader, PrintWriter writer) {
        String userAnswer = null;
        writer.println("Moechten Sie die Welt aus einer [D]atei laden, oder selbst [k]onfigurieren?");
        try {
            userAnswer = reader.readLine();
            while (!userAnswer.equals("D") && !userAnswer.equals("k")) {
                writer.println("Ungueltige Aktion! Es sind nur die beiden Aktionen k und D zulaessig.");
                writer.println("Moechten Sie die Welt aus einer [D]atei laden, oder selbst [k]onfigurieren?");
                userAnswer = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userAnswer;
    }

    private GameLogic step3(BufferedReader reader, PrintWriter writer) {
        while (true) {
            writer.println("Geben Sie den Pfad zur Datei ein:");
            String pfad;
            try {
                pfad = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                InputStream inputStream = new FileInputStream(pfad);
                WorldLoader wl = new StandardWorldLoader();
                World w = wl.load(inputStream);
                return new StandardGameLogic(w, new StandardRules());
            } catch (IOException fe) {
                writer.println("Ungueltiger Dateipfad oder der Inhalt der Datei ist inkorrekt.");
                return null;
            }
        }
    }

    private int step4(BufferedReader reader, PrintWriter writer) {

        while (true) {
            try {
                writer.println("Geben Sie die Hoehe der Welt ein:");
                String userAnswer = reader.readLine();
                int height = Integer.parseInt(userAnswer);
                if (height > 0) {
                    return height;
                } else {
                    writer.println("Ungueltige Hoehe.");
                }

            } catch (Exception e) {
                writer.println("Ungueltige Hoehe.");
            }
        }
    }

    private int step5(BufferedReader reader, PrintWriter writer) {

        while (true) {
            try {
                writer.println("Geben Sie die Breite der Welt ein:");
                String userAnswer = reader.readLine();
                int width = Integer.parseInt(userAnswer);
                if (width > 0) {
                    return width;
                } else {
                    writer.println("Ungueltige Breite.");
                }

            } catch (Exception e) {
                writer.println("Ungueltige Breite.");
            }
        }
    }

    private boolean step7(BufferedReader reader, PrintWriter writer) {

        while (true) {
            try {
                writer.println("Moechten Sie ein Feld veraendern? (Ja/Nein)");
                String userAnswer = reader.readLine();
                if (userAnswer.equals("Ja")) {
                    return true;
                } else if (userAnswer.equals("Nein")) {
                    return false;
                }


            } catch (Exception e) {

            }
            writer.println("Ungueltige Eingabe.");
        }
    }

    public void step6(PrintWriter writer, GameLogic gameLogic) {
        writer.println();
        writer.println(gameLogic.getWorld().toString());
        writer.println();
    }

    private GameLogic step8(BufferedReader reader, PrintWriter writer, GameLogic gameLogic) {


        try {
            writer.println("Geben Sie die x- und y-Koordinaten im Format <x>,<y> des zu aendernden Feldes ein:");
            String userAnswer = reader.readLine();
            String[] splitAnswer = userAnswer.split(",");
            if (splitAnswer.length != 2) {
                writer.println("Ungueltige Koordinaten!");
                return gameLogic;
            }
            int x = Integer.parseInt(splitAnswer[0]);
            int y = Integer.parseInt(splitAnswer[1]);
            gameLogic.changeState(x, y);
            return gameLogic;
        } catch (Exception e) {
            writer.println("Ungueltige Koordinaten!");
            return gameLogic;
        }


    }

    private String step12(BufferedReader reader, PrintWriter writer) {
        String userAnswer = null;
        writer.println("Soll die naechste [I]teration berechnet werden, oder das Spiel [b]eendet werden?");
        try {
            userAnswer = reader.readLine();
            while (!userAnswer.equals("I") && !userAnswer.equals("b")) {
                writer.println("Ungueltige Aktion. Es sind nur die Aktionen I und b zulaessig.");
                writer.println("Soll die naechste [I]teration berechnet werden, oder das Spiel [b]eendet werden?");
                userAnswer = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userAnswer;
    }

    public void run(InputStream in, OutputStream out) {
        InputStreamReader isReader = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(isReader);         //kullanicidan okuyacak
        PrintWriter writer = new PrintWriter(out, true);       //gosterilen dosyaya kulllanicinin girdilerini yazacak


        writer.println("Willkommen zu Game of Life.");
        GameLogic gameLogic = null;

        while (gameLogic == null) {
            String userAnswer = step2(reader, writer);
            if (userAnswer.equals("D")) {   //DATEI LADEN
                gameLogic = step3(reader, writer);
            } else {
                int height = step4(reader, writer);
                int width = step5(reader, writer);
                gameLogic = new StandardGameLogic(new World(width, height), new StandardRules());
                step6(writer, gameLogic);
                boolean choice = step7(reader, writer);
                while (choice) {
                    gameLogic = step8(reader, writer, gameLogic);
                    step6(writer, gameLogic);
                    choice = step7(reader, writer);
                }
            }
        }

        writer.println("");
        writer.println("Spiel wird gestartet.");
        while (true) {
            writer.println();
            writer.println(gameLogic.getWorld().toString());
            writer.println();
            String choice = step12(reader, writer);
            if (choice.equals("I")) {
                gameLogic.step();
            } else {
                break;
            }

        }

        writer.println("Auf Wiedersehen!");


    }

    public static void main(String[] args) {

        new GameOfLife().run(System.in, System.out);
    }
}