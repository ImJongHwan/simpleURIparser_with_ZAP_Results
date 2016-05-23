import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hwan on 2016-04-25.
 */
public class WavsepRes {


    public static final String WAVSEP_CASE_STRING = "Case";
    public static final String WAVSEP_URL_EXTENSION_JSP = ".jsp";

    public final static String WAVSEP_RES_PATH = "C:\\gitProjects\\simpleURLParser\\wavsep\\wavsep_res\\";
    public static final String WAVSEP_TC_PATH = "C:\\gitProjects\\simpleURLParser\\wavsep\\wavsep_tc\\";
    public static final String WAVSEP_SCORE_PATH = "C:\\gitProjects\\simpleURLParser\\wavsep\\wavsep_score\\";

    public static final String PATH_TRAVERSAL = "lfi";
    public static final String REFLECTED_CROSS_SITE_SCRPITING = "rxss";
    public static final String REMOTE_FILE_INCLUSION = "rfi";
    public static final String SQL_INJECTION = "sqli";
    public static final String UNVALIDATED_REDIRECT = "redir";

    public static final String DOM_XSS_ROOT = "DOM-XSS";
    public static final String LFI_ROOT = "LFI";
    public static final String OBSOLETE_FILES_ROOT = "Obsolete-Files";
    public static final String REFLECTED_XSS_ROOT = "Reflected-XSS";
    public static final String RFI_ROOT = "RFI";
    public static final String SQL_INJECTION_ROOT = "SQL-Injection";
    public static final String UNVALIDATED_REDIRECT_ROOT = "Unvalidated-Redirect";

    public static final String TRUE_POSITIVE = "tp";
    public static final String FALSE_POSITIVE = "fp";
    public static final String EXPERIMENTAL = "ex";
    public static final String ALL_SET = "all";

    public static List<String> getFailedList(List<String> urlList, String tc, String set) {
        List<String> failedList;
        List<String> debugRemoveLIst = new ArrayList<>();

        failedList = getTcList(tc, set);

        if (failedList == null || failedList == null) {
            System.err.println("WAVSEP TestCases List is Empty :" + tc);
            return null;
        }

        for (String url : urlList) {
            if (failedList.contains(url)) {
                failedList.remove(url);
                debugRemoveLIst.add(url);
            }
        }

        return failedList;
    }

    public static List<String> getTcList(String tc, String set) {
        List<String> tcList;

        if (set.equals(TRUE_POSITIVE)) {
            tcList = Main.readFile(WAVSEP_TC_PATH + tc + "_" + TRUE_POSITIVE + ".txt");
        } else if (set.equals(FALSE_POSITIVE)) {
            tcList = Main.readFile(WAVSEP_TC_PATH + tc + "_" + FALSE_POSITIVE + ".txt");
        } else if (set.equals(EXPERIMENTAL)) {
            tcList = Main.readFile(WAVSEP_TC_PATH + tc + "_" + EXPERIMENTAL + ".txt");
        } else if (set.equals(ALL_SET)) {
            tcList = Main.readFile(WAVSEP_TC_PATH + tc + "_" + TRUE_POSITIVE + ".txt");
            if (tcList != null) {
                tcList.addAll(Main.readFile(WAVSEP_TC_PATH + tc + "_" + FALSE_POSITIVE + ".txt"));
                tcList.addAll(Main.readFile(WAVSEP_TC_PATH + tc + "_" + EXPERIMENTAL + ".txt"));
            }
        } else {
            System.err.println("Setting Case is Failed" + set);
            return null;
        }

        return tcList;
    }

    public static List<String> parseWavsep(List<String> originList, String tc) {
        List<String> parsingList = new ArrayList<>();
        String root = getRoot(tc);

        if (root == null) {
            System.err.println("parsing false - test case name is fault");
            return null;
        }

        for (String uri : originList) {
            String activeRoot = "active/" + root + "/";
            int start = uri.indexOf(activeRoot);
            int endQ = uri.indexOf("?");
            int endS = uri.indexOf("#");

            if(start < 0){
                continue;
            }else{
                start += (8 + root.length());
            }

            if (endQ < start && endS < start) {
                parsingList.add(uri.substring(start));
            }else if(endQ > start && endS > start) {
                if(endQ > endS) {
                    parsingList.add(uri.substring(start, endS));
                }else{
                    parsingList.add(uri.substring(start, endQ));
                }
            }else{
                if(endQ > start){
                    parsingList.add(uri.substring(start, endQ));
                }else{
                    parsingList.add(uri.substring(start, endS));
                }
            }
        }

        return parsingList;
    }

    private static String getRoot(String tc) {
        switch (tc) {
            case PATH_TRAVERSAL:
                return LFI_ROOT;
            case REMOTE_FILE_INCLUSION:
                return RFI_ROOT;
            case REFLECTED_CROSS_SITE_SCRPITING:
                return REFLECTED_XSS_ROOT;
            case SQL_INJECTION:
                return SQL_INJECTION_ROOT;
            case UNVALIDATED_REDIRECT:
                return UNVALIDATED_REDIRECT_ROOT;
            default:
                return null;
            //todo dom and obsolete
        }
    }

}
