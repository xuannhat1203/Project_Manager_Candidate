import java.util.*;

public class tesst {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3,2,1);
//        System.out.println("Cach 1");
//        list.stream().sorted(Comparator.comparing(Integer::intValue));
//        System.out.println(list);
//        System.out.println("Cach 2");
//        Collections.sort(list);
//        System.out.println(list);
//        System.out.println("Cach 3");
//        list.sort(Comparator.comparing(Integer::intValue));
////        System.out.println(list);
//        System.out.println(list.get((int) Math.random()));
//        list.sort(Collections.reverseOrder());
//            System.out.println(list);
//        int max = 0;
//        int n = 0;
//        for (int i =0 ; i< list.size(); i++){
//            int count = 0;
//            for (int j = i + 1; j < list.size(); j++){
//                if (list.get(i).equals(list.get(j))){
//                    count++;
//                }
//            }
//            if (count > max){
//                max = count;
//                n = list.get(i);
//            }
//        }
//        System.out.println(max + " : " + n);
//        int count = 0;
//        for (int i = 0; i < list.size()/2; i++) {
//            for (int j = list.size() - 1; j > list.size()/2; j--) {
//                if (list.get(i).equals(list.get(j))) {
//                    count ++ ;
//                }
//            }
//        }
//        if (count == list.size()/2) {
//            System.out.println("Mang doi xung");
//        }
//        list.sort(Collections.reverseOrder(Collections.reverseOrder()));
//        System.out.println(list);
//        System.out.println(list.get(2));
        String s = " xuan nhat ne";
        StringBuilder builder = new StringBuilder(s);
        System.out.println(builder.reverse().toString());
    }
}
