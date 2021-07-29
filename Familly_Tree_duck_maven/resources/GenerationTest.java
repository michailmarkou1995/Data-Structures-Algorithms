import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Csv {


    public static void main(String[] args) throws FileNotFoundException, IOException {

        //String SEPARATOR = ",";
        BufferedReader reader = new BufferedReader(
                new FileReader("C:\\Users\\dark\\Documents\\NetBeansProjects\\csv\\src\\csv\\family.csv"));
        String line = null;
        //List<String> field = new LinkedList<String>();

        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            ArrayList<String> field = new ArrayList<String>();
            for (int i = 0; i < fields.length; i++)
                field.add(fields[i]);
            System.out.println(field);

        }

        HashMap<String, String> map = new HashMap<>();

        // Add elements to the map 
        map.put("Cassana Estermont", "woman");
        map.put("Cersei Lannister", "woman");
        map.put("Gendry", "man");
        map.put("Joffrey Baratheon", "woman");
        map.put("Margaery Tyrell", "woman");
        map.put("Myrchella Baratheon", "man");
        map.put("Renly Baratheon", "woman");
        map.put("Robbert Baratheon", "man");
        map.put("Shireen Baratheon", "man");
        map.put("Stannis Baratheon", "woman");
        map.put("Steffon Baratheon", "man");
        map.put("Tommen Baratheon", "woman");

        // map.entrySet()
        //    .stream()
        //     .sorted(HashMap.Entry.<String, String>comparingByKey())
        //   .forEach(System.out::println);

        final String outputFilePath = "ordered.txt";
        File file = new File(outputFilePath);

        BufferedWriter bf = null;
        ;

        try {

            //create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));
            map.entrySet()
                    .stream()
                    .sorted(HashMap.Entry.<String, String>comparingByKey())
                    .forEach(System.out::println);


            //iterate map entries
            for (HashMap.Entry<String, String> entry : map.entrySet()) {

                //put key and value separated by a colon
                bf.write(entry.getKey() + ":" + entry.getValue());

                //new line
                bf.newLine();
            }

            bf.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                //always close the writer
                bf.close();
            } catch (Exception e) {
            }
        }
    }
}
        