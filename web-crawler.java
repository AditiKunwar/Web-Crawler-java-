import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
/**
 *
 * @author diwas
 */
public class jsoup_02 {

    public static void main(String[] args) throws IOException, Exception {
        Scanner sc = new Scanner(System.in);
        int j = 1;
        System.out.print("Enter the keyword to search: ");
        String keyword = sc.nextLine();
        ArrayList<ArrayList<String>> listoflists = new ArrayList<>();
        String url = "https://www.google.com/search?q=" + keyword;
//        int count  = 0;
        for (String link : findLinks(url)) {
            if (link.contains("https")) {
                if (!link.contains("google")) {
                    if (link.contains("&")) {
                        try {
                            ArrayList<String> innerList = new ArrayList<>();
                            String newlink = link.split("&")[0];
                            String final_link = newlink.split("=")[1];

                            String para = findtext(final_link);


                            System.out.println("Link: " + final_link);
                            System.out.println("Paragraph: "+para);
                            // System.out.println("");
                            System.out.println("");
                            innerList.add(keyword);
                            innerList.add(Integer.toString(j));
                            j++;
                            Document document = Jsoup.connect(final_link).get();
                            innerList.add(document.title());
                            innerList.add(final_link);
                            innerList.add(para);
                            listoflists.add(innerList);
                        } catch (Exception io) {
//                            count++ ;
                            System.out.println("");

                        }
                    }
                }
            }
        }
//        System.out.println(count);
        System.out.println(listoflists.get(0).size());
        for (int i = 0; i < listoflists.size(); i++) {
            for (int k = 0; k < listoflists.get(i).size(); k++) {
                String Keyword=listoflists.get(i).get(0);
                String rank=listoflists.get(i).get(1);
                String title=listoflists.get(i).get(2);
                String link=listoflists.get(i).get(3);
                String para=listoflists.get(i).get(4);
                try{
                    Connection conn = getConnection();
                    System.out.println("Connected ");
//                    PreparedStatement posted = conn.prepareStatement("INSERT INTO dateset (WORD,RANKING,TITLE,LINKS,DETAILS) VALUES ('" + Keyword + "','" + rank + "','" + title + "','" + link + "','" + para + "')");
//                    posted.executeUpdate();
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
        }
    }

    public static Connection getConnection() throws Exception {
        try {
//            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/testing_01";
            String username = "root";
            String password = "Isolux@1987";
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(url, username, password);
//            System.out.println("Connected");
            return conn;
        } catch (Exception e) {
            System.out.println(e);
        }
        jdbc:mysql://localhost:3306/?user=root
        return null;
    }

    private static Set<String> findLinks(String url) throws IOException {
        Set<String> links = new HashSet<>();
        Document doc = Jsoup.connect(url).userAgent("Mozilla").timeout(500000).get();
        Elements elements = doc.select("a[href]");
        for (Element element : elements) {
            links.add(element.attr("href"));
        }
        return links;
    }

    private static String findtext(String url) throws IOException {
        Document document = Jsoup.connect("" + url).timeout(1000000).userAgent("Google").get();
        String text = document.body().text();
        return text;
    }

    private static ArrayList<String> removeDuplicates(ArrayList<String> list) {
        Set<String> set = new LinkedHashSet<>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }
}
