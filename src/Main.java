import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    private static List<String> failedCrawlingList;
    private static List<String> failedDetectedTPList;
    private static List<String> failedDetectedTNList;
    private static List<String> failedDetectedEXList;
    private static List<String> failedDetectedFPList;

    public static void main(String[] args) {
        List<String> crawledList;
        List<String> detectedList;

        if (!(args.length < 3) && args[0] != null && args[1] != null && args[2] != null) {
            crawledList = readFile(BenchmarkRes.BENCHMARK_RES_PATH + args[1]);
            detectedList = readFile(BenchmarkRes.BENCHMARK_RES_PATH + args[2]);
//            crawledList = readFile(WavsepRes.WAVSEP_RES_PATH + args[1]);
//            detectedList = readFile(WavsepRes.WAVSEP_RES_PATH + args[2]);
        } else {
            System.err.println("Please input file name to read.");
            return;
        }

        if (!crawledList.isEmpty() && !detectedList.isEmpty()) {
//            if (makeBenchmarkRes(crawledList, detectedList, args[0])) {
//            if(makeWavsepRes(crawledList, detectedList, args[0])){
//            if(getWavsepPanicRes(crawledList, detectedList, args[0])){
            if(getBenchmarkRes(crawledList, detectedList, args[0])){
                System.out.println("Success");
                return;
            }
//            crawledList = parseZapURLRes(crawledList);
//            crawledList = BenchmarkRes.parseBenchmark(crawledList);
//            failedCrawlingList = BenchmarkRes.getFailedList(crawledList, args[1], BenchmarkRes.BOTH_TRUE);
//            failedDetectedList = BenchmarkRes.getFailedList(crawledList, args[1], BenchmarkRes.TRUE_POSITIVE);
        }
        System.err.println("File is empty.");
        return;


//        String subTitle = args[0].substring(0, args[0].lastIndexOf(".txt"));
//
//        if(!crawledList.isEmpty()){
//            if(writeFile(crawledList, subTitle)){
//                System.out.println("Success");
//            }else{
//                System.out.println("Failed");
//            }
//        }else{
//            System.err.println("Parsing Contexts is empty.");
//            return;
//        }
//
//        makeNotFoundUri(crawledList, new File(args[1]));
//        //makeNotFoundUriWAVSEP(crawledList, new File(args[1]), args[2]);
    }

    private static boolean getBenchmarkRes(List<String> crawledList, List<String> detectedList, String testCase){
        List<String> parsingCrawledList = BenchmarkRes.parseBenchmark(crawledList);
        List<String> parsingDetectedList = parsingBenchmarkRes(detectedList);

        failedCrawlingList = BenchmarkRes.getFailedList(parsingCrawledList, testCase, BenchmarkRes.BOTH_TRUE);
        failedDetectedTNList = BenchmarkRes.getFailedList(parsingDetectedList, testCase, BenchmarkRes.TRUE_NEGATIVE);
        failedDetectedTPList = BenchmarkRes.getFailedList(parsingDetectedList, testCase, BenchmarkRes.TRUE_POSITIVE);

        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

        String timeSet = sdf.format(dt).toString();

        String fCrawlPath = BenchmarkRes.BENCHMARK_SCORE_PATH + timeSet + "_" + testCase + "_fcrawl.txt";
        String fDetectTNPath = BenchmarkRes.BENCHMARK_SCORE_PATH + timeSet + "_" + testCase + "_fdetectTN.txt";
        String fDetectTPPath = BenchmarkRes.BENCHMARK_SCORE_PATH + timeSet + "_" + testCase + "_fdetectTP.txt";

        if (writeFile(failedCrawlingList, fCrawlPath)
                && writeFile(failedDetectedTNList, fDetectTNPath)
                && writeFile(failedDetectedTPList, fDetectTPPath)) {
            return true;
        } else {
            return false;
        }
    }

    private static List<String> parsingBenchmarkRes(List<String> crawledList){
        List<String> parsingList = new ArrayList<>();

        for(String line : crawledList){
            if(line.contains(BenchmarkRes.BENCHMARK_CONTAIN_STRING)){
                parsingList.add(line);
            }
        }

        return parsingList;
    }

    private static boolean getWavsepPanicRes(List<String> crawledList, List<String> detectedList, String testCase){
        List<String> parsingCrawledList = WavsepRes.parseWavsep(crawledList, testCase);
        List<String> parsingDetectedList = parsingWavsepPanicRes(detectedList);

        failedCrawlingList = WavsepRes.getFailedList(parsingCrawledList, testCase, WavsepRes.ALL_SET);
        failedDetectedTPList = WavsepRes.getFailedList(parsingDetectedList, testCase, WavsepRes.TRUE_POSITIVE);
        failedDetectedFPList = WavsepRes.getFailedList(parsingDetectedList, testCase, WavsepRes.FALSE_POSITIVE);
        failedDetectedEXList = WavsepRes.getFailedList(parsingDetectedList, testCase, WavsepRes.EXPERIMENTAL);

        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        String timeSet = sdf.format(dt).toString();

        String fDetectTPPath = WavsepRes.WAVSEP_SCORE_PATH + timeSet + "_WAVSEP_acunetix_" + testCase + "_fdetectTP.txt";
        String fCrawlPath = WavsepRes.WAVSEP_SCORE_PATH + timeSet + "_WAVSEP_acunetix_" + testCase + "_fcrawl.txt";
        String fDetectFPPath = WavsepRes.WAVSEP_SCORE_PATH + timeSet + "_WAVSEP_acunetix_" + testCase + "_fdetectFP.txt";
        String fDetectEXPath = WavsepRes.WAVSEP_SCORE_PATH + timeSet + "_WAVSEP_acunetix_" + testCase + "_fdetectEX.txt";

        if(writeFile(failedCrawlingList, fCrawlPath)
                && writeFile(failedDetectedTPList, fDetectTPPath)
                && writeFile(failedDetectedFPList, fDetectFPPath)
                && writeFile(failedDetectedEXList, fDetectEXPath)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * parse WAVSEP Panic Results List,
     * wavsep panic results list form
     * ex) total count = 1
     * {testcase_root}/{jsp file Name}*
     *
     * @param detectedList wavsep panic results list
     * @return parsing results lis
     */
    private static List<String> parsingWavsepPanicRes(List<String> detectedList){
        List<String> parsingList = new ArrayList<>();
        for(String detected : detectedList){
            if(detected.contains(WavsepRes.WAVSEP_CASE_STRING) && detected.contains(WavsepRes.WAVSEP_URL_EXTENSION_JSP)){
                parsingList.add(detected);
            }
        }
        return parsingList;
    }

    private static boolean makeWavsepRes(List<String> crawledList, List<String> detectedList, String testCase) {
        List<String> parsingCrawledList = WavsepRes.parseWavsep(crawledList, testCase);
        List<String> parsingDetectedList = WavsepRes.parseWavsep(detectedList, testCase);

        failedCrawlingList = WavsepRes.getFailedList(parsingCrawledList, testCase, WavsepRes.ALL_SET);
        failedDetectedTPList = WavsepRes.getFailedList(parsingDetectedList, testCase, WavsepRes.TRUE_POSITIVE);
        failedDetectedFPList = WavsepRes.getFailedList(parsingDetectedList, testCase, WavsepRes.FALSE_POSITIVE);
        failedDetectedEXList = WavsepRes.getFailedList(parsingDetectedList, testCase, WavsepRes.EXPERIMENTAL);

        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        String timeSet = sdf.format(dt).toString();

        String fDetectTPPath = WavsepRes.WAVSEP_SCORE_PATH + timeSet + "_" + testCase + "_fdetectTP.txt";
        String fCrawlPath = WavsepRes.WAVSEP_SCORE_PATH + timeSet + "_" + testCase + "_fcrawl.txt";
        String fDetectFPPath = WavsepRes.WAVSEP_SCORE_PATH + timeSet + "_" + testCase + "_fdetectFP.txt";
        String fDetectEXPath = WavsepRes.WAVSEP_SCORE_PATH + timeSet + "_" + testCase + "_fdetectEX.txt";

        if(writeFile(failedCrawlingList, fCrawlPath)
                && writeFile(failedDetectedTPList, fDetectTPPath)
                && writeFile(failedDetectedFPList, fDetectFPPath)
                && writeFile(failedDetectedEXList, fDetectEXPath)){
            return true;
        }else{
            return false;
        }

    }

    private static boolean makeBenchmarkRes(List<String> crawledList, List<String> detectedList, String testCase) {
        List<String> parsingCrawledList = BenchmarkRes.parseBenchmark(crawledList);
        List<String> parsingDetectedList = BenchmarkRes.parseBenchmark(detectedList);

        failedCrawlingList = BenchmarkRes.getFailedList(parsingCrawledList, testCase, BenchmarkRes.BOTH_TRUE);
        failedDetectedTNList = BenchmarkRes.getFailedList(parsingDetectedList, testCase, BenchmarkRes.TRUE_NEGATIVE);
        failedDetectedTPList = BenchmarkRes.getFailedList(parsingDetectedList, testCase, BenchmarkRes.TRUE_POSITIVE);

        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

        String timeSet = sdf.format(dt).toString();

        String fCrawlPath = BenchmarkRes.BENCHMARK_SCORE_PATH + timeSet + "_" + testCase + "_fcrawl.txt";
        String fDetectTNPath = BenchmarkRes.BENCHMARK_SCORE_PATH + timeSet + "_" + testCase + "_fdetectTN.txt";
        String fDetectTPPath = BenchmarkRes.BENCHMARK_SCORE_PATH + timeSet + "_" + testCase + "_fdetectTP.txt";

        if (writeFile(failedCrawlingList, fCrawlPath)
                && writeFile(failedDetectedTNList, fDetectTNPath)
                && writeFile(failedDetectedTPList, fDetectTPPath)) {
            return true;
        } else {
            return false;
        }


    }

    public static List<String> readFile(String readFilePath) {
        List<String> fileContexts = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(readFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("\u0000", "");
                line = line.replaceAll("\\s+", "");
                fileContexts.add(line);
            }
            reader.close();
            return fileContexts;
        } catch (Exception e) {
            System.err.println("Exception occurred try reading file" + readFilePath);
            e.printStackTrace();
            return null;
        }
    }

    public static boolean writeFile(List<String> context, String filePath) {
        try {
            File newFile = new File(filePath);

            if (newFile.exists()) {
                return false;
            } else {
                newFile.createNewFile();
            }

            FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("Total count : " + context.size());
            bw.newLine();

            for (String line : context) {
                bw.write(line);
                bw.newLine();
            }

            bw.close();

            System.out.println("Making FilePath : " + filePath);

            return true;
        } catch (Exception e) {
            System.err.println("Loading File is failed.");
            e.printStackTrace();
            return false;
        }
    }

//    public static List<String> parseZapURLRes(List<String> orgin){
//        List<String> resParsing = new ArrayList<String>();
//        for(String line : orgin){
//            int start;
//            int end;
//
//            if(line.indexOf("GET") != -1){
//                start = line.indexOf("GET");
//            }else if(line.indexOf("POST") != -1){
//                start = line.indexOf("POST");
//            }else{
//                start = 0;
//            }
//
//            if(line.indexOf("<",start) != -1){
//                end = line.indexOf("<", start);
//            }else{
//                end = line.length();
//            }
//
//            resParsing.add(line.substring(start, end));
//        }
//        return resParsing;
//    }


//
//    public static boolean makeNotFoundUri(List<String> context, File resource){
//        List<String> find = new ArrayList<>();
//        List<String> comp = new ArrayList<>();
//        for(String uri : context){
//            int start = uri.indexOf("benchmark/") + 10;
//            int end = uri.indexOf("?");
//            if(end < 0){
//                end = uri.indexOf(".");
//                if(end < 0){
//                    find.add(uri.substring(start));
//                }else{
//                    find.add(uri.substring(start,end));
//                }
//            }else{
//                find.add(uri.substring(start,end));
//            }
//        }
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(resource));
//            String line;
//
//            while((line=br.readLine()) != null){
//                comp.add(line);
//            }
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
//        try{
//            Date dt = new Date();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
//            String fileName = ".\\" + sdf.format(dt).toString() + "_" + resource.getName() + ".txt";
//            File newFile = new File(fileName);
//
//            String fileName2 = ".\\" + sdf.format(dt).toString() + "true_" + resource.getName() + ".txt";
//            File newFile2 = new File(fileName2);
//
//            if(newFile.exists()) {
//                return false;
//            }else{
//                newFile.createNewFile();
//            }
//
//            FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
//            BufferedWriter bw = new BufferedWriter(fw);
//
//            FileWriter fw2 = new FileWriter(newFile2.getAbsoluteFile());
//            BufferedWriter bw2 = new BufferedWriter(fw2);
//
//            for(String line : find){
//                if(comp.contains(line)){
//                    bw2.write(line);
//                    bw2.newLine();
//                    comp.remove(comp.indexOf(line));
//                }else{
//                    System.err.println("line is not contained in all");
//                    System.err.println(">> " + line);
//                }
//            }
//
//            bw2.close();
//
//            bw.write("Can't found URI Total Count : " + comp.size());
//            bw.newLine();
//
//            for(String line : comp){
//                bw.write(line);
//                bw.newLine();
//            }
//
//            bw.close();
//
//            System.out.println("FileName : " + fileName);
//
//            return true;
//        }catch(Exception e){
//            System.err.println("Loading File is failed.");
//            e.printStackTrace();
//            return false;
//        }
//    }

    public static boolean makeNotFoundUriWAVSEP(List<String> context, File resource, String category) {
        List<String> find = new ArrayList<>();
        List<String> comp = new ArrayList<>();
        for (String uri : context) {
            int start = uri.indexOf(category + "/") + category.length() + 1;
            int end = uri.indexOf("?");
            if (end < 0) {
                find.add(uri.substring(start));
            } else {
                find.add(uri.substring(start, end));
            }
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(resource));
            String line;

            while ((line = br.readLine()) != null) {
                comp.add(line);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
            String fileName = ".\\" + sdf.format(dt).toString() + "_" + resource.getName() + ".txt";
            File newFile = new File(fileName);

            if (newFile.exists()) {
                return false;
            } else {
                newFile.createNewFile();
            }

            FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (String line : find) {
                if (comp.contains(line)) {
                    comp.remove(comp.indexOf(line));
                } else {
                    System.err.println("line is not contained in all");
                    System.err.println(">> " + line);
                }
            }

            bw.write("Can't found URI Total Count : " + comp.size());
            bw.newLine();

            for (String line : comp) {
                bw.write(line);
                bw.newLine();
            }

            bw.close();

            System.out.println("FileName : " + fileName);

            return true;
        } catch (Exception e) {
            System.err.println("Loading File is failed.");
            e.printStackTrace();
            return false;
        }
    }
}
