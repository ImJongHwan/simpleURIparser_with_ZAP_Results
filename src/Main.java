import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> context;
        if(args[0] != null) {
            context = readFile(args[0]);
        }else{
            System.err.println("Please input file name to read.");
            return;
        }

        if(!context.isEmpty()){
            context = parseContext(context);
        }else{
            System.err.println("File is empty.");
            return;
        }

        String subTitle = args[0].substring(0, args[0].lastIndexOf(".txt"));

        if(!context.isEmpty()){
            if(makeFile(context, subTitle)){
                System.out.println("Success");
            }else{
                System.out.println("Failed");
            }
        }else{
            System.err.println("Parsing Contexts is empty.");
            return;
        }

        makeNotFoundUri(context, new File(args[1]));
        //makeNotFoundUriWAVSEP(context, new File(args[1]), args[2]);
    }

    public static List<String> readFile(String fileName){
        List<String> fileContexts = new ArrayList<String>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = reader.readLine()) != null){
                line =line.replaceAll("\u0000","");
                line = line.replaceAll("\\s+","");
                fileContexts.add(line);
            }
            reader.close();
            return fileContexts;
        }catch(Exception e){
            System.err.println("Exception occurred try reading file" + fileName);
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> parseContext(List<String> orgin){
        List<String> resParsing = new ArrayList<String>();
        for(String line : orgin){
            int start;
            int end;

            if(line.indexOf("GET") != -1){
                start = line.indexOf("GET");
            }else if(line.indexOf("POST") != -1){
                start = line.indexOf("POST");
            }else{
                start = 0;
            }

            if(line.indexOf("<",start) != -1){
                end = line.indexOf("<", start);
            }else{
                end = line.length();
            }

            resParsing.add(line.substring(start, end));
        }
        return resParsing;
    }

    public static boolean makeFile(List<String> context, String originFileName){
        try{
            Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
            String fileName = ".\\" + sdf.format(dt).toString() + "_" + originFileName +".txt";
            File newFile = new File(fileName);

            if(newFile.exists()) {
                return false;
            }else{
                newFile.createNewFile();
            }

            FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for(String line : context){
                bw.write(line);
                bw.newLine();
            }

            bw.close();

            System.out.println("FileName : " + fileName);

            return true;
        }catch(Exception e){
            System.err.println("Loading File is failed.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean makeNotFoundUri(List<String> context, File resource){
        List<String> find = new ArrayList<>();
        List<String> comp = new ArrayList<>();
        for(String uri : context){
            int start = uri.indexOf("benchmark/") + 10;
            int end = uri.indexOf("?");
            if(end < 0){
                end = uri.indexOf(".");
                if(end < 0){
                    find.add(uri.substring(start));
                }else{
                    find.add(uri.substring(start,end));
                }
            }else{
                find.add(uri.substring(start,end));
            }
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(resource));
            String line;

            while((line=br.readLine()) != null){
                comp.add(line);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        try{
            Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
            String fileName = ".\\" + sdf.format(dt).toString() + "_" + resource.getName() + ".txt";
            File newFile = new File(fileName);

            String fileName2 = ".\\" + sdf.format(dt).toString() + "true_" + resource.getName() + ".txt";
            File newFile2 = new File(fileName2);

            if(newFile.exists()) {
                return false;
            }else{
                newFile.createNewFile();
            }

            FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            FileWriter fw2 = new FileWriter(newFile2.getAbsoluteFile());
            BufferedWriter bw2 = new BufferedWriter(fw2);

            for(String line : find){
                if(comp.contains(line)){
                    bw2.write(line);
                    bw2.newLine();
                    comp.remove(comp.indexOf(line));
                }else{
                    System.err.println("line is not contained in all");
                    System.err.println(">> " + line);
                }
            }

            bw2.close();

            bw.write("Can't found URI Total Count : " + comp.size());
            bw.newLine();

            for(String line : comp){
                bw.write(line);
                bw.newLine();
            }

            bw.close();

            System.out.println("FileName : " + fileName);

            return true;
        }catch(Exception e){
            System.err.println("Loading File is failed.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean makeNotFoundUriWAVSEP(List<String> context, File resource, String category){
        List<String> find = new ArrayList<>();
        List<String> comp = new ArrayList<>();
        for(String uri : context){
            int start = uri.indexOf(category + "/") + category.length()+1;
            int end = uri.indexOf("?");
            if(end < 0){
                find.add(uri.substring(start));
            }else{
                find.add(uri.substring(start,end));
            }
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(resource));
            String line;

            while((line=br.readLine()) != null){
                comp.add(line);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        try{
            Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
            String fileName = ".\\" + sdf.format(dt).toString() + "_" + resource.getName() + ".txt";
            File newFile = new File(fileName);

            if(newFile.exists()) {
                return false;
            }else{
                newFile.createNewFile();
            }

            FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for(String line : find){
                if(comp.contains(line)){
                    comp.remove(comp.indexOf(line));
                }else{
                    System.err.println("line is not contained in all");
                    System.err.println(">> " + line);
                }
            }

            bw.write("Can't found URI Total Count : " + comp.size());
            bw.newLine();

            for(String line : comp){
                bw.write(line);
                bw.newLine();
            }

            bw.close();

            System.out.println("FileName : " + fileName);

            return true;
        }catch(Exception e){
            System.err.println("Loading File is failed.");
            e.printStackTrace();
            return false;
        }
    }
}
