import java.util.Arrays;

public class MisMatchExample {
    public static void main(String... args){
        char[] DNAStrand5 = {'G', 'A', 'G', 'T', 'C', 'T'};
        char[] DNAStrand6 = {'G', 'A', 'G', 'C', 'C', 'C'};
        int result = Arrays.mismatch(DNAStrand5, DNAStrand6); // 3
        // mismatch method returns the position of the first mismatch when comparing two arrays
        System.out.println("Arrays.mismatch: " + result); //3

        char[] DNAStrand1 = {'G', 'A', 'G', 'C', 'C', 'T'};
        char[] DNAStrand2 = {'G', 'A', 'G', 'C', 'C', 'C'};
        System.out.println("Arrays.mismatch: " + Arrays.mismatch(DNAStrand1, DNAStrand2)); // 5

        char[] DNAStrand3 = {'A', 'G', 'C', 'C', 'T'};
        char[] DNAStrand4 = {'A', 'B', 'B', 'B', 'B', 'C'};
        System.out.println("Arrays.mismatch: " + Arrays.mismatch(DNAStrand3, DNAStrand4)); // 1
    }
}
