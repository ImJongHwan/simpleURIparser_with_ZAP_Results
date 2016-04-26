import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hwan on 2016-04-25.
 */
public class BenchmarkRes {
    final static public String BENCHMARK_TC_PATH = "C:\\gitProjects\\simpleURLParser\\benchmark\\benchmark_tc\\";
    final static public String BENCHMARK_RES_PATH = "C:\\gitProjects\\simpleURLParser\\benchmark\\benchmark_res\\";
    final static public String BENCHMARK_SCORE_PATH = "C:\\gitProjects\\simpleURLParser\\benchmark\\benchmark_score\\";

    final static public String ALL_TC = "all_tc";
    final static public String COMMAND_INJECTION = "cmdi";
    final static public String WEAK_ENCRYPTION = "crypto";
    final static public String SQL_INJECTION = "sqli";
    final static public String WEAK_HASH = "hash";
    final static public String LEAP_INJECTION = "ldapi";
    final static public String PATH_TRAVERSAL = "pathtraver";
    final static public String INSECURE_COOKIE = "securecookie";
    final static public String TRUST_BOUNDARY_VIOLATION = "trustbound";
    final static public String WEAK_RANDOM = "weakrand";
    final static public String XPATH_INJECTION = "xpathi";
    final static public String CROSS_SITE_SCRIPTING = "xss";

    final static public String TRUE_POSITIVE = "tp";
    final static public String TRUE_NEGATIVE = "tn";
    final static public String BOTH_TRUE = "both_case";

    final static public int BENCHMAKR_LENGHT = 18;

    public static List<String> getFailedList(List<String> urlList, String tc, String set){
        //todo is tc contained in caseList
        List<String> failedList;
        List<String> debugRemoveLIst = new ArrayList<>();

       failedList = getTcList(tc, set);

        if(failedList == null || failedList.isEmpty()){
            System.err.println("Benchmark TestCases List is Empty :" + tc);
            return null;
        }

        for(String url : urlList){
            if(failedList.contains(url)){
                failedList.remove(url);
                debugRemoveLIst.add(url);
            }
        }

        return failedList;
    }

    private static List<String> getTcList(String tc, String setCase){
        List<String> tcList;

        if(tc.equals(ALL_TC)){
            tcList = Main.readFile(BENCHMARK_TC_PATH+ALL_TC+".txt");
        }else {
            if (setCase.equals(TRUE_POSITIVE)) {
                tcList = Main.readFile(BENCHMARK_TC_PATH + tc + "_" + TRUE_POSITIVE + ".txt");
            }else if(setCase.equals(TRUE_NEGATIVE)){
                tcList = Main.readFile(BENCHMARK_TC_PATH + tc + "_" + TRUE_NEGATIVE + ".txt");
            }else if(setCase.equals(BOTH_TRUE)){
                tcList = Main.readFile(BENCHMARK_TC_PATH + tc + "_" + TRUE_POSITIVE + ".txt");
                if(tcList != null) {
                    tcList.addAll(Main.readFile(BENCHMARK_TC_PATH + tc + "_" + TRUE_NEGATIVE + ".txt"));
                }
            }else{
                System.err.println("Setting Case is Failed" + setCase);
                return null;
            }
        }

        return tcList;
    }

    public static List<String> parseBenchmark(List<String> originList){
        List<String> parsingList = new ArrayList<>();

        for(String uri : originList){
//            int start = uri.indexOf("benchmark/") + 10;
            int start = uri.indexOf(":Benchmark") + 1;
            if(start < 1){
                start = uri.indexOf("benchmark/") + 10;
            }
//            int end = uri.indexOf("?");
            int end = start + BENCHMAKR_LENGHT;

            parsingList.add(uri.substring(start, end));

//            if(end < 0){
////                end = uri.indexOf(".");
//                end = uri.indexOf("?");
//                if(end < 0){
//                    end = uri.lastIndexOf(".");
//                    if(end < 0) {
//                        parsingList.add(uri.substring(start));
//                    }
//                }else{
//                    parsingList.add(uri.substring(start,end));
//                }
//            }else{
//                parsingList.add(uri.substring(start,end));
//            }
        }

        return parsingList;
    }

}
