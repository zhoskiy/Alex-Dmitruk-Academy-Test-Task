import java.util.*;

public class Intervals {

    private static final List<String> notes = Arrays.asList("C", "D", "E", "F", "G", "A", "B");
    private static final int[] semitones = new int[] {2,2,1,2,2,2,1};
    private static HashMap<String, ArrayList<Integer>> intervals;
    private static String intervalName;
    private static int degree;
    private static int semitone;
    private static String startNote;
    private static int startNotePosition;
    private static int endNotePosition;
    private static String endNote;
    private static Boolean asc = true;

    public static String intervalConstruction(String[] args) {
        try{
          if(args.length <=1 || args.length >3)
              throw new Exception("Illegal number of elements in input array");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        createIntervals();

        intervalName = args[0];
        startNote = args[1];

        semitone = intervals.get(intervalName).get(0);
        degree = intervals.get(intervalName).get(1);
        startNotePosition = notes.indexOf(startNote.substring(0,1));

        asc = args.length != 3 || !args[2].equals("dsc");

        if(asc) {
            if (degree + startNotePosition - 1 < notes.size()) {
                endNotePosition = degree + startNotePosition - 1;
            } else {
                endNotePosition = -(notes.size() + 1 - (degree + startNotePosition));
            }
        }
        else{
            if(startNotePosition - degree +1 >=0){
                endNotePosition = startNotePosition - degree +1;
            } else {
                endNotePosition = notes.size() + 1 + (startNotePosition - degree);
            }
        }
        endNote = notes.get(endNotePosition);

        String result = endNote;
        int tempSemitone = 0;

        if(!asc)
        {
            int temp = startNotePosition;
            startNotePosition = endNotePosition;
            endNotePosition = temp;
        }

        if(startNotePosition < endNotePosition)
            for (int i = startNotePosition; i < endNotePosition ; i++) {
                tempSemitone += semitones[i];
            }
        else {
            for (int i = startNotePosition; i < semitones.length; i++) {
                tempSemitone += semitones[i];
            }
            for (int i = 0; i < endNotePosition; i++) {
                tempSemitone += semitones[i];
            }
        }

        if(asc)
           return calculateResult(tempSemitone, result,1);
        else
            return calculateResult(tempSemitone, result,-1);
    }

    public static String intervalIdentification(String[] args) {
        createIntervals();

        startNote = args[0];
        endNote = args[1];

        asc = args.length != 3 || !args[2].equals("dsc");

        startNotePosition = notes.lastIndexOf(startNote.substring(0,1));
        endNotePosition = notes.indexOf(endNote.substring(0,1));

        if(startNotePosition > endNotePosition){
            String tempName = startNote;
            startNote = endNote;
            endNote = tempName;

            int temp = startNotePosition;
            startNotePosition = endNotePosition;
            endNotePosition = temp;
            asc = !asc;
        }

        if(asc)
            degree = endNotePosition - startNotePosition + 1;
        else
            degree = notes.size() - (endNotePosition - startNotePosition - 1);

        int tempSemitone = 0;

        if(asc)
            for (int i = startNotePosition; i < endNotePosition ; i++) {
                tempSemitone += semitones[i];
            }
        else {
            for (int i = endNotePosition; i < semitones.length; i++) {
                tempSemitone += semitones[i];
            }
            for (int i = 0; i < startNotePosition; i++) {
                tempSemitone += semitones[i];
            }
        }

        if (asc)
            intervalName = totalInterval(correctSemitone(tempSemitone, 1), degree);
        else
            intervalName = totalInterval(correctSemitone(tempSemitone, -1), degree);

        try{
            if(intervalName == null)
                throw new Exception("Cannot identify the interval");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return intervalName;
    }

    private static String calculateResult(int tempSemitone, String result, int a){
        switch (startNote.substring(1)) {
            case "b" -> tempSemitone += a;
            case "bb" -> tempSemitone += 2*a;
            case "#" -> tempSemitone -= a;
            case "##" -> tempSemitone -= 2*a;
        }

        if(semitone == tempSemitone )
            return result;
        else if(semitone - tempSemitone == a)
            return result + "#";
        else if(semitone - tempSemitone == 2*a)
            return result + "##";
        else if(semitone - tempSemitone == -a)
            return result + "b";
        else if(semitone - tempSemitone == -2*a)
            return result + "bb";

        return result;
    }

    private static String totalInterval(int semitone, int degree){
        for (HashMap.Entry<String, ArrayList<Integer>> entry : intervals.entrySet()) {
            if (Objects.equals(semitonesAndDegreesList(semitone, degree), entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static int correctSemitone(int tempSemitone, int a){
        switch (startNote.substring(1)) {
            case "b" -> tempSemitone += a;
            case "bb" -> tempSemitone += 2*a;
            case "#" -> tempSemitone -= a;
            case "##" -> tempSemitone -= 2*a;
        }

        switch (endNote.substring(1)) {
            case "b" -> tempSemitone -= a;
            case "bb" -> tempSemitone -= 2*a;
            case "#" -> tempSemitone += a;
            case "##" -> tempSemitone += 2*a;
        }

        return tempSemitone;
    }

    private static void createIntervals(){
        intervals = new HashMap<>();

        intervals.put("m2", semitonesAndDegreesList(1,2));
        intervals.put("M2", semitonesAndDegreesList(2,2));
        intervals.put("m3", semitonesAndDegreesList(3,3));
        intervals.put("M3", semitonesAndDegreesList(4,3));
        intervals.put("P4", semitonesAndDegreesList(5,4));
        intervals.put("P5", semitonesAndDegreesList(7,5));
        intervals.put("m6", semitonesAndDegreesList(8,6));
        intervals.put("M6", semitonesAndDegreesList(9,6));
        intervals.put("m7", semitonesAndDegreesList(10,7));
        intervals.put("M7", semitonesAndDegreesList(11,7));
        intervals.put("P8", semitonesAndDegreesList(12,8));

    }

    private static ArrayList<Integer> semitonesAndDegreesList (int a, int b) {
        ArrayList<Integer> list = new ArrayList<>(2);
        list.add(a);
        list.add(b);
        return list;
    }
}
