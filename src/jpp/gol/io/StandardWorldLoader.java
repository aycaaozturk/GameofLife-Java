package jpp.gol.io;

import jpp.gol.model.CellState;
import jpp.gol.model.World;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StandardWorldLoader implements WorldLoader {

//    Bayt tabanlı veri: Ham veriyi temsil eder, genellikle dosya veya medya içeriği gibi verileri işler.
//    Karakter tabanlı veri: Metin verisini temsil eder, insan tarafından okunabilir metinleri işler
//                           ve genellikle belirli bir karakter kodlamasıyla ilişkilidir.

//    InputStream, bayt tabanlı veri akışını okur.
//    InputStreamReader, bu bayt akışını karakter akışına dönüştürür.
//    InputStreamReader, bir InputStream nesnesine ihtiyaç duyar ve bu nesneden aldığı baytları karakter olarak okur.

    //inputstream: kaynaktan veriyi okumak icin (byte tabanli verileri okumak icin, soyut bir sinif)

    //inputstreamreader: (inputstreamden okunan) bayt tabanlı veri kaynaklarından okuma işlemi yapmak istediğinizde kullanılır.
    //                   Bu sınıf, verilerin belirli bir karakter setine (örn. UTF-8) göre okunmasını sağlar.
    //                   isreader, bir inputstream nesnesi olmadan calisamaz, inputstreami kaynak olarak kullanir

//    BufferedReader: Java'da karakter tabanlı veri okuma işlemlerini hızlandırmak için kullanılan bir sınıftır.
//                    Özellikle büyük dosyalar veya yavaş veri kaynaklarıyla çalışırken kullanışlıdır.
//                    readLine() gibi yöntemlerle, dosyalardan veya diğer karakter tabanlı akışlardan verimli bir şekilde
//                    veri okumayı sağlar.
//


    //outputstream: hedefe yazmak icin


    @Override
    public World load(InputStream in) throws IOException {
        if(in == null){
            throw new IOException("input is null");
        }

        InputStreamReader isReader = new InputStreamReader(in);
        BufferedReader loadedWorld = new BufferedReader(isReader);
        String firstLine = loadedWorld.readLine();

        Pattern regex = Pattern.compile("(\\d+)x(\\d+)");   //sayi x sayi seklinde regex (desen) tanimladik
        Matcher matcher = regex.matcher(firstLine);         // matcher nesnesi olusturduk, bununla kontrol edicez
        int height = 0;
        int width =  0;                             //regex'i sagliyor mu diye icine firstLine koyduk
        int countLines =0;

        if(firstLine==null || !matcher.matches()){
            throw new IOException("first line invalid");
        }

        if(matcher.matches()){
            width = Integer.parseInt(matcher.group(1));
            height = Integer.parseInt(matcher.group(2));    //en yukaridaki satirdan (örn 4x5) bu sayilari aldik


            if(height<=0 || width<=0){                          //      5x4   genislikxsatir    4 satir 5 sütun
                throw new IOException("values invalid");        //0   11101
            }                                                   //1   10101
        }                               //  (satir, sütun)      //2   11001
                                                                //3   11001
                                        //
        World newWorldSymphony = new World(width,height);

        for(int i=0; i<height;i++) {//                         //readLine kaldigi yerden devam ediyor, döngünün basinda 2. satirda
            String readThisLine = loadedWorld.readLine();     // yani yukaridaki 0 indexli satirdan basliyor

            if (readThisLine == null) {
                throw new IOException("line: null");
            }
            else if (readThisLine.length() != width) {
                throw new IOException("Invalid width");
            }

            for(int e=0; e<width; e++){
                //soldan saga karakterler 0 ya da 1 mi diye bakicaz
              char cellState = readThisLine.charAt(e);

              if(cellState!='0' && cellState!='1'){
                  throw new IOException("cells must be either 0 or 1");
              }

              if(cellState=='0'){     //x,y
                  newWorldSymphony.set(e,i, CellState.DEAD);
              }
              else{
                  newWorldSymphony.set(e,i, CellState.ALIVE);
              }
            }

            countLines++;

        }
        if(countLines!=height){
            throw new IOException("invalid height");
        }

      return newWorldSymphony;

    }

//    OutputStream: Bayt tabanlı veri yazmak için kullanılır.
//    OutputStreamWriter: Karakter tabanlı verileri bayta dönüştürüp bir OutputStream'e yazmak için kullanılır.
//    BufferedWriter: Bir Writer nesnesine tamponlama ekleyerek yazma işlemini hızlandırır ve verimli hale getirir.

    @Override
    public void save(World world, OutputStream out) throws IOException {
        if(world==null || out==null){
            throw new IOException("parameters (world and out) are null");
        }

        OutputStreamWriter osWriter = new OutputStreamWriter(out);
        BufferedWriter writeWorld = new BufferedWriter(osWriter);
        int width = world.getWidth();
        int height = world.getHeight();
        writeWorld.write(width+"x"+height);
        writeWorld.newLine();

        for (int i = 0; i < height; i++) {
            StringBuilder line = new StringBuilder();
            for (int e = 0; e < width; e++) {
                int cellZustand =0;
                if(world.get(e,i)==CellState.DEAD){
                    cellZustand=0;
                }
                else{
                    cellZustand=1;
                }

                line.append(cellZustand);
            }
            writeWorld.write(line.toString());
            if(i<height-1){
                writeWorld.newLine(); // Satır sonu ekle
            }

        }
        writeWorld.flush();

    }
}
